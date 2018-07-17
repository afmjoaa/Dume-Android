package io.dume.dume.splash;

import io.dume.dume.auth.AuthGlobalModel;

public interface SplashContract {
    interface View {

        void gotoLoginActivity();

        void gotoTeacherActivity();

        void gotoStudentActivity();
    }

    interface Model extends AuthGlobalModel {


    }

    interface Presenter {
        void enqueue();
    }

    /*Callback for Data/Model Model*/

}

