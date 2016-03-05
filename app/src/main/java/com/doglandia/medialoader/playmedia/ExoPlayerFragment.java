package com.doglandia.medialoader.playmedia;

import android.app.Fragment;
import android.content.Context;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.FrameworkSampleSource;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecSelector;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.SampleSource;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultHttpDataSource;

public class ExoPlayerFragment extends Fragment implements MediaPlayerFragment {

    private static final int BUFFER_SEGMENT_SIZE = 64 * 1024;

    final int numRenderers = 2;

    private Surface surface;

    private ExoPlayer exoPlayer;

    private Uri videoUri;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPlayer();
    }

    @Override
    public void play() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void setVideo(Uri uri) {
        this.videoUri = uri;
    }

    @Override
    public void seekForward() {

    }

    @Override
    public void seekBackward() {

    }

    private void initPlayer(){
        Context context = getActivity();
        DataSource dataSource = new DefaultHttpDataSource("android",null);
        Allocator allocator = new DefaultAllocator(BUFFER_SEGMENT_SIZE);
        SampleSource sampleSource = new ExtractorSampleSource(videoUri, dataSource, allocator, BUFFER_SEGMENT_SIZE, null);

        TrackRenderer videoRenderer = new MediaCodecVideoTrackRenderer(context,sampleSource, MediaCodecSelector.DEFAULT, MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT);
        TrackRenderer audioRenderer = new MediaCodecAudioTrackRenderer(sampleSource, MediaCodecSelector.DEFAULT);

        exoPlayer = ExoPlayer.Factory.newInstance(numRenderers);
        exoPlayer.prepare(videoRenderer, audioRenderer);

        // Pass the surface to the video renderer.
        exoPlayer.sendMessage(videoRenderer, MediaCodecVideoTrackRenderer.MSG_SET_SURFACE, surface);

        exoPlayer.setPlayWhenReady(true);
    }
}
