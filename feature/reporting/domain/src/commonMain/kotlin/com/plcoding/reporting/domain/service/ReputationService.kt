package com.plcoding.reporting.domain.service

import com.plcoding.reporting.domain.models.UserReputation

interface ReputationService {
    suspend fun getUserReputation(userId: String): Result<UserReputation>
    suspend fun decreaseReputation(userId: String, amount: Int = 10): Result<Unit>
    suspend fun increaseReputation(userId: String, amount: Int = 5): Result<Unit>
    suspend fun autoBlockUser(userId: String, reason: String): Result<Unit>
    suspend fun banUser(userId: String, reason: String): Result<Unit>
}
