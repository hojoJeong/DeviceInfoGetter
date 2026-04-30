package com.example.deviceinfogetter.data

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
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

interface ScreenshotRepository {
    fun takeScreenshot(view: View, context: Context)
}

@Singleton
class ScreenshotRepositoryImpl @Inject constructor() : ScreenshotRepository {

    override fun takeScreenshot(view: View, context: Context) {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PixelCopy.request(
                (context as Activity).window,
                bitmap,
                { result ->
                    if (result == PixelCopy.SUCCESS) saveBitmapToGallery(context, bitmap)
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
}
