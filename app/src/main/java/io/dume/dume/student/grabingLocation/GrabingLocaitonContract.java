package io.dume.dume.student.grabingLocation;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Map;

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
    }

    interface Presenter {

        void grabingLocationPageEnqueue();

        void onGrabingLocationViewIntracted(android.view.View view);

    }

    interface Model {

        void addShapShotListener(EventListener<DocumentSnapshot> updateViewListener);

        void grabingLocationPagehawwa();
    }
}
