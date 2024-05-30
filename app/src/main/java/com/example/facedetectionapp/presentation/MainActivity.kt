package com.example.facedetectionapp.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.facedetectionapp.data.local.entities.PersonEntity
import com.example.facedetectionapp.databinding.ActivityMainBinding
import com.example.facedetectionapp.presentation.boxUi.FaceBoxBounding
import com.example.facedetectionapp.presentation.faceDetection.FaceDetection
import com.example.facedetectionapp.presentation.faceDetection.FaceDetectorViewModel
import com.example.facedetectionapp.presentation.inputDialog.InputDialogFragment
import com.example.facedetectionapp.presentation.inputDialog.InputDialogHandler
import com.example.facedetectionapp.presentation.utils.saveBitmapToGallery
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetector
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ExecutorService
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var faceDetector: FaceDetector

    @Inject
    lateinit var cameraExecutor: ExecutorService

    private var cameraMode = CameraSelector.DEFAULT_BACK_CAMERA

    private lateinit var faceDetection: FaceDetection
    private val viewModel: FaceDetectorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkPermissions()
        initFaceDetection()

        binding.floatingActionButton2.setOnClickListener {
            if (cameraMode == CameraSelector.DEFAULT_BACK_CAMERA) {
                cameraMode = CameraSelector.DEFAULT_FRONT_CAMERA
            } else {
                cameraMode = CameraSelector.DEFAULT_BACK_CAMERA
            }
            startCamera()
        }

    }

    private fun initFaceDetection() {
        faceDetection = FaceDetection(
            activity = this,
            faceDetector = faceDetector,
            onFacesDetected = ::onFacesDetected,
            onFaceCropped = ::onFaceCropped
        )
    }

    private fun onFacesDetected(faces: List<Face>) = with(binding.graphicOverlay) {
        if (faces.isEmpty()) {
            return
        }
        this.clear()
        for (i in faces.indices) {
            val face = faces[i]
            val faceGraphic = FaceBoxBounding(this)
            this.add(faceGraphic)
            faceGraphic.updatedFace(face)
        }
    }

    private fun onFaceCropped(face: Bitmap) {
        binding.floatingActionButton.setOnClickListener {
            faceDetection.stop()
            showInputDialog(face)
        }
    }

    private fun showInputDialog(faceBitmap: Bitmap) {
        val dialog = InputDialogFragment()
        val bitmapPath = faceBitmap.saveBitmapToGallery(this,"face")
        val args = Bundle()
        args.putString("bitmap_path", bitmapPath)
        dialog.arguments = args
        dialog.setListener(object : InputDialogHandler {
            override fun onSave(id: String, name: String) {
                viewModel.saveNewFace(
                    PersonEntity(
                        id = id,
                        name = name,
                        bitmapImage = faceBitmap
                    )
                )
                startFaceDetector()
            }

            override fun onCancel() {
                startFaceDetector()
            }

        })
        dialog.show(supportFragmentManager, "InputDialogFragment")
    }

    private fun startFaceDetector(delayToStartRecognition: Long = 1000) {
        Handler(Looper.getMainLooper()).postDelayed({
            faceDetection.start()
        }, delayToStartRecognition)
    }


    private fun checkPermissions() {
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        resultLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            var permissionGranted = true
            it.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && !it.value) {
                    permissionGranted = false
                }
            }
            if (!permissionGranted) {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            } else {
                startCamera()
            }
        }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        val cameraInstance = ProcessCameraProvider.getInstance(this)

        cameraInstance.addListener({
            val cameraProvider: ProcessCameraProvider = cameraInstance.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewer.surfaceProvider)
                }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setTargetResolution(Size(1280, 720))
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, faceDetection)
                }
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraMode, preview, imageAnalyzer)
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }

        }, ContextCompat.getMainExecutor(this))

    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "main_activity"
        private val REQUIRED_PERMISSIONS = mutableListOf(Manifest.permission.CAMERA)
            .apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()

    }
}