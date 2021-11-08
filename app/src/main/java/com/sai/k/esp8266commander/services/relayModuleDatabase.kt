package com.sai.k.esp8266commander.services

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sai.k.esp8266commander.models.RelayModule

@Database(entities = arrayOf(RelayModule::class), version = 1)
abstract class RelayModuleDatabase : RoomDatabase() {
    abstract fun relayModuleDao(): RelayModuleDao
}