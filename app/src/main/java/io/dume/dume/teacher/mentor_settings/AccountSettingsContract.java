package io.dume.dume.teacher.mentor_settings;

import android.view.View;

import java.util.ArrayList;

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
    }

    interface Presenter {
        void enqueue();

        void onViewClicked(View activityView);

        void loadData();
    }

    interface MentorModel {
        int getData();

        void getDataArray(DataListener listener);

        interface DataListener

        {
            void onSuccess(ArrayList<String> arrayList);

            void onFailure(String message);
        }
    }
}
