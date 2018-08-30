package io.dume.dume.student.recordsCompleted;

public interface RecordsCompletedContract {
    interface View {

        void configRecordsCompleted();

        void initRecordsCompleted();

        void findView();

    }

    interface Presenter {

        void recordsCompletedEnqueue();

        void onRecordsCompletedIntracted(android.view.View view);

    }

    interface Model {

        void recordsCompletedHawwa();
    }
}
