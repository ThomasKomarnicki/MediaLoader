package com.doglandia.medialoader.content;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.DHT;
import com.frostwire.jlibtorrent.Downloader;
import com.frostwire.jlibtorrent.Entry;
import com.frostwire.jlibtorrent.Session;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;
import com.frostwire.jlibtorrent.alerts.DhtStatsAlert;
import com.frostwire.jlibtorrent.swig.add_torrent_params;
import com.frostwire.jlibtorrent.swig.error_code;
import com.frostwire.jlibtorrent.swig.libtorrent;
import com.frostwire.jlibtorrent.swig.torrent_handle;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

/**
 * Used to download torrent file the initiate the torrent
 *
 */
public class ContentDownloader {
    private static final String TAG = "ContentDownloader";

    private String magnetUri;

    public ContentDownloader(String magnetUri){
        this.magnetUri = magnetUri;
        this.magnetUri = "magnet:?xt=urn:btih:302bb06718b3979f94b7ec9be3b4ad4eaf7c061c&dn=La+Roux+-+In+For+The+Kill+%28Skream+Remix%29&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A80&tr=udp%3A%2F%2Fopen.demonii.com%3A1337&tr=udp%3A%2F%2Ftracker.coppersurfer.tk%3A6969&tr=udp%3A%2F%2Fexodus.desync.com%3A6969";

        TorrentDownloadTask torrentDownloadTask = new TorrentDownloadTask();
        torrentDownloadTask.execute(magnetUri);

         }

    private void initiateTorrent(File torrentFile){
        File outputFile = new File(getTorrentOutputUri(torrentFile.getName()).getPath());
        TorrentDownloader torrentDownloader = new TorrentDownloader(torrentFile, outputFile);
        torrentDownloader.startDownloading();
    }


    private class TorrentDownloadTask extends AsyncTask<String, Integer, File>{

        @Override
        protected File doInBackground(String... params) {
            String magnetUri = params[0];


//            OkHttpClient client = new OkHttpClient();
//            Request.Builder builder = new Request.Builder();
//            Request request = builder.get().url(torrentUrl).build();
//            Call call = client.newCall(request);
//            try {
//                Response response = call.execute();
//
//                File downloadedFile = new File(getTorrentFileUri("torrent.torrent").getPath());
//
//                BufferedSink sink = Okio.buffer(Okio.sink(downloadedFile));
//                sink.writeAll(response.body().source());
//                sink.close();
//
//                Log.d(TAG, "downloaded torrent file to: " + downloadedFile.getPath());
//
//                return downloadedFile;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return null;

            return null;

        }

        @Override
        protected void onPostExecute(File downloadedTorrentFile) {
            super.onPostExecute(downloadedTorrentFile);
            Log.d(TAG, "finished downloading .torrent file");

//            initiateTorrent(downloadedTorrentFile);
        }
    }

    private static final Uri getTorrentFileUri(String fileName) {
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File (sdCard.getAbsolutePath() + "/MediaLoader/torrent");
        dir.mkdirs();
        File file = new File(dir, fileName);
        Log.d(TAG, "file created for torrent at: "+file.getPath());
        return Uri.parse(file.getPath());
//        FileOutputStream f = new FileOutputStream(file);
    }

    private static final Uri getTorrentOutputUri(String fileName){
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File (sdCard.getAbsolutePath() + "/MediaLoader/media"); // media directory will be crawled for content
        dir.mkdirs();
        File file = new File(dir, fileName);
        Log.d(TAG, "file created for output torrent at: "+file.getPath());
        return Uri.parse(file.getPath());
    }
}
