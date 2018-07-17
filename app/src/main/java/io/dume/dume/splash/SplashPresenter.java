package io.dume.dume.splash;

import android.util.Log;

import io.dume.dume.auth.AuthGlobalModel;

public class SplashPresenter implements SplashContract.Presenter {
    SplashContract.View view;
    SplashContract.Model model;
    private static final String TAG = "SplashPresenter";

    public SplashPresenter(SplashContract.View view, SplashContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void enqueue() {
        Log.w(TAG, "enqueue: ");
        if (model.isUserLoggedIn()) {
            model.onAccountTypeFound(model.getUser(), new AuthGlobalModel.AccountTypeFoundListener() {
                @Override
                public void onStart() {
                    Log.w(TAG, "onStart: ");
                }

                @Override
                public void onTeacherFound() {
                    view.gotoTeacherActivity();
                    Log.w(TAG, "onTeacherFound: ");
                }

                @Override
                public void onStudentFound() {
                    view.gotoStudentActivity();
                    Log.w(TAG, "onStudentFound: ");
                }

                @Override
                public void onFail(String exeption) {

                }
            });
        } else {
            view.gotoLoginActivity();
            Log.w(TAG, "enqueue: login");
        }

    }
}