package io.dume.dume.student.recordsAccepted;

public interface RecordsAcceptedContract {
    interface View {

        void configRecordsAccepted();

        void initRecordsAccepted();

        void findView();

        void contactBtnClicked();
    }

    interface Presenter {

        void recordsAcceptedEnqueue();

        void onRecordsAcceptedIntracted(android.view.View view);

    }

    interface Model {

        void recordsAcceptedHawwa();
    }

}
