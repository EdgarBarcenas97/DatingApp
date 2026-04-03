package com.plcoding.emergency.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import com.plcoding.emergency.database.dao.EmergencyContactDao
import com.plcoding.emergency.database.dao.EmergencySettingsDao
import com.plcoding.emergency.database.entities.EmergencyContactEntity
import com.plcoding.emergency.database.entities.EmergencySettingsEntity

@Database(
    entities = [
        EmergencyContactEntity::class,
        EmergencySettingsEntity::class,
    ],
    version = 1,
)
@ConstructedBy(EmergencyDatabaseConstructor::class)
abstract class EmergencyDatabase : RoomDatabase() {
    abstract val emergencyContactDao: EmergencyContactDao
    abstract val emergencySettingsDao: EmergencySettingsDao

    companion object {
        const val DB_NAME = "emergency.db"
    }
}
