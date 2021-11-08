package com.sai.k.esp8266commander.ui.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.sai.k.esp8266commander.R

class HempFragment : Fragment() {

    private lateinit var helpViewModel: HelpViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        helpViewModel =
            ViewModelProviders.of(this).get(HelpViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_help, container, false)
        //val textView: TextView = root.findViewById(R.id.text_notifications)
        //notificationsViewModel.text.observe(this, Observer {
            //textView.text = it
        //})
        return root
    }
}