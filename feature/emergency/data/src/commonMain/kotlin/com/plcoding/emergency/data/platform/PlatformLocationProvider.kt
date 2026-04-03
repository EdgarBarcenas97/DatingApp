package com.plcoding.emergency.data.platform

import com.plcoding.emergency.domain.LocationData
import com.plcoding.emergency.domain.LocationProvider
import kotlinx.coroutines.flow.Flow

expect class PlatformLocationProvider : LocationProvider {
    override fun observeLocation(): Flow<LocationData>
    override suspend fun getLastKnownLocation(): LocationData?
    override fun startTracking()
    override fun stopTracking()
}
