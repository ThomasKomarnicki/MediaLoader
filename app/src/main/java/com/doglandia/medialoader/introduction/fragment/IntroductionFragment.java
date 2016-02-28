package com.doglandia.medialoader.introduction.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.app.OnboardingFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doglandia.medialoader.R;

import java.util.List;

/**
 * Created by tdk10 on 2/27/2016.
 */
public class IntroductionFragment extends OnboardingFragment {

    private static final long NEXT = 1;
    private static final long MORE_INFO = 2;

    @Override
    protected int getPageCount() {
        return 3;
    }

    @Override
    protected String getPageTitle(int pageIndex) {
        return "Page "+pageIndex;
    }

    @Override
    protected String getPageDescription(int pageIndex) {
        return "Description "+1;
    }

    @Nullable
    @Override
    protected View onCreateBackgroundView(LayoutInflater inflater, ViewGroup container) {
        getCurrentPageIndex();
        return null;
    }

    @Nullable
    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container) {
        getCurrentPageIndex();
        return null;
    }

    @Nullable
    @Override
    protected View onCreateForegroundView(LayoutInflater inflater, ViewGroup container) {
        getCurrentPageIndex();
        return null;
    }


//    @NonNull
//    @Override
//    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
//        return new GuidanceStylist.Guidance("Get Started","To stream videos from your PC, download the PC app at doglandia.com/pc",
//                "",
//                getResources().getDrawable(R.drawable.app_icon_quantum));
//
//
//    }
//
//    @Override
//    public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {
//        super.onCreateActions(actions, savedInstanceState);
//
//        GuidedAction.Builder builder = new GuidedAction.Builder(getActivity());
//        builder.title("I've Done This");
//        builder.clickAction(1);
//        actions.add(builder.build());
//
//        builder = new GuidedAction.Builder(getActivity());
//        builder.title("More Information");
//        builder.clickAction(2);
//        actions.add(builder.build());
//    }
//
//    @Override
//    public void onGuidedActionClicked(GuidedAction action) {
//        super.onGuidedActionClicked(action);
//
//        if(action.getId() == NEXT){
//            ConnectingToPcFragment connectingToPcFragment = new ConnectingToPcFragment();
//            getFragmentManager().beginTransaction().add( R.id.intro_content, connectingToPcFragment, "connectingFragment").addToBackStack(null).commit();
//        }else if(action.getId() == MORE_INFO){
//
//        }
//    }
}
