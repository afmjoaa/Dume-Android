package io.dume.dume.teacher.homepage;

import android.util.Log;

import io.dume.dume.teacher.model.ModelSource;

public class TeacherPresenter implements TeacherContract.Presenter, TeacherContract.Model.CallBack {

    private static final String TAG = TeacherPresenter.class.getSimpleName().toString();
    TeacherContract.View view;
    TeacherContract.Model model;

    public TeacherPresenter(TeacherContract.View view, ModelSource model) {
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
