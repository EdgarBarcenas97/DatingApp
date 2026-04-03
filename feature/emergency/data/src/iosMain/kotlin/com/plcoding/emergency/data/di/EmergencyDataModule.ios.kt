package com.plcoding.emergency.data.di

import com.plcoding.emergency.database.EmergencyDatabaseFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformEmergencyDataModule: Module = module {
    single { EmergencyDatabaseFactory() }
}
