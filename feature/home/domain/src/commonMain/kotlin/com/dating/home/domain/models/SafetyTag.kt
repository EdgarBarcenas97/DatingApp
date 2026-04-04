package com.dating.home.domain.models

data class SafetyTag(
    val id: String,
    val label: String,
    val icon: String? = null
)

val DEFAULT_SAFETY_TAGS = listOf(
    SafetyTag("well_lit", "Bien iluminado"),
    SafetyTag("staff_attentive", "Staff atento"),
    SafetyTag("public_area", "Zona pública"),
    SafetyTag("easy_exit", "Fácil salida"),
    SafetyTag("good_crowd", "Buen ambiente"),
    SafetyTag("safe_neighborhood", "Barrio seguro"),
    SafetyTag("cctv", "Cámaras de seguridad"),
    SafetyTag("taxi_nearby", "Taxis/transporte cerca"),
    SafetyTag("loud_music", "Música fuerte"),
    SafetyTag("isolated", "Zona aislada"),
    SafetyTag("poor_lighting", "Poca iluminación"),
    SafetyTag("sketchy_area", "Zona insegura")
)
