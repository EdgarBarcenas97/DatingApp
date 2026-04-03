package com.plcoding.emergency.data.platform

import com.plcoding.emergency.domain.ShakeDetector
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

actual class PlatformShakeDetector : ShakeDetector {
    private val shakeFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    override fun observeShake(): Flow<Unit> = shakeFlow
    override fun start() {
        // CMMotionManager implementation would go here
    }
    override fun stop() {}
}
