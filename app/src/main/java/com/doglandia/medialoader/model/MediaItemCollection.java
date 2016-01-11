package com.doglandia.medialoader.model;

import com.doglandia.medialoader.model.mediaItem.MediaItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Thomas on 1/5/2016.
 */
public class MediaItemCollection implements Iterable<List<MediaItem>> {

    List<List<MediaItem>> mediaRows;
    List<String> headers;

    public MediaItemCollection(List<MediaItem> mediaItemList){
        mediaRows = new ArrayList<>();
        headers = new ArrayList<>();

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
        if(index > headers.size()) {
            throw new RuntimeException("index can't be less than headers count");
        }
        return headers.get(index);
    }
}
