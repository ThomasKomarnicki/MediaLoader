package com.doglandia.medialoader.content;

import android.content.Context;
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

    private static final Uri getTorrentFilesUri(String fileName) {
        // todo
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File (sdCard.getAbsolutePath() + "/MediaLoader/torrent");
        dir.mkdirs();
        File file = new File(dir, fileName);
        Log.d(TAG, "file created for torrent at: "+file.getPath());
        return Uri.parse(file.getPath());
//        FileOutputStream f = new FileOutputStream(file);
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

                File downloadedFile = new File(getTorrentFilesUri("torrent.torrent").getPath());

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
        protected void onPostExecute(File file) {
            super.onPostExecute(file);

            // todo torrent file
        }
    }
}
