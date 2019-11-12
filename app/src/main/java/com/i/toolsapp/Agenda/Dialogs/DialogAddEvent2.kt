package com.i.toolsapp.Agenda.Dialogs

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.alamkanak.weekview.WeekViewEvent
import com.i.toolsapp.Agenda.Lib2.Adapter.RowEvent
import com.i.toolsapp.Agenda.Lib2.OnClicked
import com.i.toolsapp.R



import kotlinx.android.synthetic.main.activity_lib.*
import kotlinx.android.synthetic.main.dialog_add.*
import java.util.*
import kotlin.random.Random

class DialogAddEvent2(var time : Calendar,var onClicked: OnClicked) : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        var hour = p1.toString()
        var min = p2.toString()
        if(p1<=9)
            hour = "0"+hour
        if(p2<=9)
            min = "0" + min
        h_fin.setText(hour+":"+min)
    }

    private var timeDialog : TimePickerDialog? =null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_add,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        valider.setOnClickListener {
            if(checkInputs()){
                val date = (if(time.get(Calendar.DAY_OF_MONTH)<=9) "0"+time.get(Calendar.DAY_OF_MONTH)
                        else time.get(Calendar.DAY_OF_MONTH).toString()) + "/"+
                        (if(time.get(Calendar.MONTH)<=9) "0"+time.get(Calendar.MONTH)
                        else time.get(Calendar.MONTH).toString()) +"/"+time.get(Calendar.YEAR).toString()
                val event = RowEvent(h_debut.text.toString(),h_fin.text.toString(),date,titre.text.toString(),"#ff1247")
                onClicked.onHourClicked(event)
                dismiss()
            }else {
                Toast.makeText(context!!,"Vérfier heure debut/fin",Toast.LENGTH_SHORT).show()
            }
        }

        h_debut.setOnClickListener {
            showDialog()

        }

        h_fin.setOnClickListener {
            timeDialog = TimePickerDialog(context!!,this,time.get(Calendar.HOUR_OF_DAY),time.get(Calendar.MINUTE),true)
            timeDialog?.show()
        }
    }

    override fun onResume() {
        super.onResume()
        val params = dialog!!.window!!.attributes
        params.width = LinearLayout.LayoutParams.MATCH_PARENT
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as android.view.WindowManager.LayoutParams
    }

    fun showDialog(){
        val dialog = Dialog(activity!!)
        dialog.setTitle("Number picker")
        dialog.setContentView(R.layout.dialog_number_picker)
        val btnOk = dialog.findViewById<Button>(R.id.ok)
        val btnAnnuler = dialog.findViewById<Button>(R.id.annuler)
        val nbPicker = dialog.findViewById<NumberPicker>(R.id.number_picker)
        nbPicker.maxValue = 59
        nbPicker.minValue=time.get(Calendar.MINUTE)
        nbPicker.wrapSelectorWheel=false
        btnOk.setOnClickListener {
            val h = if(time.get(Calendar.HOUR_OF_DAY)<=9) "0"+time.get(Calendar.HOUR_OF_DAY).toString()
            else time.get(Calendar.HOUR_OF_DAY).toString()
            val m = if (nbPicker.value<=9) "0"+nbPicker.value.toString() else nbPicker.value.toString()
            h_debut.setText(h + ":" +m)
            dialog.dismiss()
        }
        btnAnnuler.setOnClickListener {
            dialog.dismiss()
        }

        val width = (resources.displayMetrics.widthPixels*0.5).toInt()
        dialog.window?.setLayout(width,LinearLayout.LayoutParams.WRAP_CONTENT)
        dialog.show()
    }


    fun checkInputs() : Boolean{
        return h_debut.text.isNotEmpty() && h_fin.text.isNotEmpty()
    }
}