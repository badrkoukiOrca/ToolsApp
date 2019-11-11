package com.maa.wifi_direct_app

import android.net.wifi.p2p.WifiP2pDevice

interface onConnect{
    fun onConnect(device : WifiP2pDevice)
}