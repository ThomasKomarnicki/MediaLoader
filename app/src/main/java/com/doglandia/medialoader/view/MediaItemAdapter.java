package com.doglandia.medialoader.view;

import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.RowHeaderPresenter;
import android.util.Log;

import com.doglandia.medialoader.model.MediaItemCollection;
import com.doglandia.medialoader.model.mediaItem.MediaItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 1/16/2016.
 */
public class MediaItemAdapter extends ObjectAdapter {

    private static final String TAG = MediaItemAdapter.class.getSimpleName();

    private MediaItemCollection mediaItemCollection;
    private Presenter presenter;

    private List<ListRow> listRows;

    public MediaItemAdapter(MediaItemCollection mediaItemCollection, Presenter presenter){
        super(new ListRowPresenter());
        this.mediaItemCollection = mediaItemCollection;
        this.presenter = presenter;

        listRows = new ArrayList<>();
        setData();
    }

    public void update(){
//        mediaItemCollection.update(mediaItemList);
//        listRows.clear();
//        setData();
//        updateData(mediaItemList);
        Log.d(TAG, "mediaItemCollection = " + mediaItemCollection.toString());
        updateData();
        notifyChanged();

    }

    @Override
    public int size() {
        return  listRows.size();
    }

    @Override
    public Object get(int position) {
        return listRows.get(position);
    }

    private void setData(){
        int i = 0;
        for (List<MediaItem> mediaItems : mediaItemCollection) {
//            if (i != 0) {
//                Collections.shuffle(list);
//            }
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(presenter);
            for (MediaItem mediaItem : mediaItems) {
//                listRowAdapter.add(list.get(j % 5));
                listRowAdapter.add(mediaItem);
            }
            HeaderItem header = new HeaderItem(i, mediaItemCollection.getHeaderAt(i));
            listRows.add(new ListRow(header, listRowAdapter));
            i++;
        }
        Log.d(TAG, "kee");
    }

    // update adapters to represent underlying mediaItemCollection
    private void updateData(){

        for(int i = 0; i < mediaItemCollection.getMediaRows().size(); i++){
            if(listRows.size()-1 <  i){
                ListRow listRow = new ListRow(new HeaderItem(mediaItemCollection.getHeaderAt(i)),new ArrayObjectAdapter(presenter));
                listRows.add(listRow);
            }
            ListRow listRow = listRows.get(i);
            List<MediaItem> mediaItems = mediaItemCollection.getMediaRowAt(i);

            for(int j = 0; j < mediaItems.size(); j++){
                if(listRow.getAdapter().size() > j){
                    MediaItem rowMediaItem = (MediaItem) listRow.getAdapter().get(j);
                    MediaItem mediaItem = mediaItems.get(j);
                    if(!rowMediaItem.getName().equals(mediaItem.getName())){
                        ArrayObjectAdapter arrayObjectAdapter = (ArrayObjectAdapter) listRow.getAdapter();
                        arrayObjectAdapter.replace(j,mediaItem);
                    }
                    // else if they are equal do nothing
                }else{
                    ArrayObjectAdapter arrayObjectAdapter = (ArrayObjectAdapter) listRow.getAdapter();
                    arrayObjectAdapter.add(mediaItems.get(j));
                }
            }
        }
    }

    public void update(MediaItem mediaItem) {
        boolean added = false;
        if(!mediaItemCollection.contains(mediaItem)){
            mediaItemCollection.addItem(mediaItem);
            added = true;
        }
        updateData();

        if(added){
            notifyChanged();
        }
    }
}
