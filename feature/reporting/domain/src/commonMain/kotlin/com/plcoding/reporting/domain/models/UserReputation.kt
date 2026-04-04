package com.plcoding.reporting.domain.models

data class UserReputation(
    val userId: String,
    val reputationScore: Int = 100,
    val reportCount: Int = 0,
    val isBlocked: Boolean = false,
    val isBanned: Boolean = false
) {
    fun shouldAutoBlock(): Boolean = reportCount >= 5
    fun shouldBan(): Boolean = reportCount >= 10
    fun isPrioritized(): Boolean = reputationScore >= 80
}
