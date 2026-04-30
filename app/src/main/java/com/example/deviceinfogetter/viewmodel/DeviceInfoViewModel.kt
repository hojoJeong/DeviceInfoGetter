package com.example.deviceinfogetter.viewmodel

import androidx.lifecycle.ViewModel
import com.example.deviceinfogetter.DeviceInfoDto
import com.example.deviceinfogetter.data.DeviceInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DeviceInfoViewModel @Inject constructor(
    private val deviceInfoRepository: DeviceInfoRepository
) : ViewModel() {

    private val _deviceInfo = MutableStateFlow<List<DeviceInfoDto>>(emptyList())
    val deviceInfo: StateFlow<List<DeviceInfoDto>> = _deviceInfo.asStateFlow()

    init {
        _deviceInfo.value = deviceInfoRepository.getDeviceInfo()
    }
}
