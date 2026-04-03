package com.plcoding.emergency.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.plcoding.emergency.database.entities.EmergencyContactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EmergencyContactDao {

    @Query("SELECT * FROM emergencycontactentity ORDER BY createdAt DESC")
    fun observeContacts(): Flow<List<EmergencyContactEntity>>

    @Query("SELECT * FROM emergencycontactentity WHERE contactId = :id")
    suspend fun getContactById(id: String): EmergencyContactEntity?

    @Upsert
    suspend fun upsertContact(contact: EmergencyContactEntity)

    @Query("DELETE FROM emergencycontactentity WHERE contactId = :id")
    suspend fun deleteContact(id: String)

    @Query("SELECT COUNT(*) FROM emergencycontactentity")
    fun getContactCount(): Flow<Int>
}
