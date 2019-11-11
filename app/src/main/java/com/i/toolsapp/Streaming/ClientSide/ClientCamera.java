package com.i.toolsapp.Streaming.ClientSide;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.media.FaceDetector;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.i.toolsapp.R;
import com.i.toolsapp.Streaming.P2PConnection.ConnectioData;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ClientCamera extends AppCompatActivity implements View.OnClickListener {





    ArrayList<Bitmap> bitmaps ;

    Boolean videorecording_state = false ;
    LinearLayout videorecord ;
    Chronometer chrono ;
    ImageView reddot ;
    ImageView recordImg ;
    private RelativeLayout image;


    public static String SERVERIP = ConnectioData.ServerIPAdress;
    public static final int SERVERPORT = 9191;
    public MyClientThread mClient;
    public Bitmap mLastFrame;

    private int face_count;
    private final Handler handler = new MyHandler(ClientCamera.this);

    private FaceDetector mFaceDetector = new FaceDetector(320,240,10);
    private FaceDetector.Face[] faces = new FaceDetector.Face[10];
    private PointF tmp_point = new PointF();
    private Paint tmp_paint = new Paint();

    private Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (ConnectioData.Connection_state){
                            if(mLastFrame!=null){
                                Bitmap mutableBitmap = mLastFrame.copy(Bitmap.Config.RGB_565, true);
                                face_count = mFaceDetector.findFaces(mLastFrame, faces);
                                Log.d("Face_Detection", "Face Count: " + String.valueOf(face_count));
                                Canvas canvas = new Canvas(mutableBitmap);

                                for (int i = 0; i < face_count; i++) {
                                    FaceDetector.Face face = faces[i];
                                    tmp_paint.setColor(Color.RED);
                                    tmp_paint.setAlpha(100);
                                    face.getMidPoint(tmp_point);
                                    canvas.drawCircle(tmp_point.x, tmp_point.y, face.eyesDistance(),
                                            tmp_paint);
                                }
                                Bitmap bit = rotateImage(mutableBitmap,90) ;
                                BitmapDrawable ob = new BitmapDrawable(getResources(), bit);
                                image.setBackground(ob);
                                if(videorecording_state){
                                    Video.video_images.add(bit);
                                }
                            }

                        }else{
                            handler.removeCallbacks(mStatusChecker);
                            final Dialog dialog =  new Dialog(ClientCamera.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.error_dialog);
                            Button btn = dialog.findViewById(R.id.close_streaming);
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                    ConnectioData.wifiManager.setWifiEnabled(false);
                                    finishAffinity();
                                    System.exit(0);
                                }
                            });
                            dialog.show();
                        }

                    }
                });
            } finally {

                handler.postDelayed(mStatusChecker, 1000/30);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_camera);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        image = findViewById(R.id.relative) ;
        recordImg= findViewById(R.id.record);
        reddot = findViewById(R.id.reddot);
        chrono = findViewById(R.id.chrono);
        videorecord = findViewById(R.id.videorecord) ;

        bitmaps = new ArrayList<>();

        recordImg.setOnClickListener(this);

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... unused) {
                // Background Code
                Socket s;
                try {
                    s = new Socket(SERVERIP, SERVERPORT);
                    mClient = new MyClientThread(s, handler);
                    new Thread(mClient).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

        }.execute();
        mStatusChecker.run();
    }
    public static Bitmap rotateImage(Bitmap source, float angle) {
        if (source != null){
            Bitmap retVal;
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
            source.recycle();
            return retVal;
        }
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.record:
                if(videorecording_state==false){
                    Video.video_images= new ArrayList<>();
                    videorecording_state = true ;
                    recordImg.setBackgroundResource(R.drawable.stop);
                    videorecord.setVisibility(View.VISIBLE);
                    chrono.setBase(SystemClock.elapsedRealtime());
                    chrono.start();
                    Timer timer = new Timer();
                    timer.schedule( new TimerTask()
                    {
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    if(reddot.getVisibility()==View.VISIBLE){
                                        reddot.setVisibility(View.INVISIBLE);
                                    }else{
                                        reddot.setVisibility(View.VISIBLE);
                                    }
                                }
                            });

                        }
                    }, 0, 600);

                }else{
                    startActivity(new Intent(getApplicationContext(), RenderVideo.class));
                    finish();

                }

                break;
        }
    }


}
