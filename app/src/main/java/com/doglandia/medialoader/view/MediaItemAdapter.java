package com.doglandia.medialoader.view;

import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.Presenter;
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
        this.mediaItemCollection = mediaItemCollection;
        this.presenter = presenter;

        listRows = new ArrayList<>();
        setData();
    }

    public void update(List<MediaItem> mediaItemList){
        mediaItemCollection.update(mediaItemList);
        listRows.clear();
        setData();
        Log.d(TAG, "mediaItemCollection = "+mediaItemCollection.toString());
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
}
