package com.doglandia.medialoader.activity;

import android.app.Activity;
import android.os.Bundle;

import com.doglandia.medialoader.R;
import com.doglandia.medialoader.content.ContentDownloader;
import com.doglandia.medialoader.fragment.MainVideosFragment;
import com.doglandia.medialoader.presenter.MainVideoFragmentPresenter;

/**
 * Created by Thomas on 1/3/2016.
 */
public class MainVideosActivity extends Activity {

    private MainVideoFragmentPresenter mainVideoFragmentPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_videos_activity);

        MainVideosFragment fragment = (MainVideosFragment) getFragmentManager().findFragmentById(R.id.main_videos_fragment);

        mainVideoFragmentPresenter = new MainVideoFragmentPresenter(this, fragment);
        mainVideoFragmentPresenter.showMediaItems();

        ContentDownloader contentDownloader = new ContentDownloader();
        contentDownloader.initiateDownload("http://torcache.net/torrent/0F155777D1B5165D911854E305BE786588AB2B64.torrent");
        // "http://torcache.net/torrent/0F155777D1B5165D911854E305BE786588AB2B64.torrent" -- archer episode
        // "http://torcache.net/torrent/302BB06718B3979F94B7EC9BE3B4AD4EAF7C061C.torrent" -- in for the kill skream
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainVideoFragmentPresenter.registerEventListeners();
        mainVideoFragmentPresenter.showMediaItems();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mainVideoFragmentPresenter.unregisterEventListeners();
    }
}
