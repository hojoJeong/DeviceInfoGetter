package com.example.deviceinfogetter.ui.screen

import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.hardware.display.DisplayManager
import android.os.Build
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.deviceinfogetter.DeviceInfoDto
import com.example.deviceinfogetter.DeviceInfoTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceInfoScreen(
    testInfo: List<DeviceInfoDto>? = null,
    moveToSquareScreen: () -> Unit,
    ) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    val deviceInfo = remember<List<DeviceInfoDto>> {
        testInfo?: getDeviceInfo(context, configuration)
    }

    Scaffold { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Button(
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(10.dp),
                    onClick = moveToSquareScreen
                ) { Text("Check the square") }
            }
            items(deviceInfo.size) { index ->
                DeviceInfoItem(deviceInfo[index].label, deviceInfo[index].value)
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

    val dpiCategory = { dpi: Int ->
        when (dpi) {
            in 0..120 -> "LDPI (120)"
            in 121..160 -> "MDPI (160)"
            in 161..240 -> "HDPI (240)"
            in 241..320 -> "XHDPI (320)"
            in 321..480 -> "XXHDPI (480)"
            in 481..640 -> "XXXHDPI (640)"
            else -> "Unknown"
        }
    }

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
            Point().also { windowManager.defaultDisplay.getRealSize(it) }.let { it.x to it.y }
        }

        val screenSizeText = when (configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) {
            Configuration.SCREENLAYOUT_SIZE_SMALL -> "Small"
            Configuration.SCREENLAYOUT_SIZE_NORMAL -> "Normal"
            Configuration.SCREENLAYOUT_SIZE_LARGE -> "Large"
            Configuration.SCREENLAYOUT_SIZE_XLARGE -> "XLarge"
            else -> "Undefined"
        }

        infoList += listOf(
            DeviceInfoDto("Resolution", "$prefix ${metrics.widthPixels} × ${metrics.heightPixels} px"),
            DeviceInfoDto("Density", "$prefix ${metrics.density}x"),
            DeviceInfoDto("DPI", "$prefix ${metrics.densityDpi} dpi"),
            DeviceInfoDto("DPI Category", "$prefix ${dpiCategory(metrics.densityDpi)}"),
            DeviceInfoDto("Ratio", "$prefix ${"%.2f:1".format(maxOf(w, h).toFloat() / minOf(w, h))}"),
            DeviceInfoDto("Display Size (dp)", "$prefix ${"%.2f".format(w / metrics.density)} × ${"%.2f".format(h / metrics.density)} dp"),
            DeviceInfoDto("Display Size Category", "$prefix $screenSizeText"),
        )
    }

    val orientation = when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> "Portrait"
        Configuration.ORIENTATION_LANDSCAPE -> "Landscape"
        else -> "Undefined"
    }
    infoList += DeviceInfoDto("Orientation", orientation)

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
            DeviceInfoScreen(testDeviceInfo, {})
        }
    }
}
