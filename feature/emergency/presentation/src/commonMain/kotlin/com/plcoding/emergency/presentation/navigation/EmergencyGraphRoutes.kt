package com.plcoding.emergency.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface EmergencyGraphRoutes {
    @Serializable
    data object Graph : EmergencyGraphRoutes

    @Serializable
    data object EmergencySettings : EmergencyGraphRoutes

    @Serializable
    data object AddEditContact : EmergencyGraphRoutes

    @Serializable
    data class EditContact(val contactId: String) : EmergencyGraphRoutes

    @Serializable
    data object Onboarding : EmergencyGraphRoutes
}
