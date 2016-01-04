package com.doglandia.medialoader.fragment;

import android.os.Bundle;
import android.support.v17.leanback.app.BrowseFragment;

/**
 * Created by Thomas on 1/3/2016.
 */
public class MainVideosFragment extends BrowseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHeadersState(HEADERS_HIDDEN);
    }
}
