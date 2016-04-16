package com.doglandia.medialoader.videolib;

import android.graphics.Color;
import android.support.v17.leanback.widget.BaseCardView;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doglandia.medialoader.R;

/**
 * Created by tdk10 on 3/29/2016.
 */
public class IconPresenter extends Presenter {

    public IconPresenter(){

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        ViewHolder viewHolder = new Presenter.ViewHolder(new IconCardView(parent.getContext()));
        viewHolder.view.setFocusable(true);
        viewHolder.view.setFocusableInTouchMode(true);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        ActionIcon actionIcon = (ActionIcon) item;

        IconCardView cardView = (IconCardView) viewHolder.view;

        cardView.getTitle().setText(actionIcon.getTitle());

        cardView.setOnClickListener(actionIcon.getOnClickListener());
        cardView.getIcon().setImageResource(actionIcon.getResId());
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
    }
}
