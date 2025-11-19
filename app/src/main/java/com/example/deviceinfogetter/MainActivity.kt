package com.example.deviceinfogetter

import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.hardware.display.DisplayManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            DeviceInfoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DeviceInfoScreen()
                }
            }
        }
    }

}

@Composable
fun DeviceInfoTheme(content: @Composable () -> Unit) {

    MaterialTheme(
        colorScheme = lightColorScheme(),
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceInfoScreen(testInfo: List<DeviceInfoDto>? = null) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    val deviceInfo = remember<List<DeviceInfoDto>> {
        testInfo?: getDeviceInfo(context, configuration)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Device Information") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(deviceInfo.size){index ->
                    DeviceInfoItem(deviceInfo[index].label, deviceInfo[index].value)}
            }
        }
    }
}

@Composable
fun DeviceInfoItem(label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

fun getDeviceInfo(context: Context, configuration: Configuration): List<DeviceInfoDto> {

    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val displayManager = context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager

    // Default Info
    val infoList = mutableListOf<DeviceInfoDto>(
        DeviceInfoDto("Device Name",  Settings.Global.getString(context.contentResolver, "default_device_name")),
        DeviceInfoDto("Model Name" , Build.MODEL),
        DeviceInfoDto("Manufacture",  Build.MANUFACTURER),
    )

    Log.d("@#$@#$", "getDeviceInfo: ${displayManager.displays.size} ")
    displayManager.displays.forEachIndexed { index, display ->

        val metrics = DisplayMetrics()
        display.getRealMetrics(metrics)

        // Resolution
        infoList.add(DeviceInfoDto("Resolution", "screen number : $index, ${metrics.widthPixels} × ${metrics.heightPixels} px"))

        // DPI
        infoList.add(DeviceInfoDto("Density", "screen number : $index, ${metrics.density}x"))
        infoList.add(DeviceInfoDto("DPI", "screen number : $index, ${metrics.densityDpi} dpi"))
        infoList.add(
            DeviceInfoDto("DPI Category", "screen number : $index, "
                + when (metrics.densityDpi) {
                    in 0..120 -> "LDPI (120)"
                    in 121..160 -> "MDPI (160)"
                    in 161..240 -> "HDPI (240)"
                    in 241..320 -> "XHDPI (320)"
                    in 321..480 -> "XXHDPI (480)"
                    in 481..640 -> "XXXHDPI (640)"
                    else -> "Unknown"
                }
            )
        )

        val realSize = Point()
        val aspectRatio = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            val ratio = maxOf(metrics.widthPixels, metrics.heightPixels).toFloat() / minOf(metrics.widthPixels, metrics.heightPixels).toFloat()
            String.format("%.2f:1", ratio)
        } else {

            windowManager.defaultDisplay.getRealSize(realSize)
            val ratio = maxOf(realSize.x, realSize.y).toFloat() / minOf(realSize.x, realSize.y).toFloat()
            String.format("%.2f:1", ratio)
        }
        infoList.add(DeviceInfoDto("Ratio", "screen number : ${index} | $aspectRatio"))

        // Display Size
        infoList.add(DeviceInfoDto("Display Size (dp)", "screen number : ${index} | ${configuration.screenWidthDp} × ${configuration.screenHeightDp} dp"))

        // Display Size Category
        val screenSize = configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
        val screenSizeText = when (screenSize) {
            Configuration.SCREENLAYOUT_SIZE_SMALL -> "Small"
            Configuration.SCREENLAYOUT_SIZE_NORMAL -> "Normal"
            Configuration.SCREENLAYOUT_SIZE_LARGE -> "Large"
            Configuration.SCREENLAYOUT_SIZE_XLARGE -> "XLarge"
            else -> "Undefined"
        }
        infoList.add(DeviceInfoDto("Display Size Category", "screen number : ${index} | $screenSizeText"))
    }

    // Orientation
    val orientation = when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> "Portrait"
        Configuration.ORIENTATION_LANDSCAPE -> "Landscape"
        else -> "Undefined"
    }
    infoList.add(DeviceInfoDto("Orientation", orientation))

    return infoList
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DeviceInfoScreenPreview() {

    val testDeviceInfo = listOf(
        DeviceInfoDto("Device Name", "Galaxy S25"),
        DeviceInfoDto("Model Name", "SM-S123N"),
        DeviceInfoDto("Manufacture", "Samsung"),
        DeviceInfoDto("Resolution", "screen number : 0 | 1080 × 2340 px"),
        DeviceInfoDto("Density", "screen number : 0 | 3.0x"),
        DeviceInfoDto("DPI", "screen number : 0 | 480 dpi"),
        DeviceInfoDto("DPI Category", "screen number : 0 | XXHDPI (480)"),
        DeviceInfoDto("Ratio", "screen number : 0 | 19.5:1"),
        DeviceInfoDto("Display Size (dp)", "360 × 780 dp"),
        DeviceInfoDto("Display Size Category", "Normal"),
        DeviceInfoDto("Orientation", "Portrait")
    )

    DeviceInfoTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            DeviceInfoScreen(testDeviceInfo)
        }
    }
}
