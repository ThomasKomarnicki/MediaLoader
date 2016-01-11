package com.doglandia.medialoader.event;

import com.doglandia.medialoader.model.mediaItem.MediaItem;

/**
 * Created by Thomas on 1/10/2016.
 */
public class MediaUpdateEvent {

    private MediaItem mediaItem;

    public MediaUpdateEvent(MediaItem mediaItem) {
        this.mediaItem = mediaItem;
    }

    public MediaItem getMediaItem() {
        return mediaItem;
    }
}
