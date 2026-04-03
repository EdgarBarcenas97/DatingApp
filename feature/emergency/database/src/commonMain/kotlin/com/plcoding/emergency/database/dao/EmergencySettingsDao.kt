package com.plcoding.emergency.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.plcoding.emergency.database.entities.EmergencySettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EmergencySettingsDao {

    @Query("SELECT * FROM emergencysettingsentity WHERE id = 1")
    fun observeSettings(): Flow<EmergencySettingsEntity?>

    @Upsert
    suspend fun upsertSettings(settings: EmergencySettingsEntity)

    @Query("UPDATE emergencysettingsentity SET isEnabled = :enabled WHERE id = 1")
    suspend fun updateEnabled(enabled: Boolean)

    @Query("UPDATE emergencysettingsentity SET defaultMessage = :message WHERE id = 1")
    suspend fun updateDefaultMessage(message: String)
}
