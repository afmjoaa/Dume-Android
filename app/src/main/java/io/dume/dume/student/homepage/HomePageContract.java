package io.dume.dume.student.homepage;

import android.view.MenuItem;

import com.google.android.gms.location.LocationServices;

public interface HomePageContract {
    interface View {

        void onSwitchAccount();

        void configHomePage();

        void makingCallbackInterfaces();

        void init();

        void findView();

        void onCenterCurrentLocation();

        void updateProfileBadge(char character);

        void updateNotificationsBadge(int count);

        void updateChatBadge(int count);

        void updateRecordsBadge(int penCount, int acptCount, int curCount);

        void onShowBottomSheet();

        void onBottomSheetClicked();

        void gotoProfilePage();

        void gotoGrabingInfoPage();

        void gotoGrabingLocationPage();

    }

    interface Presenter {

        void homePageEnqueue();

        void onViewIntracted(android.view.View view);

        void onMenuItemInteracted(MenuItem item);

        void checkNetworkAndGps();
    }

    interface Model {

        void hawwa();
    }

    interface ParentCallback {

        void onNetworkPause();

        void onNetworkResume();

    }
}
