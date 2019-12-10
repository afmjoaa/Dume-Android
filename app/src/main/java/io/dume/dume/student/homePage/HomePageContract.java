package io.dume.dume.student.homePage;

import android.view.MenuItem;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;

import java.util.ArrayList;
import java.util.Map;

import io.dume.dume.student.homePage.adapter.HomePageRatingData;
import io.dume.dume.student.homePage.adapter.HomePageRecyclerData;
import io.dume.dume.student.recordsPage.Record;
import io.dume.dume.teacher.homepage.TeacherContract;

public interface HomePageContract {


    interface View {

        void onSwitchAccount();

        void configHomePage();

        void makingCallbackInterfaces();

        void setHeadsUpPromo(String discount, String dayLeft, String packageName);

        void init();

        void loadHeadsUpPromo(HomePageRecyclerData promoData);

        void loadPromoData(HomePageRecyclerData promoList);

        void findView();

        void onCenterCurrentLocation();

        void updateProfileBadge(char character);

        void updateNotificationsBadge(int count);

        void updateChatBadge(int count);

        void updateRecordsBadge(int penCount, int acptCount, int curCount);

        void gotoProfilePage();

        void gotoGrabingInfoPage();

        void gotoGrabingLocationPage();

        void gotoHeatMapActivity();

        void gotoRecordsPage();

        void gotoSettingActivity();

        void gotoHelpActivity();

        void gotoPaymentActivity();

        void gotoInboxActivity();

        void gotoAboutUsActivity();

        void gotoPrivacyPolicyActivity();

        void gotoNotificationTab();

        void referMentorImageViewClicked();

        void freeCashBackImageViewClicked();

        void startMentoringImageViewClicked();



        //testing the customDialogue
        void testingCustomDialogue(HomePageRatingData myData, Record record);

        void gotoMentorProfile();

        void gotoStudentProfile();

        void flush(String msg);

        void setDocumentSnapshot(DocumentSnapshot documentSnapshot);

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

        void showSnackBar(String completePercent);

        void switchProfileDialog(String identify);

        void showPercentSnak(String message, String actionName);

        void showProgressTwo();

        void hideProgressTwo();

        void gotoTestingActivity();

        void gotoBootCampActivity();

        boolean checkNull();

        void initRecentSearchRecycler(DocumentSnapshot documentSnapshot);

        void showSingleBottomSheetRating(HomePageRatingData currentRatingDataList);

        void searchFilterClicked();
    }

    interface Presenter {

        void homePageEnqueue();

        void onViewIntracted(android.view.View view);

        void onMenuItemInteracted(MenuItem item);

        void checkNetworkAndGps();

        void defaultOptionMenuCreated();


        void appliedPromo(DocumentSnapshot documentSnapshot);

        void getDataFromDB();
    }

    interface Model {

        void applyPromo(HomePageRecyclerData promoData, String promo_code, String accountType, TeacherContract.Model.Listener<String> listener);

        void updatePromo(HomePageRecyclerData promoData, TeacherContract.Model.Listener<String> listener);

        void removeAppliedPromo(HomePageRecyclerData promoData, TeacherContract.Model.Listener<Boolean> listener);

        void hawwa();


        void getPromo(String promoCode, TeacherContract.Model.Listener<HomePageRecyclerData> listener);

        void submitRating(String record_id, String skill_id, String opponent_uid, String myAccountType, Map<String, Boolean> inputRating, Float inputStar, String feedbackString, TeacherContract.Model.Listener<Void> listener);

        void getSingleRecords(String recordId, TeacherContract.Model.Listener<Record> listener);

        void addShapShotListener(EventListener<DocumentSnapshot> updateViewListener);

        void removeCompletedRating(String identify, TeacherContract.Model.Listener<Void> listener);
    }

    interface ParentCallback {

        void onNetworkPause();

        void onNetworkResume();

    }
}
