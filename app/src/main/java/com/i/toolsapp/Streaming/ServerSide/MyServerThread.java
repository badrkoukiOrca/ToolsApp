package com.i.toolsapp.Streaming.ServerSide;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.i.toolsapp.Streaming.P2PConnection.ConnectioData;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class MyServerThread implements Runnable {
    private int mServerPort;
    private String mServerIP;
    private Context mContext;
    private Handler mHandler;
    private ServerActivity mActivityInstance;
    public MyServerThread(Context context,String serverip,int serverport,Handler handler){
        super();
        mContext=context;
        mServerIP = serverip;
        mServerPort = serverport;
        mHandler = handler;
        mActivityInstance = (ServerActivity)mContext;
    }
    public void run(){
        try {
            ServerSocket ss = new ServerSocket(mServerPort,5, InetAddress.getByName(ConnectioData.ServerIPAdress));
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mActivityInstance.serverStatus.setText("Listening on IP: " + mServerIP);
                }
            });
            while (true){
                Socket s = ss.accept();

                new Thread(new ServerSocketThread(s)).start();
            }
        }catch(Exception e){
            Log.d("ServerThread", "run: erro");
        }
    }

    public class ServerSocketThread implements Runnable{
        Socket s = null;

        OutputStream os = null;
        public ServerSocketThread(Socket s) throws IOException {
            this.s = s;

        }
        @Override
        public void run() {
            if(s !=null){
                String clientIp = s.getInetAddress().toString().replace("/", "");
                int clientPort = s.getPort();
                System.out.println("====client ip====="+clientIp);
                System.out.println("====client port====="+clientPort);
                try {

                    s.setKeepAlive(true);
                    os = s.getOutputStream();
                    while(true){

                        DataOutputStream dos = new DataOutputStream(os);
                        dos.writeInt(4);
                        dos.writeUTF("#@@#");
                        dos.writeInt(mActivityInstance.mPreview.mFrameBuffer.size());
                        dos.writeUTF("-@@-");
                        dos.flush();
                        System.out.println(mActivityInstance.mPreview.mFrameBuffer.size());
                        dos.write(mActivityInstance.mPreview.mFrameBuffer.toByteArray());

                        dos.flush();
                        Thread.sleep(1000/30);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        if (os!= null)
                            os.close();

                    } catch (Exception e2) {
                        e.printStackTrace();
                    }

                }



            }
            else{
                System.out.println("socket is null");

            }
        }

    }
}
