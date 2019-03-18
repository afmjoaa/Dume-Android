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

        void recordsPageLoadData(TeacherContract.Model.Listener<Void> listener);

        void onRecordsPageIntracted(android.view.View view);

    }

    interface Model {

        void recordsPageHawwa();

        void getRecords(TeacherContract.Model.Listener<List<Record>> listener);

        void changeRecordStatus(String recordId, String status, String rejectedBy, TeacherContract.Model.Listener<Void> listener);

        void changeRecordValues(String recordId, String key, boolean value, TeacherContract.Model.Listener<Void> listener);

        void setPenalty(String acountMajor, Integer amount, boolean ratingPenalty, String ratingVal, TeacherContract.Model.Listener<Void> listener);
    }
}
