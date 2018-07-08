package io.dume.dume.splash;

public class SplashPresenter implements SplashContract.Presenter {
    SplashContract.View view;
    SplashContract.Auth auth;

    public SplashPresenter(SplashContract.View view, SplashContract.Auth auth) {
        this.view = view;
        this.auth = auth;
    }

    @Override
    public void enqueue() {
        if (auth.isUserLoggedIn()) {
            auth.onAccountTypeFound(new SplashContract.AuthCallbackListener() {
                @Override
                public void onTeacherFound() {
                    view.gotoTeacherActivity();
                }

                @Override
                public void onStudentFound() {
                    view.gotoStudentActivity();
                }
            });
        } else view.gotoLoginActivity();
    }
}