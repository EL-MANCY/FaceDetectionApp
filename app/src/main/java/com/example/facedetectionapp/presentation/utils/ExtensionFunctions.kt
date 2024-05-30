package com.example.facedetectionapp.presentation.utils

import android.content.ContentValues
import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

fun Bitmap.rotateBitmap(angle: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
}

fun Bitmap.resizePreviewImage(width: Int, height: Int): Bitmap {
    val scaleFactor: Float = Math.max(
        this.width.toFloat() / width.toFloat(),
        this.height.toFloat() / height.toFloat()
    )

    return Bitmap.createScaledBitmap(
        this,
        (this.width / scaleFactor).toInt(),
        (this.height / scaleFactor).toInt(),
        true
    )
}


fun Bitmap.saveBitmapToGallery(context: Context, displayName: String): String? {
    val resolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, "$displayName.png")
        put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }
    }

    val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    imageUri?.let { uri ->
        val outputStream: OutputStream? = resolver.openOutputStream(uri)
        outputStream?.use {
            this.compress(Bitmap.CompressFormat.PNG, 90, it)
            it.flush()
        }
        return getAbsolutePathFromUri(context, uri)
    }

    return null
}

private fun getAbsolutePathFromUri(context: Context, uri: android.net.Uri): String? {
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            return cursor.getString(columnIndex)
        }
    }
    return null
}