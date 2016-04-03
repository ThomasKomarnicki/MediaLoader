package com.doglandia.medialoader.videolib;

import android.app.Fragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doglandia.medialoader.MediaLoaderApplication;
import com.doglandia.medialoader.R;
import com.doglandia.medialoader.event.ResourceServerConnectFailed;
import com.doglandia.medialoader.event.ResourceServerConnected;
import com.doglandia.medialoader.model.ResourcesResponse;
import com.doglandia.medialoader.resourceserver.ResourceServer;
import com.squareup.otto.Subscribe;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by tdk10 on 2/28/2016.
 */
public class ReconnectingFragment extends Fragment {

    private View connectingView;
    private View errorView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reconnecting, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        connectingView = view.findViewById(R.id.connecting_view);
        errorView = view.findViewById(R.id.error_view);

        view.findViewById(R.id.try_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startClientDiscovery();
                showConnectingView();
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MediaLoaderApplication.getBus().register(this);
        showConnectingView();

        startClientDiscovery();
    }

    private void startClientDiscovery(){
        ((MediaLoaderApplication) getActivity().getApplication()).getResourceServer().startClientDiscovery();
    }

    @Subscribe
    public void onResourceServerConnectionFailed(ResourceServerConnectFailed event){
        showConnectingErrorView();
    }

    private void showConnectingView(){
        connectingView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
    }

    private void showConnectingErrorView(){
        errorView.setVisibility(View.VISIBLE);
        connectingView.setVisibility(View.GONE);
        getView().findViewById(R.id.try_again).requestFocus();
//        horizontalGridView.setSelected(true);
//        horizontalGridView.setSelectedPosition(0);
    }


    
}
