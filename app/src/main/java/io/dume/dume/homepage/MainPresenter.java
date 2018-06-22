package io.dume.dume.homepage;

import android.util.Log;

import io.dume.dume.model.ModelSource;

public class MainPresenter implements MainContract.Presenter, MainContract.Model.CallBack {

    private static final String TAG = MainPresenter.class.getSimpleName().toString();
    MainContract.View view;
    MainContract.Model model;

    public MainPresenter(MainContract.View view, ModelSource model) {
        this.view = view;
        this.model = model;
        init();
    }

    @Override
    public void init() {
        view.init();
        model.setListener(this);
    }


    @Override
    public void onButtonClicked() {
        int i = model.getRandomNumber();
        view.showRandomNumber(i);
    }


    /*Callback listener for DataModel*/
    @Override
    public void onSuccess() {

    }

    @Override
    public void onError(String msg) {
        Log.w(TAG, "onError: " + msg);
    }
}
