package com.doglandia.medialoader.event;

import com.doglandia.medialoader.model.mediaItem.BTMediaItem;

/**
 * Created by Thomas on 1/10/2016.
 */
public class BTMediaUpdateEvent {

    private BTMediaItem mediaItem;

    public BTMediaUpdateEvent(BTMediaItem mediaItem) {
        this.mediaItem = mediaItem;
    }

    public BTMediaItem getMediaItem() {
        return mediaItem;
    }
}
