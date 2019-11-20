package com.i.toolsapp.Agenda.Dialogs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.i.toolsapp.Agenda.Lib2.Adapter.RowEvent
import com.i.toolsapp.Agenda.Lib2.Models.Time
import com.i.toolsapp.Agenda.Lib2.OnClicked
import com.i.toolsapp.R
import kotlinx.android.synthetic.main.dialog_add.*
import java.util.*

class AddReservation(val time : Time, var onClicked: OnClicked, var interval : Int, var isPilote : Boolean) : DialogFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_add,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            if((isPilote && checkInputs()) || !isPilote){
                val color = if(isPilote) "#1261A0" else "#FF7F50"
                if(!isPilote)
                    titre.setText("Réservation pour skieur No: ${(1..100).random()}")
                onClicked.onHourClicked(RowEvent(h_debut+":"+m_debut,h_fin+":"+m_fin,sDate,titre.text.toString(),color))
                dismiss()
            }else {
                Toast.makeText(context!!,"Vérfier heure debut/fin", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun checkInputs() : Boolean{
        return titre.text.isNotEmpty()
    }

    override fun onResume() {
        super.onResume()
        val params = dialog!!.window!!.attributes
        params.width = LinearLayout.LayoutParams.MATCH_PARENT
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as android.view.WindowManager.LayoutParams
    }
}