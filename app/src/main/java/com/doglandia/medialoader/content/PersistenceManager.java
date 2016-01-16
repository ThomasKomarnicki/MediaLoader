package com.doglandia.medialoader.content;

import android.util.Log;

import com.doglandia.medialoader.localStore.MediaRecord;
import com.doglandia.medialoader.model.BTDownloadWrapper;
import com.doglandia.medialoader.model.mediaItem.BTMediaItem;
import com.doglandia.medialoader.model.mediaItem.FileMediaItem;
import com.doglandia.medialoader.model.mediaItem.MediaItem;
import com.frostwire.bittorrent.BTDownload;

import java.util.ArrayList;
import java.util.List;

/**
 * saves and loads media records to content manager
 */
public class PersistenceManager {

    private static final String TAG = PersistenceManager.class.getSimpleName();

    public PersistenceManager(){

    }

    public void addTorrent(BTDownloadWrapper btDownloadWrapper){
        MediaRecord mediaRecord = new MediaRecord();

        mediaRecord.setDisplayName(btDownloadWrapper.getDisplayName());
        mediaRecord.setName(btDownloadWrapper.getName());
        mediaRecord.setTorrentLocation(btDownloadWrapper.getTorrentPath());
        mediaRecord.setMediaFileLocation(btDownloadWrapper.getFilePath());
        mediaRecord.setComplete(false);
        mediaRecord.setDownloadStartTime(System.currentTimeMillis());
        mediaRecord.setDownloadFinishTime(-1);

        mediaRecord.save();
    }

    public boolean torrentExists(BTDownloadWrapper btDownloadWrapper){
        List<MediaRecord> recordList = MediaRecord.find(MediaRecord.class, "name = ?", new String[]{btDownloadWrapper.getName()});
        return recordList != null && recordList.size() > 0;

    }


    public List<MediaItem> loadRecords(ContentManager contentManager) {
        List<MediaRecord> records = MediaRecord.find(MediaRecord.class,null,new String[]{},null,"download_start_time DESC",null);
        List<MediaItem> mediaItems = new ArrayList<>();
        for(MediaRecord mediaRecord : records){
            MediaItem mediaItem = null;
            if(mediaRecord.isComplete()){
                mediaItem = new FileMediaItem(mediaRecord);
            }else{
                BTDownload btDownload = contentManager.getTorrentByName(mediaRecord.getName());
                if(btDownload != null){
                    mediaItem = new BTMediaItem(new BTDownloadWrapper(btDownload));
                }
            }

            if(mediaItem != null){
                mediaItems.add(mediaItem);
            }
        }

        return mediaItems;
    }

    public MediaRecord finishTorrent(BTMediaItem btMediaItem) {
        List<MediaRecord> mediaRecords = MediaRecord.find(MediaRecord.class, "name = ?",btMediaItem.getName());

        if(mediaRecords == null || mediaRecords.size() != 1){
//            Log.e(TAG, "error finishing torrent");
            throw new RuntimeException("no media record to finish");
        }
        MediaRecord mediaRecord = mediaRecords.get(0);
        mediaRecord.setComplete(true);
        mediaRecord.setDownloadFinishTime(System.currentTimeMillis());
        mediaRecord.save();
        return mediaRecord;
    }
}
