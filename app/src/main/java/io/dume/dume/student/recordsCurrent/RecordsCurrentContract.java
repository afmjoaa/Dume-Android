package io.dume.dume.student.recordsCurrent;

public interface RecordsCurrentContract {
    interface View {

        void configRecordsCurrent();

        void initRecordsCurrent();

        void findView();

        void flush(String msg);

        void contactBtnClicked();
    }

    interface Presenter {

        void recordsCurrentEnqueue();

        void onRecordsCurrentIntracted(android.view.View view);

    }

    interface Model {

        void recordsCurrentHawwa();
    }
}
