package com.i.toolsapp.Streaming.ServerSide;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.ByteArrayOutputStream;


public class MyCameraView  extends SurfaceView implements SurfaceHolder.Callback,Camera.PreviewCallback{
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private int width;
    private int height;
    public ByteArrayOutputStream mFrameBuffer;
    private Context con;

    public MyCameraView(Context context,Camera camera){
        super(context);
        con=context;
        mCamera=camera;
        mHolder=getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.setPreviewCallback(null);
        mCamera.release();
        mCamera=null;
    }


    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        try{
            mCamera.stopPreview();
        } catch (Exception e){
            e.printStackTrace();
        }
        try{

            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(320,240);
            this.width=parameters.getPreviewSize().width;
            this.height=parameters.getPreviewSize().height;
            parameters.setPreviewFormat(ImageFormat.NV21);
            mCamera.setParameters(parameters);

            mCamera.setPreviewCallback(this);
            mCamera.startPreview();
        }catch(Exception e){
            e.printStackTrace();
        }
    }



    public void onPreviewFrame(byte[] data,Camera camera){
        try{

            YuvImage yuvimage=new YuvImage(data,ImageFormat.NV21,this.width,this.height,null);
            System.out.println("WidthandHeight"+yuvimage.getHeight()+"::"+yuvimage.getWidth());
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            yuvimage.compressToJpeg(new Rect(0,0,this.width,this.height),100,baos);
            mFrameBuffer = baos;
        }catch(Exception e){
            Log.d("parse","errpr");
        }
    }

}

