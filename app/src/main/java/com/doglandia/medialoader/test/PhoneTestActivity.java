package com.doglandia.medialoader.test;

import android.os.Bundle;
import android.app.Activity;

import com.doglandia.medialoader.R;
import com.doglandia.medialoader.content.ContentDownloader;

public class PhoneTestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_test);

        ContentDownloader contentDownloader = new ContentDownloader();


    }

}
