package io.dume.dume.student.grabingLocation;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Map;

import io.dume.dume.student.studentSettings.SavedPlacesAdaData;
import io.dume.dume.teacher.homepage.TeacherContract;

public interface GrabingLocaitonContract {
    interface View {

        void configGrabingLocationPage();

        void initGrabingLocationPage();

        void findView();

        void makingCallbackInterfaces();

        void onCenterCurrentLocation();

        void onDiscardSearchClicked();

        void onLocationDoneBtnClicked();

        GeoPoint getCurrentAddress();

        Map<String, Object> getHomeAddress();

        Map<String, Object> getWorkAddress();

        ArrayList< Map<String, Object>> getSavedPlaces();

        ArrayList< Map<String, Object>> getBackInTime();

        void setCurrentAddress(GeoPoint currentAddress);

        void setHomeAddress(Map<String, Object> homeAddress);

        void setWorkAddress(Map<String, Object> workAddress);

        void setSavedPlaces(ArrayList< Map<String, Object>> savedPlaces);

        void setBackInTimePlaces(ArrayList< Map<String, Object>> backInTimePlaces);

        void flush(String msg);

        void retriveSavedData();

        SavedPlacesAdaData generateCAAdapterData(GeoPoint geoPoint);

        void hackSetLocaOnMapClicked();

        void setDocumentSnapshot(DocumentSnapshot documentSnapshot);

        void updateViewOnMatch(String s);

        boolean checkIfInDB(GeoPoint geoPoint);
    }

    interface Presenter {

        void grabingLocationPageEnqueue();

        void onGrabingLocationViewIntracted(android.view.View view);

        void retriveSavedPlacesData(TeacherContract.Model.Listener<DocumentSnapshot> listener);

    }

    interface Model {

        void addShapShotListener(EventListener<DocumentSnapshot> updateViewListener);

        void grabingLocationPagehawwa();

        void updateFavoritePlaces(String identify, Map<String, Object> savedPlacesAdaData, TeacherContract.Model.Listener<Void> listener);

        void updatePermanentAddress(GeoPoint point, TeacherContract.Model.Listener listener);

        void updateRecentPlaces(String identify, Map<String, Object> savedPlacesAdaData, TeacherContract.Model.Listener<Void> listener);
    }
}
