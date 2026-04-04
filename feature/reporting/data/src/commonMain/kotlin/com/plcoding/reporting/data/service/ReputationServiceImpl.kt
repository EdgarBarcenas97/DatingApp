package com.plcoding.reporting.data.service

import com.plcoding.reporting.domain.models.UserReputation
import com.plcoding.reporting.domain.service.ReputationService

class ReputationServiceImpl : ReputationService {

    private val userReputations = mutableMapOf<String, UserReputation>()

    override suspend fun getUserReputation(userId: String): Result<UserReputation> {
        return try {
            val reputation = userReputations.getOrDefault(
                userId,
                UserReputation(userId = userId)
            )
            Result.success(reputation)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun decreaseReputation(userId: String, amount: Int): Result<Unit> {
        return try {
            val currentReputation = userReputations.getOrDefault(
                userId,
                UserReputation(userId = userId)
            )
            val newScore = (currentReputation.reputationScore - amount).coerceAtLeast(0)
            val newReportCount = currentReputation.reportCount + 1

            val updated = currentReputation.copy(
                reputationScore = newScore,
                reportCount = newReportCount,
                isBlocked = newReportCount >= 5,
                isBanned = newReportCount >= 10
            )

            userReputations[userId] = updated
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun increaseReputation(userId: String, amount: Int): Result<Unit> {
        return try {
            val currentReputation = userReputations.getOrDefault(
                userId,
                UserReputation(userId = userId)
            )
            val newScore = (currentReputation.reputationScore + amount).coerceAtMost(100)

            val updated = currentReputation.copy(reputationScore = newScore)
            userReputations[userId] = updated
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun autoBlockUser(userId: String, reason: String): Result<Unit> {
        return try {
            val currentReputation = userReputations.getOrDefault(
                userId,
                UserReputation(userId = userId)
            )
            val updated = currentReputation.copy(isBlocked = true)
            userReputations[userId] = updated
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun banUser(userId: String, reason: String): Result<Unit> {
        return try {
            val currentReputation = userReputations.getOrDefault(
                userId,
                UserReputation(userId = userId)
            )
            val updated = currentReputation.copy(isBanned = true, isBlocked = true)
            userReputations[userId] = updated
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
