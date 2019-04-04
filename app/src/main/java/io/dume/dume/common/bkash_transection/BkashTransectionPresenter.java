package io.dume.dume.common.bkash_transection;

import android.util.Log;

import java.util.Map;

import io.dume.dume.teacher.homepage.TeacherContract;

public class BkashTransectionPresenter implements BkashTransContact.Presenter {

    private BkashTransContact.View view;
    private BkashTransContact.Model model;
    private static final String TAG = "BkashTransectionPresent";
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

        model.isTransectionIdExists(transData.get("transection_id").toString(), new TeacherContract.Model.Listener<Boolean>() {
            @Override
            public void onSuccess(Boolean yes) {

                Log.w(TAG, "onSuccess: "+ yes );
                if (!yes) {
                    model.pushTransection(transData, new TeacherContract.Model.Listener<Void>() {
                        @Override
                        public void onSuccess(Void list) {
                            view.stopLoad();
                            view.onVerificaitonProgress("Dummy");
                            view.setEnable();
                        }

                        @Override
                        public void onError(String msg) {
                            view.flush(msg);
                            view.stopLoad();
                            view.setEnable();
                        }
                    });
                } else {
                    view.stopLoad();
                    view.setError("Transection Id Already Used");
                }
            }

            @Override
            public void onError(String msg) {
                view.stopLoad();
                view.flush(msg);
            }
        });


    }


}
