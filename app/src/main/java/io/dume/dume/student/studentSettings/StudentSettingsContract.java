package io.dume.dume.student.studentSettings;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.GeoPoint;

import io.dume.dume.teacher.homepage.TeacherContract;

public interface StudentSettingsContract {
    interface View {

        void configStudentSettings();

        void initStudentSettings();

        void findView();

        void gotoProfilePage();

        void flush(String msg);

        void fabClicked();

        void setUserName(String name);

        void setAvatar(String avatarString);

        void setPhoneNum(String phoneNum);

        void setEmail(String email);

    }

    interface Presenter {

        void studentSettingsEnqueue();

        void onStudentSettingsIntracted(android.view.View view);

        void retriveSavedPlacesData(TeacherContract.Model.Listener<DocumentSnapshot> listener);
    }

    interface Model {

        void studentSettingshawwa();

        void addShapShotListener(EventListener<DocumentSnapshot> updateViewListener);

    }
}
