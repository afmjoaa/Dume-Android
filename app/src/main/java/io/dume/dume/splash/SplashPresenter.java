package io.dume.dume.splash;

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
            model.onAccountTypeFound(new SplashContract.AuthCallbackListener() {
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