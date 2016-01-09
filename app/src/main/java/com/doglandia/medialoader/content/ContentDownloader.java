package com.doglandia.medialoader.content;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.doglandia.medialoader.MediaLoaderApplication;
import com.frostwire.bittorrent.BTEngine;
import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.DHT;
import com.frostwire.jlibtorrent.Downloader;
import com.frostwire.jlibtorrent.Entry;
import com.frostwire.jlibtorrent.LibTorrent;
import com.frostwire.jlibtorrent.Session;
import com.frostwire.jlibtorrent.TorrentAlertAdapter;
import com.frostwire.jlibtorrent.TorrentHandle;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;
import com.frostwire.jlibtorrent.alerts.BlockFinishedAlert;
import com.frostwire.jlibtorrent.alerts.DhtStatsAlert;
import com.frostwire.jlibtorrent.alerts.TorrentAlert;
import com.frostwire.jlibtorrent.alerts.TorrentFinishedAlert;
import com.frostwire.jlibtorrent.swig.add_torrent_params;
import com.frostwire.jlibtorrent.swig.error_code;
import com.frostwire.jlibtorrent.swig.libtorrent;
import com.frostwire.jlibtorrent.swig.torrent_handle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

    private Context context;

    public ContentDownloader(String magnetUri,Context context){
        this.context = context;
        this.magnetUri = magnetUri;
        this.magnetUri = "magnet:?xt=urn:btih:302bb06718b3979f94b7ec9be3b4ad4eaf7c061c&dn=La+Roux+-+In+For+The+Kill+%28Skream+Remix%29&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A80&tr=udp%3A%2F%2Fopen.demonii.com%3A1337&tr=udp%3A%2F%2Ftracker.coppersurfer.tk%3A6969&tr=udp%3A%2F%2Fexodus.desync.com%3A6969";



        File[] files = Environment.getExternalStorageDirectory().listFiles();
        Log.d(TAG,files.toString());


//         Environment.getExternalStorageDirectory().getPath() + File.separator + "Android"
//        + File.separator +"data" + File.separator + "com.doglandia.medialoader"+ File.separator+"torrents"
//                + File.separator + "302BB06718B3979F94B7EC9BE3B4AD4EAF7C061C.torrent";

        String filePath = MediaLoaderApplication.getTorrentsPath() + File.separator + "302BB06718B3979F94B7EC9BE3B4AD4EAF7C061C.torrent";

        File file = new File(filePath);
        Log.d(TAG, "file exists = "+ file.exists());
        Log.d(TAG,filePath);


        TorrentDownloadTask torrentDownloadTask = new TorrentDownloadTask();
        torrentDownloadTask.execute(filePath);

         }

    private void initiateTorrent(File torrentFile){
        File outputFile = new File(getTorrentOutputUri(torrentFile.getName()).getPath());
        TorrentDownloader torrentDownloader = new TorrentDownloader(torrentFile, outputFile);
        torrentDownloader.startDownloading();
    }


    private class TorrentDownloadTask extends AsyncTask<String, Integer, File>{

        @Override
        protected File doInBackground(String... params) {
            String filePath = params[0];


            String[] args = new String[]{filePath};


            File torrentFile = new File(args[0]);

            System.out.println("Using libtorrent version: " + LibTorrent.version());

            BTEngine.getInstance().download(torrentFile, MediaLoaderApplication.getMediaPath());
            BTEngine.getInstance().getSession().addListener(new AlertListener() {
                @Override
                public int[] types() {
                    return null;
                }

                @Override
                public void alert(Alert<?> alert) {

                    AlertType type = alert.getType();
                    Log.i(TAG, "alert: "+type+": "+alert.toString());
                }
            });
//            final Session s = new Session();
//
//            final TorrentHandle th = s.addTorrent(torrentFile, torrentFile.getParentFile());
//
//            final CountDownLatch signal = new CountDownLatch(1);
//
//            s.addListener(new TorrentAlertAdapter(th) {
//                @Override
//                public void blockFinished(BlockFinishedAlert alert) {
//                    int p = (int) (th.getStatus().getProgress() * 100);
//                    System.out.println("Progress: " + p + " for torrent name: " + alert.torrentName());
//                    System.out.println(s.getStats().download());
//                }
//
//                @Override
//                public void torrentFinished(TorrentFinishedAlert alert) {
//                    System.out.print("Torrent finished");
//                    signal.countDown();
//                }
//            });
//
//            th.resume();
//
//            try {
//                signal.await();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

// MAGNET DOWNLOAD
//            Session s = new Session();
//
//            s.addListener(new AlertListener() {
//                @Override
//                public int[] types() {
//                    return null;
//                }
//
//                @Override
//                public void alert(Alert<?> alert) {
//                    System.out.println(alert);
//                }
//            });
//
//            DHT dht = new DHT(s);
//
//            dht.getPeers("302bb06718b3979f94b7ec9be3b4ad4eaf7c061c");
//
//            System.out.println("Waiting for nodes in DHT");
//            try {
//                Thread.sleep(20000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            Log.d(TAG,"done waiting for nodes in DHT");
//
//            add_torrent_params p = add_torrent_params.create_instance_no_storage();
//            error_code ec = new error_code();
//            libtorrent.parse_magnet_uri(uri, p, ec);
//
//            p.setName("fetchMagnet - " + uri);
//
//            long flags = p.getFlags();
//            flags &= ~add_torrent_params.flags_t.flag_auto_managed.swigValue();
//            p.setFlags(flags);
//
//            torrent_handle th = s.getSwig().add_torrent(p);
//            th.resume();
//
//            Log.d(TAG, "done waiting for nodes in DHT");

//            System.in.read();


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
        Log.d(TAG, "file created for torrent at: " + file.getPath());
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
