package com.plcoding.feature.verification.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.plcoding.feature.verification.presentation.face_verification.FaceVerificationRoot
import kotlinx.serialization.Serializable

sealed interface VerificationGraphRoutes {
    @Serializable
    data object Graph : VerificationGraphRoutes

    @Serializable
    data object FaceVerification : VerificationGraphRoutes
}

fun NavGraphBuilder.verificationGraph(
    navController: NavController,
    onNavigateBack: () -> Unit
) {
    navigation<VerificationGraphRoutes.Graph>(
        startDestination = VerificationGraphRoutes.FaceVerification
    ) {
        composable<VerificationGraphRoutes.FaceVerification> {
            FaceVerificationRoot(
                onSuccess = onNavigateBack,
                onDismiss = onNavigateBack
            )
        }
    }
}
