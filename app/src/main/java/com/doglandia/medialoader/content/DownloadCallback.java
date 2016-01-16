package com.doglandia.medialoader.content;

import com.frostwire.jlibtorrent.TorrentHandle;
import com.frostwire.jlibtorrent.TorrentStatus;

/**
 * Created by Thomas on 1/16/2016.
 */
public interface DownloadCallback {
    void onDownloadUpdate(TorrentHandle torrentHandle, TorrentStatus.State state);
    void onDownloadUpdate();
}
