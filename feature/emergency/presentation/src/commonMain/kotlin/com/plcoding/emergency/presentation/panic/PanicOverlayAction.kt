package com.plcoding.emergency.presentation.panic

sealed interface PanicOverlayAction {
    data object OnPanicButtonClick : PanicOverlayAction
    data object OnConfirmPanic : PanicOverlayAction
    data object OnCancelPanic : PanicOverlayAction
    data object OnDeactivatePanic : PanicOverlayAction
    data object OnCallEmergency : PanicOverlayAction
}
