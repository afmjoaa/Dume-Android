package io.dume.dume.presenter;

import android.util.Log;

import io.dume.dume.interfaces.Model;
import io.dume.dume.interfaces.Views;
import io.dume.dume.interfaces.Presenter;
import io.dume.dume.model.ModelSource;

public class PresenterMainActivity implements Presenter.MainActivityPresenter, Model.StringData.DataListener {

    private static final String TAG = PresenterMainActivity.class.getSimpleName().toString();
    Views.MainActivityView view;
    Model.StringData model;

    public PresenterMainActivity(Views.MainActivityView view, ModelSource model) {
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
