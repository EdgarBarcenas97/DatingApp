package com.plcoding.feature.verification.domain

import com.plcoding.core.domain.auth.SessionStorage
import kotlinx.coroutines.flow.first

class UpdateUserVerificationStatusUseCase(
    private val sessionStorage: SessionStorage
) {
    suspend operator fun invoke() {
        val currentAuthInfo = sessionStorage.observeAuthInfo().first()
        if (currentAuthInfo != null) {
            val updatedUser = currentAuthInfo.user.copy(hasVerifiedFace = true)
            val updatedAuthInfo = currentAuthInfo.copy(user = updatedUser)
            sessionStorage.set(updatedAuthInfo)
        }
    }
}
