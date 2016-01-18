package com.doglandia.medialoader.fragment;

import android.os.Bundle;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;
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

    MediaItemPresenter presenter = new MediaItemPresenter(){
        @Override
        public void onMediaItemClick(MediaItem mediaItem) {
            mainVideoFragmentPresenter.onMediaItemClick(mediaItem);
        }
    };

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

        mediaItemAdapter = new MediaItemAdapter(mMediaItemCollection, presenter);

        setAdapter(mediaItemAdapter);

    }

    @Override
    public void showMediaItems(List<MediaItem> mediaItemList) {
        if(mediaItemAdapter == null){
            Log.d(TAG, "creating mediaItem Adapter");
            mMediaItemCollection = new MediaItemCollection(mediaItemList);
            loadRows();
        }else{
            Log.d(TAG, "updating all media items");
            mediaItemAdapter.update();

        }
    }

    @Override
    public void updateMediaItem(MediaItem mediaItem) {
        // todo update

        mediaItemAdapter.update(mediaItem);
        Log.d(TAG, mediaItem.getDisplayName() + "  update, progress = " + mediaItem.getProgress());
    }

    @Override
    public void playVideo(MediaItem mediaItem) {

    }

    @Override
    public void refreshMediaItems() {

    }
}
