package  com.i.toolsapp.Agenda.Dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.alamkanak.weekview.WeekViewEvent
import com.i.toolsapp.Agenda.Lib2.Adapter.RowEvent
import com.i.toolsapp.Agenda.Lib2.OnClicked
import com.i.toolsapp.R
import kotlinx.android.synthetic.main.dialog_details.*

class DialogDetails2(var event: RowEvent, var onClicked: OnClicked) : DialogFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_details,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Rname.text = event.text


        modifier.setOnClickListener {
            val dialog = ModifierDialog(event.text,onClicked)
            dialog.show(activity!!.supportFragmentManager,"")
            dismiss()
        }

        annuler.setOnClickListener {
            dismiss()
        }

        supprimer.setOnClickListener {
            onClicked.onDeleteReservation(event.text)
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        val params = dialog!!.window!!.attributes
        params.width = LinearLayout.LayoutParams.MATCH_PARENT
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as android.view.WindowManager.LayoutParams
    }
}