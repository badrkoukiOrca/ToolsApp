package  com.i.toolsapp.Agenda.Lib2.Dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.i.toolsapp.Agenda.Lib2.OnClicked
import com.i.toolsapp.R
import kotlinx.android.synthetic.main.dialog_modifier.*

class ModifyReservation(var name : String, var onClicked: OnClicked) : DialogFragment(){
    var oldTitle : String=""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_modifier,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        oldTitle = name
        txt.setText(name)


        valider.setOnClickListener {
            if(txt.text.toString().isNotEmpty()) {
                onClicked.onModifieReservation(oldTitle, txt.text.toString())
                dismiss()
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
}