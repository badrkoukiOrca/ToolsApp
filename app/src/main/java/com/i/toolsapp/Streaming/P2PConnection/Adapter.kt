package com.maa.wifi_direct_app

import android.content.Context
import android.net.wifi.p2p.WifiP2pDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.i.toolsapp.R
import kotlinx.android.synthetic.main.item_peer.view.*

class Adapter(var context: Context,var peers : MutableList<WifiP2pDevice>,var onConnect: onConnect) : RecyclerView.Adapter<Adapter.Holder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_peer,parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return peers.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.tag = peers.get(position)
        holder.txt.text = peers.get(position).deviceName
        holder.mac.text = peers.get(position).deviceAddress
    }


    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val txt : TextView
        val mac : TextView
        init {
            txt = itemView.peer_text
            mac = itemView.mac

            itemView.setOnClickListener {
                val peer = itemView.tag as? WifiP2pDevice
                if(peer!=null)
                    onConnect.onConnect(peer)
            }
        }


    }
}