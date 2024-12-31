package com.rahmath888hussain.cameraonwifi

import android.os.Bundle
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
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var cameraExecutor: ExecutorService


    private val requiredPermissions = arrayOf(
        android.Manifest.permission.CAMERA
    )

    private val REQUEST_CODE_PERMISSIONS = 10
    private lateinit var previewView: PreviewView
    private lateinit var btnPreview: Button
    private var isPreviewing = false

    private var cameraProvider: ProcessCameraProvider? = null
    private var preview: Preview? = null
    private var cameraSelector: CameraSelector? = null

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
        btnPreview = findViewById(R.id.btn_Preview)

        if (allPermissionsGranted(requiredPermissions)) {
            startCamera()
        } else {
            requestPermissions(requiredPermissions)
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        btnPreview.setOnClickListener {
            if (isPreviewing) {
                stopCameraPreview()
            } else {
                startCameraPreview()
            }
        }
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
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

            preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

//            try {
//                cameraProvider.unbindAll()
//                cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview)
//            } catch (exc: Exception) {
//                // Handle any exceptions
//                exc.printStackTrace()
//            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun startCameraPreview() {
        cameraProvider?.let { cameraProvider ->
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this as LifecycleOwner, cameraSelector!!, preview!!
                )
                isPreviewing = true
            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }
    }

    private fun stopCameraPreview() {
        cameraProvider?.unbindAll()
        isPreviewing = false
    }
}
