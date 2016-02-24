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

    private MetaFile metaFile;

    private ThumbnailRetrievedListener listener;

    /**
     *
     * @param localRoot base directory for thumbnails
     */
    public ThumbnailManager(ResourceServer resourceServer, File localRoot){
        this.localRoot = localRoot;
        this.server = resourceServer;
        metaFile = new MetaFile(new File(localRoot.getPath()+File.separator+"meta.json"));
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
        // todo optimize with string builder
        String path = localRoot.getPath() + File.separator;

        String[] split = resource.getLocation().split("\\.");
        String fileName =  "";
        for(int i = 0; i < split.length-1; i++){
            fileName += split[i]+".";
        }
        fileName += "jpg";
        fileName = replaceSeparators(fileName, "-");
        path += fileName;
        return path;
    }

    private static String replaceSeparators(String s, String replacement){
        return s.replace("/", replacement);
    }

}
