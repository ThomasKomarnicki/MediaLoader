package com.doglandia.medialoader.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v17.leanback.app.MediaControllerGlue;
import android.support.v17.leanback.app.PlaybackControlGlue;
import android.support.v17.leanback.app.PlaybackOverlayFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.PlaybackControlsRow;
import android.support.v17.leanback.widget.PlaybackControlsRowPresenter;

import com.doglandia.medialoader.activity.PlaybackActivity;
import com.doglandia.medialoader.model.mediaItem.MediaItem;

/**
 * Created by Thomas on 1/23/2016.
 */
public class PlaybackControlsFragment extends android.support.v17.leanback.app.PlaybackOverlayFragment {

    private PlaybackControlGlue playbackGlue;

    private PlaybackActivity playbackActivity;

    private MediaItem mediaItem;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int[] playbackSpeeds = new int[]{PlaybackControlGlue.PLAYBACK_SPEED_NORMAL, PlaybackControlGlue.PLAYBACK_SPEED_FAST_L0};
//        playbackGlue = new PlaybackGlue(getActivity(), this, playbackSpeeds, playbackSpeeds);

        mediaItem = getActivity().getIntent().getParcelableExtra("media_item");

        PlaybackActivity playbackActivity = (PlaybackActivity) getActivity();

        ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter(new PlaybackControlsRowPresenter());
        arrayObjectAdapter.add(new PlaybackControlsRow(mediaItem));
        setAdapter(arrayObjectAdapter);

        MediaControllerGlue mediaControllerGlue = new MediaControllerGlue(getActivity(),this, playbackSpeeds, playbackSpeeds) {
            @Override
            protected void onRowChanged(PlaybackControlsRow row) {

            }
        };
        mediaControllerGlue.attachToMediaController(playbackActivity.getMediaControllerCompat());
    }




    class PlaybackGlue extends PlaybackControlGlue{

        public PlaybackGlue(Context context, PlaybackOverlayFragment fragment, int[] fastForwardSpeeds, int[] rewindSpeeds) {
            super(context, fragment, fastForwardSpeeds, rewindSpeeds);

        }

        @Override
        public boolean hasValidMedia() {
            return true;
        }

        @Override
        public boolean isMediaPlaying() {
            return false;
        }

        @Override
        public CharSequence getMediaTitle() {
            return mediaItem.getDisplayName();
        }

        @Override
        public CharSequence getMediaSubtitle() {
            return null;
        }

        @Override
        public int getMediaDuration() {
            return 0;
        }

        @Override
        public Drawable getMediaArt() {
            return null;
        }

        @Override
        public long getSupportedActions() {
            return 0;
        }

        @Override
        public int getCurrentSpeedId() {
            return 0;
        }

        @Override
        public int getCurrentPosition() {
            return 0;
        }

        @Override
        protected void startPlayback(int speed) {

        }

        @Override
        protected void pausePlayback() {

        }

        @Override
        protected void skipToNext() {

        }

        @Override
        protected void skipToPrevious() {

        }

        @Override
        protected void onRowChanged(PlaybackControlsRow row) {

        }
    }
}
