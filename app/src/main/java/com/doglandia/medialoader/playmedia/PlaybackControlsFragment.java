package com.doglandia.medialoader.playmedia;

import android.app.Activity;
import android.os.Bundle;
import android.support.v17.leanback.app.MediaControllerGlue;
import android.support.v17.leanback.app.PlaybackControlGlue;
import android.support.v17.leanback.app.PlaybackOverlayFragment;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ControlButtonPresenterSelector;
import android.support.v17.leanback.widget.OnActionClickedListener;
import android.support.v17.leanback.widget.PlaybackControlsRow;
import android.support.v17.leanback.widget.PlaybackControlsRowPresenter;
import android.support.v17.leanback.widget.SparseArrayObjectAdapter;
import android.util.Log;

import com.doglandia.medialoader.model.Resource;

/**
 * Created by tdk10 on 2/26/2016.
 */
public class PlaybackControlsFragment extends PlaybackOverlayFragment {

    private static final String TAG = "PlabackControls";
    private PlaybackControlsRow.PlayPauseAction playPauseAction;
    private PlaybackControlsRow.RewindAction rewindAction;
    private PlaybackControlsRow.FastForwardAction fastForwardAction;

    private PlaybackControlsRow controlsRow;

    private MediaPlaybackListener mediaPlaybackListener;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int[] playbackSpeeds = new int[]{PlaybackControlGlue.PLAYBACK_SPEED_NORMAL, PlaybackControlGlue.PLAYBACK_SPEED_FAST_L0};
//        playbackGlue = new PlaybackGlue(getActivity(), this, playbackSpeeds, playbackSpeeds);

        Resource resource = getActivity().getIntent().getParcelableExtra("resource");

        Activity activity = getActivity();


        ControlButtonPresenterSelector presenterSelector = new ControlButtonPresenterSelector();
        final SparseArrayObjectAdapter controlsAdapter = new SparseArrayObjectAdapter(presenterSelector);
        playPauseAction = new PlaybackControlsRow.PlayPauseAction(activity);
        rewindAction = new PlaybackControlsRow.RewindAction(activity);
        fastForwardAction = new PlaybackControlsRow.FastForwardAction(activity);

        controlsAdapter.set(0,rewindAction);
        controlsAdapter.set(1, playPauseAction);
        controlsAdapter.set(2, fastForwardAction);

        controlsRow = new PlaybackControlsRow();
        controlsRow.setPrimaryActionsAdapter(controlsAdapter);

        PlaybackControlsRowPresenter playbackControlsRowPresenter = new PlaybackControlsRowPresenter();
        playbackControlsRowPresenter.setOnActionClickedListener(new OnActionClickedListener() {
            @Override
            public void onActionClicked(Action action) {
                Log.i(TAG, "action "+action.getId() + " clicked: "+action);
                if (action.getId() == playPauseAction.getId()) {
                    boolean playing = playPauseAction.getIndex() == PlaybackControlsRow.PlayPauseAction.PLAY;

//                    boolean playing = playbackActivity.togglePlayback();
                    Log.i(TAG, "playing = "+playing);
                    if(playing) {
                        playPauseAction.setIcon(playPauseAction.getDrawable(PlaybackControlsRow.PlayPauseAction.PLAY));
                        playPauseAction.setIndex(PlaybackControlsRow.PlayPauseAction.PAUSE);
                        mediaPlaybackListener.onPause();
                    }else{
                        playPauseAction.setIcon(playPauseAction.getDrawable(PlaybackControlsRow.PlayPauseAction.PAUSE));
                        playPauseAction.setIndex(PlaybackControlsRow.PlayPauseAction.PLAY);
                        mediaPlaybackListener.onPlay();
                    }
                    controlsAdapter.notifyArrayItemRangeChanged(1,1);


                } else if (action.getId() == fastForwardAction.getId()) {
//                    playbackActivity.seekForward();
                    mediaPlaybackListener.onFastForward();
                } else if (action.getId() == rewindAction.getId()) {
//                    playbackActivity.seekBackward();
                    mediaPlaybackListener.onRewind();
                }

            }
        });

        ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter(playbackControlsRowPresenter);
        arrayObjectAdapter.add(controlsRow);
        setAdapter(arrayObjectAdapter);

        playPauseAction.setIcon(playPauseAction.getDrawable(PlaybackControlsRow.PlayPauseAction.PAUSE));

    }

    public void setMediaPlaybackListener(MediaPlaybackListener mediaPlaybackListener) {
        this.mediaPlaybackListener = mediaPlaybackListener;
    }

    public void setPlayDuration(int millis){
        controlsRow.setCurrentTime(millis);
    }

    public void setTotalPlayDuration(int millis){
        controlsRow.setTotalTime(millis);
        controlsRow.setBufferedProgress(millis);
        controlsRow.setCurrentTime(0);
    }
}
