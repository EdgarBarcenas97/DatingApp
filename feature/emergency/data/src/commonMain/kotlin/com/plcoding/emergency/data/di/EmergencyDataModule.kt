package com.plcoding.emergency.data.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.plcoding.emergency.data.LocalEmergencyContactRepository
import com.plcoding.emergency.database.EmergencyDatabaseFactory
import com.plcoding.emergency.domain.EmergencyContactRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformEmergencyDataModule: Module

val emergencyDataModule = module {
    includes(platformEmergencyDataModule)

    singleOf(::LocalEmergencyContactRepository) bind EmergencyContactRepository::class
    single {
        get<EmergencyDatabaseFactory>()
            .create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<com.plcoding.emergency.database.EmergencyDatabase>().emergencyContactDao }
    single { get<com.plcoding.emergency.database.EmergencyDatabase>().emergencySettingsDao }
}
