package io.dume.dume.splash;

import javax.sql.DataSource;

import io.dume.dume.teacher.model.ModelSource;

public class AuthModel implements SplashContract.Auth {


    @Override
    public boolean isUserLoggedIn() {
        return false;
    }

    @Override
    public void onAccountTypeFound(SplashContract.AuthCallbackListener listener) {
        listener.onTeacherFound();
    }
}
