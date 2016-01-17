package com.doglandia.medialoader.view;

import com.doglandia.medialoader.model.mediaItem.MediaItem;

import java.util.List;

/**
 * Created by Thomas on 1/10/2016.
 */
public interface MainVideosView {

    void showMediaItems(List<MediaItem> mediaItemList);
    void updateMediaItem(MediaItem mediaItem);
    void playVideo(MediaItem mediaItem);
    void refreshMediaItems();
}
