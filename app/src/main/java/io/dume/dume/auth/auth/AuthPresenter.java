package io.dume.dume.auth.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthProvider;

import io.dume.dume.R;
import io.dume.dume.auth.AuthGlobalContract;
import io.dume.dume.auth.DataStore;
import io.dume.dume.util.DumeUtils;

public class AuthPresenter implements AuthContract.Presenter {

    AuthContract.View view;
    AuthContract.Model model;
    private Bundle mBundle;
    private final DataStore dataStore;

    public AuthPresenter(AuthContract.View view, @Nullable AuthContract.Model model) {
        this.view = view;
        this.model = model;
        mBundle = new Bundle();
        dataStore = DataStore.getInstance();
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
            model.sendMessage("+88" + phoneNumber, new AuthContract.Model.Callback() {
                @Override
                public void onStart() {
                    view.showProgress("Sending Code....", "");
                }

                @Override
                public void onFail(String error) {
                    view.hideProgress();
                    view.onValidationFailed(error);
                    view.showToast(error);
                }

                @Override
                public void onSuccess(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    view.hideProgress();
                    dataStore.setVerificationId(s);
                    DataStore.resendingToken = forceResendingToken;
                    dataStore.setPhoneNumber(phoneNumber);
                    view.goToVerificationActivity(dataStore);
                }

                @Override
                public void onAutoSuccess(AuthResult authResult) {
                    view.hideProgress();
                    model.onAccountTypeFound(authResult.getUser(), new AuthGlobalContract.AccountTypeFoundListener() {
                        @Override
                        public void onStart() {
                            view.showProgress("Getting User Info", "");
                        }

                        @Override
                        public void onTeacherFound() {
                            view.hideProgress();
                            view.gotoTeacherActivity();
                        }

                        @Override
                        public void onStudentFound() {
                            view.hideProgress();
                            view.gotoStudentActivity();
                        }

                        @Override
                        public void onFail(String exeption) {
                            view.hideProgress();
                            view.showToast(exeption);
                        }
                    });

                }
            });

        } else {
            dataStore.setPhoneNumber(phoneNumber);
            view.goToRegesterActivity(dataStore);

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
        if (model.getIntent().getSerializableExtra("datastore") != null) {
            view.restoreData((DataStore) model.getIntent().getSerializableExtra("datastore"));
        }
    }
}
