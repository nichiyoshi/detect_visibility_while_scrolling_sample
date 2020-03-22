package com.example.detectvisibilitywhilescrolling

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

        val scrollBounds = Rect()

        viewModel.visibilityChanged.observe(this) {
            if(it) {
                Toast.makeText(this, "Text Four SHOWN!!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Text Four HIDDEN!!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.scrollView.apply {
            setOnScrollChangeListener { _, _, _, _, _ ->
                this.getHitRect(scrollBounds)
                val isTextFourVisible = binding.textFour.getLocalVisibleRect(scrollBounds)
                viewModel.toggleIsVisible(isTextFourVisible)
            }
        }

        setContentView(binding.root)
    }
}

class MainViewModel: ViewModel() {

    private val visibilityChangedMutable = MutableLiveData<Boolean>().apply { postValue(false) }
    val visibilityChanged: LiveData<Boolean> get() = visibilityChangedMutable.distinctUntilChanged()

    fun toggleIsVisible(isVisible: Boolean) {
        if(visibilityChangedMutable.value != isVisible) {
            visibilityChangedMutable.postValue(isVisible)
        }
    }

}
