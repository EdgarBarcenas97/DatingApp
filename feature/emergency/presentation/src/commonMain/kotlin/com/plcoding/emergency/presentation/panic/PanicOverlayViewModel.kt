package com.plcoding.emergency.presentation.panic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.emergency.domain.EmergencyContactRepository
import com.plcoding.emergency.domain.PanicService
import com.plcoding.emergency.domain.PanicState
import com.plcoding.emergency.domain.PhoneCaller
import com.plcoding.emergency.domain.ShakeDetector
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PanicOverlayViewModel(
    private val panicService: PanicService,
    private val repository: EmergencyContactRepository,
    private val phoneCaller: PhoneCaller,
    private val shakeDetector: ShakeDetector
) : ViewModel() {

    private val _state = MutableStateFlow(PanicOverlayState())
    private var countdownJob: Job? = null
    private var shakeCount = 0
    private var lastShakeTime = 0L

    val state = combine(
        _state,
        panicService.panicState,
        repository.getEmergencySettings(),
        repository.getContacts()
    ) { currentState, panicState, settings, contacts ->
        currentState.copy(
            panicState = panicState,
            isEmergencyEnabled = settings.isEnabled,
            contactCount = contacts.size
        )
    }
        .onStart {
            observeShake()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = PanicOverlayState()
        )

    fun onAction(action: PanicOverlayAction) {
        when (action) {
            PanicOverlayAction.OnPanicButtonClick -> {
                viewModelScope.launch {
                    panicService.activatePanic()
                    startCountdown()
                }
            }
            PanicOverlayAction.OnConfirmPanic -> {
                countdownJob?.cancel()
                viewModelScope.launch {
                    panicService.confirmPanic()
                }
            }
            PanicOverlayAction.OnCancelPanic -> {
                countdownJob?.cancel()
                _state.update { it.copy(countdownSeconds = 5) }
                panicService.cancelConfirmation()
            }
            PanicOverlayAction.OnDeactivatePanic -> {
                viewModelScope.launch {
                    panicService.deactivatePanic()
                }
            }
            PanicOverlayAction.OnCallEmergency -> {
                phoneCaller.callEmergencyNumber()
            }
        }
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        _state.update { it.copy(countdownSeconds = 5) }
        countdownJob = viewModelScope.launch {
            for (i in 5 downTo 1) {
                _state.update { it.copy(countdownSeconds = i) }
                delay(1000L)
            }
            panicService.confirmPanic()
        }
    }

    private fun observeShake() {
        shakeDetector.start()
        shakeDetector.observeShake()
            .onEach {
                val now = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
                if (now - lastShakeTime > 3000L) {
                    shakeCount = 0
                }
                shakeCount++
                lastShakeTime = now

                if (shakeCount >= 3 && _state.value.isEmergencyEnabled && _state.value.panicState == PanicState.IDLE) {
                    panicService.activatePanic()
                    startCountdown()
                    shakeCount = 0
                }
            }
            .launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        shakeDetector.stop()
    }
}
