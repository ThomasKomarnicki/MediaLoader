package com.doglandia.medialoader.thumbnail;

import com.doglandia.medialoader.model.Resource;
import com.doglandia.medialoader.model.ResourceGroup;
import com.doglandia.medialoader.resourceserver.ResourceServer;

import java.io.File;
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

        String path = localRoot.getPath() + File.separator;




        String[] split = resource.getLocation().split("\\.");
        String fileName = split[0]+".jpg";
        fileName = replaceSeparators(fileName, "-");
//        path =  "";
//        for(int i = 0; i < split.length-1; i++){
//            path += split[i]+".";
//        }
//        path += split[0]+".jpg";
        path += fileName;
//        path = localRoot.getPath() + File.separator + "dogs1.jpg";
        return path;
//        File thumbnailFile = new File(path);
//
//
//        return thumbnailFile;
    }

    private static String replaceSeparators(String s, String replacement){
        return s.replace("/", replacement);
    }

}
