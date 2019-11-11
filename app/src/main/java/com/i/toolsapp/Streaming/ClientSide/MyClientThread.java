package com.i.toolsapp.Streaming.ClientSide;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.i.toolsapp.Streaming.P2PConnection.ConnectioData;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;


public class MyClientThread implements Runnable {
    private Socket mSocket;
    private Handler mHandler;
    private Boolean mRunFlag = true;
    final private String TAG = "MyClientThread";
    private BitmapFactory.Options bitmap_options = new BitmapFactory.Options();



    public MyClientThread(Socket socket, Handler handler) {
        this.mSocket = socket;
        this.mHandler = handler;
        bitmap_options.inPreferredConfig = Bitmap.Config.RGB_565;

    }

    @Override
    public void run() {
        try {
            InputStream inStream = null;
            try {
                inStream = mSocket.getInputStream();
            } catch (Exception e) {
                e.printStackTrace();
            }
            DataInputStream is = new DataInputStream(inStream);
            while (mRunFlag) {
                try {
                    int token = is.readInt();
                    if (token == 4) {
                        if (is.readUTF().equals("#@@#")) {

                            int imgLength = is.readInt();
                            System.out.println("getLength:" + imgLength);
                            System.out.println("back-token" + is.readUTF());
                            byte[] buffer = new byte[imgLength];
                            int len = 0;
                            while (len < imgLength) {
                                len += is.read(buffer, len, imgLength - len);
                            }
                            Message m = mHandler.obtainMessage();
                            m.obj = BitmapFactory.decodeByteArray(buffer, 0, buffer.length,bitmap_options);
                            if (m.obj != null) {
                                mHandler.sendMessage(m);
                            } else {
                                System.out.println("Decode Failed");
                            }
                        }
                    }else{
                        Log.d(TAG,"Skip Dirty bytes "+Integer.toString(token));
                    }
                } catch (Exception e) {
                    ConnectioData.Connection_state = false;
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}

