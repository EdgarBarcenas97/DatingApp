package com.plcoding.emergency.data.platform

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import com.plcoding.emergency.domain.LocationData
import com.plcoding.emergency.domain.LocationProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow

actual class PlatformLocationProvider(
    private val context: Context
) : LocationProvider {

    private val locationManager by lazy {
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private var isTracking = false

    @SuppressLint("MissingPermission")
    override fun observeLocation(): Flow<LocationData> = callbackFlow {
        val listener = LocationListener { location ->
            trySend(location.toLocationData())
        }

        try {
            locationManager.requestLocationUpdates(
                LocationManager.FUSED_PROVIDER,
                5000L,
                10f,
                listener
            )
        } catch (e: Exception) {
            try {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000L,
                    10f,
                    listener
                )
            } catch (e2: Exception) {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    5000L,
                    10f,
                    listener
                )
            }
        }

        awaitClose {
            locationManager.removeUpdates(listener)
        }
    }

    @SuppressLint("MissingPermission")
    override suspend fun getLastKnownLocation(): LocationData? {
        return try {
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            location?.toLocationData()
        } catch (e: Exception) {
            null
        }
    }

    override fun startTracking() {
        isTracking = true
    }

    override fun stopTracking() {
        isTracking = false
    }

    private fun Location.toLocationData() = LocationData(
        latitude = latitude,
        longitude = longitude,
        accuracy = accuracy,
        timestamp = time
    )
}
