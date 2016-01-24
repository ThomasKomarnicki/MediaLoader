package com.doglandia.medialoader.localStore;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;


/**
 * Created by Thomas on 1/10/2016.
 */
public class MediaRecord extends SugarRecord implements Parcelable {

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

    protected MediaRecord(Parcel in) {
        displayName = in.readString();
        name = in.readString();
        torrentLocation = in.readString();
        mediaFileLocation = in.readString();
        backgroundUrl = in.readString();
        complete = in.readByte() != 0;
        downloadStartTime = in.readLong();
        downloadFinishTime = in.readLong();
    }

    public static final Creator<MediaRecord> CREATOR = new Creator<MediaRecord>() {
        @Override
        public MediaRecord createFromParcel(Parcel in) {
            return new MediaRecord(in);
        }

        @Override
        public MediaRecord[] newArray(int size) {
            return new MediaRecord[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(displayName);
        dest.writeString(name);
        dest.writeString(torrentLocation);
        dest.writeString(mediaFileLocation);
        dest.writeString(backgroundUrl);
        dest.writeByte((byte) (complete ? 1 : 0));
        dest.writeLong(downloadStartTime);
        dest.writeLong(downloadFinishTime);
    }
}
