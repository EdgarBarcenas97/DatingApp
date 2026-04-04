package com.plcoding.reporting.data.service

import com.plcoding.reporting.domain.models.ContentViolation
import com.plcoding.reporting.domain.models.ViolationType
import com.plcoding.reporting.domain.service.ContentModerationService

class ContentModerationServiceImpl : ContentModerationService {

    private val offensiveKeywords = setOf(
        "idiota", "estúpido", "imbécil", "pendejo", "basura", "inútil",
        "fool", "idiot", "stupid", "asshole", "jerk", "bastard"
    )

    private val sexualKeywords = setOf(
        "puto", "puta", "coger", "verga", "culo", "teta", "vaginita",
        "fuck", "sex", "dick", "pussy", "horny", "naked", "porn"
    )

    private val threatKeywords = setOf(
        "matar", "muerte", "golpear", "violar", "incendiar", "bomba",
        "kill", "death", "hurt", "rape", "bomb", "shoot"
    )

    override suspend fun analyzeMessage(content: String): ContentViolation {
        val lowerContent = content.lowercase()

        return when {
            detectThreats(content) -> {
                ContentViolation(
                    isViolation = true,
                    violationType = ViolationType.THREATS,
                    confidenceScore = 0.95f,
                    message = "Detected threatening language"
                )
            }
            detectSexualContent(content) -> {
                ContentViolation(
                    isViolation = true,
                    violationType = ViolationType.SEXUAL_CONTENT,
                    confidenceScore = 0.85f,
                    message = "Detected sexual or explicit content"
                )
            }
            detectOffensiveLanguage(content) -> {
                ContentViolation(
                    isViolation = true,
                    violationType = ViolationType.OFFENSIVE_LANGUAGE,
                    confidenceScore = 0.80f,
                    message = "Detected offensive language"
                )
            }
            else -> {
                ContentViolation(
                    isViolation = false,
                    violationType = null,
                    confidenceScore = 0f,
                    message = "Content is safe"
                )
            }
        }
    }

    override suspend fun detectOffensiveLanguage(content: String): Boolean {
        val lowerContent = content.lowercase()
        return offensiveKeywords.any { keyword ->
            lowerContent.contains(keyword)
        }
    }

    override suspend fun detectSexualContent(content: String): Boolean {
        val lowerContent = content.lowercase()
        return sexualKeywords.any { keyword ->
            lowerContent.contains(keyword)
        }
    }

    override suspend fun detectThreats(content: String): Boolean {
        val lowerContent = content.lowercase()
        return threatKeywords.any { keyword ->
            lowerContent.contains(keyword)
        }
    }
}
