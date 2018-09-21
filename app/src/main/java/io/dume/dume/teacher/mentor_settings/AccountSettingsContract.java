package io.dume.dume.teacher.mentor_settings;

import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import io.dume.dume.inter_face.UserQueryListener;
import io.dume.dume.teacher.pojo.Education;
import io.dume.dume.teacher.pojo.GlobalListener;

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

        void updateUserInfo(Map<String, Object> data);

        void updatAcademicList(ArrayList<Education> arrayList);
    }

    interface Presenter {
        void enqueue();

        void onViewClicked(View activityView);

        void loadData();
    }

    interface MentorModel {
        int getData();

        void queryUserData(UserQueryListener listener);

        void queryAcademicData(GlobalListener.AcademicQuery listener);

        void getDataArray(DataListener listener);

        interface DataListener

        {
            void onSuccess(ArrayList<String> arrayList);

            void onFailure(String message);
        }

        /* void getUserInfo();*/
    }
}
