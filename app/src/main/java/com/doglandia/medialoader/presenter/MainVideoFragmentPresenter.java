package com.doglandia.medialoader.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;

import com.doglandia.medialoader.activity.PlaybackActivity;
import com.doglandia.medialoader.content.ContentManager;
import com.doglandia.medialoader.event.MediaItemsRefreshEvent;
import com.doglandia.medialoader.event.MediaUpdateEvent;
import com.doglandia.medialoader.model.mediaItem.MediaItem;
import com.doglandia.medialoader.view.MainVideosView;
import com.squareup.otto.Subscribe;

/**
 * Created by Thomas on 1/10/2016.
 */
public class MainVideoFragmentPresenter {

    private ContentManager contentManager;
    private MainVideosView view;

    public MainVideoFragmentPresenter(Activity activity, MainVideosView view) {
        contentManager = ContentManager.getInstance();
        contentManager.setContext(activity);

        this.view = view;
    }


    public void showMediaItems() {
        view.showMediaItems(contentManager.getMediaItems());
    }

    public void onMediaItemClick(Activity activity, MediaItem mediaItem){
        Intent intent = new Intent(activity, PlaybackActivity.class);
        intent.putExtra("media_item", (Parcelable) mediaItem);
        activity.startActivity(intent);
    }

    public void registerEventListeners(){
        ContentManager.getBus().register(this);
    }

    public void unregisterEventListeners(){
        ContentManager.getBus().unregister(this);
    }

    @Subscribe
    public void onMediaUpdate(MediaUpdateEvent event){
        view.updateMediaItem(event.getMediaItem());
    }

    @Subscribe
    public void onMediaItemsRefreshEvent(MediaItemsRefreshEvent mediaItemsRefreshEvent){
        view.showMediaItems(mediaItemsRefreshEvent.getMediaItemList());
    }
}
