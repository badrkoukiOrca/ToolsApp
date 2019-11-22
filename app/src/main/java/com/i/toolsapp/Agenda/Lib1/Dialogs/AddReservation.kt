package com.i.toolsapp.Agenda.Lib1.Dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.alamkanak.weekview.WeekViewEvent
import com.i.toolsapp.Agenda.Lib2.Adapter.RowEvent
import com.i.toolsapp.Agenda.Models.Time
import com.i.toolsapp.Agenda.Lib2.OnClicked
import com.i.toolsapp.Agenda.Models.Interval
import com.i.toolsapp.R
import kotlinx.android.synthetic.main.dialog_add_reservation_weekview.*
import java.util.*

class AddReservation(val time : Time, var onClicked: OnClicked, var interval : Int, var isPilote : Boolean,var events : MutableList<WeekViewEvent>) : DialogFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_add_reservation_weekview,container,false)
    }


    lateinit var selectedInterval : Interval
    var aucunInterval = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spHours.adapter = SpinnerAdapter(context!!,generateHoursList(time,interval))
        if(!isPilote)
            titre.visibility = GONE
        val h_debut = if(time.hour<=9) "0"+time.hour.toString()
        else time.hour.toString()
        val m_debut = if(time.minute<=9) "0"+time.minute.toString()
        else time.minute.toString()
        val timeFin = Calendar.getInstance()
        timeFin.set(Calendar.YEAR,time.year)
        timeFin.set(Calendar.MONTH,time.month)
        timeFin.set(Calendar.DAY_OF_MONTH,time.day)
        timeFin.set(Calendar.HOUR_OF_DAY,time.hour)
        timeFin.set(Calendar.MINUTE,time.minute)


        timeFin.add(Calendar.MINUTE,interval)
        val h_fin = if(timeFin.get(Calendar.HOUR_OF_DAY)<=9) "0"+timeFin.get(Calendar.HOUR_OF_DAY).toString()
        else timeFin.get(Calendar.HOUR_OF_DAY).toString()
        val m_fin = if(timeFin.get(Calendar.MINUTE)<=9) "0"+timeFin.get(Calendar.MINUTE).toString()
        else timeFin.get(Calendar.MINUTE).toString()


        val sDate = (if(time.day<=9) "0"+time.day.toString() else
            time.day.toString())+"/"+
                (if(time.month<=9) "0"+time.month.toString() else
                    time.month.toString()) + "/"+
                (if(time.year<=9) "0"+time.year.toString() else
                    time.year.toString())
        date.text = sDate
//        debut.text = "Heure début : "+h_debut+":"+m_debut
//        fin.text = "Heure fin : "+h_fin+":"+m_fin
        de_a.text = "De $h_debut:$m_debut à $h_fin:$m_fin \n ($interval min)"
        valider.setOnClickListener {
            if(aucunInterval){
                dismiss()
                return@setOnClickListener
            }
            if((isPilote && checkInputs()) || !isPilote){
                val color = if(isPilote) "#1261A0" else "#FF7F50"
                if(!isPilote)
                    titre.setText("Réservation pour skieur No: ${(1..100).random()}")

                val eventWeek = WeekViewEvent((1..100).random().toLong(),
                        titre.text.toString(),selectedInterval.from.year,selectedInterval.from.month,
                        selectedInterval.from.day,selectedInterval.from.hour,selectedInterval.from.minute,
                        selectedInterval.to.year,selectedInterval.to.month,selectedInterval.to.day,selectedInterval.to.hour,selectedInterval.to.minute)
                eventWeek.color = if(isPilote) ContextCompat.getColor(context!!,R.color.pilote_color) else ContextCompat.getColor(context!!,R.color.skieur_color)
                onClicked.onHourClicked(eventWeek)
                dismiss()
            }else {
                Toast.makeText(context!!,"Vérfier heure debut/fin", Toast.LENGTH_SHORT).show()
            }
        }

        spHours.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(parent!=null && parent.adapter !=null){
                    val inter = parent.getItemAtPosition(position) as Interval
                    val from_hour = if(inter.from.hour <=9) "0"+inter.from.hour.toString()
                    else inter.from.hour.toString()
                    val from_min = if(inter.from.minute <=9) "0"+inter.from.minute.toString()
                    else inter.from.minute.toString()

                    val to_hour = if(inter.to.hour <=9) "0"+inter.to.hour.toString()
                    else inter.to.hour.toString()
                    val to_min = if(inter.to.minute <=9) "0"+inter.to.minute.toString()
                    else inter.to.minute.toString()
                    val diffMinute = (inter.to.hour*60 + inter.to.minute) - (inter.from.hour*60 + inter.from.minute)
                    de_a.text = "De $from_hour:$from_min - $to_hour:$to_min\n ($diffMinute min)"
                    selectedInterval = inter
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }


    fun checkInputs() : Boolean{
        return titre.text.isNotEmpty()
    }

    fun generateHoursList(time : Time,interval: Int) : MutableList<Interval> {
        val intervals = mutableListOf<Interval>()

        for(i in 0..59 step interval){
            var j = i
            if(i+interval>=60){
                val inter = Interval(Time(time.year,time.month,time.day,time.hour,i),
                        Time(time.year,time.month,time.day,time.hour+1,0))
                if(checkIntervalEmpty(inter,time.hour,time.hour+1,i,0))
                    intervals.add(inter)
            } else {
                val inter = Interval(Time(time.year,time.month,time.day,time.hour,i),
                        Time(time.year,time.month,time.day,time.hour,i+interval))
                if(checkIntervalEmpty(inter,time.hour,time.hour,i,i+interval))
                    intervals.add(inter)
            }
        }
        if(intervals.size==0) {
            aucunInterval = true
            hideInputs()
        }
        return intervals
    }

    fun checkIntervalEmpty(time : Interval,from_hour : Int,to_hour : Int,from_min : Int , to_min : Int) : Boolean{
        var tr = true
        val tempEvent = events.filter {
            it.startTime.get(Calendar.YEAR) == time.from.year &&
            it.startTime.get(Calendar.MONTH)+1 == time.from.month &&
            it.startTime.get(Calendar.DAY_OF_MONTH) == time.from.day &&
            it.startTime.get(Calendar.HOUR_OF_DAY) == time.from.hour &&


                    it.endTime.get(Calendar.YEAR) == time.to.year &&
                    it.endTime.get(Calendar.MONTH)+1 == time.to.month &&
                    it.endTime.get(Calendar.DAY_OF_MONTH) == time.to.day}

        for(event in tempEvent){
            if( (from_hour == to_hour && from_min >= event.startTime.get(Calendar.MINUTE) && to_min<=event.endTime.get(Calendar.MINUTE))
                    || (from_hour<to_hour && from_min >= event.startTime.get(Calendar.MINUTE) && event.endTime.get(Calendar.MINUTE) == to_min ))
                tr=false
        }
        return tr
    }
    override fun onResume() {
        super.onResume()
        val params = dialog!!.window!!.attributes
        params.width = LinearLayout.LayoutParams.MATCH_PARENT
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as android.view.WindowManager.LayoutParams
    }

    fun hideInputs(){
        container_add_reservation_weekview.visibility = GONE
        aucun_interval_dispo.visibility = VISIBLE
    }


    inner class SpinnerAdapter(var context : Context,var intervals : MutableList<Interval>) : BaseAdapter(){
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var view = convertView
            if(view==null){
                view = LayoutInflater.from(context).inflate(R.layout.item_spinner_interval,parent,false)
            }

            val txt = view?.findViewById<TextView>(R.id.txt_interval)
            val from_hour = if(intervals.get(position).from.hour <=9) "0"+intervals.get(position).from.hour.toString()
            else intervals.get(position).from.hour.toString()
            val from_min = if(intervals.get(position).from.minute <=9) "0"+intervals.get(position).from.minute.toString()
            else intervals.get(position).from.minute.toString()

            val to_hour = if(intervals.get(position).to.hour <=9) "0"+intervals.get(position).to.hour.toString()
            else intervals.get(position).to.hour.toString()
            val to_min = if(intervals.get(position).to.minute <=9) "0"+intervals.get(position).to.minute.toString()
            else intervals.get(position).to.minute.toString()
            txt?.text = "$from_hour:$from_min - $to_hour:$to_min"
            return view!!
        }

        override fun getItem(position: Int): Any {
            return  intervals.get(position)
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getCount(): Int {
            return intervals.size
        }

    }
}