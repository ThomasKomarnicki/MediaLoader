package com.doglandia.medialoader.playmedia;

import android.app.Activity;
import android.os.Bundle;
import android.widget.VideoView;

import com.doglandia.medialoader.MediaLoaderApplication;
import com.doglandia.medialoader.R;
import com.doglandia.medialoader.model.Resource;
import com.doglandia.medialoader.resourceserver.ResourceServer;

/**
 * Created by tdk10 on 2/21/2016.
 */
public class PlayMediaActivity extends Activity {

    private VideoView mVideoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playback_controls);

        Resource resource = getIntent().getParcelableExtra("resource");

        ResourceServer resourceServer = ((MediaLoaderApplication) getApplication()).getResourceServer();
        String videoPath = resourceServer.getMediaUrl(resource);

        mVideoView = (VideoView) findViewById(R.id.videoView);
        mVideoView.setFocusable(false);
        mVideoView.setFocusableInTouchMode(false);



        mVideoView.setVideoPath(videoPath);
        mVideoView.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mVideoView.suspend();
    }
}
