package com.doglandia.medialoader.introduction;

import android.app.Activity;
import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepFragment;

import com.doglandia.medialoader.R;
import com.doglandia.medialoader.introduction.fragment.IntroductionFragment;

/**
 * Created by tdk10 on 2/27/2016.
 */
public class IntroductionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        IntroductionFragment introductionFragment = new IntroductionFragment();

        GuidedStepFragment.addAsRoot(this, introductionFragment, R.id.intro_content);
    }
}
