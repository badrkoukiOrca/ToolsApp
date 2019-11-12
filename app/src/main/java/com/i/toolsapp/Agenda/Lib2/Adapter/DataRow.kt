package com.i.toolsapp.Agenda.Lib2.Adapter

data class DataRow(
    var debut : String,
    var fin : String,
    var events : MutableList<RowEvent>
)