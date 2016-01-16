package com.doglandia.medialoader.content;

import android.util.Log;

import com.frostwire.jlibtorrent.TorrentHandle;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Thomas on 1/16/2016.
 */
public class DownloadUpdater {

    private static final String TAG = "DownloadUpdater";

    private ContentManager contentManager;
    private DownloadCallback downloadCallback;

    private Timer timer;
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            List<TorrentHandle> torrents = contentManager.getAllBtDownloads();
            // update local storage or send updates to
            if(torrents != null && torrents.size() > 0) {
                Log.d(TAG, "updating torrents:");
//                for (TorrentHandle torrentHandle : torrents) {
//                    Log.d(TAG, "torrent: " + torrentHandle.getInfoHash().toString() + ", state = " + torrentHandle.getStatus().getState().toString() + ", progress = " + torrentHandle.getStatus().getProgress());
//                    downloadCallback.onDownloadUpdate(torrentHandle, torrentHandle.getStatus().getState());
//                }
                downloadCallback.onDownloadUpdate();
            }else{
                Log.d(TAG, "no torrents to update");
            }

        }
    };

    public DownloadUpdater(ContentManager contentManager, DownloadCallback downloadCallback){
        this.contentManager = contentManager;
        this.downloadCallback = downloadCallback;
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 2000,5000);
    }
}
