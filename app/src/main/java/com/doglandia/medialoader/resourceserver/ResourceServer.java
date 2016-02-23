package com.doglandia.medialoader.resourceserver;

import android.content.Context;

import com.doglandia.medialoader.model.Resource;
import com.doglandia.medialoader.model.ResourcesResponse;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by tdk10 on 2/21/2016.
 */
public class ResourceServer implements ServerInterface{

    private ServerInterface instance;

    public ResourceServer(Context context){
        RestAdapter.Builder builder = new RestAdapter.Builder();
        builder.setEndpoint("http://192.168.0.9:8080/");
        builder.setLogLevel(RestAdapter.LogLevel.FULL);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

        builder.setConverter(new GsonConverter(gsonBuilder.create()));
        instance = builder.build().create(ServerInterface.class); // possible to switch to testable instance
    }

    // todo find ip of pc


    @Override
    public void getResourceGroups(Callback<ResourcesResponse> callback) {
        instance.getResourceGroups(callback);
    }

    public String getMediaUrl(Resource resource) {
        // todo
        return "http://192.168.0.9:8080/" + resource.getLocation();
    }
}
