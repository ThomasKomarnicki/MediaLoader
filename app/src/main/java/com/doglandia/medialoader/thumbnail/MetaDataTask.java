package com.doglandia.medialoader.thumbnail;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.doglandia.medialoader.model.Resource;
import com.doglandia.medialoader.resourceserver.ResourceServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import wseemann.media.FFmpegMediaMetadataRetriever;

/**
 * take Resource and extract thumbnail and save to file
 */
public class MetaDataTask extends AsyncTask<Resource, Void, File> {

    private ResourceServer resourceServer;

    private ThumbnailManager thumbnailManager;

    public MetaDataTask(ResourceServer resourceServer, ThumbnailManager thumbnailManager){
        this.resourceServer = resourceServer;
        this.thumbnailManager = thumbnailManager;
    }

    private static final String TAG = "MetaDataTask";
    @Override
    protected File doInBackground(Resource... params) {
        Log.d(TAG, "started metadata task for "+params.length + " items");
        FFmpegMediaMetadataRetriever mmr = new FFmpegMediaMetadataRetriever();

        for(Resource resource : params) {

            String mediaResource = resourceServer.getMediaUrl(resource);
            mmr.setDataSource(mediaResource);
            mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM);
            mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ARTIST);
            HashMap<String,String> metaData = mmr.getMetadata().getAll();
            Bitmap b = mmr.getFrameAtTime(8 * (1000) * (1000), FFmpegMediaMetadataRetriever.OPTION_CLOSEST); // frame at 8 seconds

            String thumbnailFile = thumbnailManager.getThumbnailFileForResource(resource);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(thumbnailFile);
                b.compress(Bitmap.CompressFormat.JPEG, 30,fileOutputStream);
                fileOutputStream.close();
                b.recycle();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            resource.setThumbnailPath(thumbnailFile);
            thumbnailManager.getMetaFile().addMetaForThumbnail(resource, thumbnailFile);

//            byte[] artwork = mmr.getEmbeddedPicture();

        }

        mmr.release();



        Log.d(TAG, "ended metadata task for "+params.length + " items");

        return null;
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
    }


}
