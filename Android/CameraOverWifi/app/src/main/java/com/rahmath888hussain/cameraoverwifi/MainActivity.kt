package com.rahmath888hussain.cameraoverwifi

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.rahmath888hussain.cameraoverwifi.databinding.ActivityMainBinding
import java.util.concurrent.ExecutionException


class MainActivity : AppCompatActivity() {

    val REQUEST_CAMERA_PERMISSION = 1011
    private lateinit var binding: ActivityMainBinding
    private lateinit var previewView: PreviewView
    private val viewModel by viewModels<CameraViewModel>()
    private lateinit var cameraPreviewManager: CameraPreviewManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        setupWindowInsets()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        cameraPreviewManager = CameraPreviewManager(this)


        binding.btnStartStopPreview.setOnClickListener{
            viewModel.togglePreview()
        }

        viewModel.isPreviewEnabled.observe(this) { isEnabled ->
            if (isEnabled) {
                requestCameraPermission()
                binding.btnStartStopPreview.text = "Stop Preview"
            } else {
                cameraPreviewManager.stopCamera()
                binding.btnStartStopPreview.text = "Start Preview"
            }
        }
    }

    private fun init(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        previewView = binding.previewView

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        previewView = binding.previewView
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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
        cameraPreviewManager.startCamera(previewView)
    }

}