package com.doglandia.medialoader.videolib;

import android.view.View;

/**
 * Created by tdk10 on 3/29/2016.
 */
public class ActionIcon {

    private int resId;

    private View.OnClickListener onClickListener;

    public ActionIcon(int resId, View.OnClickListener onClickListener) {
        this.resId = resId;
        this.onClickListener = onClickListener;
    }

    public int getResId() {
        return resId;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }
}
