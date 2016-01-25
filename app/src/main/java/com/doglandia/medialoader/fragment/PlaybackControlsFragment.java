package com.doglandia.medialoader.fragment;

import android.os.Bundle;
import android.support.v17.leanback.app.MediaControllerGlue;
import android.support.v17.leanback.app.PlaybackControlGlue;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ControlButtonPresenterSelector;
import android.support.v17.leanback.widget.OnActionClickedListener;
import android.support.v17.leanback.widget.PlaybackControlsRow;
import android.support.v17.leanback.widget.PlaybackControlsRowPresenter;
import android.support.v17.leanback.widget.PresenterSelector;
import android.support.v17.leanback.widget.SparseArrayObjectAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.doglandia.medialoader.activity.PlaybackActivity;
import com.doglandia.medialoader.model.mediaItem.MediaItem;

/**
 * Created by Thomas on 1/23/2016.
 */
public class PlaybackControlsFragment extends android.support.v17.leanback.app.PlaybackOverlayFragment {

    private static final String TAG = "PlaybackControlsFrag";

//    private PlaybackControlGlue playbackGlue;

    private PlaybackActivity playbackActivity;

    private MediaControllerGlue mediaControllerGlue;

    private MediaItem mediaItem;

    private PlaybackControlsRow.PlayPauseAction playPauseAction;
    private PlaybackControlsRow.RewindAction rewindAction;
    private PlaybackControlsRow.FastForwardAction fastForwardAction;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int[] playbackSpeeds = new int[]{PlaybackControlGlue.PLAYBACK_SPEED_NORMAL, PlaybackControlGlue.PLAYBACK_SPEED_FAST_L0};
//        playbackGlue = new PlaybackGlue(getActivity(), this, playbackSpeeds, playbackSpeeds);

        mediaItem = getActivity().getIntent().getParcelableExtra("media_item");

        playbackActivity = (PlaybackActivity) getActivity();


        ControlButtonPresenterSelector presenterSelector = new ControlButtonPresenterSelector();
        final SparseArrayObjectAdapter controlsAdapter = new SparseArrayObjectAdapter(presenterSelector);
        playPauseAction = new PlaybackControlsRow.PlayPauseAction(playbackActivity);
        rewindAction = new PlaybackControlsRow.RewindAction(playbackActivity);
        fastForwardAction = new PlaybackControlsRow.FastForwardAction(playbackActivity);

        controlsAdapter.set(0,rewindAction);
        controlsAdapter.set(1, playPauseAction);
        controlsAdapter.set(2, fastForwardAction);

        PlaybackControlsRow controlsRow = new PlaybackControlsRow();
        controlsRow.setPrimaryActionsAdapter(controlsAdapter);

        PlaybackControlsRowPresenter playbackControlsRowPresenter = new PlaybackControlsRowPresenter();
        playbackControlsRowPresenter.setOnActionClickedListener(new OnActionClickedListener() {
            @Override
            public void onActionClicked(Action action) {
                Log.i(TAG, "action "+action.getId() + " clicked: "+action);
                if (action.getId() == playPauseAction.getId()) {
//                    boolean playing = playPauseAction.getIndex() == PlaybackControlsRow.PlayPauseAction.PLAY;

                    boolean playing = playbackActivity.togglePlayback();
                    Log.i(TAG, "playing = "+playing);
                    if(playing) {
                        playPauseAction.setIcon(playPauseAction.getDrawable(PlaybackControlsRow.PlayPauseAction.PAUSE));
                    }else{
                        playPauseAction.setIcon(playPauseAction.getDrawable(PlaybackControlsRow.PlayPauseAction.PLAY));
                    }
                    controlsAdapter.notifyArrayItemRangeChanged(1,1);


                } else if (action.getId() == fastForwardAction.getId()) {

                } else if (action.getId() == rewindAction.getId()) {

                }

            }
        });

        ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter(playbackControlsRowPresenter);
        arrayObjectAdapter.add(controlsRow);
        setAdapter(arrayObjectAdapter);

        playPauseAction.setIcon(playPauseAction.getDrawable(PlaybackControlsRow.PlayPauseAction.PAUSE));

    }

    public void onKeyUp(int keyCode, KeyEvent event) {
//        mediaControllerGlue.onKey(null, keyCode,event);
    }
}
