package com.example.deviceinfogetter.mapper

import android.content.res.Configuration
import com.example.deviceinfogetter.util.dpiCategory
import com.example.deviceinfogetter.util.orientationText
import com.example.deviceinfogetter.util.screenSizeText
import org.junit.Assert.assertEquals
import org.junit.Test

class DeviceInfoMapperTest {

    /** dpiCategory */
    @Test
    fun `dpiCategory returns LDPI for boundary value 120`() {
        assertEquals("LDPI (120)", dpiCategory(120))
    }

    @Test
    fun `dpiCategory returns MDPI for boundary value 160`() {
        assertEquals("MDPI (160)", dpiCategory(160))
    }

    @Test
    fun `dpiCategory returns XHDPI for boundary value 320`() {
        assertEquals("XHDPI (320)", dpiCategory(320))
    }

    @Test
    fun `dpiCategory returns XXHDPI for 480`() {
        assertEquals("XXHDPI (480)", dpiCategory(480))
    }

    @Test
    fun `dpiCategory returns XXXHDPI for boundary value 640`() {
        assertEquals("XXXHDPI (640)", dpiCategory(640))
    }

    @Test
    fun `dpiCategory returns Unknown for dpi above 640`() {
        assertEquals("Unknown", dpiCategory(641))
    }

    @Test
    fun `dpiCategory returns LDPI for value 0`() {
        assertEquals("LDPI (120)", dpiCategory(0))
    }

    /** orientationText */
    @Test
    fun `orientationText returns Portrait for ORIENTATION_PORTRAIT`() {
        assertEquals("Portrait", orientationText(Configuration.ORIENTATION_PORTRAIT))
    }

    @Test
    fun `orientationText returns Landscape for ORIENTATION_LANDSCAPE`() {
        assertEquals("Landscape", orientationText(Configuration.ORIENTATION_LANDSCAPE))
    }

    @Test
    fun `orientationText returns Undefined for unknown value`() {
        assertEquals("Undefined", orientationText(0))
    }

    /** screenSizeText */
    @Test
    fun `screenSizeText returns Small for SCREENLAYOUT_SIZE_SMALL`() {
        assertEquals("Small", screenSizeText(Configuration.SCREENLAYOUT_SIZE_SMALL))
    }

    @Test
    fun `screenSizeText returns Normal for SCREENLAYOUT_SIZE_NORMAL`() {
        assertEquals("Normal", screenSizeText(Configuration.SCREENLAYOUT_SIZE_NORMAL))
    }

    @Test
    fun `screenSizeText returns Large for SCREENLAYOUT_SIZE_LARGE`() {
        assertEquals("Large", screenSizeText(Configuration.SCREENLAYOUT_SIZE_LARGE))
    }

    @Test
    fun `screenSizeText returns XLarge for SCREENLAYOUT_SIZE_XLARGE`() {
        assertEquals("XLarge", screenSizeText(Configuration.SCREENLAYOUT_SIZE_XLARGE))
    }

    @Test
    fun `screenSizeText returns Undefined for unknown value`() {
        assertEquals("Undefined", screenSizeText(0))
    }
}
