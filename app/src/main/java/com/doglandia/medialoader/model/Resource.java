package com.doglandia.medialoader.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tdk10 on 2/21/2016.
 */
public class Resource implements Parcelable{

    private String name;
    private String location;
    private String thumbnailPath;

    protected Resource(Parcel in) {
        name = in.readString();
        location = in.readString();
    }

    public static final Creator<Resource> CREATOR = new Creator<Resource>() {
        @Override
        public Resource createFromParcel(Parcel in) {
            return new Resource(in);
        }

        @Override
        public Resource[] newArray(int size) {
            return new Resource[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(location);
    }
}
