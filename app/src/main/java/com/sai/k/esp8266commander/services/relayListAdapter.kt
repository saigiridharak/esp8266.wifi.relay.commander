package com.sai.k.esp8266commander.services

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import com.sai.k.esp8266commander.R
import kotlinx.android.synthetic.main.fragment_settings.view.*
import kotlinx.android.synthetic.main.fragment_settings.view.txtFriendlyName
import kotlinx.android.synthetic.main.relaymodule_listitem.view.*
import com.sai.k.esp8266commander.models.RelayModule as RelayModule

public class RelayListAdapter(private val context: Context,
                              private val relayList: ArrayList<RelayModule>) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    //1
    override fun getCount(): Int {
        return relayList.size
    }

    //2
    override fun getItem(position: Int): Any {
        return relayList[position]
    }

    //3
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    //4
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for row item
        val rowView = inflater.inflate(R.layout.relaymodule_listitem, parent, false)

        val frieldlyNameTextView = rowView.findViewById<TextView>(R.id.txtFriendlyName)
        val ipAndPortTextView = rowView.findViewById<TextView>(R.id.txtIpAndPort)
        val relaysTextView = rowView.findViewById<TextView>(R.id.txtRelays)

        val relayModule = getItem(position) as RelayModule
        frieldlyNameTextView.text = relayModule.friendlyName
        ipAndPortTextView.text = "${relayModule.ipAddress}:${relayModule.port}"
        relaysTextView.text = "${relayModule.relays} relays"

        return rowView
    }
}