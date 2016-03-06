package com.doglandia.medialoader.videolib;

import android.app.Activity;
import android.os.Bundle;

import com.doglandia.medialoader.MediaLoaderApplication;
import com.doglandia.medialoader.R;
import com.doglandia.medialoader.event.ResourceServerConnected;
import com.doglandia.medialoader.model.Resource;
import com.doglandia.medialoader.model.ResourceGroup;
import com.doglandia.medialoader.model.ResourcesResponse;
import com.doglandia.medialoader.resourceserver.ResourceServer;
import com.doglandia.medialoader.thumbnail.ThumbnailManager;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by tdk10 on 2/21/2016.
 */
public class VideoLibraryActivity extends Activity {

    private VideoLibraryFragment videoLibraryFragment;

    private LoadingVideosFragment loadingContentFragment;

    private List<ResourceGroup> resourceGroups;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_library);

        loadingContentFragment = new LoadingVideosFragment();
        videoLibraryFragment = new VideoLibraryFragment();

//        getFragmentManager().beginTransaction().add(R.id.video_lib_content,loadingContentFragment).commit();


        MediaLoaderApplication.getBus().register(this);

        ResourceServer server = ((MediaLoaderApplication) getApplication()).getResourceServer();
        if(!server.isConnected()) {
            server.startClientDiscovery();
        }else{
            getResourceData();
        }

    }

    private void getResourceData(){
        ResourceServer server = ((MediaLoaderApplication) getApplication()).getResourceServer();
        server.getResourceGroups(new Callback<ResourcesResponse>() {
            @Override
            public void success(ResourcesResponse resourcesResponse, Response response) {
                resourceGroups = resourcesResponse.getResourceGroups();
//                initViews(resourceGroups);

                getFragmentManager().beginTransaction().replace(R.id.video_lib_content, videoLibraryFragment).commitAllowingStateLoss();
                videoLibraryFragment.initViews(resourceGroups);
                loadThumbnails(resourceGroups);

            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private void loadThumbnails(final List<ResourceGroup> resourceGroups){
        ThumbnailManager thumbnailManager = ((MediaLoaderApplication) getApplication()).getThumbnailManager();
        thumbnailManager.addThumbnails(resourceGroups);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MediaLoaderApplication.getBus().unregister(this);
    }

    @Subscribe
    public void onResourceServerConnected(ResourceServerConnected resourceServerConnected){
        getResourceData();
    }
}
