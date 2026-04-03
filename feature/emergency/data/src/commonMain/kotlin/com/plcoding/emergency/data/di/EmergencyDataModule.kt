package com.plcoding.emergency.data.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.plcoding.emergency.data.DefaultPanicService
import com.plcoding.emergency.data.LocalEmergencyContactRepository
import com.plcoding.emergency.data.platform.PlatformAudioRecorder
import com.plcoding.emergency.data.platform.PlatformLocationProvider
import com.plcoding.emergency.data.platform.PlatformPhoneCaller
import com.plcoding.emergency.data.platform.PlatformShakeDetector
import com.plcoding.emergency.data.platform.PlatformSmsSender
import com.plcoding.emergency.database.EmergencyDatabaseFactory
import com.plcoding.emergency.domain.AudioRecorder
import com.plcoding.emergency.domain.EmergencyContactRepository
import com.plcoding.emergency.domain.LocationProvider
import com.plcoding.emergency.domain.PanicService
import com.plcoding.emergency.domain.PhoneCaller
import com.plcoding.emergency.domain.ShakeDetector
import com.plcoding.emergency.domain.SmsSender
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformEmergencyDataModule: Module

val emergencyDataModule = module {
    includes(platformEmergencyDataModule)

    singleOf(::LocalEmergencyContactRepository) bind EmergencyContactRepository::class
    singleOf(::DefaultPanicService) bind PanicService::class
    singleOf(::PlatformLocationProvider) bind LocationProvider::class
    singleOf(::PlatformAudioRecorder) bind AudioRecorder::class
    singleOf(::PlatformSmsSender) bind SmsSender::class
    singleOf(::PlatformPhoneCaller) bind PhoneCaller::class
    singleOf(::PlatformShakeDetector) bind ShakeDetector::class

    single {
        get<EmergencyDatabaseFactory>()
            .create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<com.plcoding.emergency.database.EmergencyDatabase>().emergencyContactDao }
    single { get<com.plcoding.emergency.database.EmergencyDatabase>().emergencySettingsDao }
}
