package com.doglandia.medialoader.model.mediaItem;

/**
 * Created by Thomas on 1/5/2016.
 */
public interface MediaItem {

//    private Uri mediaFileUri;
//    private Uri mediaDirectoryUri;
//
//    private String mediaName;
//    private String backgroundImageUrl;
//
//    private long downloadStartDate;
//    private long downloadFinishedDate;

    String getDisplayName();
    int getProgress();
    String getBackgroundUrl();

}
