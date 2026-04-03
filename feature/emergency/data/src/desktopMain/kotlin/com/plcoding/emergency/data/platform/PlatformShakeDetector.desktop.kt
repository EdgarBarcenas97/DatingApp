package com.plcoding.emergency.data.platform

import com.plcoding.emergency.domain.ShakeDetector
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

actual class PlatformShakeDetector : ShakeDetector {
    override fun observeShake(): Flow<Unit> = emptyFlow()
    override fun start() {}
    override fun stop() {}
}
