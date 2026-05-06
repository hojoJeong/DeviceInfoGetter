package com.example.deviceinfogetter.viewmodel

import android.content.Context
import android.view.View
import com.example.deviceinfogetter.data.ScreenshotRepository
import org.junit.Assert.assertEquals
import org.junit.Test

class SquareViewModelTest {

    private val fakeScreenshotRepository = object : ScreenshotRepository {
        override fun takeScreenshot(view: View, context: Context) {}
    }

    @Test
    fun `initial boxSize is 200`() {
        val viewModel = SquareViewModel(fakeScreenshotRepository)

        assertEquals(200, viewModel.boxSize.value)
    }

    @Test
    fun `initial boxSizeText is 200`() {
        val viewModel = SquareViewModel(fakeScreenshotRepository)

        assertEquals("200", viewModel.boxSizeText.value)
    }

    @Test
    fun `onBoxSizeTextChange updates boxSize for valid integer input`() {
        val viewModel = SquareViewModel(fakeScreenshotRepository)

        viewModel.onBoxSizeTextChange("300")

        assertEquals(300, viewModel.boxSize.value)
        assertEquals("300", viewModel.boxSizeText.value)
    }

    @Test
    fun `onBoxSizeTextChange keeps previous boxSize for non-integer input`() {
        val viewModel = SquareViewModel(fakeScreenshotRepository)

        viewModel.onBoxSizeTextChange("abc")

        assertEquals(200, viewModel.boxSize.value)
        assertEquals("abc", viewModel.boxSizeText.value)
    }

    @Test
    fun `onBoxSizeTextChange keeps previous boxSize for empty input`() {
        val viewModel = SquareViewModel(fakeScreenshotRepository)

        viewModel.onBoxSizeTextChange("")

        assertEquals(200, viewModel.boxSize.value)
        assertEquals("", viewModel.boxSizeText.value)
    }
}
