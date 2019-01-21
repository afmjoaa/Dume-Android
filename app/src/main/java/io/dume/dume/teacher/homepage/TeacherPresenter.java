package io.dume.dume.teacher.homepage;

import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.dume.dume.R;
import io.dume.dume.teacher.pojo.Feedback;
import io.dume.dume.teacher.pojo.Inbox;

public class TeacherPresenter implements TeacherContract.Presenter {

    private static final String TAG = TeacherPresenter.class.getSimpleName().toString();
    private TeacherContract.View view;
    private TeacherContract.Model model;
    private TeacherDataStore teachearDataStore;


    public TeacherPresenter(TeacherContract.View view, TeacherContract.Model model) {
        this.view = view;
        this.model = model;
        teachearDataStore = TeacherDataStore.getInstance();

    }

    @Override
    public void init() {
        view.init();
        view.configView();
        model.getFeedBack(new TeacherContract.Model.Listener<ArrayList<Feedback>>() {
            @Override
            public void onSuccess(ArrayList<Feedback> list) {

            }

            @Override
            public void onError(String msg) {
                view.flush(msg);
            }
        });
        model.getInbox(new TeacherContract.Model.Listener<ArrayList<Inbox>>() {
            @Override
            public void onSuccess(ArrayList<Inbox> list) {

            }

            @Override
            public void onError(String msg) {
                view.flush(msg);
            }
        });
        model.getChartEntry(new TeacherContract.Model.Listener<List<ArrayList<Entry>>>() {
            @Override
            public void onSuccess(List<ArrayList<Entry>> entrieslist) {
                List<ILineDataSet> dataSets = new ArrayList<>();
                for (int i = 0; i < entrieslist.size(); i++) {
                    LineDataSet lineDataSet = null;
                    lineDataSet = new LineDataSet(entrieslist.get(i), "Impressions");
                    lineDataSet.setDrawFilled(true);
                    lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                    lineDataSet.setFillColor(Color.RED);
                    lineDataSet.setColor(Color.RED);
                    if (i == 1) {
                        lineDataSet = new LineDataSet(entrieslist.get(i), "Views");
                        lineDataSet.setDrawFilled(true);
                        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                        lineDataSet.setFillColor(Color.MAGENTA);
                        lineDataSet.setColor(Color.MAGENTA);
                    }
                    lineDataSet.setLineWidth(0.0f);
                    lineDataSet.setDrawValues(false);
                    dataSets.add(lineDataSet);
                }
                LineData lineData = new LineData(dataSets);

            }

            @Override
            public void onError(String msg) {
                view.flush(msg);
            }
        });

    }

    @Override
    public void loadProfile(TeacherContract.Model.Listener<Void> listener) {
        model.getMendatory(new TeacherContract.Model.Listener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                view.setDocumentSnapshot(documentSnapshot);
                GeoPoint location = (GeoPoint) documentSnapshot.get("location");
                if (location == null) {
                    view.showSnackBar("Point Your Location", "Settings > Location");
                }

                final String avatar = documentSnapshot.getString("avatar");
                if (avatar != null && !avatar.equals("")) {
                    view.setAvatar(avatar);
                }
                String o = documentSnapshot.getString("last_name");
                String o1 = documentSnapshot.getString("first_name");
                view.setUserName(o1, o);
                view.setMsgName(view.generateMsgName(o1, o));
                view.setRating((Map<String, Object>) documentSnapshot.get("self_rating"));


                /*Enams Code Goes Here*/
                final Map<String, Object> selfRating = (Map<String, Object>) documentSnapshot.get("self_rating");
                teachearDataStore.setSelfRating(selfRating);
                teachearDataStore.setDocumentSnapshot(documentSnapshot);

                view.setUnreadMsg(documentSnapshot.getString("unread_msg"));
                view.setUnreadNoti(documentSnapshot.getString("unread_noti"));
                view.setUnreadRecords((Map<String, Object>) documentSnapshot.get("unread_records"));

                if (Objects.requireNonNull(documentSnapshot.getString("pro_com_%")).equals("100")) {
                    view.setProfileComPercent(documentSnapshot.getString("pro_com_%"));
                } else {
                    view.setProfileComPercent(documentSnapshot.getString("pro_com_%"));
                    view.showPercentSnackBar(documentSnapshot.getString("pro_com_%"));
                }

                listener.onSuccess(null);
            }

            @Override
            public void onError(String msg) {
                view.flush(msg);
            }
        });
    }


    @Override
    public void onButtonClicked() {


    }

    @Override
    public void onViewInteracted(View component) {
        switch (component.getId()) {
            case R.id.switch_account_btn:
                view.onSwitchAccount();
                break;
            case R.id.refer_mentor_imageView:
                view.referMentorImageViewClicked();
                break;
            case R.id.free_cashback_imageView:
                view.freeCashBackImageViewClicked();
                break;
            case R.id.enhance_skill_imageview:
                view.enhanceVIewImageClicked();
                break;
            case R.id.fab:
                view.onCenterCurrentLocation();
                break;

        }
    }


}
