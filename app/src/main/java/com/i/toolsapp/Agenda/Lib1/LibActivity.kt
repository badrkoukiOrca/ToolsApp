package com.i.toolsapp.Agenda.Lib1

import android.graphics.RectF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.alamkanak.weekview.MonthLoader
import com.alamkanak.weekview.WeekView
import com.alamkanak.weekview.WeekViewEvent
import kotlinx.android.synthetic.main.activity_lib.*
import java.util.*
import android.util.TypedValue
import com.i.toolsapp.Agenda.Dialogs.DialogNumberPicker
import com.i.toolsapp.Agenda.Dialogs.ProfileDialog
import com.i.toolsapp.Agenda.Lib1.Dialogs.AddReservation
import com.i.toolsapp.Agenda.Lib1.Dialogs.DetailsReservation
import com.i.toolsapp.Agenda.Models.Time
import com.i.toolsapp.Agenda.Lib2.OnClicked
import com.i.toolsapp.R
import kotlinx.android.synthetic.main.activity_lib.account
import kotlinx.android.synthetic.main.activity_lib.interval


class LibActivity : AppCompatActivity(),OnClicked {
    override fun onInterval(interval: Int) {
        super.onInterval(interval)
        INTERVAL = interval
    }
    override fun onProfileClicked(profile: String) {
        super.onProfileClicked(profile)
        isPilote = profile.equals("PILOTE")
        if(!isPilote){
            weekView.eventTextSize = 0
        }else {
            weekView.eventTextSize = 24
        }
        weekView.notifyDatasetChanged()
    }
    override fun onModifieReservation(oldTitle: String, newTitle: String) {
        super.onModifieReservation(oldTitle, newTitle)
        ModifyEventByTitle(oldTitle,newTitle)
    }
    override fun onDeleteReservation(title: String) {
        super.onDeleteReservation(title)
        DeleteReservationByTitle(title)
    }
    override fun onHourClicked(event: WeekViewEvent) {
        super.onHourClicked(event)
        events.add(event)
        weekView.notifyDatasetChanged()
    }
    val events = mutableListOf<WeekViewEvent>()
    var isPilote = false
    var INTERVAL = 18
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lib)
        if(resources.getBoolean(R.bool.isTablet)) {
            weekView.numberOfVisibleDays = 7

            // Lets change some dimensions to best fit the view.
            weekView.columnGap = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                2f,
                resources.displayMetrics
            ).toInt()
            weekView.textSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                10f,
                resources.displayMetrics
            ).toInt()
            weekView.eventTextSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                10f,
                resources.displayMetrics
            ).toInt()
        }

        weekView.goToHour("8".toDouble())
        weekView.setOnEventClickListener(object : WeekView.EventClickListener{
            override fun onEventClick(event: WeekViewEvent?, eventRect: RectF?) {
                if(isPilote && event!=null){
                    val dialog = DetailsReservation(event,this@LibActivity)
                    dialog.show(supportFragmentManager,"")
                }

            }
        })
        weekView.monthChangeListener = object : MonthLoader.MonthChangeListener{
            override fun onMonthChange(newYear: Int, newMonth: Int): MutableList<out WeekViewEvent> {
                val events = getEventByMonth(newMonth-1)
                return events
            }
        }
        weekView.emptyViewClickListener = object : WeekView.EmptyViewClickListener{
            override fun onEmptyViewClicked(time: Calendar?) {
                if(time!=null){
                    time.set(Calendar.MONTH,time.get(Calendar.MONTH)+1)
                    val t = Time(time.get(Calendar.YEAR),time.get(Calendar.MONTH),time.get(Calendar.DAY_OF_MONTH),
                            time.get(Calendar.HOUR),0)
                    val dialog = AddReservation(t,this@LibActivity,INTERVAL,isPilote,events)
                    dialog.show(supportFragmentManager,"")
                }
            }
        }
        weekView.eventLongPressListener = object : WeekView.EventLongPressListener{
            override fun onEventLongPress(event: WeekViewEvent?, eventRect: RectF?) {
            }
        }

        val dialog = ProfileDialog(this)
        dialog.show(supportFragmentManager,"")


        account.setOnClickListener {
            val dialog = ProfileDialog(this)
            dialog.show(supportFragmentManager,"")
        }

        interval.setOnClickListener {
            val dialog = DialogNumberPicker(INTERVAL,this)
            dialog.show(supportFragmentManager,"")
        }
    }
    fun getEventByMonth(month : Int) : MutableList<WeekViewEvent>{
        val data = mutableListOf<WeekViewEvent>()
        for(event in events){
            val m = event.startTime.get(Calendar.MONTH)
            if( m == month)
                data.add(event)
        }

        return data
    }
    fun initEvents(){
        events.clear()
        var  startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY,8)
        startTime.set(Calendar.MINUTE,0)
        startTime.set(Calendar.DAY_OF_MONTH,21)
        startTime.set(Calendar.MONTH,11-1)
        startTime.set(Calendar.YEAR,2019)
        var  endTime = Calendar.getInstance()
        endTime.set(Calendar.HOUR_OF_DAY,8)
        endTime.set(Calendar.MINUTE,20)
        endTime.set(Calendar.DAY_OF_MONTH,21)
        endTime.set(Calendar.MONTH,11-1)
        endTime.set(Calendar.YEAR,2019)

        var event = WeekViewEvent(0,"Réservation 11/06 N1",startTime,endTime)
        event.setColor(resources.getColor(R.color.colorAccent))
        events.add(event)


        //new Event
        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY,8)
        startTime.set(Calendar.MINUTE,30)
        startTime.set(Calendar.DAY_OF_MONTH,21)
        startTime.set(Calendar.MONTH,11-1)
        startTime.set(Calendar.YEAR,2019)
        endTime = Calendar.getInstance()
        endTime.set(Calendar.HOUR_OF_DAY,8)
        endTime.set(Calendar.MINUTE,40)
        endTime.set(Calendar.DAY_OF_MONTH,21)
        endTime.set(Calendar.MONTH,11-1)
        endTime.set(Calendar.YEAR,2019)

        event = WeekViewEvent(0,"Réservation 11/06 N1",startTime,endTime)
        event.setColor(resources.getColor(R.color.colorPrimary))
        events.add(event)

        //new Event
        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY,8)
        startTime.set(Calendar.MINUTE,50)
        startTime.set(Calendar.DAY_OF_MONTH,21)
        startTime.set(Calendar.MONTH,11-1)
        startTime.set(Calendar.YEAR,2019)
        endTime = Calendar.getInstance()
        endTime.set(Calendar.HOUR_OF_DAY,9)
        endTime.set(Calendar.MINUTE,0)
        endTime.set(Calendar.DAY_OF_MONTH,21)
        endTime.set(Calendar.MONTH,11-1)
        endTime.set(Calendar.YEAR,2019)

        event = WeekViewEvent(0,"Réservation 11/06 N1",startTime,endTime)
        event.setColor(resources.getColor(R.color.colorPrimary))
        events.add(event)


        //new Event
        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY,8)
        startTime.set(Calendar.MINUTE,10)
        startTime.set(Calendar.DAY_OF_MONTH,21)
        startTime.set(Calendar.MONTH,10-1)
        startTime.set(Calendar.YEAR,2019)
        endTime = Calendar.getInstance()
        endTime.set(Calendar.HOUR_OF_DAY,8)
        endTime.set(Calendar.MINUTE,20)
        endTime.set(Calendar.DAY_OF_MONTH,21)
        endTime.set(Calendar.MONTH,10-1)
        endTime.set(Calendar.YEAR,2019)

        event = WeekViewEvent(0,"Réservation 10/07 N1",startTime,endTime)
        event.setColor(resources.getColor(R.color.colorPrimary))
        events.add(event)

        //new Event
        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY,8)
        startTime.set(Calendar.MINUTE,35)
        startTime.set(Calendar.DAY_OF_MONTH,21)
        startTime.set(Calendar.MONTH,10-1)
        startTime.set(Calendar.YEAR,2019)
        endTime = Calendar.getInstance()
        endTime.set(Calendar.HOUR_OF_DAY,8)
        endTime.set(Calendar.MINUTE,45)
        endTime.set(Calendar.DAY_OF_MONTH,21)
        endTime.set(Calendar.MONTH,10-1)
        endTime.set(Calendar.YEAR,2019)

        event = WeekViewEvent(0,"Réservation Octobre 10/07 N1",startTime,endTime)
        event.setColor(resources.getColor(R.color.colorPrimaryDark))
        events.add(event)
    }
    fun DeleteReservationByTitle(title : String){
        for(i in 0..events.size-1){
            if(events.get(i).name.equals(title)) {
                events.removeAt(i)
                break
            }
        }
        weekView.notifyDatasetChanged()
    }
    fun ModifyEventByTitle(oldTitle: String,newTitle: String){
        for(event in events){
            if(event.name.equals(oldTitle)) {
                event.name = newTitle
            }
        }

        weekView.notifyDatasetChanged()
    }
}
