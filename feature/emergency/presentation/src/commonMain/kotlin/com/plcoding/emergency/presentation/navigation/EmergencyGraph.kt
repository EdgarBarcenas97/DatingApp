package com.plcoding.emergency.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.plcoding.emergency.presentation.add_edit_contact.AddEditContactRoot
import com.plcoding.emergency.presentation.onboarding.EmergencyOnboardingRoot
import com.plcoding.emergency.presentation.settings.EmergencySettingsRoot

fun NavGraphBuilder.emergencyGraph(
    navController: NavController,
    onBack: () -> Unit
) {
    navigation<EmergencyGraphRoutes.Graph>(
        startDestination = EmergencyGraphRoutes.EmergencySettings
    ) {
        composable<EmergencyGraphRoutes.EmergencySettings> {
            EmergencySettingsRoot(
                onBackClick = onBack,
                onAddContactClick = {
                    navController.navigate(EmergencyGraphRoutes.AddEditContact)
                },
                onEditContactClick = { contactId ->
                    navController.navigate(EmergencyGraphRoutes.EditContact(contactId))
                },
                onOnboardingClick = {
                    navController.navigate(EmergencyGraphRoutes.Onboarding)
                }
            )
        }
        composable<EmergencyGraphRoutes.AddEditContact> {
            AddEditContactRoot(
                contactId = null,
                onBackClick = {
                    navController.popBackStack()
                },
                onContactSaved = {
                    navController.popBackStack()
                }
            )
        }
        composable<EmergencyGraphRoutes.EditContact> { backStackEntry ->
            val route = backStackEntry.toRoute<EmergencyGraphRoutes.EditContact>()
            AddEditContactRoot(
                contactId = route.contactId,
                onBackClick = {
                    navController.popBackStack()
                },
                onContactSaved = {
                    navController.popBackStack()
                }
            )
        }
        composable<EmergencyGraphRoutes.Onboarding> {
            EmergencyOnboardingRoot(
                onFinish = {
                    navController.popBackStack()
                },
                onAddContactClick = {
                    navController.navigate(EmergencyGraphRoutes.AddEditContact)
                }
            )
        }
    }
}
