package com.i.toolsapp.Agenda.Lib2

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.alamkanak.weekview.WeekViewEvent
import com.i.toolsapp.Agenda.Lib2.Adapter.DataRow
import com.i.toolsapp.Agenda.Lib2.Adapter.RecyclerAdapter
import com.i.toolsapp.Agenda.Lib2.Adapter.RowEvent
import com.i.toolsapp.R
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.MonthScrollListener



import kotlinx.android.synthetic.main.activity_lib2.*
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.temporal.WeekFields
import java.text.SimpleDateFormat
import java.util.*

class LibActivity2 : AppCompatActivity(),OnClicked {
    var selectedDate : LocalDate? = null
    var adapter : RecyclerAdapter? =null
    var INTERVAL : Int = 10
    val events = mutableListOf<RowEvent>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lib2)


        selectedDate = LocalDate.now()
        calendarLib2.dayBinder = object : DayBinder<DayViewContainer>{
            override fun create(view: View) = DayViewContainer(view,this@LibActivity2)

            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                container.dayNb.text = day.date.dayOfMonth.toString()
                container.dayName.text = day.date.dayOfWeek.name
                container.month.text = day.date.month.name.substring(0,3)


                container.dayContainer.setBackgroundResource(if (selectedDate == day.date) R.drawable.background_selected else
                    0)

            }
        }
        calendarLib2.monthScrollListener = object : MonthScrollListener{
            override fun invoke(p1: CalendarMonth) {
                monthName.text = p1.yearMonth.month.name + " " + p1.yearMonth.year
            }
        }
        calendarLib2.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthHeaderView> {
            override fun create(view: View): MonthHeaderView {return MonthHeaderView(view)
            }
            override fun bind(header: MonthHeaderView, month: CalendarMonth) {}
        }
        calendarLib2.monthFooterBinder = object : MonthHeaderFooterBinder<MonthHeaderView>{
            override fun create(view: View): MonthHeaderView { return MonthHeaderView(view)
            }
            override fun bind(header: MonthHeaderView, month: CalendarMonth) {}
        }


        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek


        calendarLib2.setup(firstMonth, lastMonth, firstDayOfWeek)
        calendarLib2.scrollToMonth(currentMonth)



        generateHours(10)
        generateEvents()
        recycler.layoutManager = LinearLayoutManager(this@LibActivity2)
        adapter = RecyclerAdapter(this@LibActivity2,generateHours(10),INTERVAL,this,selectedDate)
        recycler.adapter = adapter

        intervalOK.setOnClickListener {
            INTERVAL = interval.text.toString().toInt()
            adapter = RecyclerAdapter(this@LibActivity2,generateHours(interval.text.toString().toInt()),
                INTERVAL,this,selectedDate)
            recycler.adapter = adapter
        }
        adapter?.updateHourEvents(events)

        addEvents.setOnClickListener {
            adapter?.updateHourEvents(events)
        }
    }

    override fun onViewClicked(view: View, day: CalendarDay) {
        if(day.owner == DayOwner.THIS_MONTH){
            val oldDate = selectedDate
            selectedDate = day.date
            calendarLib2.notifyDayChanged(day)
            oldDate?.let { calendarLib2.notifyDateChanged(it) }
        }
    }

    fun generateHours(interval : Int) : MutableList<DataRow> {
        val hours = mutableListOf<DataRow>()

        for(hour in 0..23) {
            for (minute in 0..59 step interval) {
                val h = if (hour <= 9) "0" + hour.toString() else hour.toString()
                val m = if (minute <= 9) "0" + minute.toString() else minute.toString()

                var fin = minute+interval
                if(fin>=60)
                    fin = fin - (fin-60+1)
                val mFin = if (fin <= 9) "0" + fin.toString() else fin.toString()
                hours.add(DataRow(h + ":" + m,h+":"+mFin,mutableListOf<RowEvent>()))
            }
        }

        return hours
    }

    fun generateEvents(){
        val events = mutableListOf<RowEvent>()
        events.add(RowEvent("00:05","00:10","11/11/2019","Réservation 2","#ff00ff"))
        events.add(RowEvent("00:00","00:05","11/11/2019","Réservation 1","#ffff00"))

        events.add(RowEvent("01:00","01:10","11/11/2019","Réservation 3","#ff4444"))
        events.add(RowEvent("02:00","02:10","11/11/2019","Réservation 4","#884854"))
        this.events.addAll(events)
    }


    override fun onHourClicked(event : RowEvent) {
        events.add(event)
        val e = mutableListOf<RowEvent>()
        e.add(event)
        adapter?.updateHourEvents(e)

        var eventDate = SimpleDateFormat("dd/MM/yyyy").parse(event.date)
        val start = Calendar.getInstance()
        start.time = eventDate
        start.set(Calendar.HOUR_OF_DAY,event.h_debut.split(":")[0].toInt())
        start.set(Calendar.MINUTE,event.h_debut.split(":")[1].toInt())

        val end = Calendar.getInstance()
        end.time = eventDate
        end.set(Calendar.HOUR_OF_DAY,event.h_fin.split(":")[0].toInt())
        end.set(Calendar.MINUTE,event.h_fin.split(":")[1].toInt())


        val result = Event(start,end,event.text)

        AddEvent(this@LibActivity2).execute(result)
    }

    class AddEvent(var context: Context) : AsyncTask<Event, Void, Void?>(){
        @SuppressLint("MissingPermission")
        override fun doInBackground(vararg p0: Event?): Void? {
            val calId : Long = 1
            val event = p0[0]
            val values = ContentValues().apply {
                put(CalendarContract.Events.DTSTART, event?.start?.timeInMillis)
                put(CalendarContract.Events.DTEND, event?.start?.timeInMillis)
                put(CalendarContract.Events.TITLE, event?.title)
                put(CalendarContract.Events.DESCRIPTION, event?.title)
                put(CalendarContract.Events.CALENDAR_ID, calId)
                put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Paris")
            }

            val uri: Uri? = context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
            return null
        }
        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            Toast.makeText(context,"Event created", Toast.LENGTH_SHORT).show()
        }
    }
}
