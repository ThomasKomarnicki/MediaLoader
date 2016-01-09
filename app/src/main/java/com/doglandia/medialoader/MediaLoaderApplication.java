package com.doglandia.medialoader;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.frostwire.bittorrent.BTContext;
import com.frostwire.bittorrent.BTEngine;
import com.frostwire.jlibtorrent.DHT;

import java.io.File;

/**
 * Created by Thomas on 1/8/2016.
 */
public class MediaLoaderApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        setupBTEngine();
    }

    private void setupBTEngine() {
        BTEngine.ctx = new BTContext();
        BTEngine.getInstance().reloadBTContext(getTorrentsPath(),
                getMediaPath(),
                getLibTorrent(),
                0,0,"0.0.0.0",false,false);
        BTEngine.ctx.optimizeMemory = true;
        BTEngine.getInstance().start();


//        boolean enable_dht = ConfigurationManager.instance().getBoolean(Constants.PREF_KEY_NETWORK_ENABLE_DHT);
        DHT dht = new DHT(BTEngine.getInstance().getSession());
//        if (!enable_dht) {
            dht.stop();
//        } else {
            // just make sure it's started otherwise.
            // (we could be coming back from a crash on an unstable state)
//            dht.start();
//        }
    }

    private File getTorrentsPath(){
        File torrentFile = new File(Environment.getExternalStorageDirectory().getPath()+File.separator+"torrents");
        torrentFile.mkdirs();
        return torrentFile;
    }

    private File getMediaPath(){
        File mediaFile = new File(Environment.getExternalStorageDirectory().getPath()+File.separator+"media");
        mediaFile.mkdirs();
        return mediaFile;
    }

    public File getLibTorrent() {
        return new File(getExternalFilesDir(null), "libtorrent");
    }


}
