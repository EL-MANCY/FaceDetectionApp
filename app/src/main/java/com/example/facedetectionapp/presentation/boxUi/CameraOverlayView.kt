package com.example.facedetectionapp.presentation.boxUi

import android.content.Context
import android.graphics.Canvas
import android.hardware.camera2.CameraCharacteristics
import android.util.AttributeSet
import android.view.View

class CameraOverlayView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val lock = Any()
    private var previewWidth = 0

    internal var widthScaleFactor = 1.0f

    private var previewHeight = 0

    internal var heightScaleFactor = 1.0f

    internal var facing = CameraCharacteristics.LENS_FACING_BACK

    private val graphics: MutableSet<CameraOverlay> = mutableSetOf()

    fun clear() {
        synchronized(lock) {
            graphics.clear()
        }
        postInvalidate()
    }

    fun add(graphic: CameraOverlay) {
        synchronized(lock) {
            graphics.add(graphic)
        }
        postInvalidate()
    }

    fun setCameraInfo(previewWidth: Int, previewHeight: Int, facing: Int) {
        synchronized(lock) {
            this.previewWidth = previewWidth
            this.previewHeight = previewHeight
            this.facing = facing
        }
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        synchronized(lock) {
            if (previewWidth != 0 && previewHeight != 0) {
                widthScaleFactor = width.toFloat() / previewWidth
                heightScaleFactor = height.toFloat() / previewHeight
            }
            for (graphic in graphics) {
                graphic.draw(canvas)
            }
        }
    }
}