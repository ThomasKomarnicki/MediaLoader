package com.doglandia.medialoader.introduction.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.test.mock.MockApplication;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doglandia.medialoader.MediaLoaderApplication;
import com.doglandia.medialoader.R;
import com.doglandia.medialoader.event.ResourceServerConnected;
import com.doglandia.medialoader.introduction.IntroductionActivity;
import com.doglandia.medialoader.model.ResourceGroup;
import com.doglandia.medialoader.model.ResourcesResponse;
import com.doglandia.medialoader.resourceserver.ResourceServer;
import com.doglandia.medialoader.videolib.VideoLibraryActivity;
import com.squareup.otto.Subscribe;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by tdk10 on 2/27/2016.
 */
public class ConnectingToPcFragment extends Fragment {

    private CountDownTimer countDownTimer;

    private boolean doneCountingDown = false;
    private List<ResourceGroup> resourceGroups;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_connecting, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // wait atlesat 3 seconds before continuing to the next screen
        countDownTimer = new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                doneCountingDown = true;
            }
        };
        countDownTimer.start();

        //start connection and when connected advanced to media library activity

        MediaLoaderApplication.getBus().register(this);

        ((MediaLoaderApplication) getActivity().getApplication()).getResourceServer().startClientDiscovery();
    }

    @Subscribe
    public void onResourceServerConnected(ResourceServerConnected resourceServerConnected){

        if(getActivity() != null) { //
            ResourceServer server = ((MediaLoaderApplication) getActivity().getApplication()).getResourceServer();
            server.getResourceGroups(new Callback<ResourcesResponse>() {
                @Override
                public void success(ResourcesResponse resourcesResponse, Response response) {
                    resourceGroups = resourcesResponse.getResourceGroups();
                    onResourceGroupsRetrieved();
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                }
            });
        }

    }

    private void onResourceGroupsRetrieved(){
        if(resourceGroups == null || !doneCountingDown){
            return;
        }

        // if resource groups size == 0, set message for adding folders

        // else go to video library

        if(resourceGroups.size() == 0){

        }else{
            setFirstRunPreference();
            Intent intent = new Intent(getActivity(), VideoLibraryActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    private void setFirstRunPreference(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(IntroductionActivity.FIRST_RUN_CONNECTED,true);
        editor.commit();
    }
}
