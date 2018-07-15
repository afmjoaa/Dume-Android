package io.dume.dume.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import io.dume.dume.R;
import io.dume.dume.util.DumeUtils;

public class AuthPresenter implements AuthContract.Presenter {

    AuthContract.View view;
    AuthContract.Model model;
    private Bundle mBundle;

    public AuthPresenter(AuthContract.View view, @Nullable AuthContract.Model model) {
        this.view = view;
        this.model = model;
        mBundle = new Bundle();
    }

    @Override
    public void enqueue() {
        view.findView();
        view.initActionBar();
        view.init();


    }

    @Override
    public void onPhoneTextChange(String text) {
        if (text.length() > 0) {
            view.showCount(String.valueOf(text.length()));
        } else view.hideCount();
    }

    @Override
    public void onBottomNavChange(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.student_nav:
                view.onStudentSelected();
                break;
            case R.id.teacher_nav:
                view.onTeacherSelected();
                break;
            case R.id.bootcamp_nav:
                view.onBootcampSelected();
                break;
        }
    }

    @Override
    public void onPhoneValidation(String phoneNumber) {

        if (phoneNumber.isEmpty()) {
            view.onValidationFailed("Should not be empty");
        } else if (!DumeUtils.isInteger(phoneNumber)) {
            view.onValidationFailed("Only Digits Allowed (0-9)");
        } else if (phoneNumber.length() != 11) {
            view.onValidationFailed("Should be 11 Digits");
        } else if (model.isExistingUser(phoneNumber)) {
            view.showProgress("User Found. Sending Code", "We are sending 6 digits code to your given phone number. please wait a bit");
            model.sendMessage("+88" + phoneNumber, new AuthContract.Model.Callback() {
                @Override
                public void onFail(String error) {
                    view.hideProgress();
                    view.onValidationFailed(error);
                    view.showToast(error);
                }

                @Override
                public void onSuccess(String s) {
                    view.hideProgress();
                    mBundle.putString("verification_id", s);
                    mBundle.putInt("station", 1);
                    mBundle.putString("phone_number", phoneNumber);
                    view.goToVerificationActivity(mBundle);
                }
            });

        } else {
            mBundle.putInt("station", 1);
            mBundle.putString("phone_number", phoneNumber);
            view.goToRegesterActivity(mBundle);

        }
    }


    @Override
    public void onAppBarStateChange(AppbarStateChangeListener.State state) {
        String appState = state.name();
        if (appState == "EXPANDED") {
            view.onAppBarLayoutExpanded();
        }
        if (appState == "COLLAPSED") {
            view.onAppBarLayoutCollapsed();
        }
    }


    @Override
    public void setBundle() {
        if (model.getIntent().getBundleExtra("bundle") != null) {
            view.fillBundle(model.getIntent().getBundleExtra("bundle"));
        }
    }
}
