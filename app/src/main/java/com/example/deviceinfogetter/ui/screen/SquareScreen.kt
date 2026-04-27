package com.example.deviceinfogetter.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.deviceinfogetter.DeviceInfoTheme

/**
 * Figma 레솔루션과 비교해보기 위한 Test Screen
 * */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SquareScreen() {

    val BOX_SIZE = 200

    Scaffold {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Gray)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(BOX_SIZE.dp)
                    .background(color = Color.Red)
            ) {
                Text(
                    text = "Size : $BOX_SIZE dp",
                    color = Color.White,
                    fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SquareScreenPreview() {

    DeviceInfoTheme {

        SquareScreen()
    }
}