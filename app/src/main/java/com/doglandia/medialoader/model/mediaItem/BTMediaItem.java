package com.doglandia.medialoader.model.mediaItem;

import com.frostwire.jlibtorrent.TorrentHandle;

/**
 * Created by Thomas on 1/9/2016.
 */
public class BTMediaItem implements MediaItem {


    public BTMediaItem(TorrentHandle torrentHandle){

    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public int getProgress() {
        return 0;
    }

    @Override
    public String getBackgroundUrl() {
        return null;
    }
}
