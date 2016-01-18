package com.doglandia.medialoader.model;

import android.util.Log;

import com.doglandia.medialoader.model.mediaItem.MediaItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Thomas on 1/5/2016.
 */
public class MediaItemCollection implements Iterable<List<MediaItem>> {

    private static final String TAG = MediaItemCollection.class.getSimpleName();
    List<List<MediaItem>> mediaRows;
    List<String> headers;

    public MediaItemCollection(List<MediaItem> mediaItemList){
        mediaRows = new ArrayList<>();
        headers = new ArrayList<>();

        setData(mediaItemList);
    }

    public List<MediaItem> getMediaRowAt(int index){
        if(index > mediaRows.size()){
            throw new RuntimeException("index can't be less than rows count");
        }else{
            return mediaRows.get(index);
        }
    }

    public List<List<MediaItem>> getMediaRows() {
        return mediaRows;
    }

    @Override
    public Iterator<List<MediaItem>> iterator() {
        return mediaRows.iterator();
    }

    public String getHeaderAt(int index) {
        if(index >= headers.size()) {
            throw new RuntimeException("index can't be greater than headers count");
        }
        return headers.get(index);
    }

    @Override
    public String toString() {
        if(mediaRows == null || mediaRows.size() == 0){
            return "no items";
        }else{
            MediaItem mediaItem = mediaRows.get(0).get(0);
            return "item 1 = "+mediaItem.getName() + ", progress = "+mediaItem.getProgress();
        }
    }

    private void setData(List<MediaItem> mediaItemList){
        List<MediaItem> currentRow = new ArrayList<>();

        for(MediaItem mediaItem : mediaItemList){
            currentRow.add(mediaItem);
            if(currentRow.size() >= 5){
                mediaRows.add(currentRow);
                currentRow = new ArrayList<>();

            }
        }
        if(currentRow.size() != 0) {
            mediaRows.add(currentRow);
        }

        for(int i = 0; i < mediaRows.size(); i++){
            headers.add("Header 1");
        }
        Log.d("tag", "ree");
    }

    public void update(List<MediaItem> mediaItemList) {
        mediaRows.clear();
        headers.clear();

        setData(mediaItemList);
    }

    public boolean contains(MediaItem mediaItem) {
        for(List<MediaItem> mediaItems : mediaRows){
            for(MediaItem mediaItem1 : mediaItems){
                if(mediaItem.getName().equals(mediaItem1.getName())){
                    return true;
                }
            }
        }
        return false;
    }

    public void addItem(MediaItem mediaItem) {
        Log.d(TAG, "adding media item "+mediaItem.getName());
        if(mediaRows.size() == 0){
            mediaRows.add(new ArrayList<MediaItem>());
            headers.add("Header "+mediaRows.size());
        }
        List<MediaItem> row = mediaRows.get(mediaRows.size() - 1);
        if(row.size() >= 5){
            row = new ArrayList<>();
            mediaRows.add(row);
            headers.add("Header "+mediaRows.size());
        }
        row.add(mediaItem);
    }
}
