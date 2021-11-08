package com.sai.k.esp8266commander.services

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.sai.k.esp8266commander.models.RelayModule

@Dao
interface RelayModuleDao {
    @Query("SELECT * FROM relayModule")
    fun getAll(): List<RelayModule>

    @Query("SELECT * FROM relayModule WHERE relayModuleId IN (:moduleIds)")
    fun loadAllByIds(moduleIds: IntArray): List<RelayModule>

    //@Query("SELECT * FROM relayModule WHERE first_name LIKE :first AND " +
            //"last_name LIKE :last LIMIT 1")
    //fun findByName(first: String, last: String): RelayModule

    @Insert
    fun insertAll(vararg relayModules: RelayModule)

    @Delete
    fun delete(relayModule: RelayModule)
}