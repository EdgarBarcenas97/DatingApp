package com.plcoding.emergency.database

import androidx.room.RoomDatabase

expect class EmergencyDatabaseFactory {
    fun create(): RoomDatabase.Builder<EmergencyDatabase>
}
