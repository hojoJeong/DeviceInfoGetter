package com.example.deviceinfogetter.ui.screen

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.deviceinfogetter.DeviceInfoDto
import com.example.deviceinfogetter.DeviceInfoTheme
import com.example.deviceinfogetter.viewmodel.DeviceInfoViewModel

@Composable
fun DeviceInfoScreen(
    viewModel: DeviceInfoViewModel = hiltViewModel(),
    moveToSquareScreen: () -> Unit,
) {
    val deviceInfo by viewModel.deviceInfo.collectAsState()
    DeviceInfoContent(deviceInfo = deviceInfo, moveToSquareScreen = moveToSquareScreen)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeviceInfoContent(
    deviceInfo: List<DeviceInfoDto>,
    moveToSquareScreen: () -> Unit,
) {
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DeviceInfoContentPreview() {
    val previewData = listOf(
        DeviceInfoDto("Device Name", "Galaxy S25"),
        DeviceInfoDto("Model Name", "SM-S123N"),
        DeviceInfoDto("Manufacture", "Samsung"),
        DeviceInfoDto("Resolution", "screen number : 0 | 1080 × 2340 px"),
        DeviceInfoDto("Density", "screen number : 0 | 3.0x"),
        DeviceInfoDto("DPI", "screen number : 0 | 480 dpi"),
        DeviceInfoDto("DPI Category", "screen number : 0 | XXHDPI (480)"),
        DeviceInfoDto("Ratio", "screen number : 0 | 2.17:1"),
        DeviceInfoDto("Display Size (dp)", "screen number : 0 | 360.00 × 780.00 dp"),
        DeviceInfoDto("Display Size Category", "screen number : 0 | Normal"),
        DeviceInfoDto("Orientation", "Portrait")
    )

    DeviceInfoTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            DeviceInfoContent(deviceInfo = previewData, moveToSquareScreen = {})
        }
    }
}
