package com.example.detectvisibilitywhilescrolling

import android.graphics.Point
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import com.example.detectvisibilitywhilescrolling.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        viewModel.visibilityChanged.observe(this) {
            if(it) {
                Toast.makeText(this, "Text Four SHOWN!!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Text Four HIDDEN!!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.scrollView.apply {
            val scrollBounds = Rect()
            val point = Point()
            setOnScrollChangeListener { _, _, _, _, _ ->
                getHitRect(scrollBounds)
                val isVisible = getChildVisibleRect(binding.textFour, scrollBounds, point)
                viewModel.toggleIsVisible(isVisible)
            }
        }

        setContentView(binding.root)
    }
}

class MainViewModel: ViewModel() {

    private var previousVisibility = false

    private val visibilityChangedMutable = MutableLiveData<Boolean>()
    val visibilityChanged: LiveData<Boolean> get() = visibilityChangedMutable.distinctUntilChanged()

    fun toggleIsVisible(isVisible: Boolean) {
        if(previousVisibility!= isVisible) {
            visibilityChangedMutable.postValue(isVisible)
        }
        previousVisibility = isVisible
    }

}
