package com.doglandia.medialoader.content;

import android.os.AsyncTask;
import android.util.Log;

//import com.turn.ttorrent.client.Client;
//import com.turn.ttorrent.client.SharedTorrent;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by Thomas on 1/3/2016.
 */
public class TorrentDownloader {

    private static final String TAG = "TorrentDownloader";

    private File torrentFile;
    private File outputFile;

    public TorrentDownloader(File torrentFile, File outputFile){
        this.torrentFile = torrentFile;
        this.outputFile = outputFile;
    }

    public  void startDownloading(){
        TorrentTask torrentTask = new TorrentTask();
        torrentTask.execute(torrentFile, outputFile);
    }

    private class TorrentTask extends AsyncTask<File, Integer, File>{

        @Override
        protected File doInBackground(File... params) {

//            Client client = null;
//            File torrentFile = params[0];
//            File outputFile = params[1];
//            try {
//                client = new Client(InetAddress.getLocalHost(), SharedTorrent.fromFile(torrentFile, outputFile));
//                Log.d(TAG, "created client");
//
//                client.setMaxDownloadRate(1000.0);
//                client.setMaxUploadRate(1000.0);
//                Log.d(TAG, "set up and down rates");
//
//                client.download();
//                Log.d(TAG, "started client downloading");
//
//                client.waitForCompletion();
//                Log.d(TAG, "torrent completed downloading");
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }



            return null;
        }
    }


}
