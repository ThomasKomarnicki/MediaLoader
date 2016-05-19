package com.doglandia.hometheater.resourceserver;

import com.doglandia.hometheater.model.ResourcesResponse;

import retrofit2.http.GET;
import rx.Observable;


public interface ServerInterface {

    @GET("/data")
    Observable<ResourcesResponse> getResourceGroups();


}
