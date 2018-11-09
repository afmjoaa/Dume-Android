package io.dume.dume.student.profilePage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import io.dume.dume.R;

public class ProfilePagePresenter implements ProfilePageContract.Presenter {

    private ProfilePageContract.View mView;
    private ProfilePageContract.Model mModel;
    private Context context;
    private Activity activity;

    public ProfilePagePresenter(Context context, ProfilePageContract.View mView, ProfilePageContract.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = mView;
        this.mModel = mModel;
    }

    @Override
    public void profilePageEnqueue() {
        mView.findView();
        mView.initProfilePage();
        mView.configProfilePage();
    }

    @Override
    public void onProfileViewIntracted(View view) {
        switch (view.getId()) {
            case R.id.input_gerder:
                mView.onGenderClicked();
                break;
            case R.id.input_current_address:
                mView.onCurrentAddressClicked();
                break;
            case R.id.input_previous_result:
                mView.onPreviousResultClicked();
                break;


        }
    }

}
