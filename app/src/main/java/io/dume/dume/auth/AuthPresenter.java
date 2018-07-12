package io.dume.dume.auth;

import android.support.annotation.Nullable;
import android.view.MenuItem;

import io.dume.dume.R;
import io.dume.dume.util.DumeUtils;

public class AuthPresenter implements AuthContract.Presenter {

    AuthContract.View view;
    AuthContract.Model model;

    public AuthPresenter(AuthContract.View view, @Nullable AuthContract.Model model) {
        this.view = view;
        this.model = model;
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
        } else view.onValidationSuccess(phoneNumber);
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
    public void isExistingUser(String phoneNumber) {
        view.goToVerificationActivity();
    }
}
