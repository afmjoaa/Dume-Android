package io.dume.dume.student.recordsPending;

import android.net.Uri;

import io.dume.dume.inter_face.usefulListeners;
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

        void uploadPhotoId(Uri uri, usefulListeners.uploadToSTGListererMin progressListener);
    }
}
