package io.dume.dume.student.recordsPage;

import com.google.firebase.firestore.DocumentSnapshot;

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

        void changeRecordStatus(DocumentSnapshot recordId, String status, String rejectedBy, TeacherContract.Model.Listener<Void> listener);

        void changeRecordValues(String recordId, String key, Boolean value, TeacherContract.Model.Listener<Void> listener);

        void setPenalty(String acountMajor, Integer amount, TeacherContract.Model.Listener<Void> listener);
    }
}
