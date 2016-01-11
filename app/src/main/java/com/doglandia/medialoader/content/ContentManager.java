package com.doglandia.medialoader.content;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.doglandia.medialoader.event.BTMediaUpdateEvent;
import com.doglandia.medialoader.event.MediaUpdateEvent;
import com.doglandia.medialoader.fragment.MainVideosFragment;
import com.doglandia.medialoader.localStore.MediaRecord;
import com.doglandia.medialoader.model.BTDownloadWrapper;
import com.doglandia.medialoader.model.mediaItem.BTMediaItem;
import com.doglandia.medialoader.model.mediaItem.MediaItem;
import com.doglandia.medialoader.view.MainVideosView;
import com.frostwire.bittorrent.BTDownload;
import com.frostwire.bittorrent.BTEngine;
import com.frostwire.bittorrent.BTEngineListener;
import com.frostwire.jlibtorrent.TorrentHandle;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 1/9/2016.
 */
public class ContentManager implements BTEngineListener {

    private static final String TAG = ContentManager.class.getSimpleName();

    private static Bus bus;

    public static Bus getBus(){
        if(bus == null){
            bus = new Bus();
        }
        return bus;
    }

    private static ContentManager contentManager;

    public static ContentManager getInstance(){
        if(contentManager == null){
            contentManager = new ContentManager();
        }
        return contentManager;
    }

    private List<MediaItem> mediaList;

    private Activity activity;
    private PersistenceManager persistenceManager;

    private ContentManager(){
        mediaList = new ArrayList<>();
        persistenceManager = new PersistenceManager();
        BTEngine.getInstance().setListener(this);

        loadMediaItems();
    }

    private void loadMediaItems(){
        mediaList.addAll(persistenceManager.loadRecords(this));

    }

    public BTDownload getTorrentByName(String name){
        List<TorrentHandle> torrents = BTEngine.getInstance().getSession().getTorrents();
        for(TorrentHandle torrentHandle : torrents){
            if(torrentHandle.getName().equals(name)){
                return new BTDownload(BTEngine.getInstance(), torrentHandle);
            }
        }

        return null;
    }

    public void setContext(Activity activity){
        this.activity = activity;
    }

    private void addMediaItem(final MediaItem mediaItem){
        mediaList.add(0,mediaItem);

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getBus().post(new MediaUpdateEvent(mediaItem));
            }
        });
    }

    public List<MediaItem> getMediaItems() {
        return mediaList;
    }

    @Override
    public void started(BTEngine engine) {
        Log.d(TAG,"started");
    }

    @Override
    public void stopped(BTEngine engine) {

    }

    @Override
    public void downloadAdded(BTEngine engine, BTDownload dl) {
        Log.d(TAG,"download added");
        BTDownloadWrapper btDownloadWrapper = new BTDownloadWrapper(dl);
        if(!persistenceManager.torrentExists(btDownloadWrapper)) {
            Log.e(TAG, "torrent: " + dl.getDisplayName() + " added");
            persistenceManager.addTorrent(btDownloadWrapper);
            final BTMediaItem btMediaItem = new BTMediaItem(btDownloadWrapper);
            addMediaItem(btMediaItem);
        }else{
            Log.e(TAG, "torrent: "+ dl.getDisplayName() + " already exists, not added");
        }

    }

    @Override
    public void downloadUpdate(BTEngine engine, final BTDownload dl) {
        // update mediaItemInstance and post update event
        Log.d(TAG, "download update: " + dl.getInfoHash());
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (MediaItem mediaItem : mediaList) {
                    if (mediaItem instanceof BTMediaItem) {
                        BTMediaItem btMediaItem = (BTMediaItem) mediaItem;
                        if (btMediaItem.getName().equals(dl.getName())) {
                            btMediaItem.update(dl);
                            getBus().post(new BTMediaUpdateEvent(btMediaItem));
                        }
                    }
                }
            }
        });

    }

}
