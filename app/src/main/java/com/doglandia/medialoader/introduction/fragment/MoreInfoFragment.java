package com.doglandia.medialoader.introduction.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doglandia.medialoader.R;

/**
 * Created by tdk10 on 2/27/2016.
 */
public class MoreInfoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_more_info, null);
    }

}
