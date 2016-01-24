package com.doglandia.medialoader.model.mediaItem;

import android.os.Parcel;
import android.os.Parcelable;

import com.doglandia.medialoader.localStore.MediaRecord;


/**
 * Created by Thomas on 1/10/2016.
 */
public class FileMediaItem implements MediaItem, Parcelable {

    private MediaRecord mediaRecord;

    public FileMediaItem(MediaRecord mediaRecord) {
        this.mediaRecord = mediaRecord;
    }

    protected FileMediaItem(Parcel in) {
        mediaRecord = in.readParcelable(MediaRecord.class.getClassLoader());
    }

    public static final Creator<FileMediaItem> CREATOR = new Creator<FileMediaItem>() {
        @Override
        public FileMediaItem createFromParcel(Parcel in) {
            return new FileMediaItem(in);
        }

        @Override
        public FileMediaItem[] newArray(int size) {
            return new FileMediaItem[size];
        }
    };

    @Override
    public String getDisplayName() {
        return mediaRecord.getDisplayName();
    }

    @Override
    public String getName() {
        return mediaRecord.getName();
    }

    @Override
    public int getProgress() {
        return 100;
    }

    @Override
    public String getBackgroundUrl() {
        return mediaRecord.getBackgroundUrl();
    }

    @Override
    public boolean isAvailable() {
        return mediaRecord.isComplete();
    }

    @Override
    public String getFileLocation() {
        return mediaRecord.getMediaFileLocation();
    }

    @Override
    public long getTimeDownloaded() {
        return mediaRecord.getDownloadStartTime();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mediaRecord,0);
    }
}
