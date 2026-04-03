package com.plcoding.emergency.data.platform

import com.plcoding.emergency.domain.LocationData
import com.plcoding.emergency.domain.LocationProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

actual class PlatformLocationProvider : LocationProvider {
    override fun observeLocation(): Flow<LocationData> = emptyFlow()
    override suspend fun getLastKnownLocation(): LocationData? = null
    override fun startTracking() {}
    override fun stopTracking() {}
}
