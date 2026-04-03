package com.plcoding.emergency.presentation.add_edit_contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.emergency.domain.EmergencyContact
import com.plcoding.emergency.domain.EmergencyContactRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AddEditContactViewModel(
    private val repository: EmergencyContactRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditContactState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<AddEditContactEvent>()
    val events = eventChannel.receiveAsFlow()

    fun loadContact(contactId: String?) {
        if (contactId == null) return
        viewModelScope.launch {
            val contact = repository.getContactById(contactId) ?: return@launch
            _state.update {
                it.copy(
                    contactId = contact.id,
                    name = contact.name,
                    phoneNumber = contact.phoneNumber,
                    relationship = contact.relationship ?: "",
                    isEditing = true
                )
            }
        }
    }

    fun onAction(action: AddEditContactAction) {
        when (action) {
            is AddEditContactAction.OnNameChanged -> {
                _state.update { it.copy(name = action.name, nameError = null) }
            }
            is AddEditContactAction.OnPhoneNumberChanged -> {
                _state.update { it.copy(phoneNumber = action.phoneNumber, phoneError = null) }
            }
            is AddEditContactAction.OnRelationshipChanged -> {
                _state.update { it.copy(relationship = action.relationship) }
            }
            AddEditContactAction.OnSaveClick -> saveContact()
            else -> Unit
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun saveContact() {
        val currentState = _state.value

        var hasError = false
        if (currentState.name.isBlank()) {
            _state.update { it.copy(nameError = "El nombre es obligatorio") }
            hasError = true
        }
        if (currentState.phoneNumber.isBlank()) {
            _state.update { it.copy(phoneError = "El teléfono es obligatorio") }
            hasError = true
        }
        if (hasError) return

        _state.update { it.copy(isSaving = true) }

        viewModelScope.launch {
            val contact = EmergencyContact(
                id = currentState.contactId ?: Uuid.random().toString(),
                name = currentState.name.trim(),
                phoneNumber = currentState.phoneNumber.trim(),
                relationship = currentState.relationship.trim().ifEmpty { null },
                createdAt = Clock.System.now().toEpochMilliseconds()
            )
            repository.upsertContact(contact)
            eventChannel.send(AddEditContactEvent.OnContactSaved)
        }
    }
}
