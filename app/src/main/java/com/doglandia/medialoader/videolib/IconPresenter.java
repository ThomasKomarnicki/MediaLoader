package com.doglandia.medialoader.videolib;

import android.graphics.Color;
import android.support.v17.leanback.widget.BaseCardView;
import android.support.v17.leanback.widget.ImageCardView;
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
        ImageCardView cardView = new ImageCardView(parent.getContext()) {
            @Override
            public void setSelected(boolean selected) {
//                updateCardBackgroundColor(this, selected);
                super.setSelected(selected);
            }
        };
        cardView.setCardType(BaseCardView.CARD_TYPE_INFO_UNDER);

        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        return new Presenter.ViewHolder(cardView);
//        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        ActionIcon actionIcon = (ActionIcon) item;

        ImageCardView cardView = (ImageCardView) viewHolder.view;
        cardView.setMainImageDimensions(120,140);
        cardView.setMainImageAdjustViewBounds(true);
        cardView.getMainImageView().setColorFilter(Color.WHITE);


//        cardView.setTitleText(actionIcon.getTitle());

        cardView.setContentText(actionIcon.getTitle());
        cardView.setOnClickListener(actionIcon.getOnClickListener());
        cardView.setMainImage(cardView.getResources().getDrawable(actionIcon.getResId()));

    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }
}
