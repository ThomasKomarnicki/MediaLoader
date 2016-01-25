package com.doglandia.medialoader.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaMetadata;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.KeyEvent;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.doglandia.medialoader.R;
import com.doglandia.medialoader.fragment.PlaybackControlsFragment;
import com.doglandia.medialoader.model.mediaItem.MediaItem;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Thomas on 1/23/2016.
 */
public class PlaybackActivity extends Activity /*implements PlaybackFragment.OnPlayPauseClickedListener*/ {
    private static final String TAG = "PlaybackActivity";

    private VideoView mVideoView;
    private LeanbackPlaybackState mPlaybackState = LeanbackPlaybackState.IDLE;
    private MediaSessionCompat mSession;

    private MediaItem mediaItem;

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

    private PlaybackControlsFragment playbackControlsFragment;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playback_controls);
        mediaItem = getIntent().getParcelableExtra("media_item");
        loadViews();
        setupCallbacks();
        mSession = new MediaSessionCompat(this, "MediaLoader");
        mSession.setCallback(new MediaSessionCallback());
        mSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mSession.setActive(true);

        mVideoView.setVideoPath(mediaItem.getFileLocation());
        mVideoView.start();

        playbackControlsFragment = (PlaybackControlsFragment) getFragmentManager().findFragmentById(R.id.playback_fragment);

        timer = new Timer();
        timer.scheduleAtFixedRate(playBackTimerTask, 1000,1000);

    }

    public MediaControllerCompat getMediaControllerCompat(){
        return mSession.getController();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mVideoView.suspend();
    }

//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//
//        PlaybackControlsFragment fragment = (PlaybackControlsFragment) getFragmentManager().findFragmentById(R.id.playback_fragment);
//        fragment.onKeyUp(keyCode, event);
//        return super.onKeyUp(keyCode, event);
//    }

    //    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        PlaybackOverlayFragment playbackOverlayFragment = (PlaybackOverlayFragment) getFragmentManager().findFragmentById(R.id.playback_controls_fragment);
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_MEDIA_PLAY:
//                playbackOverlayFragment.togglePlayback(false);
//                return true;
//            case KeyEvent.KEYCODE_MEDIA_PAUSE:
//                playbackOverlayFragment.togglePlayback(false);
//                return true;
//            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
//                if (mPlaybackState == LeanbackPlaybackState.PLAYING) {
//                    playbackOverlayFragment.togglePlayback(false);
//                } else {
//                    playbackOverlayFragment.togglePlayback(true);
//                }
//                return true;
//            default:
//                return super.onKeyUp(keyCode, event);
//        }
//    }

    /**
     * Implementation of OnPlayPauseClickedListener
     */
    public void onFragmentPlayPause(MediaItem mediaItem, int position, Boolean playPause) {
        mVideoView.setVideoPath(mediaItem.getFileLocation());

        if (position == 0 || mPlaybackState == LeanbackPlaybackState.IDLE) {
            setupCallbacks();
            mPlaybackState = LeanbackPlaybackState.IDLE;
        }

        if (playPause && mPlaybackState != LeanbackPlaybackState.PLAYING) {
            mPlaybackState = LeanbackPlaybackState.PLAYING;
            if (position > 0) {
                mVideoView.seekTo(position);
                mVideoView.start();
            }
        } else {
            mPlaybackState = LeanbackPlaybackState.PAUSED;
            mVideoView.pause();
        }
        updatePlaybackState(position);
//        updateMetadata(movie);
    }

    private void updatePlaybackState(int position) {
        PlaybackStateCompat.Builder stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(getAvailableActions());
        int state = PlaybackStateCompat.STATE_PLAYING;
        if (mPlaybackState == LeanbackPlaybackState.PAUSED) {
            state = PlaybackStateCompat.STATE_PAUSED;
        }
        stateBuilder.setState(state, position, 1.0f);
        mSession.setPlaybackState(stateBuilder.build());
    }

    private long getAvailableActions() {
        long actions = PlaybackStateCompat.ACTION_PLAY |
                PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID |
                PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH;

        if (mPlaybackState == LeanbackPlaybackState.PLAYING) {
            actions |= PlaybackStateCompat.ACTION_PAUSE;
        }

        return actions;
    }

    private void updateMetadata(final MediaItem mediaItem) {
        final MediaMetadataCompat.Builder metadataBuilder = new MediaMetadataCompat.Builder();

        String title = mediaItem.getDisplayName();

        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, title);
//        metadataBuilder.putString(MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE,
//                movie.getDescription());
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI,
                mediaItem.getBackgroundUrl());

        // And at minimum the title and artist for legacy support
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, title);
//        metadataBuilder.putString(MediaMetadata.METADATA_KEY_ARTIST, movie.getStudio());

        Glide.with(this)
                .load(Uri.parse(mediaItem.getBackgroundUrl()))
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(500, 500) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        metadataBuilder.putBitmap(MediaMetadataCompat.METADATA_KEY_ART, bitmap);
                        mSession.setMetadata(metadataBuilder.build());
                    }
                });
    }

    private void loadViews() {
        mVideoView = (VideoView) findViewById(R.id.videoView);
        mVideoView.setFocusable(false);
        mVideoView.setFocusableInTouchMode(false);
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
                mPlaybackState = LeanbackPlaybackState.IDLE;
                return false;
            }
        });

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (mPlaybackState == LeanbackPlaybackState.PLAYING) {
                    mVideoView.start();
                    playbackControlsFragment.setTotalPlayDuration(mVideoView.getDuration());
                }
            }
        });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlaybackState = LeanbackPlaybackState.IDLE;
            }
        });

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


    @Override
    public void onVisibleBehindCanceled() {
        super.onVisibleBehindCanceled();
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
        if(mVideoView.canSeekForward()) {
            mVideoView.seekTo(mVideoView.getCurrentPosition()+(60000 * 2)); // seek forward 2 minutes
            updatePlaybackPosition();
        }
    }

    public void seekBackward(){
        if(mVideoView.canSeekBackward()) {
            mVideoView.seekTo(mVideoView.getCurrentPosition() - (60000 * 2)); // seek forward 2 minutes
            updatePlaybackPosition();
        }

    }

    private void updatePlaybackPosition(){
        playbackControlsFragment.setPlayDuration(mVideoView.getCurrentPosition());
    }

    /*
     * List of various states that we can be in
     */
    public enum LeanbackPlaybackState {
        PLAYING, PAUSED, BUFFERING, IDLE;
    }

    private class MediaSessionCallback extends MediaSessionCompat.Callback {

    }
}