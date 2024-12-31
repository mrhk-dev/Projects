package com.rahmath888hussain.cameraoverwifi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CameraViewModel : ViewModel() {

    private val _isPreviewEnabled = MutableLiveData<Boolean>().apply { value = false }
    val isPreviewEnabled: LiveData<Boolean> get() = _isPreviewEnabled

    fun togglePreview() {
        _isPreviewEnabled.value = !(_isPreviewEnabled.value ?: false)
    }
}