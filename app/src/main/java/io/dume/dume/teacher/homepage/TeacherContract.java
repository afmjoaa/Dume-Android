package io.dume.dume.teacher.homepage;

import android.view.View;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.dume.dume.student.homePage.adapter.HomePageRatingData;
import io.dume.dume.student.homePage.adapter.HomePageRecyclerData;
import io.dume.dume.student.recordsPage.Record;
import io.dume.dume.teacher.pojo.Feedback;
import io.dume.dume.teacher.pojo.Inbox;
import io.dume.dume.teacher.pojo.Stat;

public interface TeacherContract {


    interface View {
        void loadPromoData(HomePageRecyclerData promoData);

        void loadHeadsUpPromo(HomePageRecyclerData promoData);

        void updateBadge(String badgeNumber);

        void init();

        void onSwitchAccount();

        void flush(String msg);

        void onCenterCurrentLocation();

        void configView();

        void referMentorImageViewClicked();

        void freeCashBackImageViewClicked();

        void enhanceVIewImageClicked();

        void showSnackBar(String messages, String actionName);

        boolean isDialogShowing();

        void showPaymentDialogue();

        //testing the customDialogue
        void testingCustomDialogue(HomePageRatingData myData, Record record);

        void setDocumentSnapshot(DocumentSnapshot documentSnapshot);

        void switchStatus(boolean active);

        //getters

        String getAvatarString();

        Map<String, Object> getSelfRating();

        Map<String, Object> getUnreadRecords();

        String unreadMsg();

        String unreadNoti();

        String getProfileComPercent();

        ArrayList<String> getAppliedPromo();

        ArrayList<String> getAvailablePromo();

        String generateMsgName(String last, String first);

        String getUserName();

        //setters

        void setUserName(String last, String first);

        void setAvatar(String avatarString);

        void setRating(Map<String, Object> selfRating);

        void setMsgName(String msgName);

        void setAvailablePromo(ArrayList<String> availablePromo);

        void setAppliedPromo(ArrayList<String> appliedPromo);

        void setProfileComPercent(String num);

        void setUnreadMsg(String unreadMsg);

        void setUnreadNoti(String unreadNoti);

        void setUnreadRecords(Map<String, Object> unreadRecords);

        void setAvatarForMenu(String avatar);

        void showPercentSnackBar(String string);

        void gotoBootCampAddvertise();

        void gotoStudentHomePage();

        void gotoBootCamPHomePage();

        void gotoProfilePage();

        void switchProfileDialog(String identify);

        void showProgressTwo();

        void hideProgressTwo();

        void showSingleBottomSheetRating(HomePageRatingData currentRatingDataList);

        void updateAccountActive(boolean acountActive);
    }


    interface Presenter {
        void appliedPromo(Map<String, Object> documentSnapshot);

        void loadPromo();

        void loadStat(Model.Listener<List<Stat>> listener);

        void onButtonClicked();

        void onViewInteracted(android.view.View view);

        void init();

        void loadProfile(Model.Listener<Void> nah);
    }

    interface Model {
        /**
         * it returns a random number bound of 5000
         *
         * @return int
         */
        void getFeedBack(Listener listener);

        void getInbox(Listener listener);

        void getChartEntry(TeacherContract.Model.Listener<List<ArrayList<Entry>>> listener);


        void getMendatory(Listener<DocumentSnapshot> listener);

        void getStatList(Listener<QuerySnapshot> listener);


        interface Listener<T> {
            void onSuccess(T list);

            void onError(String msg);
        }

    }

}
