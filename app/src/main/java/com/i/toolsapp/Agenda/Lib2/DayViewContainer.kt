package com.i.toolsapp.Agenda.Lib2

import android.view.View
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.ui.ViewContainer
import kotlinx.android.synthetic.main.calendar_day_layout.view.*

class DayViewContainer(view: View,onClicked: OnClicked) : ViewContainer(view) {
    val dayNb = view.calendarDay
    val month = view.calendarDayMonth
    val dayName = view.calendarDayName
    val dayContainer = view.dayContainer

    lateinit var day : CalendarDay


    init {
        view.setOnClickListener {
            onClicked.onViewClicked(it,day)
        }
    }
}