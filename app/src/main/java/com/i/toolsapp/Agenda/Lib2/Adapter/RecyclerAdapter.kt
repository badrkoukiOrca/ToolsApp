package com.i.toolsapp.Agenda.Lib2.Adapter
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.i.toolsapp.Agenda.Dialogs.DialogAddEvent2
import com.i.toolsapp.Agenda.Dialogs.DialogDetails2
import com.i.toolsapp.Agenda.Lib2.LibActivity2
import com.i.toolsapp.Agenda.Lib2.OnClicked
import com.i.toolsapp.R


import kotlinx.android.synthetic.main.event_layout.view.*
import kotlinx.android.synthetic.main.item_data_row.view.*
import org.threeten.bp.LocalDate
import java.text.SimpleDateFormat
import java.util.*

class RecyclerAdapter(var context: Context, var list : MutableList<DataRow>, var interval : Int, var onClicked: OnClicked, var selectedData : LocalDate?) : RecyclerView.Adapter<RecyclerAdapter.Holder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_data_row,parent,false)
        return Holder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.tag = list.get(position)
        holder.Hdebut.text = list.get(position).debut
        holder.eventsContainer.removeAllViews()

        holder.eventsContainer.setOnClickListener {
            if(holder.eventsContainer.childCount==0){
                if(selectedData!=null){
                    val time = Calendar.getInstance()
                    time.set(Calendar.MONTH,selectedData!!.monthValue)
                    time.set(Calendar.YEAR,selectedData!!.year)
                    time.set(Calendar.DAY_OF_MONTH,selectedData!!.dayOfMonth)
                    time.set(Calendar.HOUR_OF_DAY,list.get(position).debut.split(":")[0].toInt())
                    time.set(Calendar.MINUTE,list.get(position).debut.split(":")[1].toInt())
                    val dialog = DialogAddEvent2(time,onClicked)
                    if(context is LibActivity2){
                        dialog.show((context as LibActivity2).supportFragmentManager,"")
                    }
                }
            }
        }
        if(list.get(position).events.size>0){
            val nbSegment = interval / 5
            list.get(position).events.sortBy { it.h_debut }
            for(event in list.get(position).events){
                val layout = LayoutInflater.from(context).inflate(R.layout.event_layout,null,false)
                layout.eventName.text = event.text
                layout.eventBackground.setBackgroundColor(Color.parseColor(event.color))
                layout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,1f)

                layout.setOnClickListener {
                    val dialog = DialogDetails2(event)
                    if(context is LibActivity2){
                        dialog.show((context as LibActivity2).supportFragmentManager,"")
                    }
                }


                holder.eventsContainer.weightSum = nbSegment.toFloat()
                holder.eventsContainer.addView(layout)
            }
        }
    }

    fun updateHourEvents(events : MutableList<RowEvent>){
        for(event in events){
//            list.find { it.debut.equals(event.h_debut) ||
//                    (SimpleDateFormat("HH:mm").parse(event.h_debut).after(SimpleDateFormat("HH:mm").parse(it.debut))
//                            &&
//                            SimpleDateFormat("HH:mm").parse(event.h_debut).before(SimpleDateFormat("HH:mm").parse(it.fin)))
//            }?.events?.add(event)

            for(row in list){
                if(row.debut.equals(event.h_debut) ||
                    (SimpleDateFormat("HH:mm").parse(event.h_debut).after(SimpleDateFormat("HH:mm").parse(row.debut)) &&
                            SimpleDateFormat("HH:mm").parse(event.h_debut).before(SimpleDateFormat("HH:mm").parse(row.fin)))){
                    row.events.add(event)
                }
            }
        }
        notifyDataSetChanged()
    }

    inner class Holder(view : View) : RecyclerView.ViewHolder(view){
        val Hdebut : TextView
        val eventsContainer : LinearLayout
        val item_data_row_container : LinearLayout
        init {
            Hdebut = view.Hdebut
            eventsContainer = view.eventContainer
            item_data_row_container = view.item_data_row_container
        }
    }
}