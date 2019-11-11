package com.i.toolsapp.Streaming.ClientSide;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import org.jcodec.api.android.AndroidSequenceEncoder;
import org.jcodec.common.io.FileChannelWrapper;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Rational;

import java.io.File;

public class SaveFileThread implements Runnable {


    String filename ;
    Context context ;

    public SaveFileThread(String filename, Context context){
        this.filename = filename ;
        this.context = context ;

    }


    @Override
    public void run() {
        File file = new File("/storage/emulated/0/Download/", filename+".mp4");

        FileChannelWrapper out = null;
        try {
            out = NIOUtils.writableFileChannel(file.getAbsolutePath());
            AndroidSequenceEncoder encoder = new AndroidSequenceEncoder(out, Rational.R(15, 1));

            final int x = 100 / Video.video_images.size();
            final Activity activity = (Activity) context ;
            for (Bitmap bitmap : Video.video_images) {
                encoder.encodeImage(bitmap);
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        RenderVideo.progressBar.setProgress(RenderVideo.progressBar.getProgress()+x);
                    }
                });
            }
            encoder.finish();
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    RenderVideo.progressBar.setProgress(100);
                    new AlertDialog.Builder(context)
                            .setTitle("Rendering video")
                            .setMessage("Your video is created successfuly")
                            .setPositiveButton("Open", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("/storage/emulated/0/Download/"+filename+".mp4"));
                                    intent.setDataAndType(Uri.parse("/storage/emulated/0/Download/"+filename+".mp4"), "video/mp4");
                                    activity.startActivity(intent);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
