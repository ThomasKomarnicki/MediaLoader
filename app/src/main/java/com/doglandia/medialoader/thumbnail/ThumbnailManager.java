package com.doglandia.medialoader.thumbnail;

import com.doglandia.medialoader.model.Resource;
import com.doglandia.medialoader.model.ResourceGroup;
import com.doglandia.medialoader.resourceserver.ResourceServer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * takes a List<ResourceGroup> and makes sure the thumbnails are downloaded into files using the
 * same location structure
 */
public class ThumbnailManager {

    public interface ThumbnailRetrievedListener{
        void onThumbnailRetrieved(Resource resource, File file);
        void onAllThumbnailsRetrieved(ThumbnailManager thumbnailManager);
    }

    private File localRoot;
    private ResourceServer server;

    private ThumbnailRetrievedListener listener;

    /**
     *
     * @param localRoot base directory for thumbnails
     */
    public ThumbnailManager(ResourceServer resourceServer, File localRoot){
        this.localRoot = localRoot;
        this.server = resourceServer;
    }

    public void setListener(ThumbnailRetrievedListener listener) {
        this.listener = listener;
    }

    public void addThumbnails(List<ResourceGroup> resourceGroups){
        List<Resource> flattened = new ArrayList<>();
        for(ResourceGroup resourceGroup : resourceGroups){
            for(Resource resource : resourceGroup.getResourceList()){
                flattened.add(resource);
            }
        }

        MetaDataTask metaDataTask = new MetaDataTask(server, this){
            @Override
            protected void onPostExecute(File file) {
                super.onPostExecute(file);
                if(listener != null){
                    listener.onAllThumbnailsRetrieved(ThumbnailManager.this);
                }
            }
        };
        metaDataTask.execute(flattened.toArray(new Resource[flattened.size()]));
    }

    public String getThumbnailFileForResource(Resource resource){

        String path = localRoot.getPath() + File.separator + resource.getLocation();
        String[] split = path.split("\\.");
        path =  "";
//        path = split[0] + ".jpg";
        for(int i = 0; i < split.length-1; i++){
            path += split[i]+".";
        }
        path += "jpg";
//        return path;
        return localRoot.getPath() + File.separator + "dogs1.jpg";
//        File thumbnailFile = new File(path);
//
//
//        return thumbnailFile;
    }

}
