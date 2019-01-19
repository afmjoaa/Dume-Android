package io.dume.dume.student.grabingInfo;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import io.dume.dume.R;

public class GrabingInfoPresenter implements GrabingInfoContract.Presenter {

    private GrabingInfoContract.View mView;
    private GrabingInfoContract.Model mModel;
    private Context context;
    private Activity activity;

    public GrabingInfoPresenter(Context context, GrabingInfoContract.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (GrabingInfoContract.View) context;
        this.mModel = mModel;
    }

    @Override
    public void grabingInfoPageEnqueue() {
        mView.findView();
        mView.initGrabingInfoPage();
        mView.configGrabingInfoPage();
    }

    @Override
    public void onGrabingInfoViewIntracted(View view) {
        switch (view.getId()) {
            case R.id.view_musk:
                mView.viewMuskClicked();
                break;
            case R.id.fab:
                mView.fabClicked(view);
                break;
            case R.id.select_other_contact:
                mView.selectFromContactClicked();
                break;
            case R.id.first_contact:
                mView.firstContactClicked();
                break;
            case R.id.second_contact:
                mView.secondContactClicked();
                break;

        }
    }
}
