package com.plcoding.emergency.presentation.panic

import com.plcoding.emergency.domain.LocationData
import com.plcoding.emergency.domain.PanicState

data class PanicOverlayState(
    val panicState: PanicState = PanicState.IDLE,
    val isEmergencyEnabled: Boolean = false,
    val contactCount: Int = 0,
    val isRecordingAudio: Boolean = false,
    val currentLocation: LocationData? = null,
    val countdownSeconds: Int = 5
)
