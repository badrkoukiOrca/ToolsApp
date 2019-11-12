package com.i.toolsapp.Agenda

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.alamkanak.weekview.WeekViewEvent
import com.i.toolsapp.Agenda.Lib1.LibActivity
import com.i.toolsapp.Agenda.Lib2.LibActivity2
import com.i.toolsapp.R
import com.jakewharton.threetenabp.AndroidThreeTen

import kotlinx.android.synthetic.main.activity_agenda.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agenda)
        AndroidThreeTen.init(this)
        library.setOnClickListener {
            if(checkPermissions())
                startActivity(Intent(this@MainActivity, LibActivity::class.java))
        }
        library2.setOnClickListener {
            if(checkPermissions())
                startActivity(Intent(this@MainActivity, LibActivity2::class.java))
        }
    }


    fun checkPermissions() : Boolean{
        if((ContextCompat.checkSelfPermission(this@MainActivity,Manifest.permission.READ_CALENDAR) != PERMISSION_GRANTED)
            || (ActivityCompat.checkSelfPermission(this@MainActivity,Manifest.permission.WRITE_CALENDAR) != PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this@MainActivity,arrayOf(Manifest.permission.READ_CALENDAR,Manifest.permission.WRITE_CALENDAR),0)
            return false
        } else
            return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    }


    fun addEventWithIntent(){
        var  startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY,8)
        startTime.set(Calendar.MINUTE,0)
        startTime.set(Calendar.DAY_OF_MONTH,6)
        startTime.set(Calendar.MONTH,11)
        startTime.set(Calendar.YEAR,2019)
        var  endTime = Calendar.getInstance()
        endTime.set(Calendar.HOUR_OF_DAY,8)
        endTime.set(Calendar.MINUTE,20)
        endTime.set(Calendar.DAY_OF_MONTH,6)
        endTime.set(Calendar.MONTH,11)
        endTime.set(Calendar.YEAR,2019)
        val intent = Intent(Intent.ACTION_INSERT)
        intent.setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime.getTimeInMillis())
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
            .putExtra(CalendarContract.Events.TITLE, "Yoga")
            .putExtra(CalendarContract.Events.DESCRIPTION, "Group class")
            .putExtra(CalendarContract.Events.EVENT_LOCATION, "The gym")
            .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
            .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
        startActivity(intent)
    }

    class AddEvent(var context: Context) : AsyncTask<WeekViewEvent,Void,Void?>(){
        @SuppressLint("MissingPermission")
        override fun doInBackground(vararg p0: WeekViewEvent?): Void? {
            val calId : Long = 1
            val event = p0[0]
            val values = ContentValues().apply {
                put(CalendarContract.Events.DTSTART, event?.startTime?.timeInMillis)
                put(CalendarContract.Events.DTEND, event?.endTime?.timeInMillis)
                put(CalendarContract.Events.TITLE, event?.name)
                put(CalendarContract.Events.DESCRIPTION, event?.name)
                put(CalendarContract.Events.CALENDAR_ID, calId)
                put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Paris")
            }

            val uri: Uri? = context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
            return null
        }
        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            Toast.makeText(context,"Event created",Toast.LENGTH_SHORT).show()
        }
    }
}
