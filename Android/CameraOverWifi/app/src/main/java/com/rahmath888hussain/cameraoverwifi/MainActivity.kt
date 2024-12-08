package com.rahmath888hussain.cameraoverwifi

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.concurrent.ExecutionException


class MainActivity : AppCompatActivity() {

    val REQUEST_CAMERA_PERMISSION = 1011
    private lateinit var previewView: PreviewView
    private var previewEnabled: Boolean = false
    private lateinit var startStopButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        previewView = findViewById(R.id.previewView)
        startStopButton = findViewById(R.id.btn_startStopPreview)
        requestCameraPermission()

        startStopButton.setOnClickListener {
            if (previewEnabled) {
                stopCameraPreview()
                startStopButton.text = "Start Preview"
            } else {
                requestCameraPermission() // Check permission again when enabling
                startStopButton.text = "Stop Preview"
            }
            previewEnabled = !previewEnabled // Toggle preview state
        }

    }

    private fun requestCameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, "android.permission.CAMERA") != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf("android.permission.CAMERA"), REQUEST_CAMERA_PERMISSION)
        } else {
            startCamera()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            previewView.viewTreeObserver.addOnGlobalLayoutListener {
                val width = previewView.width
                val height = (width * 0.8).toInt()
                previewView.layoutParams.height = height
                previewView.requestLayout()
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview)
            } catch (exc: Exception) {
                Log.e("CameraPreview", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun stopCameraPreview() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = try {
                cameraProviderFuture.get()
            } catch (e: ExecutionException) {
                Log.e("CameraPreview", "Unable to get camera provider", e)
                return@addListener
            }
            cameraProvider.unbindAll()
        }, ContextCompat.getMainExecutor(this))
    }

}