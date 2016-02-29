package com.doglandia.medialoader.playmedia;

import com.doglandia.medialoader.model.Resource;

/**
 * Created by tdk10 on 2/26/2016.
 */
public interface MediaPlaybackListener {
    void onPlay();
    void onPause();
    void onFastForward();
    void onRewind();
    void onVideoChange(Resource resource);
}
