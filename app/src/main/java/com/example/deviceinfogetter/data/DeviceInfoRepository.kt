package com.example.deviceinfogetter.data

import android.content.Context
import android.graphics.Point
import android.hardware.display.DisplayManager
import android.os.Build
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.WindowManager
import com.example.deviceinfogetter.DeviceInfoDto
import com.example.deviceinfogetter.util.dpiCategory
import com.example.deviceinfogetter.util.orientationText
import com.example.deviceinfogetter.util.screenSizeText
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

interface DeviceInfoRepository {
    fun getDeviceInfo(): List<DeviceInfoDto>
}

@Singleton
class DeviceInfoRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : DeviceInfoRepository {

    override fun getDeviceInfo(): List<DeviceInfoDto> {
        val configuration = context.resources.configuration
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayManager = context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager

        val infoList = mutableListOf(
            DeviceInfoDto("Device Name", Settings.Global.getString(context.contentResolver, "default_device_name")),
            DeviceInfoDto("Model Name", Build.MODEL),
            DeviceInfoDto("Manufacture", Build.MANUFACTURER),
        )

        displayManager.displays.forEachIndexed { index, display ->
            val metrics = DisplayMetrics().also { display.getRealMetrics(it) }
            val prefix = "screen number : $index |"

            val (w, h) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                metrics.widthPixels to metrics.heightPixels
            } else {
                @Suppress("DEPRECATION")
                Point().also { windowManager.defaultDisplay.getRealSize(it) }.let { it.x to it.y }
            }

            infoList += listOf(
                DeviceInfoDto("Resolution", "$prefix ${metrics.widthPixels} × ${metrics.heightPixels} px"),
                DeviceInfoDto("Density", "$prefix ${metrics.density}x"),
                DeviceInfoDto("DPI", "$prefix ${metrics.densityDpi} dpi"),
                DeviceInfoDto("DPI Category", "$prefix ${dpiCategory(metrics.densityDpi)}"),
                DeviceInfoDto("Ratio", "$prefix ${"%.2f:1".format(maxOf(w, h).toFloat() / minOf(w, h))}"),
                DeviceInfoDto("Display Size (dp)", "$prefix ${"%.2f".format(w / metrics.density)} × ${"%.2f".format(h / metrics.density)} dp"),
                DeviceInfoDto("Display Size Category", "$prefix ${screenSizeText(configuration.screenLayout)}"),
            )
        }

        infoList += DeviceInfoDto("Orientation", orientationText(configuration.orientation))
        return infoList
    }
}
