package io.dume.dume.splash;

public interface SplashContract {
    interface View {

        void gotoLoginActivity();

        void gotoTeacherActivity();

        void gotoStudentActivity();
    }

    interface Model {
        boolean isUserLoggedIn();
        void onAccountTypeFound(AuthCallbackListener listener);

    }

    interface Presenter {
        void enqueue();
    }

    /*Callback for Data/Model Model*/
    interface AuthCallbackListener {
        void onTeacherFound();

        void onStudentFound();
    }
}

