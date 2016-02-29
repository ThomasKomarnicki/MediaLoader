package com.doglandia.medialoader.introduction;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.app.OnboardingFragment;

import com.doglandia.medialoader.R;
import com.doglandia.medialoader.introduction.fragment.ConnectingToPcFragment;
import com.doglandia.medialoader.introduction.fragment.IntroductionFragment;
import com.doglandia.medialoader.videolib.VideoLibraryActivity;

/**
 * Created by tdk10 on 2/27/2016.
 */
public class IntroductionActivity extends Activity {

    public static final String FIRST_RUN_CONNECTED = "first_run_connected";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getBoolean(FIRST_RUN_CONNECTED,false)){

            Intent intent = new Intent(this, VideoLibraryActivity.class);
            startActivity(intent);
            finish();

            return;
        }

        setContentView(R.layout.activity_introduction);

        IntroductionFragment introductionFragment = new IntroductionFragment();
        introductionFragment.setOnboardingFinishedListener(new IntroductionFragment.OnOnboardingFinishedListener() {
            @Override
            public void onOnboardingFinished() {
                startConnectingFragment();
            }
        });
        getFragmentManager().beginTransaction().add(R.id.intro_content, introductionFragment).commit();

//        GuidedStepFragment.addAsRoot(this, introductionFragment, R.id.intro_content);
    }

    private void startConnectingFragment(){
        ConnectingToPcFragment fragment = new ConnectingToPcFragment();
        getFragmentManager().beginTransaction().replace(R.id.intro_content, fragment).addToBackStack(null).commit();
    }

}
