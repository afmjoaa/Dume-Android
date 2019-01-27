package io.dume.dume.student.homePage;

import android.view.MenuItem;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;

import java.util.ArrayList;
import java.util.Map;

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

        void gotoProfilePage();

        void gotoGrabingInfoPage();

        void gotoGrabingLocationPage();

        void gotoHeatMapActivity();

        void gotoRecordsPage();

        void gotoSettingActivity();

        void gotoHelpActivity();

        void gotoPaymentActivity();

        void gotoInboxActivity();

        void gotoFreeCashBackActivity();

        void gotoAboutUsActivity();

        void gotoPrivacyPolicyActivity();

        void gotoMentorAddvertise();

        void gotoBootCampHomePage();

        void gotoNotificationTab();

        void referMentorImageViewClicked();

        void freeCashBackImageViewClicked();

        void startMentoringImageViewClicked();

        void testingCustomDialogue();

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

        void gotoTestingActivity();

        boolean checkNull();

        void initRecentSearchRecycler(DocumentSnapshot documentSnapshot);
    }

    interface Presenter {

        void homePageEnqueue();

        void onViewIntracted(android.view.View view);

        void onMenuItemInteracted(MenuItem item);

        void checkNetworkAndGps();

        void defaultOptionMenuCreated();

        void getDataFromDB();
    }

    interface Model {

        void hawwa();

        void addShapShotListener(EventListener<DocumentSnapshot> updateViewListener);
    }

    interface ParentCallback {

        void onNetworkPause();

        void onNetworkResume();

    }
}
