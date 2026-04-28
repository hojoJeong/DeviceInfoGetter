package com.example.deviceinfogetter.ui.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.PixelCopy
import android.view.View
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.deviceinfogetter.DeviceInfoTheme
import java.io.File
import java.io.FileOutputStream

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SquareScreen() {
    val DEFAULT_BOX_SIZE = 200
    val context = LocalContext.current
    val view = LocalView.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            takeScreenshot(view, context)
        } else {
            Toast.makeText(context, "저장 권한이 필요합니다", Toast.LENGTH_SHORT).show()
        }
    }

    var boxSize by remember { mutableIntStateOf(DEFAULT_BOX_SIZE) }
    var boxSizeText by remember { mutableStateOf(DEFAULT_BOX_SIZE.toString()) }

    Scaffold {
        ConstraintLayout (
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Gray)
        ) {
            val (textField, box, button) = createRefs()

            // Input Box Size
            TextField(
                value = boxSizeText,
                onValueChange = {
                    boxSizeText = it
                    it.toIntOrNull()?.let { size -> boxSize = size }
                },
                modifier = Modifier
                    .constrainAs(textField) {
                        bottom.linkTo(box.top)
                        start.linkTo(box.start)
                        end.linkTo(box.end)
                    }
                    .padding(bottom = 20.dp),
                label = { Text("Box Size") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            // Red Box
            Box(
                modifier = Modifier
                    .size(boxSize.dp)
                    .background(color = Color.Red)
                    .constrainAs(box){
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


            // Screenshot Button
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
                        takeScreenshot(view, context)
                    }
                }
            ) {
                Text("Screenshot")
            }
        }
    }
}

private fun takeScreenshot(view: View, context: Context) {
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        PixelCopy.request(
            (context as Activity).window,
            bitmap,
            { result ->
                if (result == PixelCopy.SUCCESS) {
                    saveBitmapToGallery(context, bitmap)
                }
            },
            Handler(Looper.getMainLooper())
        )
    } else {
        view.draw(Canvas(bitmap))
        saveBitmapToGallery(context, bitmap)
    }
}

private fun saveBitmapToGallery(context: Context, bitmap: Bitmap) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "screenshot_${System.currentTimeMillis()}.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_DCIM}/Screenshots")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
        val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        uri?.let {
            context.contentResolver.openOutputStream(it)?.use { stream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            }
            values.clear()
            values.put(MediaStore.Images.Media.IS_PENDING, 0)
            context.contentResolver.update(it, values, null, null)
        }
    } else {
        val dir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
            "Screenshots"
        )
        dir.mkdirs()
        val file = File(dir, "screenshot_${System.currentTimeMillis()}.png")
        FileOutputStream(file).use { stream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        }
        MediaScannerConnection.scanFile(context, arrayOf(file.absolutePath), null, null)
    }
    Toast.makeText(context, "스크린샷이 저장되었습니다", Toast.LENGTH_SHORT).show()
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SquareScreenPreview() {
    DeviceInfoTheme {
        SquareScreen()
    }
}
