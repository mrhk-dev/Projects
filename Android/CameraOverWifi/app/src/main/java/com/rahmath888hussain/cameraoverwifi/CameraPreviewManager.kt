package com.rahmath888hussain.cameraoverwifi

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview.Builder
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutionException

class CameraPreviewManager(private val context: Context) {
    private var isPreviewing = false

    fun startCamera(previewView: PreviewView) {
        if (isPreviewing) return // Prevent starting if already previewing

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            previewView.viewTreeObserver.addOnGlobalLayoutListener {
                val width = previewView.width
                val height = (width * 0.6).toInt()
                previewView.layoutParams.height = height
                previewView.requestLayout()
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll() // Unbind use cases before rebinding
                cameraProvider.bindToLifecycle(
                    (context as AppCompatActivity),
                    cameraSelector,
                    preview
                )
                isPreviewing = true // Update state to reflect that we are now previewing
            } catch (exc: Exception) {
                Log.e("CameraPreview", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    fun stopCamera() {
        if (!isPreviewing) return // Prevent stopping if not currently previewing

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = try {
                cameraProviderFuture.get()
            } catch (e: ExecutionException) {
                Log.e("CameraPreview", "Unable to get camera provider", e)
                return@addListener
            }
            cameraProvider.unbindAll()
            isPreviewing = false // Update state to reflect that we are no longer previewing
        }, ContextCompat.getMainExecutor(context))
    }
}