package com.i.toolsapp.Streaming.ServerSide;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.i.toolsapp.R;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class ServerActivity extends AppCompatActivity {
    private Camera mCamera;
    public MyCameraView mPreview;
    public TextView serverStatus;
    public static String SERVERIP = "localhost";
    public static final int SERVERPORT = 9191;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        serverStatus = (TextView) findViewById(R.id.textView);
        SERVERIP = getLocalIpAddress();
        mCamera = getCameraInstance();
        mPreview = new MyCameraView(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        Thread cThread = new Thread(new MyServerThread(this,SERVERIP,SERVERPORT,handler));
        cThread.start();
    }


    private String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()&& inetAddress instanceof Inet4Address) { return inetAddress.getHostAddress().toString(); }
                }
            }
        } catch (SocketException ex) {
            Log.e("ServerActivity", ex.toString());
        }
        return null;
    }


    public static Camera getCameraInstance()
    {
        Camera c=null;
        try{
            c=Camera.open();
            c.setDisplayOrientation(90);
        }catch(Exception e){
            e.printStackTrace();
        }
        return c;
    }
}
