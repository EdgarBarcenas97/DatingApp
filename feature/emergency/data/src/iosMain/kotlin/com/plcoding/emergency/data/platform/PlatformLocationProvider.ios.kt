package com.plcoding.emergency.data.platform

import com.plcoding.emergency.domain.LocationData
import com.plcoding.emergency.domain.LocationProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.darwin.NSObject

actual class PlatformLocationProvider : LocationProvider {

    private val locationFlow = MutableStateFlow<LocationData?>(null)
    private val locationManager = CLLocationManager()

    override fun observeLocation(): Flow<LocationData> = locationFlow.filterNotNull()

    override suspend fun getLastKnownLocation(): LocationData? = locationFlow.value

    override fun startTracking() {
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.requestWhenInUseAuthorization()
        locationManager.startUpdatingLocation()
    }

    override fun stopTracking() {
        locationManager.stopUpdatingLocation()
    }
}
