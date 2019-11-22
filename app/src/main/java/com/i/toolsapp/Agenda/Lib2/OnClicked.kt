package com.i.toolsapp.Agenda.Lib2

import android.view.View
import com.alamkanak.weekview.WeekViewEvent
import com.i.toolsapp.Agenda.Lib2.Adapter.RowEvent
import com.kizitonwose.calendarview.model.CalendarDay


interface OnClicked {
    fun onViewClicked(view : View, day : CalendarDay){}

    fun onHourClicked(event : RowEvent){}
    fun onHourClicked(event : WeekViewEvent){}

    fun onProfileClicked(profile : String){}

    fun onModifieReservation(oldTitle : String , newTitle : String){}


    fun onDeleteReservation(title : String){}

    fun onInterval(interval : Int){}
}