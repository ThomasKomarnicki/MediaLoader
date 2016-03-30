package com.doglandia.medialoader.videolib;

import android.support.v17.leanback.widget.Presenter;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.doglandia.medialoader.R;

/**
 * Created by tdk10 on 3/29/2016.
 */
public class IconPresenter extends Presenter {

    public IconPresenter(){

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        ActionIcon actionIcon = (ActionIcon) item;

        ImageView imageView = ((ImageView)viewHolder.view.findViewById(R.id.icon));
        imageView.setOnClickListener(actionIcon.getOnClickListener());

        imageView.setImageResource(actionIcon.getResId());
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }
}
