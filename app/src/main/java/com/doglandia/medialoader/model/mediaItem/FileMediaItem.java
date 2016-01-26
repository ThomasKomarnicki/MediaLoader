package com.doglandia.medialoader.model.mediaItem;

import android.os.Parcel;
import android.os.Parcelable;

import com.doglandia.medialoader.localStore.MediaRecord;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

import java.io.File;
import java.util.Collection;


/**
 * Created by Thomas on 1/10/2016.
 */
public class FileMediaItem implements MediaItem, Parcelable {

    private MediaRecord mediaRecord;

    private String mainFileLocation;

    public FileMediaItem(MediaRecord mediaRecord) {
        this.mediaRecord = mediaRecord;
        createMainFileLocation();
    }

    protected FileMediaItem(Parcel in) {
        mediaRecord = in.readParcelable(MediaRecord.class.getClassLoader());
        mainFileLocation = in.readString();
    }

    public static final Creator<FileMediaItem> CREATOR = new Creator<FileMediaItem>() {
        @Override
        public FileMediaItem createFromParcel(Parcel in) {
            return new FileMediaItem(in);
        }

        @Override
        public FileMediaItem[] newArray(int size) {
            return new FileMediaItem[size];
        }
    };

    @Override
    public String getDisplayName() {
        return mediaRecord.getDisplayName();
    }

    @Override
    public String getName() {
        return mediaRecord.getName();
    }

    @Override
    public int getProgress() {
        return 100;
    }

    @Override
    public String getBackgroundUrl() {
        return mediaRecord.getBackgroundUrl();
    }

    @Override
    public boolean isAvailable() {
        return mediaRecord.isComplete();
    }

    @Override
    public String getFileLocation() {
        return mainFileLocation;
    }

    @Override
    public long getTimeDownloaded() {
        return mediaRecord.getDownloadStartTime();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mediaRecord,0);
        dest.writeString(mainFileLocation);
    }

    private void createMainFileLocation(){
        File dir = new File(mediaRecord.getMediaFileLocation());
        if(dir.isDirectory()){
            Collection<File> files = FileUtils.listFiles(dir, new RegexFileFilter("^(.*?)"), DirectoryFileFilter.DIRECTORY);
            for(File file : files){
                if(fileIsVideo(file) && file.length() > (1000000 * 100) ){
                    mainFileLocation = file.getPath();
                    return;
                }
            }
        }else{
            mainFileLocation = mediaRecord.getMediaFileLocation();
        }
    }

    private boolean fileIsVideo(File file){
        String fileName = file.getName();
        return fileName.endsWith(".mp4") || fileName.endsWith(".avi") || fileName.endsWith(".mkv");
    }
}
