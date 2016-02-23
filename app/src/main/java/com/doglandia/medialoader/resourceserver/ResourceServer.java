package com.doglandia.medialoader.resourceserver;

import android.content.Context;

import com.doglandia.medialoader.clientdiscovery.ClientDiscoverer;
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
public class ResourceServer implements ServerInterface, ClientDiscoverer.OnHostFoundListener{

    private ServerInterface instance;

    private ClientDiscoverer clientDiscoverer;

    private String discoveredHost;

    public ResourceServer(Context context){

        clientDiscoverer = new ClientDiscoverer(context, this);
    }

    private void createRestAdapter(){
        RestAdapter.Builder builder = new RestAdapter.Builder();
//        builder.setEndpoint("http://192.168.0.9:8080/");
        builder.setEndpoint(discoveredHost+"/");
        builder.setLogLevel(RestAdapter.LogLevel.FULL);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

        builder.setConverter(new GsonConverter(gsonBuilder.create()));
        instance = builder.build().create(ServerInterface.class); // possible to switch to testable instance
    }


    @Override
    public void getResourceGroups(Callback<ResourcesResponse> callback) {
        instance.getResourceGroups(callback);
    }

    public String getMediaUrl(Resource resource) {
        // todo
        return discoveredHost + "/" + resource.getLocation();
    }

    @Override
    public void onHostFound(String host) {
        discoveredHost = host;
        createRestAdapter();
    }

    @Override
    public void onProgressUpdate(int progress) {

    }
}
