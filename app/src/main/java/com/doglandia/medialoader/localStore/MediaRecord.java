package com.doglandia.medialoader.localStore;

import com.orm.SugarRecord;


/**
 * Created by Thomas on 1/10/2016.
 */
public class MediaRecord extends SugarRecord {

    String displayName;
    String name;
    String torrentLocation;
    String mediaFileLocation; // could be path to file or directory containing media files
    String backgroundUrl;
    boolean complete;
    long downloadStartTime;
    long downloadFinishTime;

    public MediaRecord(){

    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTorrentLocation() {
        return torrentLocation;
    }

    public void setTorrentLocation(String torrentLocation) {
        this.torrentLocation = torrentLocation;
    }

    public String getMediaFileLocation() {
        return mediaFileLocation;
    }

    public void setMediaFileLocation(String mediaFileLocation) {
        this.mediaFileLocation = mediaFileLocation;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public long getDownloadStartTime() {
        return downloadStartTime;
    }

    public void setDownloadStartTime(long downloadStartTime) {
        this.downloadStartTime = downloadStartTime;
    }

    public long getDownloadFinishTime() {
        return downloadFinishTime;
    }

    public void setDownloadFinishTime(long downloadFinishTime) {
        this.downloadFinishTime = downloadFinishTime;
    }
}
