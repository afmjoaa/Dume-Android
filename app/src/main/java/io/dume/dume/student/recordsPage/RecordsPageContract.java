package io.dume.dume.student.recordsPage;

public interface RecordsPageContract {
    interface View {

        void configRecordsPage();

        void initRecordsPage();

        void findView();

    }

    interface Presenter {

        void recordsPageEnqueue();

        void onRecordsPageIntracted(android.view.View view);

    }

    interface Model {

        void recordsPageHawwa();
    }
}
