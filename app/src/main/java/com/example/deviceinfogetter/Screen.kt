package com.example.deviceinfogetter

import kotlinx.serialization.Serializable

sealed interface Screen {

    @Serializable
    object DeviceInfo : Screen

    @Serializable
    object Square : Screen
}