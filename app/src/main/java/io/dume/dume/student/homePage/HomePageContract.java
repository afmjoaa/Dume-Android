package io.dume.dume.student.homePage;

import android.view.MenuItem;

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

        void gotoHeatMapActivity();

        void gotoRecordsPage();
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