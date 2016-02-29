package com.doglandia.medialoader.playmedia;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.widget.VideoView;

import com.doglandia.medialoader.MediaLoaderApplication;
import com.doglandia.medialoader.R;
import com.doglandia.medialoader.model.Resource;
import com.doglandia.medialoader.resourceserver.ResourceServer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tdk10 on 2/21/2016.
 */
public class PlayMediaActivity extends Activity{

    private VideoView mVideoView;

    private MediaSession mSession;

    private PlaybackControlsFragment playbackControlsFragment;

    private ResourceServer server;

    private Timer timer;
    private TimerTask playBackTimerTask = new TimerTask() {
        @Override
        public void run() {
            if(mVideoView.isPlaying() && playbackControlsFragment != null){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updatePlaybackPosition();
                    }
                });
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playback_controls);

        Resource resource = getIntent().getParcelableExtra("resource");

        server = ((MediaLoaderApplication) getApplication()).getResourceServer();
        String videoPath = server.getMediaUrl(resource);

        mVideoView = (VideoView) findViewById(R.id.videoView);
        mVideoView.setFocusable(false);
        mVideoView.setFocusableInTouchMode(false);

        setupCallbacks();

        mVideoView.setVideoPath(videoPath);
        mVideoView.start();


        mSession = new MediaSession(this, "MediaLoader");
        mSession.setCallback(new MediaSession.Callback() {

        });
        mSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mSession.setActive(true);


        playbackControlsFragment = (PlaybackControlsFragment) getFragmentManager().findFragmentById(R.id.playback_controls_fragment);
        playbackControlsFragment.setMediaPlaybackListener(new MediaPlaybackListener() {
            @Override
            public void onPlay() {
                mVideoView.start();
            }

            @Override
            public void onPause() {
                mVideoView.pause();
            }

            @Override
            public void onFastForward() {
                seekForward();
            }

            @Override
            public void onRewind() {
                seekBackward();
            }

            @Override
            public void onVideoChange(Resource resource) {
                changeVideo(resource);
            }
        });

        timer = new Timer();
        timer.scheduleAtFixedRate(playBackTimerTask, 1000,1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mVideoView.suspend();
    }

    @Override
    public void onResume() {
        super.onResume();
        mSession.setActive(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mVideoView.isPlaying()) {
            if (!requestVisibleBehind(true)) {
                // Try to play behind launcher, but if it fails, stop playback.
                stopPlayback();
            }
        } else {
            requestVisibleBehind(false);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSession.release();
    }

    private void stopPlayback() {
        if (mVideoView != null) {
            mVideoView.stopPlayback();
        }
    }

    public boolean togglePlayback() {
        if(mVideoView.isPlaying()){
            mVideoView.pause();
            return false;
        }else{
            mVideoView.start();
            return true;
        }
//        if(play){
////            mVideoView.resume();
//            mVideoView.start();
//        }else {
//
//        }
    }

    public void seekForward(){

        // todo
//        if(mVideoView.canSeekForward()) {
//            mVideoView.seekTo(mVideoView.getCurrentPosition()+(60000 * 2)); // seek forward 2 minutes
//            updatePlaybackPosition();
//        }
    }

    public void seekBackward(){

        // todo
//        if(mVideoView.canSeekBackward()) {
//            mVideoView.seekTo(mVideoView.getCurrentPosition() - (60000 * 2)); // seek forward 2 minutes
//            updatePlaybackPosition();
//        }

    }

    public void changeVideo(Resource resource){
        mVideoView.stopPlayback();
        String videoPath = server.getMediaUrl(resource);
        mVideoView.setVideoPath(videoPath);
        mVideoView.start();
    }

    private void updatePlaybackPosition(){
        playbackControlsFragment.setPlayDuration(mVideoView.getCurrentPosition());
    }

    private void setupCallbacks() {

        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                String msg = "";
                if (extra == MediaPlayer.MEDIA_ERROR_TIMED_OUT) {
                    msg = getString(R.string.video_error_media_load_timeout);
                } else if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
                    msg = getString(R.string.video_error_server_inaccessible);
                } else {
                    msg = getString(R.string.video_error_unknown_error);
                }
                mVideoView.stopPlayback();
                return false;
            }
        });

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                    mVideoView.start();
                    playbackControlsFragment.setTotalPlayDuration(mVideoView.getDuration());
            }
        });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });

    }
}
