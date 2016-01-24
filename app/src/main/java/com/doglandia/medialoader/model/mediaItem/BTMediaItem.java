package com.doglandia.medialoader.model.mediaItem;

import com.doglandia.medialoader.model.BTDownloadWrapper;
import com.frostwire.bittorrent.BTDownload;
import com.frostwire.jlibtorrent.TorrentHandle;

/**
 * represents media item currently being downloaded and managed by BTEngine
 */
public class BTMediaItem implements MediaItem {

    String backgroundUrl;
    long dateStarted;
    private BTDownloadWrapper btDownloadWrapper;


    public BTMediaItem(BTDownloadWrapper btDownload, String backgroundUrl){
        this(btDownload);
        this.backgroundUrl = backgroundUrl;

    }
    public BTMediaItem(BTDownloadWrapper btDownload){
        this.btDownloadWrapper = btDownload;
//        update(btDownload);
    }

    public void update(BTDownload btDownload){
        this.btDownloadWrapper.updateBTDownload(btDownload);
    }

    @Override
    public String getDisplayName() {
        return btDownloadWrapper.getDisplayName();
    }

    @Override
    public int getProgress() {
        return btDownloadWrapper.getProgress();
    }

    @Override
    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public String getFileLocation() {
        return null;
    }

    @Override
    public long getTimeDownloaded() {
        return dateStarted;
    }

    @Override
    public String getName() {
        return btDownloadWrapper.getName();
    }

    public boolean containsTorrentHandle(TorrentHandle torrentHandle){
        String btDownloadInfoHash = btDownloadWrapper.getInfoHash();
        String torrentHandleInfoHash = torrentHandle.getInfoHash().toString();
        return btDownloadInfoHash.equals(torrentHandleInfoHash);
//        return btDownloadWrapper.getInfoHash().equals(torrentHandle.getInfoHash().toString());
    }
}
