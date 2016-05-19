package com.doglandia.hometheater.playmedia;

import com.doglandia.hometheater.model.Resource;

public interface MediaPlaybackListener {
    void onPlay();
    void onPause();
    void onFastForward();
    void onRewind();
    void onVideoChange(Resource resource);
}
