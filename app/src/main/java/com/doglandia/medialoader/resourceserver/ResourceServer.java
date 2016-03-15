package com.doglandia.medialoader.resourceserver;

import android.content.Context;

import com.doglandia.medialoader.MediaLoaderApplication;
import com.doglandia.medialoader.clientdiscovery.ClientDiscoverer;
import com.doglandia.medialoader.event.ResourceServerConnected;
import com.doglandia.medialoader.model.Resource;
import com.doglandia.medialoader.model.ResourcesResponse;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by tdk10 on 2/21/2016.
 */
public class ResourceServer implements ServerInterface, ClientDiscoverer.OnHostFoundListener{

    public static final String PORT = "8988";

    private ServerInterface instance;

    private ClientDiscoverer clientDiscoverer;

    private String discoveredHost;

    private Context context;

    public ResourceServer(Context context){
        this.context = context;
//        clientDiscoverer = new ClientDiscoverer(context, this);
    }

    public void startClientDiscovery(){
        clientDiscoverer = new ClientDiscoverer(context,this);
    }

    private void createRestAdapter(){
        RestAdapter.Builder builder = new RestAdapter.Builder();
//        builder.setEndpoint("http://192.168.0.9:8080/");
        builder.setEndpoint(getResourceServerEndpoint());
        builder.setLogLevel(RestAdapter.LogLevel.FULL);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

        builder.setConverter(new GsonConverter(gsonBuilder.create()));
        instance = builder.build().create(ServerInterface.class); // possible to switch to testable instance
    }

    private String getResourceServerEndpoint(){
        return "http://"+discoveredHost+":"+PORT;
    }

    public boolean isConnected(){
        return discoveredHost != null;
    }


    @Override
    public void getResourceGroups(Callback<ResourcesResponse> callback) {
        instance.getResourceGroups(callback);
    }

    public String getMediaUrl(Resource resource) {
        // todo
        return (getResourceServerEndpoint() + "/" + resource.getLocation()).replace(" ","%20");
    }

    @Override
    public void onHostFound(String host) {
        discoveredHost = host;
        createRestAdapter();

        MediaLoaderApplication.getBus().post(new ResourceServerConnected());
    }

    @Override
    public void onNoHostFound() {
        // host not found message
    }

    @Override
    public void onProgressUpdate(int progress) {

    }

    public void cancelConnectingTasks() {
        if(clientDiscoverer != null){
            clientDiscoverer.cancelTasks();
        }
    }
}
