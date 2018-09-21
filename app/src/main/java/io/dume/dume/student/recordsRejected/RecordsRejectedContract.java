package io.dume.dume.student.recordsRejected;

public interface RecordsRejectedContract {
    interface View {

        void configRecordsRejected();

        void initRecordsRejected();

        void findView();

    }

    interface Presenter {

        void recordsRejectedEnqueue();

        void onRecordsRejectedIntracted(android.view.View view);

    }

    interface Model {

        void recordsRejectedHawwa();
    }
}
