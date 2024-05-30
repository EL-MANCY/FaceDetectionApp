package com.example.facedetectionapp.presentation.boxUi

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.google.mlkit.vision.face.Face

class FaceBoxBounding(overlay: CameraOverlayView) : CameraOverlay(overlay) {

    companion object {
        private const val BOX_STROKE_WIDTH = 5.0f

        private val colors = arrayOf(
            Color.BLUE,
            Color.WHITE,
            Color.GREEN,
            Color.BLACK,
            Color.DKGRAY,
            Color.GRAY,
            Color.LTGRAY,
            Color.CYAN,
            Color.MAGENTA,
            Color.CYAN,
            Color.RED,
            Color.YELLOW
        )

        private var currentColorIndex = 0
    }

    private val boxPaint: Paint

    @Volatile
    private var face: Face? = null

    init {
        currentColorIndex = (currentColorIndex + 1) % colors.size
        val selectedColor = colors[currentColorIndex]
        boxPaint = Paint().apply {
            color = selectedColor
            style = Paint.Style.STROKE
            strokeWidth = BOX_STROKE_WIDTH
        }
    }

    fun updatedFace(face: Face) {
        this.face = face
        postInvalidate()
    }

    override fun draw(canvas: Canvas) {
        val face = this.face ?: return
        val left = translateX(face.boundingBox.left.toFloat())
        val top = translateY(face.boundingBox.top.toFloat())
        val right = translateX(face.boundingBox.right.toFloat())
        val bottom = translateY(face.boundingBox.bottom.toFloat())
        canvas.drawRect(left, top, right, bottom, boxPaint)
    }
}