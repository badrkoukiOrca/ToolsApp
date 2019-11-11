package com.maa.wifi_direct_app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log
import com.i.toolsapp.Streaming.StreamingConnection

class P2pReceiver(var manager : WifiP2pManager, var channel  :WifiP2pManager.Channel,
                  var activity: StreamingConnection, var peerListListener : WifiP2pManager.PeerListListener,
                  var connectionInfoListener : WifiP2pManager.ConnectionInfoListener) : BroadcastReceiver(){
    override fun onReceive(p0: Context?, p1: Intent?) {
        when(p1?.action) {
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                // Determine if Wifi P2P mode is enabled or not, alert
                // the Activity.
//                val state = p1.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
//                activity.isWifiP2pEnabled = state == WifiP2pManager.WIFI_P2P_STATE_ENABLED


                Log.e("P2pState","WIFI_P2P_STATE_CHANGED_ACTION")
            }
            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {

                // The peer list has changed! We should probably do something about
                // that.

                manager.requestPeers(channel,peerListListener)
                Log.e("P2pState","WIFI_P2P_PEERS_CHANGED_ACTION")
            }
            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {

                // Connection state changed! We should probably do something about
                // that.

                manager.let {
                    val networkInfo : NetworkInfo? = p1
                        .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO) as? NetworkInfo

                    if (networkInfo!=null && networkInfo.isConnected == true) {

                        // We are connected with the other device, request connection
                        // info to find group owner IP

                        it.requestConnectionInfo(channel, connectionInfoListener)
                    }
                }
                Log.e("P2pState","WIFI_P2P_CONNECTION_CHANGED_ACTION")
            }
            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                Log.e("P2pState","WIFI_P2P_THIS_DEVICE_CHANGED_ACTION")

            }
        }
    }
}