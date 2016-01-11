package com.doglandia.medialoader.model;

import com.frostwire.bittorrent.BTDownload;

/**
 * wraps BTDownload data so we can extend it
 */
public class BTDownloadWrapper {

    private BTDownload btDownload;
    private long timeCreated;

    public BTDownloadWrapper(BTDownload btDownload){
        updateBTDownload(btDownload);
    }

    public void updateBTDownload(BTDownload btDownload){
        this.btDownload = btDownload;
    }

    void setTimeCreated(long timeCreated){
        this.timeCreated = timeCreated;
    }


    public String getDisplayName() {
        return btDownload.getDisplayName();
    }

    public int getProgress() {
        return btDownload.getProgress();
    }

    public String getName() {
        return btDownload.getName();
    }

    public String getTorrentPath() {
        return btDownload.getTorrentFile().getPath();
    }

    public String getFilePath() {
        return btDownload.getSavePath().getPath();
    }

    public String getBackgroundImageUrl(){
        return null;
    }
}
