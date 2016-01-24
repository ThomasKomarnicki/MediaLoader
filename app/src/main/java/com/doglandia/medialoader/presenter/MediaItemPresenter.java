package com.doglandia.medialoader.presenter;

import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.doglandia.medialoader.model.mediaItem.MediaItem;

/**
 * Created by Thomas on 1/10/2016.
 */
public class MediaItemPresenter extends Presenter {

    private static int CARD_WIDTH = 313;
    private static int CARD_HEIGHT = 176;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        ImageCardView cardView = new ImageCardView(parent.getContext()) {
            @Override
            public void setSelected(boolean selected) {
//                updateCardBackgroundColor(this, selected);
                super.setSelected(selected);
            }
        };

        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
//        updateCardBackgroundColor(cardView, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        MediaItem mediaItem = (MediaItem) item;
        ImageCardView cardView = (ImageCardView) viewHolder.view;

        cardView.setTitleText(mediaItem.getDisplayName());
        if(mediaItem.isAvailable()){
            cardView.setContentText("Ready");
        }else {
            cardView.setContentText(String.valueOf(mediaItem.getProgress()));
        }
        cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);
        Glide.with(viewHolder.view.getContext())
                .load(mediaItem.getBackgroundUrl())
                .centerCrop()
//                    .error(mDefaultCardImage)
                .into(cardView.getMainImageView());

    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        // Remove references to images so that the garbage collector can free up memory
        cardView.setBadgeImage(null);
        cardView.setMainImage(null);
    }

    public void onMediaItemClick(MediaItem mediaItem){

    }
}
