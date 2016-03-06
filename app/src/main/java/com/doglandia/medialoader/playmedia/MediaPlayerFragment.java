package com.doglandia.medialoader.playmedia;

import android.net.Uri;

public interface MediaPlayerFragment {
    void play();
    void pause();
    void setVideo(Uri uri);
    void seekForward();
    void seekBackward();
    int getCurrentPlayPosition();
    void setDuration(PlayMediaActivity playMediaActivity);
}
