package com.doglandia.medialoader.videolib;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doglandia.medialoader.R;

/**
 * Created by tdk10 on 2/28/2016.
 */
public class ReconnectingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reconnecting, null);
    }

    public void startReconnecting(){

    }

    
}
