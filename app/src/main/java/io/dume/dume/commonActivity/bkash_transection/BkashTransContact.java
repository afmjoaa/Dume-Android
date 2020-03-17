package io.dume.dume.commonActivity.bkash_transection;

import java.util.Map;

import io.dume.dume.teacher.homepage.TeacherContract;

public interface BkashTransContact {

    interface View {
        void init();

        void flush(String msg);

        void onVerificaitonProgress(String msg);

        void load();

        void stopLoad();

        void setEnable();

        void setError(String error);
    }

    interface Presenter {
        void enqueue();

        void handleTransection(Map<String, Object> transData);

    }

    interface Model {
        void pushTransection(Map<String, Object> transData, TeacherContract.Model.Listener<Void> listener);

        void isTransectionIdExists(String id, TeacherContract.Model.Listener<Boolean> listener);
    }

}
