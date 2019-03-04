package io.dume.dume.student.recordsPending;

import io.dume.dume.teacher.homepage.TeacherContract;

public interface RecordsPendingContract {
    interface View {

        void configRecordsPending();

        void flush(String msg);

        void initRecordsPending();

        void findView();

        void showProgress();

        void hideProgress();

    }

    interface Presenter {

        void recordsPendingEnqueue();

        void onRecordsPendingIntracted(android.view.View view);

    }

    interface Model {


        void recordsPendingHawwa();
    }
}
