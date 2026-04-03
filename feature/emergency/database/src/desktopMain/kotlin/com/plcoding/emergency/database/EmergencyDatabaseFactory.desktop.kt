package com.plcoding.emergency.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.plcoding.core.data.util.appDataDirectory
import java.io.File

actual class EmergencyDatabaseFactory {
    actual fun create(): RoomDatabase.Builder<EmergencyDatabase> {
        val directory = appDataDirectory

        if(!directory.exists()) {
            directory.mkdirs()
        }

        val dbFile = File(directory, EmergencyDatabase.DB_NAME)
        return Room.databaseBuilder(dbFile.absolutePath)
    }
}
