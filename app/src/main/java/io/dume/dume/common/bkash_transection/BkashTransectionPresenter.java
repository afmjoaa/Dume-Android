package io.dume.dume.common.bkash_transection;

import java.util.Map;

import io.dume.dume.teacher.homepage.TeacherContract;

public class BkashTransectionPresenter implements BkashTransContact.Presenter {

    private BkashTransContact.View view;
    private BkashTransContact.Model model;

    BkashTransectionPresenter(BkashTransContact.View view, BkashTransContact.Model model) {
        this.view = view;
        this.model = model;
    }


    @Override
    public void enqueue() {
        view.init();
    }

    @Override
    public void handleTransection(Map<String, Object> transData) {
        view.load();
        model.pushTransection(transData, new TeacherContract.Model.Listener<Void>() {
            @Override
            public void onSuccess(Void list) {
                view.stopLoad();
                view.onVerificaitonProgress("Dummy");
            }

            @Override
            public void onError(String msg) {
                view.flush(msg);
                view.stopLoad();

            }
        });
    }


}
