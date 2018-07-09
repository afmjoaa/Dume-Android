package io.dume.dume.auth;

import android.support.annotation.Nullable;

public class AuthPresenter implements AuthContract.Presenter {

    AuthContract.View view;
    AuthContract.Model model;

    public AuthPresenter(AuthContract.View view, @Nullable AuthContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void enqueue() {
        view.findView();
        view.init();
    }
}
