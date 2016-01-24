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
import com.doglandia.medialoader.media.MediaScannerTask;
import com.doglandia.medialoader.model.BTDownloadWrapper;
import com.doglandia.medialoader.model.mediaItem.BTMediaItem;
import com.doglandia.medialoader.model.mediaItem.FileMediaItem;
import com.doglandia.medialoader.model.mediaItem.MediaItem;
import com.doglandia.medialoader.view.MainVideosView;
import com.frostwire.bittorrent.BTDownload;
import com.frostwire.bittorrent.BTEngine;
import com.frostwire.bittorrent.BTEngineListener;
import com.frostwire.jlibtorrent.TorrentHandle;
import com.frostwire.jlibtorrent.TorrentInfo;
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
    private List<Integer> mediaItemStates;
    private List<Integer> newMediaItemStates;

    private Activity activity;
    private PersistenceManager persistenceManager;

    private DownloadUpdater downloadUpdater;

    private ContentManager(){
        mediaList = new ArrayList<>();
        persistenceManager = new PersistenceManager();
        BTEngine.getInstance().setListener(this);

        downloadUpdater = new DownloadUpdater(this, this);

        loadMediaItems();

        // at this point mediaItems should have all completed mediaItems
    }

    private void loadMediaItems(){
        mediaList.addAll(persistenceManager.loadRecords(this));
        mediaItemStates = new ArrayList<>();
        newMediaItemStates = new ArrayList<>();
        for(MediaItem mediaItem : mediaList){
            mediaItemStates.add(0);
        }

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
//                getBus().post(new MediaUpdateEvent(mediaItem));
            }
        });
    }

    public List<MediaItem> getMediaItems() {
        return mediaList;
    }

    public void downloadTorrent(TorrentInfo torrentInfo, boolean force){
        if(!persistenceManager.torrentExists(torrentInfo.getName()) || force){
            BTEngine.getInstance().download(torrentInfo, null, null);
        }


    }

    @Override
    public void started(BTEngine engine) {
        Log.d(TAG, "started");
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
//                            getBus().post(new BTMediaUpdateEvent(btMediaItem));
                        }
                    }
                }
            }
        });

    }

    List<TorrentHandle> getAllBtDownloads() {
        return BTEngine.getInstance().getSession().getTorrents();
    }

    /**
     * called when a torrent handle is polled for an update
     * could be a different state than previously or could have a progress update or no state change.
     * @param torrentHandle
     * @param state
     */
    @Override
    public void onDownloadUpdate(TorrentHandle torrentHandle, TorrentStatus.State state) {

        for(MediaItem mediaItem : mediaList){
            if(mediaItem instanceof BTMediaItem){
                BTMediaItem btMediaItem = (BTMediaItem) mediaItem;
                if(btMediaItem.containsTorrentHandle(torrentHandle)){
                    if(state.equals(TorrentStatus.State.FINISHED) || state.equals(TorrentStatus.State.SEEDING)){
                        onDownloadFinish(btMediaItem, torrentHandle);
                    }else if(state.equals(TorrentStatus.State.DOWNLOADING)){
//                        getBus().post(new BTMediaUpdateEvent(btMediaItem));
                    }
                }
            }
        }
    }

    @Override
    public void onDownloadUpdate() {
        newMediaItemStates.clear();
        List<TorrentHandle> torrents = getAllBtDownloads();
        int i = 0;
        for (TorrentHandle torrentHandle : torrents) {
            Log.d(TAG, "torrent: " + torrentHandle.getInfoHash().toString() + ", state = " + torrentHandle.getStatus().getState().toString() + ", progress = " + torrentHandle.getStatus().getProgress());
            onDownloadUpdate(torrentHandle, torrentHandle.getStatus().getState());
            if( i >= newMediaItemStates.size()){
                newMediaItemStates.add(torrentHandle.getStatus().getState().ordinal());
            }else{
                newMediaItemStates.set(i, torrentHandle.getStatus().getState().ordinal());
            }
        }

        // if state of a media item has changed, update media items
        if(mediaItemsStateChanged()) {
            updateMediaItemsUi();
        }

    }

    private void updateMediaItemsUi(){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getBus().post(new MediaItemsRefreshEvent(mediaList));
            }
        });
    }

    /**
     * compare newMediaItemStates to mediaItemStates to see if anything has changed, if so, refresh
     * @return
     */
    private boolean mediaItemsStateChanged(){
        boolean result = false;
        if(mediaItemStates.size() != newMediaItemStates.size()){
            result = true;
        }else{
            for(int i = 0; i < mediaItemStates.size(); i++){
                if(mediaItemStates.get(i) != newMediaItemStates.get(i)){
                    result = true;
                    break;
                }
            }
        }

        mediaItemStates.clear();
        mediaItemStates.addAll(newMediaItemStates);

        Log.d(TAG, "mediaItemsStateChanged() returned "+result);

        return result;
    }

    private void onDownloadFinish(BTMediaItem btMediaItem, TorrentHandle torrentHandle){
        Log.i(TAG, "download finished: "+torrentHandle.getName());
        BTEngine.getInstance().getSession().removeTorrent(torrentHandle);
        BTDownload btDownload = new BTDownload(BTEngine.getInstance(),torrentHandle);
        btMediaItem.update(btDownload);

        // update local store
        MediaRecord mediaRecord = persistenceManager.finishTorrent(btMediaItem);
        replaceMediaItem(mediaRecord);

//        getBus().post(new BTMediaUpdateEvent(btMediaItem));

        MediaScannerTask mediaScannerTask = new MediaScannerTask(activity, torrentHandle.getSavePath());

//        updateMediaItemsUi();

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
