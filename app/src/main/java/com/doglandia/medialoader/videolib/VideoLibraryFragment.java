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
import android.view.View;

import com.doglandia.medialoader.R;
import com.doglandia.medialoader.event.ResourceServerConnectFailed;
import com.doglandia.medialoader.model.Resource;
import com.doglandia.medialoader.model.ResourceGroup;
import com.doglandia.medialoader.playmedia.PlayMediaActivity;

import java.util.List;

/**
 * Created by tdk10 on 2/21/2016.
 */
public class VideoLibraryFragment extends BrowseFragment {

    public static final int PLAY_MEDIA_REQUEST = 8;

    public static final int MEDIA_COULDNT_CONNECT = 9;

    private ArrayObjectAdapter mRowsAdapter;

    private List<ResourceGroup> resourceGroups;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // over title
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);

        // set fastLane (or headers) background color
        setBrandColor(getResources().getColor(R.color.primary));
        // set search icon color
//        setSearchAffordanceColor(getResources().getColor(R.color.search_opaque));

        setOnItemViewClickedListener(new ItemViewClickedListener());

//        MediaLoaderApplication.getBus().register(this);

//        if(((MediaLoaderApplication) getActivity().getApplication()).getResourceServer().isConnected()){
//            getResourceData();
//        }
    }

    public void initViews(List<ResourceGroup> resourceGroups){
        this.resourceGroups = resourceGroups;

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

        HeaderItem gridHeader = new HeaderItem(i, "More");

        IconPresenter iconPresenter = new IconPresenter();
        ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(iconPresenter);
        gridRowAdapter.add(new ActionIcon(R.drawable.ic_refresh_black_24dp, "Refresh",
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }));
        mRowsAdapter.add(new ListRow(gridHeader, gridRowAdapter));

        setAdapter(mRowsAdapter);
    }

    private ResourceGroup getResourceGroupForResource(Resource resource){
        for(ResourceGroup resourceGroup : resourceGroups){
            for(Resource resource1 : resourceGroup.getResourceList()){
                if(resource.getLocation().equals(resource1.getLocation())){
                    return resourceGroup;
                }
            }
        }

        return null;
    }


    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof Resource) {
                Resource resource = (Resource) item;
                Intent intent = new Intent(getActivity(), PlayMediaActivity.class);
                intent.putExtra("resource", resource);
                intent.putExtra("resource_group",getResourceGroupForResource(resource));

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ((ImageCardView) itemViewHolder.view).getMainImageView(),
                        "dog").toBundle();
                startActivityForResult(intent,PLAY_MEDIA_REQUEST, bundle);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PLAY_MEDIA_REQUEST && resultCode == MEDIA_COULDNT_CONNECT){
            VideoLibraryActivity activity = (VideoLibraryActivity) getActivity();
            activity.onResourceServerReconnectFailed(new ResourceServerConnectFailed());
        }
    }
}
