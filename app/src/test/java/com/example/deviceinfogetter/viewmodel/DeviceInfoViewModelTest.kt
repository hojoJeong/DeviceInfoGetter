package com.example.deviceinfogetter.viewmodel

import com.example.deviceinfogetter.DeviceInfoDto
import com.example.deviceinfogetter.data.DeviceInfoRepository
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class DeviceInfoViewModelTest {

    private val testDeviceInfo = listOf(
        DeviceInfoDto("Model Name", "Galaxy S25"),
        DeviceInfoDto("Manufacture", "Samsung"),
        DeviceInfoDto("DPI", "screen number : 0 | 480 dpi"),
    )

    private val deviceInfoRepository = object : DeviceInfoRepository {
        override fun getDeviceInfo() = testDeviceInfo
    }

    @Test
    fun `deviceInfo state reflects data from repository on init`() {
        val viewModel = DeviceInfoViewModel(deviceInfoRepository)

        assertEquals(testDeviceInfo.size, viewModel.deviceInfo.value.size)
    }

    @Test
    fun `deviceInfo state has correct label and value`() {
        val viewModel = DeviceInfoViewModel(deviceInfoRepository)

        assertEquals("Model Name", viewModel.deviceInfo.value[0].label)
        assertEquals("Galaxy S25", viewModel.deviceInfo.value[0].value)
    }

    @Test
    fun `deviceInfo state is empty when repository returns empty list`() {
        val emptyRepository = object : DeviceInfoRepository {
            override fun getDeviceInfo() = emptyList<DeviceInfoDto>()
        }
        val viewModel = DeviceInfoViewModel(emptyRepository)

        assertEquals(0, viewModel.deviceInfo.value.size)
    }
}
