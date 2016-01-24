package com.doglandia.medialoader.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.widget.VideoView;

import com.doglandia.medialoader.R;
import com.doglandia.medialoader.model.mediaItem.MediaItem;

/**
 * Created by Thomas on 1/23/2016.
 */
public class PlaybackActivity extends Activity /*implements PlaybackFragment.OnPlayPauseClickedListener*/ {
    private static final String TAG = "PlaybackActivity";

    private VideoView mVideoView;
    private LeanbackPlaybackState mPlaybackState = LeanbackPlaybackState.IDLE;
    private MediaSession mSession;

    private MediaItem mediaItem;
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
        mSession = new MediaSession(this, "LeanbackSampleApp");
        mSession.setCallback(new MediaSessionCallback());
        mSession.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mSession.setActive(true);

        mVideoView.setVideoPath(mediaItem.getFileLocation());
        mVideoView.start();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mVideoView.suspend();
    }

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
        PlaybackState.Builder stateBuilder = new PlaybackState.Builder()
                .setActions(getAvailableActions());
        int state = PlaybackState.STATE_PLAYING;
        if (mPlaybackState == LeanbackPlaybackState.PAUSED) {
            state = PlaybackState.STATE_PAUSED;
        }
        stateBuilder.setState(state, position, 1.0f);
        mSession.setPlaybackState(stateBuilder.build());
    }

    private long getAvailableActions() {
        long actions = PlaybackState.ACTION_PLAY |
                PlaybackState.ACTION_PLAY_FROM_MEDIA_ID |
                PlaybackState.ACTION_PLAY_FROM_SEARCH;

        if (mPlaybackState == LeanbackPlaybackState.PLAYING) {
            actions |= PlaybackState.ACTION_PAUSE;
        }

        return actions;
    }

//    private void updateMetadata(final MediaItem mediaItem) {
//        final MediaMetadata.Builder metadataBuilder = new MediaMetadata.Builder();
//
//        String title = movie.getTitle().replace("_", " -");
//
//        metadataBuilder.putString(MediaMetadata.METADATA_KEY_DISPLAY_TITLE, title);
//        metadataBuilder.putString(MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE,
//                movie.getDescription());
//        metadataBuilder.putString(MediaMetadata.METADATA_KEY_DISPLAY_ICON_URI,
//                movie.getCardImageUrl());
//
//        // And at minimum the title and artist for legacy support
//        metadataBuilder.putString(MediaMetadata.METADATA_KEY_TITLE, title);
//        metadataBuilder.putString(MediaMetadata.METADATA_KEY_ARTIST, movie.getStudio());
//
//        Glide.with(this)
//                .load(Uri.parse(movie.getCardImageUrl()))
//                .asBitmap()
//                .into(new SimpleTarget<Bitmap>(500, 500) {
//                    @Override
//                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
//                        metadataBuilder.putBitmap(MediaMetadata.METADATA_KEY_ART, bitmap);
//                        mSession.setMetadata(metadataBuilder.build());
//                    }
//                });
//    }

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

    /*
     * List of various states that we can be in
     */
    public enum LeanbackPlaybackState {
        PLAYING, PAUSED, BUFFERING, IDLE;
    }

    private class MediaSessionCallback extends MediaSession.Callback {
    }
}