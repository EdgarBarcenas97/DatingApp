package com.plcoding.emergency.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface PanicService {
    val panicState: StateFlow<PanicState>
    val currentLocation: Flow<LocationData?>

    suspend fun activatePanic()
    suspend fun deactivatePanic()
    suspend fun confirmPanic()
    fun cancelConfirmation()
}
