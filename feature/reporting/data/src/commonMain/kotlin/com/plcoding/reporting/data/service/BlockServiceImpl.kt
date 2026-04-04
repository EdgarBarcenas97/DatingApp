package com.plcoding.reporting.data.service

import com.plcoding.reporting.domain.models.BlockedUser
import com.plcoding.reporting.domain.service.BlockService
import kotlin.time.Clock
import kotlin.uuid.Uuid

class BlockServiceImpl : BlockService {

    private val blockedUsers = mutableListOf<BlockedUser>()

    override suspend fun blockUser(
        userId: String,
        blockedUserId: String,
        reason: String
    ): Result<BlockedUser> {
        return try {
            val isAlreadyBlocked = blockedUsers.any {
                it.blockedByUserId == userId && it.userId == blockedUserId
            }

            if (isAlreadyBlocked) {
                return Result.failure(Exception("User already blocked"))
            }

            val blocked = BlockedUser(
                userId = blockedUserId,
                blockedByUserId = userId,
                reason = reason,
                blockedAt = Clock.System.now(),
                isAutomatic = false
            )

            blockedUsers.add(blocked)
            Result.success(blocked)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun unblockUser(userId: String, blockedUserId: String): Result<Unit> {
        return try {
            blockedUsers.removeAll { user ->
                user.blockedByUserId == userId && user.userId == blockedUserId
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getBlockedUsers(userId: String): Result<List<BlockedUser>> {
        return try {
            val blocked = blockedUsers.filter { it.blockedByUserId == userId }
            Result.success(blocked)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isUserBlocked(userId: String, otherUserId: String): Result<Boolean> {
        return try {
            val isBlocked = blockedUsers.any {
                it.blockedByUserId == userId && it.userId == otherUserId
            }
            Result.success(isBlocked)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
