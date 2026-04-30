package com.example.deviceinfogetter.ui.screen

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.deviceinfogetter.DeviceInfoTheme
import com.example.deviceinfogetter.viewmodel.SquareViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SquareScreen(
    viewModel: SquareViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val view = LocalView.current
    val boxSize by viewModel.boxSize.collectAsState()
    val boxSizeText by viewModel.boxSizeText.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            viewModel.takeScreenshot(view, context)
        } else {
            Toast.makeText(context, "저장 권한이 필요합니다", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Gray)
        ) {
            val (textField, box, button) = createRefs()

            TextField(
                value = boxSizeText,
                onValueChange = viewModel::onBoxSizeTextChange,
                modifier = Modifier
                    .constrainAs(textField) {
                        bottom.linkTo(box.top)
                        start.linkTo(box.start)
                        end.linkTo(box.end)
                    }
                    .padding(bottom = 20.dp),
                label = { Text("Box Size") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Box(
                modifier = Modifier
                    .size(boxSize.dp)
                    .background(color = Color.Red)
                    .constrainAs(box) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Size : $boxSize dp",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                modifier = Modifier
                    .constrainAs(button) {
                        top.linkTo(box.bottom)
                        start.linkTo(box.start)
                        end.linkTo(box.end)
                    }
                    .padding(top = 20.dp),
                onClick = {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                        permissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    } else {
                        viewModel.takeScreenshot(view, context)
                    }
                }
            ) {
                Text("Screenshot")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SquareScreenPreview() {
    DeviceInfoTheme {
        SquareScreen()
    }
}
