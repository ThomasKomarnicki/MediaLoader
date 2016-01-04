package com.doglandia.medialoader.content;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

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

    private String torrentFileUrl;

    public ContentDownloader(String torrentFileUrl){
        this.torrentFileUrl = torrentFileUrl;

        TorrentDownloadTask torrentDownloadTask = new TorrentDownloadTask();
        torrentDownloadTask.execute(torrentFileUrl);
    }

    private void initiateTorrent(File torrentFile){
        File outputFile = new File(getTorrentOutputUri(torrentFile.getName()).getPath());
        TorrentDownloader torrentDownloader = new TorrentDownloader(torrentFile, outputFile);
    }


    private class TorrentDownloadTask extends AsyncTask<String, Integer, File>{

        @Override
        protected File doInBackground(String... params) {
            String torrentUrl = params[0];
            OkHttpClient client = new OkHttpClient();
            Request.Builder builder = new Request.Builder();
            Request request = builder.get().url(torrentUrl).build();
            Call call = client.newCall(request);
            try {
                Response response = call.execute();

                File downloadedFile = new File(getTorrentFileUri("torrent.torrent").getPath());

                BufferedSink sink = Okio.buffer(Okio.sink(downloadedFile));
                sink.writeAll(response.body().source());
                sink.close();

                Log.d(TAG, "downloaded torrent file to: " + downloadedFile.getPath());

                return downloadedFile;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(File downloadedTorrentFile) {
            super.onPostExecute(downloadedTorrentFile);
            Log.d(TAG, "finished downloading .torrent file");

            initiateTorrent(downloadedTorrentFile);
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
