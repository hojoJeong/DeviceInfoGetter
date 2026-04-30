package com.example.deviceinfogetter.viewmodel

import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.deviceinfogetter.data.ScreenshotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SquareViewModel @Inject constructor(
    private val screenshotRepository: ScreenshotRepository
) : ViewModel() {

    private val _boxSize = MutableStateFlow(DEFAULT_BOX_SIZE)
    val boxSize: StateFlow<Int> = _boxSize.asStateFlow()

    private val _boxSizeText = MutableStateFlow(DEFAULT_BOX_SIZE.toString())
    val boxSizeText: StateFlow<String> = _boxSizeText.asStateFlow()

    fun onBoxSizeTextChange(text: String) {
        _boxSizeText.value = text
        text.toIntOrNull()?.let { _boxSize.value = it }
    }

    fun takeScreenshot(view: View, context: Context) {
        screenshotRepository.takeScreenshot(view, context)
    }

    companion object {
        private const val DEFAULT_BOX_SIZE = 200
    }
}
