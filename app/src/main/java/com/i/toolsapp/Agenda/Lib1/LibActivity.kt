package com.i.toolsapp.Agenda.Lib1

import android.graphics.RectF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alamkanak.weekview.MonthLoader
import com.alamkanak.weekview.WeekView
import com.alamkanak.weekview.WeekViewEvent
import kotlinx.android.synthetic.main.activity_lib.*
import java.util.*
import android.util.TypedValue
import com.i.toolsapp.Agenda.Dialogs.DialogAddEvent
import com.i.toolsapp.Agenda.Dialogs.DialogDetails
import com.i.toolsapp.R




class LibActivity : AppCompatActivity() {

    val events = mutableListOf<WeekViewEvent>()
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
        initEvents()




        weekView.goToHour("8".toDouble())
        weekView.setOnEventClickListener(object : WeekView.EventClickListener{
            override fun onEventClick(event: WeekViewEvent?, eventRect: RectF?) {
                if(event!=null){
                    val dialog = DialogDetails(event)
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
                    val dialog = DialogAddEvent(time)
                    dialog.show(supportFragmentManager,"")
                    //weekView.notifyDatasetChanged()
                }
            }
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
        var  startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY,8)
        startTime.set(Calendar.MINUTE,0)
        startTime.set(Calendar.DAY_OF_MONTH,6)
        startTime.set(Calendar.MONTH,11-1)
        startTime.set(Calendar.YEAR,2019)
        var  endTime = Calendar.getInstance()
        endTime.set(Calendar.HOUR_OF_DAY,8)
        endTime.set(Calendar.MINUTE,20)
        endTime.set(Calendar.DAY_OF_MONTH,6)
        endTime.set(Calendar.MONTH,11-1)
        endTime.set(Calendar.YEAR,2019)

        var event = WeekViewEvent(0,"Réservation 11/06",startTime,endTime)
        event.setColor(resources.getColor(R.color.colorAccent))
        events.add(event)


        //new Event
        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY,8)
        startTime.set(Calendar.MINUTE,30)
        startTime.set(Calendar.DAY_OF_MONTH,6)
        startTime.set(Calendar.MONTH,11-1)
        startTime.set(Calendar.YEAR,2019)
        endTime = Calendar.getInstance()
        endTime.set(Calendar.HOUR_OF_DAY,8)
        endTime.set(Calendar.MINUTE,40)
        endTime.set(Calendar.DAY_OF_MONTH,6)
        endTime.set(Calendar.MONTH,11-1)
        endTime.set(Calendar.YEAR,2019)

        event = WeekViewEvent(0,"Réservation 11/06",startTime,endTime)
        event.setColor(resources.getColor(R.color.colorPrimary))
        events.add(event)

        //new Event
        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY,8)
        startTime.set(Calendar.MINUTE,50)
        startTime.set(Calendar.DAY_OF_MONTH,6)
        startTime.set(Calendar.MONTH,11-1)
        startTime.set(Calendar.YEAR,2019)
        endTime = Calendar.getInstance()
        endTime.set(Calendar.HOUR_OF_DAY,9)
        endTime.set(Calendar.MINUTE,0)
        endTime.set(Calendar.DAY_OF_MONTH,6)
        endTime.set(Calendar.MONTH,11-1)
        endTime.set(Calendar.YEAR,2019)

        event = WeekViewEvent(0,"Réservation 11/06",startTime,endTime)
        event.setColor(resources.getColor(R.color.colorPrimary))
        events.add(event)


        //new Event
        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY,8)
        startTime.set(Calendar.MINUTE,10)
        startTime.set(Calendar.DAY_OF_MONTH,7)
        startTime.set(Calendar.MONTH,10-1)
        startTime.set(Calendar.YEAR,2019)
        endTime = Calendar.getInstance()
        endTime.set(Calendar.HOUR_OF_DAY,8)
        endTime.set(Calendar.MINUTE,20)
        endTime.set(Calendar.DAY_OF_MONTH,7)
        endTime.set(Calendar.MONTH,10-1)
        endTime.set(Calendar.YEAR,2019)

        event = WeekViewEvent(0,"Réservation 10/07",startTime,endTime)
        event.setColor(resources.getColor(R.color.colorPrimary))
        events.add(event)

        //new Event
        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY,8)
        startTime.set(Calendar.MINUTE,35)
        startTime.set(Calendar.DAY_OF_MONTH,7)
        startTime.set(Calendar.MONTH,10-1)
        startTime.set(Calendar.YEAR,2019)
        endTime = Calendar.getInstance()
        endTime.set(Calendar.HOUR_OF_DAY,8)
        endTime.set(Calendar.MINUTE,45)
        endTime.set(Calendar.DAY_OF_MONTH,7)
        endTime.set(Calendar.MONTH,10-1)
        endTime.set(Calendar.YEAR,2019)

        event = WeekViewEvent(0,"Réservation Octobre 10/07",startTime,endTime)
        event.setColor(resources.getColor(R.color.colorPrimaryDark))
        events.add(event)
    }
}
