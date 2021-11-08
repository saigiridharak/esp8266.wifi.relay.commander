package com.sai.k.esp8266commander.ui.home

import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import com.sai.k.esp8266commander.R
import com.sai.k.esp8266commander.databinding.FragmentHomeBinding
import com.sai.k.esp8266commander.databinding.FragmentSettingsBinding
import com.sai.k.esp8266commander.models.RelayModule
import com.sai.k.esp8266commander.services.RelayListAdapter
import com.sai.k.esp8266commander.services.RelayListSpinnerAdapter
import com.sai.k.esp8266commander.services.RelayModuleDatabase
import com.sai.k.esp8266commander.services.SocketStuff
import com.sai.k.esp8266commander.ui.settings.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_settings.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var _ss: SocketStuff
    private lateinit var db: RelayModuleDatabase
    private lateinit var listAdapter: RelayListSpinnerAdapter
    private lateinit var relayModuleList: List<RelayModule>
    private lateinit var socketStuff: SocketStuff
    private lateinit var selectedRelayIp: String
    private var selectedRelayPort = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentHomeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false)
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = homeViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spnRelayModules.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                onRelayModuleSelected(position, id)
            }
        }

        btnOneOn.setOnClickListener { button -> onCommandButtonClick(button) }
        btnOneOff.setOnClickListener { button -> onCommandButtonClick(button) }
        btnTwoOn.setOnClickListener { button -> onCommandButtonClick(button) }
        btnTwoOff.setOnClickListener { button -> onCommandButtonClick(button) }
        btnThreeOn.setOnClickListener { button -> onCommandButtonClick(button) }
        btnThreeOff.setOnClickListener { button -> onCommandButtonClick(button) }
        btnFourOn.setOnClickListener { button -> onCommandButtonClick(button) }
        btnFourOff.setOnClickListener { button -> onCommandButtonClick(button) }

        db = Room.databaseBuilder(
            context!!.applicationContext,
            RelayModuleDatabase::class.java, "relay-module"
        ).allowMainThreadQueries().build()

        fillRelayModulesList()
        bindAdapterToListView(relayModuleList)
    }

    private fun onCommandButtonClick(button: View){
        val btn = button as Button
        var command = "RelayOn"
        var relayNo = "1"
        socketStuff = SocketStuff()
        when (btn.id) {
            btnOneOn.id -> {
                command = "RelayOn"
                relayNo = "1"
            }
            btnOneOff.id -> {
                command = "RelayOff"
                relayNo = "1"
            }
            btnTwoOn.id -> {
                command = "RelayOn"
                relayNo = "2"
            }
            btnTwoOff.id -> {
                command = "RelayOff"
                relayNo = "2"
            }
            btnThreeOn.id -> {
                command = "RelayOn"
                relayNo = "3"
            }
            btnThreeOff.id -> {
                command = "RelayOff"
                relayNo = "3"
            }
            btnFourOn.id -> {
                command = "RelayOn"
                relayNo = "4"
            }
            btnFourOff.id -> {
                command = "RelayOff"
                relayNo = "4"
            }
        }

        val socketTask = socketStuff.execute(selectedRelayIp, selectedRelayPort.toString(), command, relayNo)
        val socketResult = socketTask.get().toString()
        Toast.makeText(this.activity, socketTask.get().toString(), Toast.LENGTH_SHORT).show()

        if (socketResult != "Executed") {
            lblRelayStatus.text = socketResult
            lblRelayStatus.setTextColor(Color.RED)
        }
        else {
            lblRelayStatus.text = "Executed"
            lblRelayStatus.setTextColor(Color.GREEN)
        }
    }

    private fun onRelayModuleSelected(position: Int, id: Long){
        var socketResult = "Default"
        if (relayModuleList.count() > 0) {
            val selected = relayModuleList[position]
            selectedRelayIp = selected.ipAddress
            selectedRelayPort = selected.port
            socketResult = checkConnection(selected.ipAddress, selected.port)
        }

        lblRelayStatus.text = socketResult
        if (socketResult != "Connected"){
            lblRelayStatus.setTextColor(Color.RED)
        }
        else {
            lblRelayStatus.setTextColor(Color.GREEN)
        }
        Toast.makeText(this.activity, socketResult, Toast.LENGTH_SHORT).show()
        enableDisableButtons(socketResult)
    }

    private fun enableDisableButtons(socketResult: String) {
        if (socketResult == "Connected") {
            btnOneOn.isEnabled = true
            btnOneOff.isEnabled = true
            btnTwoOn.isEnabled = true
            btnTwoOff.isEnabled = true
            btnThreeOn.isEnabled = true
            btnThreeOff.isEnabled = true
            btnFourOn.isEnabled = true
            btnFourOff.isEnabled = true
        } else {
            btnOneOn.isEnabled = false
            btnOneOff.isEnabled = false
            btnTwoOn.isEnabled = false
            btnTwoOff.isEnabled = false
            btnThreeOn.isEnabled = false
            btnThreeOff.isEnabled = false
            btnFourOn.isEnabled = false
            btnFourOff.isEnabled = false
        }
    }

    private fun checkConnection(ipAddress: String, port: Int): String{
        socketStuff = SocketStuff()
        val task = socketStuff.execute(ipAddress, port.toString(), "CheckConnection")
        return task?.get().toString()
    }

    private fun fillRelayModulesList() {
        relayModuleList = db.relayModuleDao().getAll()
        if (relayModuleList.isEmpty()) {
            relayModuleList = ArrayList<RelayModule>()
            (relayModuleList as ArrayList<RelayModule>)
                .add(RelayModule(-1, "0", 0, "None", 0))
        }
    }

    private fun bindAdapterToListView(relayModuleList: List<RelayModule>) {
        listAdapter = RelayListSpinnerAdapter(context!!, relayModuleList as ArrayList<RelayModule>)

        spnRelayModules.adapter = listAdapter
    }

}