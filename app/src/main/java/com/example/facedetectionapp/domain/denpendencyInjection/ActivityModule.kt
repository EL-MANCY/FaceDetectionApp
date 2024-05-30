package com.example.facedetectionapp.domain.denpendencyInjection

import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {
    @Provides
    fun provideExecutorService(): ExecutorService {
        return  Executors.newSingleThreadExecutor()
    }

    @Provides
    fun provideFaceDetector(): FaceDetector {
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .build()

        return FaceDetection.getClient(options)
    }
}