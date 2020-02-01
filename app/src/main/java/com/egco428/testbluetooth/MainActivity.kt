package com.egco428.testbluetooth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.bluetooth.BluetoothAdapter
import android.widget.ListView
import android.bluetooth.BluetoothDevice
import android.companion.WifiDeviceFilter
import android.content.IntentFilter
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private var listView: ListView? = null
    private val mDeviceList = ArrayList<String>()
    private var mBluetoothAdapter: BluetoothAdapter? = null
    var REQUEST_ENABLE_BT = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupBluetooth()
        scanBluetooth()

    }

    private fun scanBluetooth(){
        mBluetoothAdapter!!.startDiscovery()
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(mReceiver, filter)
    }

    private fun setupBluetooth(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (mBluetoothAdapter == null) {
            // If the adapter is null it means that the device does not support Bluetooth
            Toast.makeText(this,"device does not support Bluetooth",Toast.LENGTH_SHORT).show()
            println("device does not support Bluetooth")
        }else{
            Toast.makeText(this,"device support Bluetooth",Toast.LENGTH_SHORT).show()
        }

        if (mBluetoothAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, 0)
        }
    }

    private val mReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action: String = intent.action!!
            when(action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address // MAC address
                    println("this is $deviceHardwareAddress")
                    Toast.makeText(context, "this is $deviceHardwareAddress",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(mReceiver)
    }
}
