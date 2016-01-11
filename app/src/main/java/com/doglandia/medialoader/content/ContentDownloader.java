package com.doglandia.medialoader.content;

import android.content.Context;
import android.util.Log;

import com.frostwire.bittorrent.BTEngine;
import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.TorrentInfo;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;
import com.frostwire.util.HttpClientFactory;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Used to download torrent file the initiate the torrent
 *
 */
public class ContentDownloader {
    private static final String TAG = "ContentDownloader";


    Thread startThread;
    Thread resumeThread;

    public ContentDownloader(){

    }

    public void initiateDownload(final String url){
        startThread = new Thread(new FetcherRunnable(url), "Torrent-Fetcher - " + "Start: " + url);
        startThread.setDaemon(true);
        startThread.start();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                startThread.interrupt();

                resumeThread = new Thread(new FetcherRunnable(url), "Torrent-Fetcher - " + "Resume: " + url);
                resumeThread.setDaemon(true);
                resumeThread.start();
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 20000);
    }

    private class FetcherRunnable implements Runnable {

        private String url;

        FetcherRunnable(String url){
            this.url = url;
        }

        @Override
        public void run() {

            byte[] data = HttpClientFactory.getInstance(HttpClientFactory.HttpContext.DOWNLOAD).getBytes(url, 30000);
            try {

                TorrentInfo ti = TorrentInfo.bdecode(data);

                BTEngine.getInstance().download(ti, null, null);

            } catch (Throwable e) {
                Log.e(TAG, "Error downloading torrent", e);
            }

//            BTEngine.getInstance().getSession().addListener(new AlertListener() {
//                String lastAlertString = "";
//
//                @Override
//                public int[] types() {
//                    return null;
//                }
//
//                @Override
//                public void alert(Alert<?> alert) {
//
//                    AlertType type = alert.getType();
//                    if (!alert.toString().equals(lastAlertString)) {
//                        Log.i(TAG, "alert: " + type + ": " + alert.toString());
//                    }
//                    lastAlertString = new String(alert.toString());
//
//
//                }
//            });
        }
    }

}
