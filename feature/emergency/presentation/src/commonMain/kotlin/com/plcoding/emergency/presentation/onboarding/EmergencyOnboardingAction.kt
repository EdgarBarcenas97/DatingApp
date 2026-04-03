package com.plcoding.emergency.presentation.onboarding

sealed interface EmergencyOnboardingAction {
    data object OnNextStep : EmergencyOnboardingAction
    data object OnPreviousStep : EmergencyOnboardingAction
    data object OnSkip : EmergencyOnboardingAction
    data object OnFinish : EmergencyOnboardingAction
    data object OnAddContactClick : EmergencyOnboardingAction
}
