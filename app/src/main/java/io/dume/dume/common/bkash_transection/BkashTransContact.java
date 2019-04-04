package io.dume.dume.common.bkash_transection;

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
    }

    interface Presenter {
        void enqueue();

        void handleTransection(Map<String, Object> transData);

    }

    interface Model {
        void pushTransection(Map<String, Object> transData, TeacherContract.Model.Listener<Void> listener);
    }

}
