package io.dume.dume.teacher.mentor_settings.basicinfo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;

import java.util.List;

import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.pojo.Academic;

public class EditContract {
    interface View {
        void configureView();

        @NonNull
        List<Academic> getAcademics(DocumentSnapshot documentSnapshot);

        void configureCallback();

        void snakbar(String msg);

        void toast(String toast);

        String firstName();

        String lastName();

        String maritalStatus();

        String gmail();

        String getLocation();

        String religion();

        String gender();

        String phone();

        String getAvatarUrl();

        GeoPoint getCurrentAddress();

        String getBirthDate();

        String getProfileComPercent();

        String getCurrentStatus();

        void enableLoad();

        void disableLoad();

        void setCurrentAddress(GeoPoint geoPoint);

        Context getActivtiyContext();

        void setImage(Uri uri);

        void setAvatarUrl(String url);

        void discardDialogue();

        void onGenderClicked();

        void onMaritalStatusClicked();

        void onReligionClicked();

        void onBirthDateClicked();

        void invalidFound(String Name);

        String generatePercent();

        void setProfileComPercent(String num);

        void setCurrentStatus(String currentStatus);

        void initProfileCompleteView();

        void addQualifiaction();

        void modifyQualification();

        void onDataLoad(DocumentSnapshot documentSnapshot);

        void someThingChanged(boolean b);

        void updateAcademics(DocumentSnapshot documentSnapshot);
    }

    interface Presenter {
        void enqueue();

        void onClick(android.view.View view);

        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

    interface Model {
        void synWithDataBase(String first, String last, String avaterUrl, String email, String gender, String phone, String religion, String marital, String birth_date, GeoPoint location, String currentStatus, String comPercent);

        void updatePercentage(String percent);

        void setListener(onDataUpdate listener);

        void uploadImage(Uri uri, DownloadListener progressListener);

        void getDocumentSnapShot(TeacherContract.Model.Listener<DocumentSnapshot> listener);

    }

    interface onDataUpdate {
        void onUpdateSuccess();

        void onFail(String error);

    }

    interface DownloadListener {
        void onSuccess(String url);

        void onFail(String msg);
    }
}
