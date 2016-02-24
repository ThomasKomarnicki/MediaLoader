package com.doglandia.medialoader;

import android.app.Application;

import com.doglandia.medialoader.resourceserver.ResourceServer;
import com.doglandia.medialoader.thumbnail.ThumbnailManager;
import com.squareup.otto.Bus;

/**
 * Created by tdk10 on 2/21/2016.
 */
public class MediaLoaderApplication extends Application {

    private static final Bus bus = new Bus();

    public static Bus getBus() {
        return bus;
    }

    private ResourceServer resourceServer;

    private ThumbnailManager thumbnailManager;


    public ResourceServer getResourceServer() {
//        if(resourceServer == null){
//            resourceServer = new ResourceServer();
//        }
        return resourceServer;
    }

    public ThumbnailManager getThumbnailManager() {
        return thumbnailManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        resourceServer = new ResourceServer(getApplicationContext());
        thumbnailManager = new ThumbnailManager(resourceServer, getFilesDir());
    }
}
