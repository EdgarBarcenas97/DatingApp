package com.plcoding.emergency.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual class EmergencyDatabaseFactory(
    private val context: Context
) {
    actual fun create(): RoomDatabase.Builder<EmergencyDatabase> {
        val dbFile = context.applicationContext.getDatabasePath(EmergencyDatabase.DB_NAME)

        return Room.databaseBuilder(
            context.applicationContext,
            dbFile.absolutePath
        )
    }
}
