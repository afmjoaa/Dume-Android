package io.dume.dume.student.grabingLocation;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import io.dume.dume.R;

public class GrabingLocationPresenter implements GrabingLocaitonContract.Presenter {

    private GrabingLocaitonContract.View mView;
    private GrabingLocaitonContract.Model mModel;
    private Context context;
    private Activity activity;

    public GrabingLocationPresenter(Context context, GrabingLocaitonContract.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (GrabingLocaitonContract.View) context;
        this.mModel = mModel;
    }

    @Override
    public void grabingLocationPageEnqueue() {
        mView.findView();
        mView.initGrabingLocationPage();
        mView.makingCallbackInterfaces();
        mView.configGrabingLocationPage();
    }

    @Override
    public void onGrabingLocationViewIntracted(View view) {
        switch (view.getId()) {
            case R.id.fab:
                mView.onCenterCurrentLocation();
                break;
            case R.id.bottom_sheet_fab:
                mView.onShowBottomSheet();
                break;

        }


    }
}
