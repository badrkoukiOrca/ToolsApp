package com.i.toolsapp.Agenda.Models

import java.util.*

data class Reservation(
    var start : Calendar,
    var end : Calendar,
    var title : String
)