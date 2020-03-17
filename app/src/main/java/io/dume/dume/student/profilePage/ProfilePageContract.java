package io.dume.dume.student.profilePage;

import android.net.Uri;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.GeoPoint;

import io.dume.dume.interfaces.usefulListeners;

public interface ProfilePageContract {
    interface View {

        void configProfilePage();

        void initProfilePage();

        void findView();

        void onGenderClicked();

        void onCurrentAddressClicked();

        void onPreviousResultClicked();

        String getFirstName();

        String getLastName();

        String getGmail();

        String getGender();

        String getPhone();

        String getCurrentStatus();

        String getPreviousResult();

        String getAvatarString();

        Uri getAvatarUri();

        String getProfileComPercent();

        GeoPoint getCurrentAddress();

        //setters
        void setUserName(String last, String first);

        void setPhone(String phone);

        void setFirstName(String first);

        void setLastName(String last);

        void setGmail(String gmail);

        void setCurrentAddress(GeoPoint geoPoint);

        void setCurrentStatus(String currentStatus);

        void setPreviousResult(String previousResult);

        void setGender(String gender);

        void setAvatar(String uri);

        void setAvatarString(String uri);

        void setProfileComPercent(String num);

        void flush(String msg);

        void discardChangesClicked();

        void updateChangesClicked();

        void hideSpiner();

        void showSpiner();

        void goBack();

        void initProfileCompleteView();

        boolean checkIfValidUpdate();

        void showInvalideInfo();
    }

    interface Presenter {

        void profilePageEnqueue();

        void onProfileViewIntracted(android.view.View view);

    }

    interface Model {

        void setInstance(ProfilePageModel mModel);

        //stage the data and call the base update method
        Boolean synWithDataBase(String fn, String ln, String mail, GeoPoint ca, String cs, String pr,String gender, String progress, String avatar, usefulListeners.uploadToDBListerer progressListener);

        void addShapShotListener(EventListener<DocumentSnapshot> updateViewListener);

        void uploadImage(Uri uri, usefulListeners.uploadToSTGListererMin progressListener);

    }
}
