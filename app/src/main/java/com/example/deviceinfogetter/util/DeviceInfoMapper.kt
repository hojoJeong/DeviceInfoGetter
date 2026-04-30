package com.example.deviceinfogetter.util

import android.content.res.Configuration

fun dpiCategory(dpi: Int): String = when (dpi) {
    in 0..120 -> "LDPI (120)"
    in 121..160 -> "MDPI (160)"
    in 161..240 -> "HDPI (240)"
    in 241..320 -> "XHDPI (320)"
    in 321..480 -> "XXHDPI (480)"
    in 481..640 -> "XXXHDPI (640)"
    else -> "Unknown"
}

fun screenSizeText(screenLayout: Int): String =
    when (screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) {
        Configuration.SCREENLAYOUT_SIZE_SMALL -> "Small"
        Configuration.SCREENLAYOUT_SIZE_NORMAL -> "Normal"
        Configuration.SCREENLAYOUT_SIZE_LARGE -> "Large"
        Configuration.SCREENLAYOUT_SIZE_XLARGE -> "XLarge"
        else -> "Undefined"
    }

fun orientationText(orientation: Int): String = when (orientation) {
    Configuration.ORIENTATION_PORTRAIT -> "Portrait"
    Configuration.ORIENTATION_LANDSCAPE -> "Landscape"
    else -> "Undefined"
}
