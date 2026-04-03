package com.plcoding.emergency.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.plcoding.core.designsystem.components.buttons.ChirpButton
import com.plcoding.core.designsystem.components.buttons.ChirpButtonStyle
import com.plcoding.core.designsystem.components.buttons.ChirpFloatingActionButton
import com.plcoding.core.designsystem.components.dialogs.DestructiveConfirmationDialog
import com.plcoding.core.designsystem.theme.extended
import com.plcoding.emergency.presentation.model.EmergencyContactUi
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EmergencySettingsRoot(
    onBackClick: () -> Unit,
    onAddContactClick: () -> Unit,
    onEditContactClick: (String) -> Unit,
    onOnboardingClick: () -> Unit,
    viewModel: EmergencySettingsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    EmergencySettingsScreen(
        state = state,
        onAction = { action ->
            when (action) {
                EmergencySettingsAction.OnBackClick -> onBackClick()
                EmergencySettingsAction.OnAddContactClick -> onAddContactClick()
                is EmergencySettingsAction.OnEditContactClick -> onEditContactClick(action.contactId)
                EmergencySettingsAction.OnOnboardingClick -> onOnboardingClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencySettingsScreen(
    state: EmergencySettingsState,
    onAction: (EmergencySettingsAction) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Contacto de Emergencia",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(EmergencySettingsAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            if (state.isEnabled) {
                ChirpFloatingActionButton(
                    onClick = { onAction(EmergencySettingsAction.OnAddContactClick) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar contacto"
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Toggle section
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Activar contacto de emergencia",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Permite enviar alertas a tus contactos de confianza",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.extended.textSecondary
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Switch(
                        checked = state.isEnabled,
                        onCheckedChange = { onAction(EmergencySettingsAction.OnToggleEnabled(it)) }
                    )
                }
            }

            if (state.isEnabled) {
                // Contacts section header
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Mis contactos de emergencia",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                if (state.contacts.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No tienes contactos de emergencia",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.extended.textSecondary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            ChirpButton(
                                text = "Agregar primer contacto",
                                onClick = { onAction(EmergencySettingsAction.OnAddContactClick) },
                                style = ChirpButtonStyle.PRIMARY
                            )
                        }
                    }
                } else {
                    items(
                        items = state.contacts,
                        key = { it.id }
                    ) { contact ->
                        EmergencyContactItem(
                            contact = contact,
                            onEditClick = { onAction(EmergencySettingsAction.OnEditContactClick(contact.id)) },
                            onDeleteClick = { onAction(EmergencySettingsAction.OnDeleteContactClick(contact.id)) }
                        )
                    }
                }

                // Default message section
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Mensaje de emergencia",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (state.isEditingMessage) {
                        Column {
                            OutlinedTextField(
                                value = state.defaultMessage,
                                onValueChange = { onAction(EmergencySettingsAction.OnDefaultMessageChanged(it)) },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 3,
                                maxLines = 5
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                ChirpButton(
                                    text = "Cancelar",
                                    onClick = { onAction(EmergencySettingsAction.OnCancelEditMessage) },
                                    style = ChirpButtonStyle.SECONDARY
                                )
                                ChirpButton(
                                    text = "Guardar",
                                    onClick = { onAction(EmergencySettingsAction.OnSaveMessage) },
                                    style = ChirpButtonStyle.PRIMARY
                                )
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = state.defaultMessage.ifEmpty { "Sin mensaje configurado" },
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (state.defaultMessage.isEmpty()) {
                                    MaterialTheme.colorScheme.extended.textSecondary
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                },
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { onAction(EmergencySettingsAction.OnEditMessageClick) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar mensaje",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                // Bottom spacing
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }

    state.showDeleteConfirmation?.let {
        DestructiveConfirmationDialog(
            title = "Eliminar contacto",
            description = "Este contacto ya no recibirá tus alertas de emergencia.",
            confirmButtonText = "Eliminar",
            cancelButtonText = "Cancelar",
            onConfirmClick = { onAction(EmergencySettingsAction.OnConfirmDelete) },
            onCancelClick = { onAction(EmergencySettingsAction.OnDismissDeleteDialog) },
            onDismiss = { onAction(EmergencySettingsAction.OnDismissDeleteDialog) }
        )
    }
}

@Composable
private fun EmergencyContactItem(
    contact: EmergencyContactUi,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEditClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = contact.initials,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = contact.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = contact.phoneNumber,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.extended.textSecondary
            )
            contact.relationship?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
        IconButton(onClick = onDeleteClick) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Eliminar",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}
