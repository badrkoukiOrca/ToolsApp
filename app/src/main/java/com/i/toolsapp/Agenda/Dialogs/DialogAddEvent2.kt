package  com.i.toolsapp.Agenda.Dialogs

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

class DialogAddEvent2(val time : Calendar, var onClicked: OnClicked, var interval : Int) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_add,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("DebugTimeY",time.get(Calendar.YEAR).toString())
        Log.e("DebugTimeM",time.get(Calendar.MONTH).toString())
        Log.e("DebugTimeD",time.get(Calendar.DAY_OF_MONTH).toString())
        Log.e("DebugTimeH",time.get(Calendar.HOUR_OF_DAY).toString())
        Log.e("DebugTimeMin",time.get(Calendar.MINUTE).toString())
        Log.e("DebugTimeMin","-------------------------")
        val h_debut = if(time.get(Calendar.HOUR_OF_DAY)<=9) "0"+time.get(Calendar.HOUR_OF_DAY).toString()
        else time.get(Calendar.HOUR_OF_DAY).toString()
        val m_debut = if(time.get(Calendar.MINUTE)<=9) "0"+time.get(Calendar.MINUTE).toString()
        else time.get(Calendar.MINUTE).toString()
        Log.e("DebugTimeY",time.get(Calendar.YEAR).toString())
        Log.e("DebugTimeM",time.get(Calendar.MONTH).toString())
        Log.e("DebugTimeD",time.get(Calendar.DAY_OF_MONTH).toString())
        Log.e("DebugTimeH",time.get(Calendar.HOUR_OF_DAY).toString())
        Log.e("DebugTimeMin",time.get(Calendar.MINUTE).toString())
        Log.e("DebugTimeMin","-------------------------")
        val timeFin = Calendar.getInstance()
        timeFin.set(Calendar.YEAR,time.get(Calendar.YEAR))
        timeFin.set(Calendar.MONTH,time.get(Calendar.MONTH))
        timeFin.set(Calendar.DAY_OF_MONTH,time.get(Calendar.DAY_OF_MONTH))
        timeFin.set(Calendar.HOUR_OF_DAY,time.get(Calendar.HOUR_OF_DAY))
        timeFin.set(Calendar.MINUTE,time.get(Calendar.MINUTE))


        timeFin.add(Calendar.MINUTE,interval)
        Log.e("DebugTimeY",time.get(Calendar.YEAR).toString())
        Log.e("DebugTimeM",time.get(Calendar.MONTH).toString())
        Log.e("DebugTimeD",time.get(Calendar.DAY_OF_MONTH).toString())
        Log.e("DebugTimeH",time.get(Calendar.HOUR_OF_DAY).toString())
        Log.e("DebugTimeMin",time.get(Calendar.MINUTE).toString())
        Log.e("DebugTimeMin","-------------------------")
        val h_fin = if(timeFin.get(Calendar.HOUR_OF_DAY)<=9) "0"+timeFin.get(Calendar.HOUR_OF_DAY).toString()
        else timeFin.get(Calendar.HOUR_OF_DAY).toString()
        val m_fin = if(timeFin.get(Calendar.MINUTE)<=9) "0"+timeFin.get(Calendar.MINUTE).toString()
        else timeFin.get(Calendar.MINUTE).toString()


        val sDate = (if(time.get(Calendar.DAY_OF_MONTH)<=9) "0"+time.get(Calendar.DAY_OF_MONTH).toString() else
            time.get(Calendar.DAY_OF_MONTH).toString())+"/"+
                (if(time.get(Calendar.MONTH)<=9) "0"+time.get(Calendar.MONTH).toString() else
                    time.get(Calendar.MONTH).toString()) + "/"+
                (if(time.get(Calendar.YEAR)<=9) "0"+time.get(Calendar.YEAR).toString() else
                    time.get(Calendar.YEAR).toString())

        Log.e("DebugTimeY",time.get(Calendar.YEAR).toString())
        Log.e("DebugTimeM",time.get(Calendar.MONTH).toString())
        Log.e("DebugTimeD",time.get(Calendar.DAY_OF_MONTH).toString())
        Log.e("DebugTimeH",time.get(Calendar.HOUR_OF_DAY).toString())
        Log.e("DebugTimeMin",time.get(Calendar.MINUTE).toString())
        Log.e("DebugTimeMin","-------------------------")
        date.text = sDate
        debut.text = "Heure début : "+h_debut+":"+m_debut
        fin.text = "Heure fin : "+h_fin+":"+m_fin
        valider.setOnClickListener {
            if(checkInputs()){
                onClicked.onHourClicked(RowEvent(h_debut+":"+m_debut,h_fin+":"+m_fin,sDate,titre.text.toString(),"#6F6FFF"))
                dismiss()
            }else {
                Toast.makeText(context!!,"Vérfier heure debut/fin",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val params = dialog!!.window!!.attributes
        params.width = LinearLayout.LayoutParams.MATCH_PARENT
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as android.view.WindowManager.LayoutParams
    }




    fun checkInputs() : Boolean{
        return titre.text.isNotEmpty()
    }
}