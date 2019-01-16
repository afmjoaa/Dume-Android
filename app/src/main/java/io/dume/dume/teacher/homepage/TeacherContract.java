package io.dume.dume.teacher.homepage;

import android.view.View;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

import io.dume.dume.teacher.pojo.Feedback;
import io.dume.dume.teacher.pojo.Inbox;

public interface TeacherContract {


    interface View {
        void init();

        void onSwitchAccount();

        void flush(String msg);

        void onCenterCurrentLocation();

        void configView();

        void referMentorImageViewClicked();

        void freeCashBackImageViewClicked();

        void enhanceVIewImageClicked();

        void testingCustomDialogue();

         void showSnackBar(String messages, String actionName) ;

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

        void showPercentSnackBar(String string);
    }


    interface Presenter {
        void onButtonClicked();

        void onViewInteracted(android.view.View view);

        void init();
    }

    interface Model {
        /**
         * it returns a random number bound of 5000
         *
         * @return int
         */
        void getFeedBack(Listener listener);

        void getInbox(Listener listener);

        void getChartEntry(Listener t);


        void getMendatory(Listener<DocumentSnapshot> listener);


        interface Listener<T> {
            void onSuccess(T list);

            void onError(String msg);
        }

    }

}
