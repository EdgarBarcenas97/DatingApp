@file:OptIn(ExperimentalForeignApi::class)

package com.plcoding.emergency.database

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual class EmergencyDatabaseFactory {
    actual fun create(): RoomDatabase.Builder<EmergencyDatabase> {
        val dbFile = documentDirectory() + "/${EmergencyDatabase.DB_NAME}"

        return Room.databaseBuilder(dbFile)
    }

    private fun documentDirectory(): String {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )

        return requireNotNull(documentDirectory?.path)
    }
}
