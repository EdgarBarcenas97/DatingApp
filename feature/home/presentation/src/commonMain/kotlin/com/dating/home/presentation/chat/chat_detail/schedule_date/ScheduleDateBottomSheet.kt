@file:OptIn(ExperimentalMaterial3Api::class)

package com.dating.home.presentation.chat.chat_detail.schedule_date

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.dating.home.domain.models.PlaceSuggestion
import com.dating.core.designsystem.components.buttons.ChirpButton
import com.dating.core.designsystem.components.buttons.ChirpButtonStyle
import com.dating.core.designsystem.components.dialogs.ChirpBottomSheet
import com.dating.core.designsystem.theme.extended
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun ScheduleDateBottomSheet(state: ScheduleDateState, onAction: (ScheduleDateAction) -> Unit) {
    if (!state.isVisible) return
    ChirpBottomSheet(onDismiss = { onAction(ScheduleDateAction.OnDismiss) }) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            ScheduleDateTopBar(currentStep = state.currentStep, onBack = { if (state.currentStep == ScheduleDateStep.DATE_TIME) onAction(ScheduleDateAction.OnDismiss) else onAction(ScheduleDateAction.OnPreviousStep) }, onClose = { onAction(ScheduleDateAction.OnDismiss) })
            Spacer(modifier = Modifier.height(16.dp))
            StepIndicator(currentStep = state.currentStep, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedContent(targetState = state.currentStep, transitionSpec = { if (targetState.ordinal > initialState.ordinal) slideInHorizontally { it } togetherWith slideOutHorizontally { -it } else slideInHorizontally { -it } togetherWith slideOutHorizontally { it } }) { step ->
                when (step) {
                    ScheduleDateStep.DATE_TIME -> DateTimeStep(state = state, onAction = onAction)
                    ScheduleDateStep.PLACE -> PlaceStep(state = state, onAction = onAction)
                    ScheduleDateStep.REVIEW -> ReviewStep(state = state, onAction = onAction)
                }
            }
        }
    }
}

@Composable
private fun ScheduleDateTopBar(currentStep: ScheduleDateStep, onBack: () -> Unit, onClose: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onBack) { Icon(imageVector = if (currentStep == ScheduleDateStep.DATE_TIME) Icons.Default.Close else Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Cerrar", tint = MaterialTheme.colorScheme.extended.textPrimary) }
        Text(text = when (currentStep) { ScheduleDateStep.DATE_TIME -> "Fecha y hora"; ScheduleDateStep.PLACE -> "Lugar"; ScheduleDateStep.REVIEW -> "Revisar propuesta" }, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.extended.textPrimary, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        if (currentStep != ScheduleDateStep.DATE_TIME) { IconButton(onClick = onClose) { Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar", tint = MaterialTheme.colorScheme.extended.textSecondary) } }
    }
}

@Composable
private fun StepIndicator(currentStep: ScheduleDateStep, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        ScheduleDateStep.entries.forEach { step ->
            val isActive = step.ordinal <= currentStep.ordinal
            Box(modifier = Modifier.weight(1f).height(4.dp).clip(RoundedCornerShape(2.dp)).background(if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.extended.disabledFill))
        }
    }
}

@Composable
private fun DateTimeStep(state: ScheduleDateState, onAction: (ScheduleDateAction) -> Unit) {
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState(initialHour = state.selectedHour, initialMinute = state.selectedMinute)
    LaunchedEffect(datePickerState) { snapshotFlow { datePickerState.selectedDateMillis }.collect { millis -> if (millis != null) { val localDate = kotlin.time.Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.currentSystemDefault()); onAction(ScheduleDateAction.OnDateSelected(year = localDate.year, month = localDate.monthNumber, day = localDate.dayOfMonth)) } } }
    LaunchedEffect(timePickerState) { snapshotFlow { timePickerState.hour to timePickerState.minute }.collect { (hour, minute) -> onAction(ScheduleDateAction.OnTimeSelected(hour, minute)) } }
    LazyColumn(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item { Text(text = "Selecciona la fecha", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.extended.textPrimary, fontWeight = FontWeight.Medium) }
        item { DatePicker(state = datePickerState, showModeToggle = false, title = null, headline = null, modifier = Modifier.fillMaxWidth()) }
        item { HorizontalDivider() }
        item { Text(text = "Selecciona la hora", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.extended.textPrimary, fontWeight = FontWeight.Medium) }
        item { Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { TimePicker(state = timePickerState) } }
        item { ChirpButton(text = "Siguiente: Elegir lugar", onClick = { onAction(ScheduleDateAction.OnNextStep) }, enabled = state.canProceedToPlace, modifier = Modifier.fillMaxWidth()) }
        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
private fun PlaceStep(state: ScheduleDateState, onAction: (ScheduleDateAction) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(value = state.placeQuery, onValueChange = { onAction(ScheduleDateAction.OnPlaceQueryChanged(it)) }, modifier = Modifier.fillMaxWidth(), placeholder = { Text("Buscar restaurante, cafe, parque...") }, leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) }, singleLine = true, keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search), shape = RoundedCornerShape(12.dp))
        Spacer(modifier = Modifier.height(12.dp))
        if (state.isSearchingPlaces) { Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) { CircularProgressIndicator() } }
        else {
            Text(text = "Sugerencias", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.extended.textSecondary, modifier = Modifier.padding(bottom = 8.dp))
            LazyColumn(modifier = Modifier.fillMaxWidth().height(350.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                items(items = state.placeSuggestions, key = { it.placeId }) { place ->
                    PlaceSuggestionItem(place = place, isSelected = state.selectedPlace?.placeId == place.placeId, onClick = { onAction(ScheduleDateAction.OnPlaceSelected(place)) }, onSafetyClick = { onAction(ScheduleDateAction.OnViewSafetyDetail(place)) })
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        ChirpButton(text = "Siguiente: Revisar", onClick = { onAction(ScheduleDateAction.OnNextStep) }, enabled = state.canProceedToReview, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun PlaceSuggestionItem(place: PlaceSuggestion, isSelected: Boolean, onClick: () -> Unit, onSafetyClick: () -> Unit, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(12.dp)
    val safetyInfo = place.safetyInfo
    val safetyColor = when { safetyInfo == null -> MaterialTheme.colorScheme.extended.textPlaceholder; safetyInfo.isVerifiedSafe -> MaterialTheme.colorScheme.extended.success; safetyInfo.averageSafetyRating >= 3.0 -> MaterialTheme.colorScheme.primary; safetyInfo.averageSafetyRating > 0 -> MaterialTheme.colorScheme.error; else -> MaterialTheme.colorScheme.extended.textPlaceholder }
    Column(modifier = modifier.fillMaxWidth().clip(shape).then(if (isSelected) Modifier.border(width = 2.dp, color = MaterialTheme.colorScheme.primary, shape = shape) else Modifier).background(color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.extended.surfaceHigher).clickable { onClick() }.padding(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(24.dp), tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.extended.textSecondary)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = place.name, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.extended.textPrimary, fontWeight = FontWeight.Medium)
                Text(text = place.address, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.extended.textSecondary)
                if (place.category != null) { Text(text = place.category, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.extended.textPlaceholder) }
            }
            Column(horizontalAlignment = Alignment.End) {
                if (place.rating != null) { Row(verticalAlignment = Alignment.CenterVertically) { Icon(imageVector = Icons.Default.Star, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.extended.accentYellow); Spacer(modifier = Modifier.width(2.dp)); Text(text = place.rating.toString(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.extended.textSecondary) } }
                if (isSelected) { Icon(imageVector = Icons.Default.Check, contentDescription = "Seleccionado", modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary) }
            }
        }
        Row(modifier = Modifier.padding(start = 36.dp, top = 6.dp).clip(RoundedCornerShape(8.dp)).background(safetyColor.copy(alpha = 0.06f)).clickable { onSafetyClick() }.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.Shield, contentDescription = null, modifier = Modifier.size(12.dp), tint = safetyColor)
            Spacer(modifier = Modifier.width(4.dp))
            if (safetyInfo != null && safetyInfo.totalReviews > 0) { Text(text = "${safetyInfo.averageSafetyRating}", style = MaterialTheme.typography.labelSmall, color = safetyColor, fontWeight = FontWeight.Bold); Text(text = " · ${safetyInfo.totalReviews} reseñas", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.extended.textSecondary); if (safetyInfo.isVerifiedSafe) { Text(text = " · Verificado", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.extended.success, fontWeight = FontWeight.Bold) } }
            else { Text(text = "Sin rating de seguridad · Calificar", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.extended.textPlaceholder) }
        }
    }
}

@Composable
private fun ReviewStep(state: ScheduleDateState, onAction: (ScheduleDateAction) -> Unit) {
    val formattedDate = if (state.selectedYear > 0) "${state.selectedDay}/${state.selectedMonth}/${state.selectedYear}" else ""
    val formattedTime = "${state.selectedHour.toString().padStart(2, '0')}:${state.selectedMinute.toString().padStart(2, '0')}"
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(text = "Resumen de tu propuesta", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.extended.textPrimary, fontWeight = FontWeight.Medium)
        Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.extended.surfaceHigher).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) { Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary); Spacer(modifier = Modifier.width(10.dp)); Column { Text(text = "Fecha", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.extended.textSecondary); Text(text = formattedDate, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.extended.textPrimary, fontWeight = FontWeight.Medium) } }
            Row(verticalAlignment = Alignment.CenterVertically) { Icon(imageVector = Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary); Spacer(modifier = Modifier.width(10.dp)); Column { Text(text = "Hora", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.extended.textSecondary); Text(text = formattedTime, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.extended.textPrimary, fontWeight = FontWeight.Medium) } }
            state.selectedPlace?.let { place -> Row(verticalAlignment = Alignment.Top) { Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary); Spacer(modifier = Modifier.width(10.dp)); Column { Text(text = "Lugar", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.extended.textSecondary); Text(text = place.name, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.extended.textPrimary, fontWeight = FontWeight.Medium); Text(text = place.address, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.extended.textSecondary) } } }
        }
        OutlinedTextField(value = state.note, onValueChange = { onAction(ScheduleDateAction.OnNoteChanged(it)) }, modifier = Modifier.fillMaxWidth(), placeholder = { Text("Agregar una nota (opcional)") }, minLines = 2, maxLines = 3, shape = RoundedCornerShape(12.dp))
        ChirpButton(text = if (state.modifyingProposalId != null) "Enviar propuesta modificada" else "Enviar propuesta de cita", onClick = { onAction(ScheduleDateAction.OnSendProposal) }, enabled = state.canSend, isLoading = state.isSending, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
    }
}
