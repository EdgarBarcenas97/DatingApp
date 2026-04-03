package com.plcoding.emergency.presentation.add_edit_contact

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.plcoding.core.designsystem.components.buttons.ChirpButton
import com.plcoding.core.designsystem.components.buttons.ChirpButtonStyle
import com.plcoding.core.presentation.util.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddEditContactRoot(
    contactId: String?,
    onBackClick: () -> Unit,
    onContactSaved: () -> Unit,
    viewModel: AddEditContactViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(contactId) {
        viewModel.loadContact(contactId)
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            AddEditContactEvent.OnContactSaved -> onContactSaved()
        }
    }

    AddEditContactScreen(
        state = state,
        onAction = { action ->
            when (action) {
                AddEditContactAction.OnBackClick -> onBackClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditContactScreen(
    state: AddEditContactState,
    onAction: (AddEditContactAction) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (state.isEditing) "Editar contacto" else "Agregar contacto",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(AddEditContactAction.OnBackClick) }) {
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.name,
                onValueChange = { onAction(AddEditContactAction.OnNameChanged(it)) },
                label = { Text("Nombre") },
                placeholder = { Text("Ej: María García") },
                isError = state.nameError != null,
                supportingText = state.nameError?.let { error ->
                    { Text(text = error, color = MaterialTheme.colorScheme.error) }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.phoneNumber,
                onValueChange = { onAction(AddEditContactAction.OnPhoneNumberChanged(it)) },
                label = { Text("Teléfono") },
                placeholder = { Text("Ej: +52 55 1234 5678") },
                isError = state.phoneError != null,
                supportingText = state.phoneError?.let { error ->
                    { Text(text = error, color = MaterialTheme.colorScheme.error) }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.relationship,
                onValueChange = { onAction(AddEditContactAction.OnRelationshipChanged(it)) },
                label = { Text("Relación (opcional)") },
                placeholder = { Text("Ej: Hermana, Amigo, Mamá") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            ChirpButton(
                text = if (state.isEditing) "Guardar cambios" else "Agregar contacto",
                onClick = { onAction(AddEditContactAction.OnSaveClick) },
                style = ChirpButtonStyle.PRIMARY,
                isLoading = state.isSaving,
                enabled = !state.isSaving,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
