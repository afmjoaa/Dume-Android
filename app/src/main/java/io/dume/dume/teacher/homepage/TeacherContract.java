package io.dume.dume.teacher.homepage;

import android.view.View;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;

import java.util.ArrayList;

import io.dume.dume.teacher.pojo.Feedback;
import io.dume.dume.teacher.pojo.Inbox;

public interface TeacherContract {


    interface View {
        void init();

        void onSwitchAccount();

        void flush(String msg);

        void showFeedbackRV(ArrayList<Feedback> list);

        void showInboxRV(ArrayList<Inbox> list);

        void showChart(LineData data);


        void onSheetChanges(boolean halfCrossed);

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

        interface Listener<T> {
            void onSuccess(T list);

            void onError(String msg);
        }

    }

}
