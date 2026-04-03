package com.plcoding.emergency.presentation.add_edit_contact

sealed interface AddEditContactEvent {
    data object OnContactSaved : AddEditContactEvent
}
