package com.sai.k.esp8266commander.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.sai.k.esp8266commander.R
import com.sai.k.esp8266commander.databinding.FragmentSettingsBinding
import com.sai.k.esp8266commander.models.RelayModule
import com.sai.k.esp8266commander.services.RelayListAdapter
import com.sai.k.esp8266commander.services.RelayModuleDatabase
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var db: RelayModuleDatabase
    private lateinit var listAdapter: RelayListAdapter
    private lateinit var relayModuleList: List<RelayModule>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentSettingsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_settings, container, false)

        settingsViewModel = ViewModelProvider(requireActivity()).get(SettingsViewModel::class.java)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = settingsViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = Room.databaseBuilder(
            context!!.applicationContext,
            RelayModuleDatabase::class.java, "relay-module"
        ).allowMainThreadQueries().build()

        btnAddRelayModule.setOnClickListener {
            onClickAddRelayModule()
        }

        fillRelayModulesList()
        bindAdapterToListView(relayModuleList)
    }

    private fun fillRelayModulesList() {
        relayModuleList = db.relayModuleDao().getAll()
        if (relayModuleList.isEmpty()) {
            relayModuleList = ArrayList<RelayModule>()
            (relayModuleList as ArrayList<RelayModule>)
                .add(RelayModule(-1, "0", 0, "None", 0))
        }
    }

    private fun onClickAddRelayModule(){
        val isValid = validateInput()

        if (isValid) {
            Toast.makeText(this.activity, "Button clicked, saving...", Toast.LENGTH_SHORT).show()
            saveToDb()
            Toast.makeText(this.activity, "Saved!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun bindAdapterToListView(relayModuleList: List<RelayModule>) {
        listAdapter = RelayListAdapter(context!!, relayModuleList as ArrayList<RelayModule>)

        relayModuleListView.adapter = listAdapter
    }

    private fun saveToDb() {
        val maxId = relayModuleList.maxBy { r -> r.relayModuleId }!!.relayModuleId
        settingsViewModel.relayModule.value!!.relayModuleId = maxId + 1
        db.relayModuleDao().insertAll(settingsViewModel.relayModule.value!!)
        fillRelayModulesList()
    }

    private fun validateInput(): Boolean {
        val requiredText = "Required"
        var isValid = true
        if (txtIpAddress.text.toString().trim() == "") {
            txtIpAddress.error = requiredText
            isValid = false
        } else {
            val enteredIpAddress = txtIpAddress.text.toString().trim()
            val zeroTo255 = ("(\\d{1,2}|(0|1)\\d{2}|2[0-4]\\d|25[0-5])")
            val pattern = Regex("$zeroTo255\\.$zeroTo255\\.$zeroTo255\\.$zeroTo255")
            if (!pattern.containsMatchIn(enteredIpAddress)) {
                txtIpAddress.error = "IP address is not a valid format"
                isValid = false
            } else {
                settingsViewModel.relayModule.value?.ipAddress = enteredIpAddress
            }
        }

        if (txtFriendlyName.text.toString().trim() == "") {
            txtFriendlyName.error = requiredText
            isValid = false
        } else {
            settingsViewModel.relayModule.value?.friendlyName = txtFriendlyName.text.toString()
        }

        if (txtPort.text.toString().trim() == "") {
            txtPort.error = requiredText
            isValid = false
        } else {
            val intPort = txtPort.text.toString().toInt()
            if (intPort < 1 || intPort > 65535) {
                txtPort.error = "Port should be between 1 and 65535"
                isValid = false
            } else
                settingsViewModel.relayModule.value?.port = intPort
        }

        if (txtRelays.text.toString().trim() == "") {
            txtRelays.error = requiredText
            isValid = false
        } else {
            val intRelays = txtRelays.text.toString().toInt()
            if (intRelays < 1 || intRelays > 4) {
                txtRelays.error = "Relays should be between 1 and 4"
                isValid = false
            } else
                settingsViewModel.relayModule.value?.relays = intRelays
        }
        return isValid
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        settingsViewModel.displayDefaultValues()
    }
}