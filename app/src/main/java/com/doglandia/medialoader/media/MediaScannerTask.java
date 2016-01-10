package com.doglandia.medialoader.media;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

import java.io.File;

/**
 * Created by Thomas on 1/9/2016.
 */
public class MediaScannerTask implements MediaScannerConnection.MediaScannerConnectionClient{

    private MediaScannerConnection mSc;
    private File file;
    public MediaScannerTask(Context context, File file){
        this.file = file;
        mSc = new MediaScannerConnection(context, this);
        mSc.connect();
    }

    public MediaScannerTask(Context context, String path){
        this(context, new File(path));
    }

    @Override
    public void onMediaScannerConnected() {
        scanFiles(file);
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        mSc.disconnect();
    }

    // give it directory or file
    private void scanFiles(File dir){
        if(dir == null){
            return;
        }else if(!dir.isDirectory()){
            mSc.scanFile(dir.getPath(),null);
            return;
        }

        File[] files = dir.listFiles();
        for(File file : files){
            if(file.isDirectory()){
                scanFiles(file);
            }else{
                mSc.scanFile(file.getPath(),null);
            }
        }
    }
}
