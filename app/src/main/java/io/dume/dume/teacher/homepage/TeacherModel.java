package io.dume.dume.teacher.homepage;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import io.dume.dume.student.homePage.HomePageModel;
import io.dume.dume.teacher.pojo.Feedback;
import io.dume.dume.teacher.pojo.Inbox;
import io.dume.dume.teacher.pojo.Stat;

public class TeacherModel extends HomePageModel implements TeacherContract.Model {
    private final TeacherDataStore teacherDataStore;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private static final String TAG = "TeacherModel";

    public TeacherModel(Context context) {
        super((Activity) context, context);
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        teacherDataStore = TeacherDataStore.getInstance();
    }

    @Override
    public void getFeedBack(Listener listener) {
        ArrayList<Feedback> list = new ArrayList<>();
        list.add(new Feedback("1H", "Response Time"));
        list.add(new Feedback("4.5", "Ratings"));
        list.add(new Feedback("75%", "Professionalism"));
        listener.onSuccess(list);
    }

    @Override
    public void getInbox(Listener listener) {
        ArrayList<Inbox> list = new ArrayList<>();
        list.add(new Inbox(false, "Unread Messages", 0));
        list.add(new Inbox(true, "Dume Request", 2));
        list.add(new Inbox(false, "Active Dume", 0));
        listener.onSuccess(list);
    }

    @Override
    public void getChartEntry(TeacherContract.Model.Listener<List<ArrayList<Entry>>> listener) {
        if (teacherDataStore.getStat() == null) {
            getStatList(new TeacherContract.Model.Listener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot list) {
                    List<DocumentSnapshot> documentSnapshots = list.getDocuments();
                    List<Stat> stat = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        DocumentSnapshot document = documentSnapshots.get(i);
                        Stat currentStat = document.toObject(Stat.class);
                        if (currentStat != null) {
                            currentStat.setIdentify(i);
                        }
                        stat.add(currentStat);
                    }
                    if (list.size() > 0) {
                        teacherDataStore.setStat(stat);
                        //listener.onSuccess(stat);
                        //TODO make arraylist of entry here
                        ArrayList<Entry> entries = new ArrayList<>();
                        ArrayList<Entry> entries1 = new ArrayList<>();
                        for (int i = 1; i < 31; i++) {
                            if (stat.size() >= i) {
                                entries.add(new Entry(i, Float.parseFloat(stat.get(i - 1).getRequest_i())));
                                entries1.add(new Entry(i, Float.parseFloat(stat.get(i - 1).getRequest_r())));
                            } else {
                                entries.add(new Entry(i, 0));
                                entries1.add(new Entry(i, 0));

                            }
                        }

                        List<ArrayList<Entry>> lists = new ArrayList<>();
                        lists.add(entries1);
                        lists.add(entries);
                        listener.onSuccess(lists);


                    } else listener.onError("No review");
                }

                @Override
                public void onError(String msg) {
                    listener.onError("Empty Response");
                    Log.e(TAG, "onError: " + msg);
                }
            });

        } else {
            List<Stat> stat = teacherDataStore.getStat();
            //TOdo same thing here as well
            ArrayList<Entry> entries = new ArrayList<>();
            ArrayList<Entry> entries1 = new ArrayList<>();
            for (int i = 1; i < 31; i++) {
                if (stat.size() >= i) {
                    entries.add(new Entry(i, Float.parseFloat(stat.get(i - 1).getRequest_i())));
                    entries1.add(new Entry(i, Float.parseFloat(stat.get(i - 1).getRequest_r())));
                } else {
                    entries.add(new Entry(i, 0));
                    entries1.add(new Entry(i, 0));

                }
            }
            List<ArrayList<Entry>> lists = new ArrayList<>();
            lists.add(entries1);
            lists.add(entries);
            listener.onSuccess(lists);
        }
    }

    @Override
    public void getMendatory(Listener<DocumentSnapshot> listener) {
        firestore.document("/users/mentors/mentor_profile/" + FirebaseAuth.getInstance().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    listener.onSuccess(documentSnapshot);
                } else {
                    if (e != null) {
                        listener.onError("Network Error 101 ");
                    }
                }
            }
        });
    }


    @Override
    public void getStatList(Listener<QuerySnapshot> listener) {
        firestore.collection("/stat/").whereEqualTo("uid", FirebaseAuth.getInstance().getUid()).orderBy("creation", Query.Direction.DESCENDING).limit(30).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    listener.onSuccess(queryDocumentSnapshots);
                } else {
                    if (e != null) {
                        listener.onError(e.getLocalizedMessage());
                    }
                }
            }

        });
    }
}
