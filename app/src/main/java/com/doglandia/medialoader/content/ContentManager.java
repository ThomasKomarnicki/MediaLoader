package com.doglandia.medialoader.content;

import android.content.Context;

import com.frostwire.bittorrent.BTDownload;
import com.frostwire.bittorrent.BTEngine;
import com.frostwire.bittorrent.BTEngineListener;
import com.frostwire.jlibtorrent.TorrentHandle;
import com.squareup.otto.Bus;

import java.util.List;

/**
 * Created by Thomas on 1/9/2016.
 */
public class ContentManager implements BTEngineListener {

    private static Bus bus;

    public static Bus getBus(){
        if(bus == null){
            bus = new Bus();
        }
        return bus;
    }

    private static ContentManager contentManager;

    public static ContentManager getInstance(){
        if(contentManager == null){
            contentManager = new ContentManager();
        }
        return contentManager;
    }

    private ContentManager(){
        BTEngine.getInstance().setListener(this);
        BTEngine.getInstance().getSession().getTorrents().
    }

    private Object getTorrents(){
        List<TorrentHandle> torrentHandleList = BTEngine.getInstance().getSession().getTorrents();
        for(TorrentHandle torrentHandle : torrentHandleList){
        }
    }

    private Context context;

    public void setContext(Context context){
        this.context = context;
    }


    @Override
    public void started(BTEngine engine) {

    }

    @Override
    public void stopped(BTEngine engine) {

    }

    @Override
    public void downloadAdded(BTEngine engine, BTDownload dl) {

    }

    @Override
    public void downloadUpdate(BTEngine engine, BTDownload dl) {

    }
}
