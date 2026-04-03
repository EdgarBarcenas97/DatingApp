package com.plcoding.auth.presentation.register

import com.plcoding.core.domain.auth.LookingFor

sealed interface RegisterAction {
    data object OnLoginClick: RegisterAction
    data object OnInputTextFocusGain: RegisterAction
    data object OnRegisterClick: RegisterAction
    data object OnTogglePasswordVisibilityClick: RegisterAction
    data class OnLookingForSelect(val lookingFor: LookingFor): RegisterAction
}