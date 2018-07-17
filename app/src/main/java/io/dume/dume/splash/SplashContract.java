package io.dume.dume.splash;

import io.dume.dume.auth.AuthGlobalContract;

public interface SplashContract {
    interface View extends AuthGlobalContract.View {
        void gotoLoginActivity();
    }

    interface Model extends AuthGlobalContract.Model {

    }

    interface Presenter {
        void enqueue();
    }

    /*Callback for Data/Model Model*/

}

