package com.doglandia.medialoader.resourceserver;

import com.doglandia.medialoader.model.ResourcesResponse;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by tdk10 on 2/21/2016.
 */
public interface ServerInterface {

    @GET("/data")
    void getResourceGroups(Callback<ResourcesResponse> callback);


}
