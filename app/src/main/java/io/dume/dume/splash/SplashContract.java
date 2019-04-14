package io.dume.dume.splash;

import android.app.Activity;

import io.dume.dume.auth.AuthGlobalContract;
import io.dume.dume.teacher.homepage.TeacherContract;

public interface SplashContract {
    interface View extends AuthGlobalContract.View {
        void foundUpdates();

        void foundErr(String msg);

        void gotoLoginActivity();
    }

    interface Model extends AuthGlobalContract.Model {
        void hasUpdate(TeacherContract.Model.Listener<Boolean> listener);
    }

    interface Presenter {
        void init(Activity myActivity);

        void enqueue();
    }

    /*Callback for Data/EditModel EditModel*/

}

