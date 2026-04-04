package com.plcoding.reporting.domain.service

import com.plcoding.reporting.domain.models.BlockedUser

interface BlockService {
    suspend fun blockUser(userId: String, blockedUserId: String, reason: String): Result<BlockedUser>
    suspend fun unblockUser(userId: String, blockedUserId: String): Result<Unit>
    suspend fun getBlockedUsers(userId: String): Result<List<BlockedUser>>
    suspend fun isUserBlocked(userId: String, otherUserId: String): Result<Boolean>
}
