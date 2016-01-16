package com.doglandia.medialoader.model.mediaItem;

import com.doglandia.medialoader.localStore.MediaRecord;

import java.io.File;

/**
 * Created by Thomas on 1/10/2016.
 */
public class FileMediaItem implements MediaItem {

    private MediaRecord mediaRecord;

    public FileMediaItem(MediaRecord mediaRecord) {
        this.mediaRecord = mediaRecord;
    }

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
}
