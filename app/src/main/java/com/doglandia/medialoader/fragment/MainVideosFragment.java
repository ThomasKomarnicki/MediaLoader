package com.doglandia.medialoader.fragment;

import android.os.Bundle;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;

import com.doglandia.medialoader.content.ContentDownloader;
import com.doglandia.medialoader.model.mediaItem.MediaItem;
import com.doglandia.medialoader.model.MediaItemCollection;
import com.doglandia.medialoader.sample.CardPresenter;

import java.util.List;

/**
 * Created by Thomas on 1/3/2016.
 */
public class MainVideosFragment extends BrowseFragment {

    private ArrayObjectAdapter mRowsAdapter;

    private MediaItemCollection mMediaItemCollection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHeadersState(HEADERS_HIDDEN);

        ContentDownloader contentDownloader = new ContentDownloader();
        contentDownloader.initiateDownload("http://torcache.net/torrent/302BB06718B3979F94B7EC9BE3B4AD4EAF7C061C.torrent");

//        String torrnetUrl = "http://torcache.net/torrent/302BB06718B3979F94B7EC9BE3B4AD4EAF7C061C.torrent";
    }

    private void loadRows() {
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        CardPresenter cardPresenter = new CardPresenter();

        int i = 0;
        for (List<MediaItem> mediaItems : mMediaItemCollection) {
//            if (i != 0) {
//                Collections.shuffle(list);
//            }
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
            for (MediaItem mediaItem : mediaItems) {
//                listRowAdapter.add(list.get(j % 5));
            }
            HeaderItem header = new HeaderItem(i, mMediaItemCollection.getHeaderAt(i));
            mRowsAdapter.add(new ListRow(header, listRowAdapter));
            i++;
        }

        setAdapter(mRowsAdapter);

    }
}
