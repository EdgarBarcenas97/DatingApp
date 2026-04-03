package com.plcoding.emergency.domain

import kotlinx.coroutines.flow.Flow

interface ShakeDetector {
    fun observeShake(): Flow<Unit>
    fun start()
    fun stop()
}
