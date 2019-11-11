package com.i.toolsapp.Streaming

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.net.wifi.WpsInfo
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.i.toolsapp.R
import com.i.toolsapp.Streaming.ClientSide.ClientCamera
import com.i.toolsapp.Streaming.P2PConnection.ConnectioData
import com.i.toolsapp.Streaming.ServerSide.ServerActivity
import com.maa.wifi_direct_app.Adapter
import com.maa.wifi_direct_app.P2pReceiver
import com.maa.wifi_direct_app.onConnect
import kotlinx.android.synthetic.main.activity_connect.*
import java.lang.reflect.InvocationTargetException


class StreamingConnection : AppCompatActivity(), onConnect {
    override fun onConnect(device: WifiP2pDevice) {
        connect(device)
    }

    val intentFilter = IntentFilter()
    lateinit var receiver: P2pReceiver
    val peers = mutableListOf<WifiP2pDevice>()

    lateinit var channel: WifiP2pManager.Channel
    lateinit var manager: WifiP2pManager
    lateinit var peerListListener: WifiP2pManager.PeerListListener
    lateinit var adapter: Adapter
    lateinit var connectionListener: WifiP2pManager.ConnectionInfoListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect)

        peersRV.layoutManager = LinearLayoutManager(this@StreamingConnection)
        adapter = Adapter(this@StreamingConnection, peers, this)
        peersRV.adapter = adapter




        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)


        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)


        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)


        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)

        manager = getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        channel = manager.initialize(this, mainLooper, null)

        deletePersistentGroup(manager,channel)


        ConnectioData.wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        ConnectioData.wifiManager.setWifiEnabled(true)
        Toast.makeText(
                this@StreamingConnection,
                "Wifi enabled",
                Toast.LENGTH_SHORT
        ).show()

        var dialog =  Dialog(this@StreamingConnection)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_box_layout)

        var button_discover = dialog.findViewById<Button>(R.id.discover_connections)
        button_discover.setOnClickListener {

            manager.discoverPeers(channel, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {

                }
                override fun onFailure(p0: Int) {

                }
            })
            dialog.dismiss()
        }
        dialog.show()


        peerListListener = WifiP2pManager.PeerListListener {
            val refreshedPeers = it.deviceList
            if (refreshedPeers != peers) {
                peers.clear()
                peers.addAll(refreshedPeers)

                adapter.peers = peers
                adapter.notifyDataSetChanged()
                Toast.makeText(
                        this@StreamingConnection,
                        "Select the device to connect to",
                        Toast.LENGTH_SHORT
                ).show()
            }

            if (peers.isEmpty()) {
                Log.d("P2p", "No devices found")
                return@PeerListListener
            }
        }


        connectionListener = WifiP2pManager.ConnectionInfoListener { info ->


            val groupOwnerAddress: String = info.groupOwnerAddress.hostAddress

            Log.e("P2p", groupOwnerAddress)
            if (info.groupFormed && info.isGroupOwner) {
                ConnectioData.ServerIPAdress = groupOwnerAddress
                val intent = Intent(this, ServerActivity::class.java)
                startActivity(intent)
            } else if (info.groupFormed) {
                Log.e("P2p", "groupFormed")
                //Starting client
                Thread.sleep(3000)
                dialog.dismiss()
                ConnectioData.ServerIPAdress = groupOwnerAddress
                val intent = Intent(this, ClientCamera::class.java)
                startActivity(intent)

            }
        }
    }

    fun connect(device: WifiP2pDevice) {
        val config = WifiP2pConfig().apply {
            deviceAddress = device.deviceAddress
            wps.setup = WpsInfo.PBC
        }

        manager.connect(channel, config, object : WifiP2pManager.ActionListener {

            override fun onSuccess() {
                Toast.makeText(
                        this@StreamingConnection,
                        "Success",
                        Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailure(reasqon: Int) {
                Toast.makeText(
                        this@StreamingConnection,
                        "Connect failed. Retry.",
                        Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun deletePersistentGroup(manager:WifiP2pManager, channel:WifiP2pManager.Channel) {
        try
        {
            val method = WifiP2pManager::class.java!!.getMethod("deletePersistentGroup",
                    WifiP2pManager.Channel::class.java, Int::class.javaPrimitiveType, WifiP2pManager.ActionListener::class.java)
            for (netId in 0..31)
            {
                method.invoke(manager, channel, netId, null)
            }
        }
        catch (e:NoSuchMethodException) {
            e.printStackTrace()
        }
        catch (e:InvocationTargetException) {
            e.printStackTrace()
        }
        catch (e:IllegalAccessException) {
            e.printStackTrace()
        }
    }


    override fun onResume() {
        super.onResume()
        receiver = P2pReceiver(manager,channel,this,peerListListener,connectionListener)
        registerReceiver(receiver,intentFilter)
    }


}
