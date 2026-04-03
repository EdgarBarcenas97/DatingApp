package com.plcoding.emergency.data

import com.plcoding.emergency.domain.AudioRecorder
import com.plcoding.emergency.domain.EmergencyContactRepository
import com.plcoding.emergency.domain.LocationData
import com.plcoding.emergency.domain.LocationProvider
import com.plcoding.emergency.domain.PanicService
import com.plcoding.emergency.domain.PanicState
import com.plcoding.emergency.domain.SmsSender
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DefaultPanicService(
    private val locationProvider: LocationProvider,
    private val audioRecorder: AudioRecorder,
    private val smsSender: SmsSender,
    private val repository: EmergencyContactRepository,
    private val scope: CoroutineScope
) : PanicService {

    private val _panicState = MutableStateFlow(PanicState.IDLE)
    override val panicState: StateFlow<PanicState> = _panicState.asStateFlow()

    override val currentLocation: Flow<LocationData?> = locationProvider.observeLocation()

    override suspend fun activatePanic() {
        if (_panicState.value == PanicState.ACTIVE) return
        _panicState.value = PanicState.CONFIRMING
    }

    override suspend fun confirmPanic() {
        if (_panicState.value != PanicState.CONFIRMING) return
        _panicState.value = PanicState.ACTIVE

        locationProvider.startTracking()

        scope.launch {
            try {
                audioRecorder.startRecording()
            } catch (_: Exception) {}
        }

        scope.launch {
            val settings = repository.getEmergencySettings().first()
            val contacts = repository.getContacts().first()

            if (contacts.isEmpty()) return@launch

            val location = locationProvider.getLastKnownLocation()
            val locationText = if (location != null) {
                "\nhttps://maps.google.com/?q=${location.latitude},${location.longitude}"
            } else ""

            val message = "${settings.defaultMessage}$locationText"
            smsSender.sendToAllContacts(contacts, message)
        }
    }

    override suspend fun deactivatePanic() {
        _panicState.value = PanicState.COOLDOWN
        locationProvider.stopTracking()

        try {
            audioRecorder.stopRecording()
        } catch (_: Exception) {}

        _panicState.value = PanicState.IDLE
    }

    override fun cancelConfirmation() {
        if (_panicState.value == PanicState.CONFIRMING) {
            _panicState.value = PanicState.IDLE
        }
    }
}
