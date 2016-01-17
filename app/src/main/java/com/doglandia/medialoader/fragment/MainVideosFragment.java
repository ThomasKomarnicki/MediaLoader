package com.doglandia.medialoader.fragment;

import android.os.Bundle;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.util.Log;

import com.doglandia.medialoader.content.ContentDownloader;
import com.doglandia.medialoader.content.ContentManager;
import com.doglandia.medialoader.model.mediaItem.MediaItem;
import com.doglandia.medialoader.model.MediaItemCollection;
import com.doglandia.medialoader.presenter.MainVideoFragmentPresenter;
import com.doglandia.medialoader.presenter.MediaItemPresenter;
import com.doglandia.medialoader.view.MainVideosView;
import com.doglandia.medialoader.view.MediaItemAdapter;

import java.util.List;

/**
 * Created by Thomas on 1/3/2016.
 */
public class MainVideosFragment extends BrowseFragment implements MainVideosView{

    private static final String TAG = MainVideosFragment.class.getSimpleName();
//    private ArrayObjectAdapter mRowsAdapter;
    private MediaItemAdapter mediaItemAdapter;

    private MediaItemCollection mMediaItemCollection;

    private MainVideoFragmentPresenter mainVideoFragmentPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHeadersState(HEADERS_HIDDEN);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainVideoFragmentPresenter = new MainVideoFragmentPresenter(getActivity(),this);
    }

    private void loadRows() {
//        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());

        MediaItemPresenter presenter = new MediaItemPresenter(){
            @Override
            public void onMediaItemClick(MediaItem mediaItem) {
                mainVideoFragmentPresenter.onMediaItemClick(mediaItem);
            }
        };
        mediaItemAdapter = new MediaItemAdapter(mMediaItemCollection, presenter);

//        int i = 0;
//        for (List<MediaItem> mediaItems : mMediaItemCollection) {
////            if (i != 0) {
////                Collections.shuffle(list);
////            }
//            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(presenter);
//            for (MediaItem mediaItem : mediaItems) {
////                listRowAdapter.add(list.get(j % 5));
//                listRowAdapter.add(mediaItem);
//            }
//            HeaderItem header = new HeaderItem(i, mMediaItemCollection.getHeaderAt(i));
//            mRowsAdapter.add(new ListRow(header, listRowAdapter));
//            i++;
//        }

        setAdapter(mediaItemAdapter);

    }

    @Override
    public void showMediaItems(List<MediaItem> mediaItemList) {
        if(mediaItemAdapter == null){
            mMediaItemCollection = new MediaItemCollection(mediaItemList);
            loadRows();
        }else{
            mediaItemAdapter.update(mediaItemList);
        }
    }

    @Override
    public void updateMediaItem(MediaItem mediaItem) {
        // todo update
        Log.d(TAG, mediaItem.getDisplayName() +"  update, progress = "+mediaItem.getProgress());
        getView().invalidate();
    }

    @Override
    public void playVideo(MediaItem mediaItem) {

    }

    @Override
    public void refreshMediaItems() {

    }
}
