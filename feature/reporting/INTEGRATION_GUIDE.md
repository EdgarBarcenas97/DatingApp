# Reporting & Anti-Harassment Integration Guide

Esta guía explica cómo integrar el módulo de reportes y anti-acoso en tu app de chat.

## Estructura del Módulo

```
feature/reporting/
├── domain/           # Interfaces y modelos
├── data/            # Implementaciones y servicios
└── presentation/    # Componentes UI
```

## Características Implementadas

### 1. **Detección Automática de Contenido (IA)**
- Detecta lenguaje ofensivo
- Detecta contenido sexual explícito
- Detecta amenazas
- Proporciona puntuación de confianza

**Ubicación**: `data/service/ContentModerationServiceImpl.kt`

### 2. **Sistema de Reportes**
- Crear reportes de usuarios/mensajes
- Asignar severidad automática
- Rastrear estado de reportes (PENDING, REVIEWING, RESOLVED, DISMISSED)

**Ubicación**: `data/service/ReportServiceImpl.kt`

### 3. **Sistema de Reputación Oculto**
- Puntuación de reputación (0-100)
- Auto-bloqueo después de 5 reportes
- Ban después de 10 reportes
- Los usuarios con baja reputación tienen menor prioridad

**Ubicación**: `data/service/ReputationServiceImpl.kt`

### 4. **Bloqueo Instantáneo**
- Bloquear usuarios con un toque
- Desbloquear usuarios
- Rastrear usuarios bloqueados
- Bloqueos automáticos

**Ubicación**: `data/service/BlockServiceImpl.kt`

### 5. **Protección Anti-Screenshot**
- Overlay visual en conversaciones protegidas
- Indicador visible de protección

**Ubicación**: `presentation/ScreenshotProtection.kt`

## Integración en el Chat

### Paso 1: Agregar Menú de Acciones en Mensajes

En tu componente de mensaje del chat, agrega `MessageActionsMenu`:

```kotlin
import com.plcoding.reporting.presentation.MessageActionsMenu

// En tu composable de mensaje
MessageActionsMenu(
    messageId = message.id,
    senderId = message.senderId,
    onReport = { messageId, userId ->
        // Abre el diálogo de reporte
        onAction(ReportingAction.OpenReportDialog(messageId, userId))
    },
    onBlock = { userId ->
        // Bloquea al usuario
        onAction(ReportingAction.BlockUser(userId, "Bloqueado por el usuario"))
    },
    isBlocked = state.blockedUsers.contains(message.senderId)
)
```

### Paso 2: Mostrar Diálogo de Reporte

```kotlin
import com.plcoding.reporting.presentation.ReportDialog

if (state.showReportDialog) {
    ReportDialog(
        state = state,
        onAction = onAction
    )
}
```

### Paso 3: Implementar Protección Anti-Screenshot

```kotlin
import com.plcoding.reporting.presentation.ScreenshotProtectionOverlay

ScreenshotProtectionOverlay(
    isProtected = chat.screenshotProtectionEnabled,
    content = {
        // Tu contenido del chat
        ChatContent()
    }
)
```

### Paso 4: Configurar Inyección de Dependencias

```kotlin
// En tu módulo de DI
single<ContentModerationService> {
    ContentModerationServiceImpl()
}

single<ReportService> {
    ReportServiceImpl(get<ContentModerationService>())
}

single<ReputationService> {
    ReputationServiceImpl()
}

single<BlockService> {
    BlockServiceImpl()
}
```

## Flujo de Reportes

1. **Usuario reporta un mensaje**
   - Se abre el diálogo de reporte
   - Usuario selecciona tipo y descripción
   - Se analiza automáticamente con IA

2. **Se calcula severidad**
   - CRITICAL: Amenazas
   - HIGH: Contenido sexual
   - MEDIUM: Lenguaje ofensivo
   - LOW: Otros reportes

3. **Se actualiza reputación**
   - Reputación del usuario disminuye (-10 puntos)
   - Se incrementa el contador de reportes
   - Si reportCount >= 5: Auto-bloqueo
   - Si reportCount >= 10: Ban permanente

4. **Se registra el bloqueo**
   - Usuario reportador bloquea automáticamente al reportado
   - Se previenen futuros mensajes

## Modelos Principales

### Report
```kotlin
data class Report(
    val id: String,
    val reporterId: String,
    val reportedUserId: String,
    val messageId: String?,
    val chatId: String,
    val type: ReportType,
    val description: String,
    val createdAt: Instant,
    val status: ReportStatus,
    val severity: ReportSeverity
)
```

### UserReputation
```kotlin
data class UserReputation(
    val userId: String,
    val reputationScore: Int,
    val reportCount: Int,
    val isBlocked: Boolean,
    val isBanned: Boolean
)
```

### BlockedUser
```kotlin
data class BlockedUser(
    val userId: String,
    val blockedByUserId: String,
    val reason: String,
    val blockedAt: Instant,
    val isAutomatic: Boolean
)
```

## Enums Disponibles

### ReportType
- OFFENSIVE_MESSAGE
- SEXUAL_HARASSMENT
- THREATS
- SPAM
- SCAM
- FAKE_PROFILE
- OTHER

### ReportStatus
- PENDING
- REVIEWING
- RESOLVED
- DISMISSED

### ReportSeverity
- LOW
- MEDIUM
- HIGH
- CRITICAL

### ViolationType
- OFFENSIVE_LANGUAGE
- SEXUAL_CONTENT
- THREATS
- SPAM

## Próximos Pasos

1. **Integrar con backend**
   - Crear endpoints API para reportes
   - Persistir en base de datos
   - Sincronizar reputación

2. **Mejorar IA**
   - Usar ML models más avanzados
   - Entrenar con datos locales
   - Detectar nuevos patrones

3. **Analytics**
   - Rastrear patrones de reporte
   - Identificar usuarios problemáticos
   - Mejorar políticas de seguridad

4. **UI Improvements**
   - Mostrar avisos de baja reputación
   - Indicadores visuales en perfiles
   - Historial de reportes
