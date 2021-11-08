package com.sai.k.esp8266commander.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sai.k.esp8266commander.models.RelayModule

class SettingsViewModel : ViewModel() {

    var relayModule: MutableLiveData<RelayModule>
            = MutableLiveData(RelayModule(0,"", 0, "", 0))

    fun displayDefaultValues() {
        if (relayModule.value?.port == 0)
            relayModule.value?.port = 8080
        if (relayModule.value?.ipAddress == "")
            relayModule.value?.ipAddress = "192.168.4.1"
        if (relayModule.value?.relays == 0)
            relayModule.value?.relays = 4
    }
}