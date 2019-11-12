package com.i.toolsapp.Agenda.Lib2

import android.view.View
import com.i.toolsapp.Agenda.Lib2.Adapter.RowEvent
import com.kizitonwose.calendarview.model.CalendarDay


interface OnClicked {
    fun onViewClicked(view : View, day : CalendarDay)

    fun onHourClicked(event : RowEvent){}
}