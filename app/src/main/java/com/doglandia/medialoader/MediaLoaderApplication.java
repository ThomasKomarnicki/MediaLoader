package com.doglandia.medialoader;

import android.app.Application;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.doglandia.medialoader.content.ContentManager;
import com.doglandia.medialoader.media.MediaScannerTask;
import com.frostwire.bittorrent.BTContext;
import com.frostwire.bittorrent.BTEngine;
import com.frostwire.jlibtorrent.DHT;
import com.orm.SugarApp;

import java.io.File;

/**
 * Created by Thomas on 1/8/2016.
 */
public class MediaLoaderApplication extends SugarApp {

    private static final String TAG = "MediaLoaderApp";

    @Override
    public void onCreate() {
        super.onCreate();

        setupBTEngine();
    }

    private void setupBTEngine() {
        BTEngine.ctx = new BTContext();
        BTEngine.getInstance().reloadBTContext(getTorrentsFile(),
                getMediaFile(),
                getLibTorrent(),
                0,0,"0.0.0.0",false,false);
        BTEngine.ctx.optimizeMemory = true;
//        BTEngine.getInstance().setMaxConnections(4);
        BTEngine.getInstance().setDownloadSpeedLimit(5000);
        BTEngine.getInstance().setUploadSpeedLimit(50);
        BTEngine.getInstance().start();


//        boolean enable_dht = ConfigurationManager.instance().getBoolean(Constants.PREF_KEY_NETWORK_ENABLE_DHT);
        DHT dht = new DHT(BTEngine.getInstance().getSession());
//        if (!enable_dht) {
//            dht.stop();
//        } else {
            // just make sure it's started otherwise.
            // (we could be coming back from a crash on an unstable state)
            dht.start();
//        }

        new MediaScannerTask(getApplicationContext(), Environment.getExternalStorageDirectory().getPath() + File.separator + "MediaLoader");

    }

    public static File getTorrentsFile(){
        File torrentFile = new File(Environment.getExternalStorageDirectory().getPath() +
                File.separator + "MediaLoader"
                +File.separator+"torrents");
        boolean madeFile = torrentFile.mkdirs();
        Log.d(TAG, "made torrent file =" + madeFile + ", " + torrentFile.isDirectory());
        return torrentFile;
    }

    public static File getMediaFile(){
        File mediaFile = new File(Environment.getExternalStorageDirectory().getPath() +
                File.separator + "MediaLoader"
                +File.separator+"media");
        boolean madeMediaFile = mediaFile.mkdirs();
        mediaFile.setReadable(true);
        Log.d(TAG, "made media file =" + madeMediaFile + ", " + mediaFile.isDirectory());
        return mediaFile;
    }

    public static File getLibTorrent() {
        File file = new File(getTorrentsFile().getPath() + File.separator + "TorrentData");
        boolean madeFile = file.mkdirs();
        Log.d(TAG, "made libtorrent file =" + madeFile + ", " + file.isDirectory());
        return file;
    }


}
