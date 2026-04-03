package com.plcoding.emergency.presentation.di

import com.plcoding.emergency.presentation.add_edit_contact.AddEditContactViewModel
import com.plcoding.emergency.presentation.onboarding.EmergencyOnboardingViewModel
import com.plcoding.emergency.presentation.settings.EmergencySettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val emergencyPresentationModule = module {
    viewModelOf(::EmergencySettingsViewModel)
    viewModelOf(::AddEditContactViewModel)
    viewModelOf(::EmergencyOnboardingViewModel)
}
