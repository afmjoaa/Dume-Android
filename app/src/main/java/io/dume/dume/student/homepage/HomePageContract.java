package io.dume.dume.student.homepage;

import android.view.MenuItem;

public interface HomePageContract {
    interface View {

        void onSwitchAccount();

        void configHomePage();

        void init();

        void findView();

        void onCenterCurrentLocation();

        void updateProfileBadge(char character);

        void updateNotificationsBadge(int count);

        void updateChatBadge(int count);

        void updateRecordsBadge(int penCount, int acptCount, int curCount);

        void onShowBottomSheet();

        void onBottomSheetClicked();
    }

        interface Presenter {

            void homePageEnqueue();

            void onViewIntracted(android.view.View view);

            void onMenuItemInteracted(MenuItem item);
        }

        interface Model {

            void hawwa();
        }
    }
