package io.dume.dume.teacher.mentor_settings;

import android.view.View;

import java.util.ArrayList;

import io.dume.dume.teacher.pojo.User;

public interface AccountSettingsContract {
    interface MentorView {
        void setViewConfig();

        void showLoading();

        void hideLoading();

        void gatherDataInListView(ArrayList<String> datalist);

        void showBasicInfo();

        void hideBasicInfo();

        boolean isBasicInfoShowing();

        void toast(String toast);

        void editAccount();

        void addLocation();

        void updateLocation();

        void addAcademic();

        void setUpBadge();

        void setUpAcademic();
    }

    interface Presenter {
        void enqueue();

        void onViewClicked(View activityView);

        void loadData();
    }

    interface MentorModel {
        int getData();

        ArrayList<User> getUser(DataListener listener);

        void getDataArray(DataListener listener);

        interface DataListener

        {
            void onSuccess(ArrayList<String> arrayList);

            void onFailure(String message);
        }

        /* void getUserInfo();*/
    }
}
