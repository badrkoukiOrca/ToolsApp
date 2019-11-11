package com.i.toolsapp.Streaming.ClientSide;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.i.toolsapp.R;

public class RenderVideo extends AppCompatActivity {

    EditText editText ;
    Button button ;
    public static ProgressBar progressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_render_video);

        editText = findViewById(R.id.filenametext);
        button = findViewById(R.id.savebtn) ;
        progressBar = findViewById(R.id.progress);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = editText.getText().toString();
                if(str.equals("")||str.equals(" ")){
                    Toast.makeText(getApplicationContext(),"Please provide file name",Toast.LENGTH_SHORT).show();
                }else{


                    SaveFileThread saveFileThread = new SaveFileThread(str,RenderVideo.this);
                    saveFileThread.run();

                }
            }
        });
    }
}
