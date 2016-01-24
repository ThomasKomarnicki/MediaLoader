package com.doglandia.medialoader.activity;

import android.app.Activity;
import android.os.Bundle;

import com.doglandia.medialoader.R;
import com.doglandia.medialoader.content.ContentDownloader;
import com.doglandia.medialoader.event.MediaItemsRefreshEvent;
import com.doglandia.medialoader.event.MediaUpdateEvent;
import com.doglandia.medialoader.fragment.MainVideosFragment;
import com.doglandia.medialoader.model.mediaItem.MediaItem;
import com.doglandia.medialoader.presenter.MainVideoFragmentPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 1/3/2016.
 */
public class MainVideosActivity extends Activity {

    private MainVideoFragmentPresenter mainVideoFragmentPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_videos_activity);

        MainVideosFragment fragment = (MainVideosFragment) getFragmentManager().findFragmentById(R.id.main_videos_fragment);

        mainVideoFragmentPresenter = new MainVideoFragmentPresenter(this, fragment);
        mainVideoFragmentPresenter.showMediaItems();

//        ContentDownloader contentDownloader = new ContentDownloader();
//        contentDownloader.initiateDownload("http://torcache.net/torrent/0F155777D1B5165D911854E305BE786588AB2B64.torrent");


        // "http://torcache.net/torrent/0F155777D1B5165D911854E305BE786588AB2B64.torrent" -- archer episode
        // "http://torcache.net/torrent/302BB06718B3979F94B7EC9BE3B4AD4EAF7C061C.torrent" -- in for the kill skream

        // testing ui and ui updates

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    final List<MediaItem> mediaItemList = new ArrayList<>();
//                    sendRefreshEvent(mediaItemList);
//                    Thread.sleep(5000);
//                    mediaItemList.add(new TestMediaItem(0));
//                    sendRefreshEvent(mediaItemList.get(0));
//                    for(int i = 0; i < 5; i++) {
//                        sendRefreshEvent(mediaItemList);
//                        Thread.sleep(5000);
//                    }
//                    mediaItemList.add(new TestMediaItem(1));
//                    sendRefreshEvent(mediaItemList.get(1));
//                    for(int i = 0; i < 5; i++) {
//                        sendRefreshEvent(mediaItemList);
//                        Thread.sleep(5000);
//                    }
//
//
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainVideoFragmentPresenter.registerEventListeners();
        mainVideoFragmentPresenter.showMediaItems();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mainVideoFragmentPresenter.unregisterEventListeners();
    }


    // BELOW IS STUFF FOR TESTING
//    private static class TestMediaItem implements MediaItem{
//
//        private int index;
//        private long progressStart;
//
//        TestMediaItem(int i){
//            index = i;
//            progressStart = System.currentTimeMillis();
//        }
//
//        @Override
//        public String getDisplayName() {
//            return "Media Item "+index;
//        }
//
//        @Override
//        public String getName() {
//            return "media_item "+index;
//        }
//
//        @Override
//        public int getProgress() {
//            return (int)((System.currentTimeMillis() - progressStart)/1000);
//        }
//
//        @Override
//        public String getBackgroundUrl() {
//            return null;
//        }
//
//        @Override
//        public boolean isAvailable() {
//            return false;
//        }
//
//        @Override
//        public String getFileLocation() {
//            return null;
//        }
//
//        @Override
//        public long getTimeDownloaded() {
//            return 0;
//        }
//
//    }
//
//    private void sendRefreshEvent(final List<MediaItem> mediaItems){
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mainVideoFragmentPresenter.onMediaItemsRefreshEvent(new MediaItemsRefreshEvent(mediaItems));
//            }
//        });
//    }
//    private void sendRefreshEvent(final MediaItem mediaItem){
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mainVideoFragmentPresenter.onMediaUpdate(new MediaUpdateEvent(mediaItem));
//            }
//        });
//    }
}
