package com.plcoding.emergency.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.emergency.domain.EmergencyContactRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EmergencyOnboardingViewModel(
    private val repository: EmergencyContactRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EmergencyOnboardingState())
    val state = _state.asStateFlow()

    fun onAction(action: EmergencyOnboardingAction) {
        when (action) {
            EmergencyOnboardingAction.OnNextStep -> {
                _state.update {
                    if (it.currentStep < it.totalSteps - 1) {
                        it.copy(currentStep = it.currentStep + 1)
                    } else it
                }
            }
            EmergencyOnboardingAction.OnPreviousStep -> {
                _state.update {
                    if (it.currentStep > 0) {
                        it.copy(currentStep = it.currentStep - 1)
                    } else it
                }
            }
            EmergencyOnboardingAction.OnFinish -> {
                viewModelScope.launch {
                    repository.updateEmergencyEnabled(true)
                }
            }
            else -> Unit
        }
    }
}
