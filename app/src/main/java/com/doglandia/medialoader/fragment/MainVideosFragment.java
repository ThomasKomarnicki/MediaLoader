package com.doglandia.medialoader.fragment;

import android.os.Bundle;
import android.support.v17.leanback.app.BrowseFragment;

import com.doglandia.medialoader.content.ContentDownloader;

/**
 * Created by Thomas on 1/3/2016.
 */
public class MainVideosFragment extends BrowseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHeadersState(HEADERS_HIDDEN);

        ContentDownloader contentDownloader = new ContentDownloader("");

//        String torrnetUrl = "http://torcache.net/torrent/302BB06718B3979F94B7EC9BE3B4AD4EAF7C061C.torrent";
    }
}
