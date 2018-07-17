package io.dume.dume.splash;

import io.dume.dume.auth.AuthGlobalContract;

public class SplashPresenter implements SplashContract.Presenter {
    SplashContract.View view;
    SplashContract.Model model;

    public SplashPresenter(SplashContract.View view, SplashContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void enqueue() {
        if (model.isUserLoggedIn()) {
            model.onAccountTypeFound(model.getUser(), new AuthGlobalContract.AccountTypeFoundListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onTeacherFound() {
                    view.gotoTeacherActivity();
                }

                @Override
                public void onStudentFound() {
                    view.gotoStudentActivity();
                }

                @Override
                public void onFail(String exeption) {

                }
            });
        } else view.gotoLoginActivity();

    }
}