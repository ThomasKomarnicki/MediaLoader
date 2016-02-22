package com.doglandia.medialoader.videolib;

import android.content.Intent;
import android.os.Bundle;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.app.ActivityOptionsCompat;
import android.test.mock.MockApplication;
import android.util.Log;
import android.widget.Toast;

import com.doglandia.medialoader.MediaLoaderApplication;
import com.doglandia.medialoader.R;
import com.doglandia.medialoader.model.Resource;
import com.doglandia.medialoader.model.ResourceGroup;
import com.doglandia.medialoader.model.ResourcesResponse;
import com.doglandia.medialoader.playmedia.PlayMediaActivity;
import com.doglandia.medialoader.resourceserver.ResourceServer;
import com.doglandia.medialoader.thumbnail.ThumbnailManager;
import com.doglandia.medialoader.zsample.BrowseErrorActivity;
import com.doglandia.medialoader.zsample.CardPresenter;
import com.doglandia.medialoader.zsample.DetailsActivity;
import com.doglandia.medialoader.zsample.Movie;
import com.doglandia.medialoader.zsample.MovieList;

import java.io.File;
import java.util.Collections;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by tdk10 on 2/21/2016.
 */
public class VideoLibraryFragment extends BrowseFragment {

    private ArrayObjectAdapter mRowsAdapter;

    private List<ResourceGroup> resourceGroups;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        setTitle(getString(R.string.browse_title)); // Badge, when set, takes precedent
        // over title
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);

        // set fastLane (or headers) background color
        setBrandColor(getResources().getColor(R.color.fastlane_background));
        // set search icon color
        setSearchAffordanceColor(getResources().getColor(R.color.search_opaque));

        getResourceData();

        setOnItemViewClickedListener(new ItemViewClickedListener());
    }

    private void getResourceData(){
        ResourceServer server = ((MediaLoaderApplication) getActivity().getApplication()).getResourceServer();
        server.getResourceGroups(new Callback<ResourcesResponse>() {
            @Override
            public void success(ResourcesResponse resourcesResponse, Response response) {
                resourceGroups = resourcesResponse.getResourceGroups();
//                initViews(resourceGroups);

                loadThumbnails(resourceGroups);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private void loadThumbnails(List<ResourceGroup> resourceGroups){
        ThumbnailManager thumbnailManager = ((MediaLoaderApplication) getActivity().getApplication()).getThumbnailManager();
        thumbnailManager.setListener(new ThumbnailManager.ThumbnailRetrievedListener() {
            @Override
            public void onThumbnailRetrieved(Resource resource, File file) {

            }

            @Override
            public void onAllThumbnailsRetrieved(ThumbnailManager thumbnailManager) {
                initViews(VideoLibraryFragment.this.resourceGroups);
            }
        });
        thumbnailManager.addThumbnails(resourceGroups);

    }

    private void initViews(List<ResourceGroup> resourceGroups){


        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        VideoPresenter presenter = new VideoPresenter();

        int i;
        for (i = 0; i < resourceGroups.size(); i++) {
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(presenter);
            ResourceGroup resourceGroup = resourceGroups.get(i);
            for (int j = 0; j < resourceGroup.getResourceList().size(); j++) {
                listRowAdapter.add(resourceGroup.getResourceList().get(j));
            }
            HeaderItem header = new HeaderItem(i, resourceGroup.getName());
            mRowsAdapter.add(new ListRow(header, listRowAdapter));
        }

//        HeaderItem gridHeader = new HeaderItem(i, "PREFERENCES");
//
//        GridItemPresenter mGridPresenter = new GridItemPresenter();
//        ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(mGridPresenter);
//        gridRowAdapter.add(getResources().getString(R.string.grid_view));
//        gridRowAdapter.add(getString(R.string.error_fragment));
//        gridRowAdapter.add(getResources().getString(R.string.personal_settings));
//        mRowsAdapter.add(new ListRow(gridHeader, gridRowAdapter));

        setAdapter(mRowsAdapter);
    }


    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof Resource) {
                Resource resource = (Resource) item;
                Intent intent = new Intent(getActivity(), PlayMediaActivity.class);
                intent.putExtra("resource", resource);

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ((ImageCardView) itemViewHolder.view).getMainImageView(),
                        DetailsActivity.SHARED_ELEMENT_NAME).toBundle();
                getActivity().startActivity(intent, bundle);
            }
        }
    }
}
