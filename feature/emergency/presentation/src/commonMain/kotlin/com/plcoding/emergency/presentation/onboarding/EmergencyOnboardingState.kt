package com.plcoding.emergency.presentation.onboarding

data class EmergencyOnboardingState(
    val currentStep: Int = 0,
    val totalSteps: Int = 3
)
