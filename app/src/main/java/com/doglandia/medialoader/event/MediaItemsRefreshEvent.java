package com.doglandia.medialoader.event;

import com.doglandia.medialoader.model.mediaItem.MediaItem;

import java.util.List;

/**
 * Created by Thomas on 1/16/2016.
 */
public class MediaItemsRefreshEvent {

    List<MediaItem> mediaItemList;

    public MediaItemsRefreshEvent(List<MediaItem> mediaItemList) {
        this.mediaItemList = mediaItemList;
    }

    public List<MediaItem> getMediaItemList() {
        return mediaItemList;
    }
}
