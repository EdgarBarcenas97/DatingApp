package com.plcoding.emergency.data.platform

import com.plcoding.emergency.domain.ShakeDetector
import kotlinx.coroutines.flow.Flow

expect class PlatformShakeDetector : ShakeDetector {
    override fun observeShake(): Flow<Unit>
    override fun start()
    override fun stop()
}
