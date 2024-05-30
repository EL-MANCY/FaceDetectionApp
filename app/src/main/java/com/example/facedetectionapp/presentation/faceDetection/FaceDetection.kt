package com.example.facedetectionapp.presentation.faceDetection

import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.graphics.Rect
import android.hardware.camera2.CameraCharacteristics
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.facedetectionapp.presentation.MainActivity
import com.example.facedetectionapp.presentation.utils.resizePreviewImage
import com.example.facedetectionapp.presentation.utils.rotateBitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetector

class FaceDetection(
    private val activity: MainActivity,
    private val faceDetector: FaceDetector,
    private val onFacesDetected: (faces: List<Face>) -> Unit,
    private val onFaceCropped: (face: Bitmap) -> Unit
) : ImageAnalysis.Analyzer {

    private var isFaceDetectorOn = true

    override fun analyze(imageProxy: ImageProxy) {
        if (!isFaceDetectorOn) {
            imageProxy.close()
            return
        }
        if (imageProxy.format == PixelFormat.RGBA_8888) {
            val rotationDegrees = imageProxy.imageInfo.rotationDegrees.toFloat()
            val bitmapBuffer = imageProxy.planes[0].buffer
            val bitmap =
                Bitmap.createBitmap(imageProxy.width, imageProxy.height, Bitmap.Config.ARGB_8888)
            bitmap.copyPixelsFromBuffer(bitmapBuffer)

            // Rotate and resize the Bitmap for better Quality
            val rotatedBitmap = bitmap?.let { it.rotateBitmap(rotationDegrees) }
            val resizedBitmap = rotatedBitmap?.let {
                it.resizePreviewImage(
                    activity.binding.graphicOverlay.width,
                    activity.binding.graphicOverlay.height
                )
            }

            processImage(resizedBitmap, imageProxy)
        } else {
            imageProxy.close()
        }
    }

    private fun processImage(bitmap: Bitmap?, imageProxy: ImageProxy) {
        val image = bitmap?.let { InputImage.fromBitmap(it, 0) }
        if (image != null) {
            activity.binding.graphicOverlay.setCameraInfo(
                image.width,
                image.height,
                CameraCharacteristics.LENS_FACING_BACK
            )
        }
        //The Magic starts here ::
        if (image != null) {
            this.faceDetector.process(image)
                .addOnSuccessListener { faces ->
                    handleFaceDetectionSuccess(faces, bitmap)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }

    private fun handleFaceDetectionSuccess(
        faces: List<Face>,
        bitmapImage: Bitmap
    ) {
        if (faces.isNotEmpty()) {
            onFacesDetected(faces)
        }
        faces.forEach { face ->
            try {
                val faceBitmap = cropFaceBitmap(bitmapImage, face.boundingBox)
                faceBitmap?.let { onFaceCropped(it) }
            } catch (e: Exception) {
                Log.e(TAG, "Error: $e")
            }
        }
    }

    private fun cropFaceBitmap(bitmapFaceImage: Bitmap, boundingBox: Rect): Bitmap? {
        val left = boundingBox.left.coerceAtLeast(0)
        val top = boundingBox.top.coerceAtLeast(0)
        val width = boundingBox.width().coerceAtMost(bitmapFaceImage.width - left)
        val height = boundingBox.height().coerceAtMost(bitmapFaceImage.height - top)

        return bitmapFaceImage.let {
            Bitmap.createBitmap(it, left, top, width, height)
        }
    }

    fun start() {
        isFaceDetectorOn = true
    }

    fun stop() {
        isFaceDetectorOn = false
    }

    companion object {
        const val TAG = "FaceDetection"
    }
}