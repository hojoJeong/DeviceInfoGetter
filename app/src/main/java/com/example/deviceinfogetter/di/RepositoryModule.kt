package com.example.deviceinfogetter.di

import com.example.deviceinfogetter.data.DeviceInfoRepository
import com.example.deviceinfogetter.data.DeviceInfoRepositoryImpl
import com.example.deviceinfogetter.data.ScreenshotRepository
import com.example.deviceinfogetter.data.ScreenshotRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindDeviceInfoRepository(impl: DeviceInfoRepositoryImpl): DeviceInfoRepository

    @Binds
    abstract fun bindScreenshotRepository(impl: ScreenshotRepositoryImpl): ScreenshotRepository
}
