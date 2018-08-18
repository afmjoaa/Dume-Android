package io.dume.dume.splash;

import android.app.Activity;

import io.dume.dume.auth.AuthGlobalContract;

public interface SplashContract {
    interface View extends AuthGlobalContract.View {
        void gotoLoginActivity();
    }

    interface Model extends AuthGlobalContract.Model {

    }

    interface Presenter {
        void init(Activity myActivity);
        void enqueue();
    }

    /*Callback for Data/EditModel EditModel*/

}

