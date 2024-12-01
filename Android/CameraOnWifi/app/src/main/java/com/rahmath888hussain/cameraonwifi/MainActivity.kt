package com.rahmath888hussain.cameraonwifi

import android.os.Bundle
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
import androidx.lifecycle.LifecycleOwner
import com.rahmath888hussain.cameraonwifi.R
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var cameraExecutor: ExecutorService

    private val requiredPermissions = arrayOf(
        android.Manifest.permission.CAMERA
    )

    private val REQUEST_CODE_PERMISSIONS = 10
    private lateinit var previewView: PreviewView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        // Edge-to-edge layout handling
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize PreviewView
        previewView = findViewById(R.id.previewView)

        // Check if permissions are granted
        if (allPermissionsGranted(requiredPermissions)) {
            startCamera()
        } else {
            requestPermissions(requiredPermissions)
        }

        // Initialize camera executor
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun requestPermissions(requiredPermissions: Array<String>) {
        ActivityCompat.requestPermissions(
            this, requiredPermissions, REQUEST_CODE_PERMISSIONS
        )
    }

    private fun allPermissionsGranted(requiredPermissions: Array<String>): Boolean {
        return requiredPermissions.all {
            ContextCompat.checkSelfPermission(this, it) == android.content.pm.PackageManager.PERMISSION_GRANTED
        }
    }

    private fun startCamera() {
        // Get the camera provider
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // Create the Preview use case
            val preview = Preview.Builder().build().also {
                // Set the surface provider to the PreviewView
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            // Select the back camera
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind any existing use cases and bind new use cases
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview)
            } catch (exc: Exception) {
                // Handle any exceptions
                exc.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }
}
