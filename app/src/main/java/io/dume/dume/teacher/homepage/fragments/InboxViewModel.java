package io.dume.dume.teacher.homepage.fragments;

import android.arch.lifecycle.ViewModel;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.pojo.Feedback;
import io.dume.dume.teacher.pojo.Inbox;

public class InboxViewModel extends ViewModel {

    public void getFeedBack(TeacherContract.Model.Listener listener) {
        ArrayList<Feedback> list = new ArrayList<>();
        list.add(new Feedback("1H", "Response Time"));
        list.add(new Feedback("4.5", "Ratings"));
        list.add(new Feedback("75%", "Professionalism"));
        listener.onSuccess(list);
    }


    public void getInbox(TeacherContract.Model.Listener listener) {
        ArrayList<Inbox> list = new ArrayList<>();
        list.add(new Inbox(false, "Unread Messages", 0));
        list.add(new Inbox(true, "Dume Request", 2));
        list.add(new Inbox(false, "Active Dume", 0));
        listener.onSuccess(list);
    }


    public void getChartEntry(TeacherContract.Model.Listener t) {
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1.0f, 2.0f));
        entries.add(new Entry(2.0f, 5.0f));
        entries.add(new Entry(3.0f, 4.0f));
        entries.add(new Entry(4.0f, 6.0f));
        entries.add(new Entry(6.0f, 1.0f));
        entries.add(new Entry(7.0f, 2.0f));
        List<Entry> entries1 = new ArrayList<>();
        entries1.add(new Entry(1.0f, 5.0f));
        entries1.add(new Entry(2.0f, 0.0f));
        entries1.add(new Entry(3.0f, 4.0f));
        entries1.add(new Entry(4.0f, 15.0f));
        entries1.add(new Entry(6.0f, 6.0f));
        entries1.add(new Entry(7.0f, 10.0f));

        ArrayList<List<Entry>> lists = new ArrayList<>();
        lists.add(entries);
        lists.add(entries1);
        t.onSuccess(lists);
    }
}
