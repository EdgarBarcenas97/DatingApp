package com.plcoding.chat.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.plcoding.chat.presentation.chat_list_detail.ChatListDetailAdaptiveLayout
import com.plcoding.feature.verification.presentation.face_verification.FaceVerificationRoot
import kotlinx.serialization.Serializable

sealed interface ChatGraphRoutes {
    @Serializable
    data object Graph: ChatGraphRoutes

    @Serializable
    data class ChatListDetailRoute(val chatId: String? = null): ChatGraphRoutes

    @Serializable
    data object FaceVerification: ChatGraphRoutes
}

fun NavGraphBuilder.chatGraph(
    navController: NavController,
    onLogout: () -> Unit
) {
    navigation<ChatGraphRoutes.Graph>(
        startDestination = ChatGraphRoutes.ChatListDetailRoute(null)
    ) {
        composable<ChatGraphRoutes.ChatListDetailRoute>(
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "chirp://chat_detail/{chatId}"
                }
            )
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<ChatGraphRoutes.ChatListDetailRoute>()
            ChatListDetailAdaptiveLayout(
                initialChatId = route.chatId,
                onLogout = onLogout,
                onNavigateToFaceVerification = {
                    navController.navigate(ChatGraphRoutes.FaceVerification)
                }
            )
        }

        composable<ChatGraphRoutes.FaceVerification> {
            FaceVerificationRoot(
                onSuccess = {
                    navController.popBackStack()
                },
                onDismiss = {
                    navController.popBackStack()
                }
            )
        }
    }
}