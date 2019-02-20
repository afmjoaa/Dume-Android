package io.dume.dume.student.recordsPage;

import java.util.List;

import io.dume.dume.teacher.homepage.TeacherContract;

public interface RecordsPageContract {
    interface View {

        void configRecordsPage();

        void initRecordsPage();

        void findView();

        void onDataLoadFinsh();

        void load();

        void stopLoad();
        void flush(String msg);

    }

    interface Presenter {

        void recordsPageEnqueue();

        void onRecordsPageIntracted(android.view.View view);

    }

    interface Model {

        void recordsPageHawwa();

        void getRecords(TeacherContract.Model.Listener<List<Record>> listener);

        void changeRecordStatus(String recordId, String status, TeacherContract.Model.Listener<Void> listener);
    }
}
