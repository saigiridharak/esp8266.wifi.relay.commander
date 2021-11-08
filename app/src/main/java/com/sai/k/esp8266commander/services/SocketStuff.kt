package com.sai.k.esp8266commander.services

import android.os.AsyncTask
import java.io.DataOutputStream
import java.net.InetSocketAddress
import java.net.Socket

class SocketStuff() : AsyncTask<String, Void, String>() {

    private lateinit var socket: Socket

    override fun doInBackground(vararg params: String): String? {
        val serverIp = params[0] //"192.168.1.32"
        val serverPort = params[1].toInt() //8080
        val address = InetSocketAddress(serverIp, serverPort)

        if (params[2] == "CheckConnection") {
            return checkConnection(address)
        }
        else if (params[2] == "RelayOn" || params[2] == "RelayOff") {
            return sendCommand(address, params[2], params[3].toInt())
        }

        return "Missing parameters"
    }

    private fun checkConnection(address: InetSocketAddress): String? {
        try {
            socket = Socket()
            socket.connect(address, 2000)
            socket.close()
        }
        catch (ex: Exception){
            return ex.message
        }

        return "Connected"
    }

    private fun sendCommand(address: InetSocketAddress, onOrOff: String, relayNo: Int): String? {
        socket = Socket()
        try {
            socket.connect(address, 2000)

            val command = getCommand(relayNo, onOrOff)

            val out = socket.getOutputStream()
            val dOut = DataOutputStream(out)
            dOut.write(command)
            dOut.flush()
            dOut.close()
            out.flush()
            out.close()
            socket.close()
        }
        catch (ex: Exception){
            return ex.message
        }

        return "Executed"
    }

    private fun getCommand(relayNo: Int, onOrOff: String): ByteArray {
        val relayOneOnCommand = byteArrayOf(0xA0.toByte(), 0x01.toByte(), 0x01.toByte(), 0xA2.toByte()) // One on
        val relayOneOffCommand = byteArrayOf(0xA0.toByte(), 0x01.toByte(), 0x00.toByte(), 0xA1.toByte()) // One off
        val relayTwoOnCommand = byteArrayOf(0xA0.toByte(), 0x02.toByte(), 0x01.toByte(), 0xA3.toByte()) // Two on
        val relayTwoOffCommand = byteArrayOf(0xA0.toByte(), 0x02.toByte(), 0x00.toByte(), 0xA2.toByte()) // Two off
        val relayThreeOnCommand = byteArrayOf(0xA0.toByte(), 0x03.toByte(), 0x01.toByte(), 0xA4.toByte()) // Three on
        val relayThreeOffCommand = byteArrayOf(0xA0.toByte(), 0x03.toByte(), 0x00.toByte(), 0xA3.toByte()) // Three off
        val relayFounrOnCommand = byteArrayOf(0xA0.toByte(), 0x04.toByte(), 0x01.toByte(), 0xA5.toByte()) // Four on
        val relayFourOffCommand = byteArrayOf(0xA0.toByte(), 0x04.toByte(), 0x00.toByte(), 0xA4.toByte()) // Four off

        var command = relayOneOnCommand
        if (relayNo == 1) {
            if (onOrOff == "RelayOn") {
                command = relayOneOnCommand
            } else if (onOrOff == "RelayOff") {
                command = relayOneOffCommand
            }
        } else if (relayNo == 2) {
            if (onOrOff == "RelayOn") {
                command = relayTwoOnCommand
            } else if (onOrOff == "RelayOff") {
                command = relayTwoOffCommand
            }
        } else if (relayNo == 3) {
            if (onOrOff == "RelayOn") {
                command = relayThreeOnCommand
            } else if (onOrOff == "RelayOff") {
                command = relayThreeOffCommand
            }
        } else if (relayNo == 4) {
            if (onOrOff == "RelayOn") {
                command = relayFounrOnCommand
            } else if (onOrOff == "RelayOff") {
                command = relayFourOffCommand
            }
        }
        return command
    }


}