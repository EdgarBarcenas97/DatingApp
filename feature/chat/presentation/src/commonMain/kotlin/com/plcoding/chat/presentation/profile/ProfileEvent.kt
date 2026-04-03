package com.plcoding.chat.presentation.profile

sealed interface ProfileEvent {
    data object OnAccountDeleted : ProfileEvent
}
