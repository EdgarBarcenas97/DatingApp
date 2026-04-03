package com.plcoding.emergency.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.emergency.domain.EmergencyContactRepository
import com.plcoding.emergency.presentation.mappers.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EmergencySettingsViewModel(
    private val repository: EmergencyContactRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EmergencySettingsState())
    val state = combine(
        _state,
        repository.getContacts(),
        repository.getEmergencySettings()
    ) { currentState, contacts, settings ->
        currentState.copy(
            isEnabled = settings.isEnabled,
            contacts = contacts.map { it.toUi() },
            defaultMessage = if (currentState.isEditingMessage) {
                currentState.defaultMessage
            } else {
                settings.defaultMessage
            }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = EmergencySettingsState()
    )

    fun onAction(action: EmergencySettingsAction) {
        when (action) {
            is EmergencySettingsAction.OnToggleEnabled -> {
                viewModelScope.launch {
                    repository.updateEmergencyEnabled(action.enabled)
                }
            }
            is EmergencySettingsAction.OnDeleteContactClick -> {
                _state.update { it.copy(showDeleteConfirmation = action.contactId) }
            }
            EmergencySettingsAction.OnConfirmDelete -> {
                val contactId = _state.value.showDeleteConfirmation ?: return
                viewModelScope.launch {
                    repository.deleteContact(contactId)
                }
                _state.update { it.copy(showDeleteConfirmation = null) }
            }
            EmergencySettingsAction.OnDismissDeleteDialog -> {
                _state.update { it.copy(showDeleteConfirmation = null) }
            }
            is EmergencySettingsAction.OnDefaultMessageChanged -> {
                _state.update { it.copy(defaultMessage = action.message) }
            }
            EmergencySettingsAction.OnEditMessageClick -> {
                _state.update { it.copy(isEditingMessage = true) }
            }
            EmergencySettingsAction.OnSaveMessage -> {
                viewModelScope.launch {
                    repository.updateDefaultMessage(_state.value.defaultMessage)
                }
                _state.update { it.copy(isEditingMessage = false) }
            }
            EmergencySettingsAction.OnCancelEditMessage -> {
                _state.update { it.copy(isEditingMessage = false) }
            }
            else -> Unit
        }
    }
}
