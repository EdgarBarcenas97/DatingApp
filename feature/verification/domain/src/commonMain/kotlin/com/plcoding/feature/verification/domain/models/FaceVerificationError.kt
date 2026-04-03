package com.plcoding.feature.verification.domain.models

enum class FaceVerificationError(val userMessage: String) {
    NO_FACE_DETECTED("No se detectó rostro. Intenta con otra foto"),
    MULTIPLE_FACES("Se detectaron múltiples rostros. Solo cargue una foto suya"),
    LOW_QUALITY("Imagen baja calidad. Intenta con mejor iluminación"),
    FACE_OBSCURED("Rostro cubierto u oscuro. Intenta nuevamente"),
    POOR_LIGHTING("Mala iluminación. Toma una foto con mejor luz"),
    INVALID_FILE_FORMAT("Formato de archivo no válido"),
    UPLOAD_FAILED("Error al cargar la imagen. Revisa tu conexión"),
    BACKEND_ERROR("Servicio no disponible. Intenta más tarde"),
    NETWORK_ERROR("Error de conexión. Revisa tu internet")
}
