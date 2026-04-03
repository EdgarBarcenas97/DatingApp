package com.plcoding.emergency.domain

import kotlinx.coroutines.flow.Flow

interface LocationProvider {
    fun observeLocation(): Flow<LocationData>
    suspend fun getLastKnownLocation(): LocationData?
    fun startTracking()
    fun stopTracking()
}
