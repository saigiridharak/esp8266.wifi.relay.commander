package com.sai.k.esp8266commander.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RelayModule(
    @PrimaryKey var relayModuleId: Int = -1,
    @ColumnInfo(name = "ipAddress") var ipAddress: String = "",
    @ColumnInfo(name = "port") var port: Int = 0,
    @ColumnInfo(name = "friendlyName") var friendlyName: String = "",
    @ColumnInfo(name = "relays") var relays: Int = 0)