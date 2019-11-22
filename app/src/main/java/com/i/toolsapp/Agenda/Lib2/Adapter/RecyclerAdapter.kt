package  com.i.toolsapp.Agenda.Lib2.Adapter

import android.content.ClipData
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.*
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.i.toolsapp.Agenda.Lib2.Dialogs.AddReservation
import com.i.toolsapp.Agenda.Lib2.Dialogs.DetailsReservation
import com.i.toolsapp.Agenda.Lib2.LibActivity2
import com.i.toolsapp.Agenda.Models.Time
import com.i.toolsapp.Agenda.Lib2.OnClicked
import com.i.toolsapp.R
import kotlinx.android.synthetic.main.activity_lib2.*
import kotlinx.android.synthetic.main.event_layout.view.*
import kotlinx.android.synthetic.main.item_data_row.view.*
import org.threeten.bp.LocalDate
import java.text.SimpleDateFormat

class RecyclerAdapter(var context: Context, var list : MutableList<DataRow>, var interval : Int, var onClicked: OnClicked,
                      var selectedData : LocalDate?, var showDetails : Boolean) : RecyclerView.Adapter<RecyclerAdapter.Holder>(){
    lateinit var activity : LibActivity2
    init {
        if(context is LibActivity2)
            activity = context as LibActivity2
    }
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
        holder.eventsContainer.tag = list.get(position).debut+"_"+list.get(position).fin
        holder.eventsContainer.removeAllViews()
        holder.eventsContainer.setOnDragListener(object : View.OnDragListener{
            override fun onDrag(p0: View?, p1: DragEvent?): Boolean {
                val action = p1?.action
                when(action){
                    DragEvent.ACTION_DRAG_STARTED -> {
                        return true
                    }
                    DragEvent.ACTION_DRAG_ENTERED -> {
                        p0?.setBackgroundResource(R.drawable.shape_drop_target)
                        if(activity.firsVisibleItem() == position && position>0){
                            activity.recycler.smoothScrollToPosition(position-1)
                        }else if(activity.lastVisibleItem() == position && position<itemCount-1){
                            activity.recycler.smoothScrollToPosition(position+1)
                        }
                    }

                    DragEvent.ACTION_DRAG_EXITED -> {
                        p0?.setBackgroundResource(0)
                    }
                    DragEvent.ACTION_DROP -> {
                        val view = p1.localState as? View
                        val viewGroup = view?.parent as? ViewGroup
                        viewGroup?.removeView(view)
                        val container = p0 as? LinearLayout
                        if(container!=null && container.childCount==0){
                            container.addView(view)
                            view?.visibility = VISIBLE
                            val title = view?.tag.toString()
                            val debut = container.tag.toString().split("_")[0]
                            val fin = container.tag.toString().split("_")[1]
                            activity.updateEventHour(title,debut,fin)
                        }else {
                            viewGroup?.addView(view)
                            view?.visibility = VISIBLE
                        }
                    }
                    DragEvent.ACTION_DRAG_ENDED-> {
                        p0?.setBackgroundResource(0)
                    }
                    else ->
                        return true
                }
                return true
            }
        })
        holder.eventsContainer.setOnClickListener {
            if(holder.eventsContainer.childCount==0){
                if(selectedData!=null){
                    val time = Time(selectedData!!.year,selectedData!!.monthValue,selectedData!!.dayOfMonth,
                        list.get(position).debut.split(":")[0].toInt(),
                        list.get(position).debut.split(":")[1].toInt())
                    val dialog = AddReservation(time, onClicked, interval, showDetails)
                    if(context is LibActivity2){
                        dialog.show((context as LibActivity2).supportFragmentManager,"")
                    }
                }
            }
        }
        if(list.get(position).events.size>0){
            val nbSegment = 1
            list.get(position).events.sortBy { it.h_debut }
            for(event in list.get(position).events){
                val layout = LayoutInflater.from(context).inflate(R.layout.event_layout,null,false)
                layout.tag = event.text
                if(showDetails) {
                    layout.eventName.text = event.text
                    layout.img_details.visibility = VISIBLE
                }
                layout.eventBackground.setBackgroundColor(Color.parseColor(event.color))
                layout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,1f)

                if(showDetails){
                    layout.img_details.setOnClickListener {
                            val dialog = DetailsReservation(event, onClicked)
                            if(context is LibActivity2){
                                dialog.show((context as LibActivity2).supportFragmentManager,"")
                            }
                    }
                }
                if(showDetails){
                    layout.setOnTouchListener(object : View.OnTouchListener{
                        override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                            Log.e("MotionEvent",p1?.action.toString())
                            if(p1?.action == MotionEvent.ACTION_DOWN){
                                val data = ClipData.newPlainText("","")
                                val shadowBuilder = View.DragShadowBuilder(p0)
                                p0?.startDrag(data,shadowBuilder,p0,0)
                                p0?.visibility = INVISIBLE
                            }
                            return false
                        }
                    })
                }

                holder.eventsContainer.weightSum = nbSegment.toFloat()
                holder.eventsContainer.addView(layout)


            }
        }
    }

    fun updateHourEvents(events : MutableList<RowEvent>){
        clearEvents()
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

    fun clearEvents(){
        for(row in list){
            row.events.clear()
        }
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