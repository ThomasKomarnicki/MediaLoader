package com.doglandia.medialoader.content;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.doglandia.medialoader.event.BTMediaUpdateEvent;
import com.doglandia.medialoader.event.MediaItemsRefreshEvent;
import com.doglandia.medialoader.event.MediaUpdateEvent;
import com.doglandia.medialoader.fragment.MainVideosFragment;
import com.doglandia.medialoader.localStore.MediaRecord;
import com.doglandia.medialoader.model.BTDownloadWrapper;
import com.doglandia.medialoader.model.mediaItem.BTMediaItem;
import com.doglandia.medialoader.model.mediaItem.FileMediaItem;
import com.doglandia.medialoader.model.mediaItem.MediaItem;
import com.doglandia.medialoader.view.MainVideosView;
import com.frostwire.bittorrent.BTDownload;
import com.frostwire.bittorrent.BTEngine;
import com.frostwire.bittorrent.BTEngineListener;
import com.frostwire.jlibtorrent.TorrentHandle;
import com.frostwire.jlibtorrent.TorrentStatus;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 1/9/2016.
 */
public class ContentManager implements BTEngineListener, DownloadCallback {

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

    private DownloadUpdater downloadUpdater;

    private ContentManager(){
        mediaList = new ArrayList<>();
        persistenceManager = new PersistenceManager();
        BTEngine.getInstance().setListener(this);

        downloadUpdater = new DownloadUpdater(this, this);

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

    List<TorrentHandle> getAllBtDownloads() {
        return BTEngine.getInstance().getSession().getTorrents();
    }

    @Override
    public void onDownloadUpdate(TorrentHandle torrentHandle, TorrentStatus.State state) {
        for(MediaItem mediaItem : mediaList){
            if(mediaItem instanceof BTMediaItem){
                BTMediaItem btMediaItem = (BTMediaItem) mediaItem;
                if(btMediaItem.containsTorrentHandle(torrentHandle)){
                    if(state.equals(TorrentStatus.State.FINISHED) || state.equals(TorrentStatus.State.SEEDING)){
                        onDownloadFinish(btMediaItem, torrentHandle);

                    }else if(state.equals(TorrentStatus.State.DOWNLOADING)){
                        getBus().post(new BTMediaUpdateEvent(btMediaItem));
                    }
                }
            }
        }
    }

    @Override
    public void onDownloadUpdate() {
        List<TorrentHandle> torrents = getAllBtDownloads();
        for (TorrentHandle torrentHandle : torrents) {
            Log.d(TAG, "torrent: " + torrentHandle.getInfoHash().toString() + ", state = " + torrentHandle.getStatus().getState().toString() + ", progress = " + torrentHandle.getStatus().getProgress());
            onDownloadUpdate(torrentHandle, torrentHandle.getStatus().getState());
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getBus().post(new MediaItemsRefreshEvent(mediaList));
            }
        });

    }

    private void onDownloadFinish(BTMediaItem btMediaItem, TorrentHandle torrentHandle){
        BTEngine.getInstance().getSession().removeTorrent(torrentHandle);
        BTDownload btDownload = new BTDownload(BTEngine.getInstance(),torrentHandle);
        btMediaItem.update(btDownload);

        // update local store
        MediaRecord mediaRecord = persistenceManager.finishTorrent(btMediaItem);
        replaceMediaItem(mediaRecord);

//        getBus().post(new BTMediaUpdateEvent(btMediaItem));

    }

    private void replaceMediaItem(MediaRecord mediaRecord){
        MediaItem mediaItem = null;
        if(!mediaRecord.isComplete()){
            BTDownload btDownload = getTorrentByName(mediaRecord.getName());
            mediaItem = new BTMediaItem(new BTDownloadWrapper(btDownload));
        }else{
            mediaItem = new FileMediaItem(mediaRecord);
        }

        for(int i = 0; i < mediaList.size(); i++){
            MediaItem mediaItem1 = mediaList.get(i);
            if(mediaItem.getName().equals(mediaItem1.getName())){
                mediaList.set(i,mediaItem);
                break;
            }
        }
    }
}
