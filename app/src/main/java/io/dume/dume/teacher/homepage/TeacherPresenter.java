package io.dume.dume.teacher.homepage;

import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.dume.dume.R;
import io.dume.dume.student.common.ReviewHighlightData;
import io.dume.dume.student.homePage.adapter.HomePageRatingData;
import io.dume.dume.student.homePage.adapter.HomePageRecyclerData;
import io.dume.dume.student.recordsPage.Record;
import io.dume.dume.teacher.pojo.Feedback;
import io.dume.dume.teacher.pojo.Inbox;
import io.dume.dume.teacher.pojo.Stat;

public class TeacherPresenter implements TeacherContract.Presenter {

    private static final String TAG = TeacherPresenter.class.getSimpleName().toString();
    private TeacherContract.View view;
    private TeacherModel model;
    private TeacherDataStore teachearDataStore;


    public TeacherPresenter(TeacherContract.View view, TeacherModel model) {
        this.view = view;
        this.model = model;
        teachearDataStore = TeacherDataStore.getInstance();

    }

    @Override
    public void init() {
        view.init();
        view.configView();
        loadRating();

    }

    @Override
    public void loadProfile(TeacherContract.Model.Listener<Void> listener) {
        model.getMendatory(new TeacherContract.Model.Listener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
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
                view.setDocumentSnapshot(documentSnapshot);

                //setting the stat in teacherdataStore
                Date date = new Date();
                List<Stat> todayStatList = new ArrayList<>();
                String dailyI = documentSnapshot.getString("daily_i");
                String dailyR = documentSnapshot.getString("daily_r");
                String pDailyI = documentSnapshot.getString("p_daily_i");
                String pDailyR = documentSnapshot.getString("p_daily_r");
                Stat todayStat = new Stat(dailyI, dailyR, date, FirebaseAuth.getInstance().getUid());
                todayStatList.add(todayStat);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                todayStat = new Stat(pDailyI, pDailyR, calendar.getTime(), FirebaseAuth.getInstance().getUid());
                todayStatList.add(todayStat);
                teachearDataStore.setTodayStatList(todayStatList);

                view.setRating((Map<String, Object>) documentSnapshot.get("self_rating"));
                /*Enams Code Goes Here*/
                Map<String, Object> achievements = (Map<String, Object>) documentSnapshot.get("achievements");
                List<Boolean> badgeStatus = new ArrayList<>();
                if (achievements != null) {
                    badgeStatus.add((boolean) achievements.get("joined"));
                    badgeStatus.add((boolean) achievements.get("inaugural"));
                    badgeStatus.add((boolean) achievements.get("leading"));
                    badgeStatus.add((boolean) achievements.get("premier"));
                }
                teachearDataStore.setBadgeList(badgeStatus);
                final Map<String, Object> selfRating = (Map<String, Object>) documentSnapshot.get("self_rating");
                teachearDataStore.setSelfRating(selfRating);
                teachearDataStore.setDocumentSnapshot(documentSnapshot.getData());
                teachearDataStore.setSnapshot(documentSnapshot);
                String unread_msg = documentSnapshot.getString("unread_msg");
                view.setUnreadMsg(unread_msg);
                String unread_noti = documentSnapshot.getString("unread_noti");
                view.setUnreadNoti(unread_noti);
                Map<String, Object> unread_records = (Map<String, Object>) documentSnapshot.get("unread_records");
                view.setUnreadRecords((Map<String, Object>) unread_records);
                int badge = 0;
                String unreadMsg = (String) documentSnapshot.get("unread_msg");
                String unreadNoti = (String) documentSnapshot.get("unread_noti");
                Map<String, Object> unreadRecords = (Map<String, Object>) documentSnapshot.get("unread_records");
                String pendingCount = (String) unreadRecords.get("pending_count");
                String acceptedCount = (String) unreadRecords.get("accepted_count");
                String currentCount = (String) unreadRecords.get("current_count");
                badge = Integer.parseInt(unreadMsg) + Integer.parseInt(unreadNoti) + Integer.parseInt(pendingCount) + Integer.parseInt(acceptedCount) +
                        Integer.parseInt(currentCount);

                view.updateBadge(String.valueOf(badge));

                if (Objects.requireNonNull(documentSnapshot.getString("pro_com_%")).equals("100")) {
                    view.setProfileComPercent(documentSnapshot.getString("pro_com_%"));
                } else {
                    view.setProfileComPercent(documentSnapshot.getString("pro_com_%"));
                    view.showPercentSnackBar(documentSnapshot.getString("pro_com_%"));
                }
                loadPromo();
                listener.onSuccess(null);
            }

            @Override
            public void onError(String msg) {
                //listener.onError(msg);
                view.flush(msg);
            }
        });
    }

    @Override public void appliedPromo()
    {
        Map<String, Object> documentSnapshot = TeacherDataStore.getInstance().getDocumentSnapshot();
        ArrayList<String> applied_promo = (ArrayList<String>) documentSnapshot.get("applied_promo");
        if (applied_promo != null) {
            for (String applied : applied_promo) {
                Map<String, Object> promo_item = (Map<String, Object>) documentSnapshot.get(applied);
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(promo_item);
                HomePageRecyclerData homePageRecyclerData = gson.fromJson(jsonElement, HomePageRecyclerData.class);
                view.loadHeadsUpPromo(homePageRecyclerData);
            }

        }
    }


    public void loadPromo() {
        Map<String, Object> documentSnapshot = TeacherDataStore.getInstance().getDocumentSnapshot();

        if (documentSnapshot != null) {
            ArrayList<String> available_promo = (ArrayList<String>) documentSnapshot.get("available_promo");

            appliedPromo();

            ArrayList<String> tempList = new ArrayList<>();
            for (String promoCode : available_promo) {
                if (!tempList.contains(promoCode)) {
                    tempList.add(promoCode);
                }
            }
            available_promo = tempList;
            for (String promoCode : available_promo) {
                model.getPromo(promoCode, new TeacherContract.Model.Listener<HomePageRecyclerData>() {
                    @Override
                    public void onSuccess(HomePageRecyclerData list) {
                        view.loadPromoData(list);
                    }

                    @Override
                    public void onError(String msg) {
                        Log.w(TAG, "onError: " + msg);
                    }
                });
            }

        } else {
            view.flush("Does not found any user");
        }
    }

    public void loadRating() {
        //testing fucking code here


        if (TeacherDataStore.getInstance().getDocumentSnapshot() == null) {
            model.getMendatory(new TeacherContract.Model.Listener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    List<String> ratingArray = (List<String>) documentSnapshot.get("rating_array");
                    if (ratingArray != null && ratingArray.size() > 0) {
                        for (int i = 0; i < ratingArray.size(); i++) {
                            int finalI = i;
                            model.getSingleRecords(ratingArray.get(i), new TeacherContract.Model.Listener<Record>() {
                                @Override
                                public void onSuccess(Record record) {

                                    String t_rate_status = record.getT_rate_status();
                                    switch (t_rate_status) {
                                        case Record.DIALOG:
                                            HomePageRatingData ratingDataList = new HomePageRatingData();
                                            List<String> ratingDataItemName = new ArrayList<>();
                                            ratingDataItemName.add("Communication");
                                            ratingDataItemName.add("Behaviour");
                                            ratingDataList.setRatingNameList(ratingDataItemName);
                                            ratingDataList.setName(record.getStudentName());
                                            ratingDataList.setAvatar(record.getStudentDpUrl());
                                            view.testingCustomDialogue(ratingDataList, record);
                                            break;
                                        case Record.BOTTOM_SHEET:
                                            HomePageRatingData currentRatingDataList = new HomePageRatingData();
                                            List<String> currentRatingDataItemName = new ArrayList<>();
                                            currentRatingDataItemName.add("Communication");
                                            currentRatingDataItemName.add("Behaviour");
                                            currentRatingDataList.setRatingNameList(currentRatingDataItemName);
                                            currentRatingDataList.setName(record.getStudentName());
                                            currentRatingDataList.setAvatar(record.getStudentDpUrl());
                                            currentRatingDataList.setRecord(record);
                                            view.showSingleBottomSheetRating(currentRatingDataList);
                                            break;
                                        case Record.DONE:
                                            model.removeCompletedRating(ratingArray.get(finalI), new TeacherContract.Model.Listener<Void>() {
                                                @Override
                                                public void onSuccess(Void list) {
                                                }

                                                @Override
                                                public void onError(String msg) {
                                                    view.flush(msg);
                                                }
                                            });
                                            break;
                                    }
                                }

                                @Override
                                public void onError(String msg) {
                                    view.flush(msg);
                                }
                            });
                        }
                    }
                }

                @Override
                public void onError(String msg) {
                    view.flush(msg);
                }
            });
        } else {
            List<String> ratingArray = (List<String>) TeacherDataStore.getInstance().getDocumentSnapshot().get("rating_array");
            if (ratingArray != null && ratingArray.size() > 0) {
                for (int i = 0; i < ratingArray.size(); i++) {
                    int finalI = i;
                    model.getSingleRecords(ratingArray.get(i), new TeacherContract.Model.Listener<Record>() {
                        @Override
                        public void onSuccess(Record record) {

                            String t_rate_status = record.getT_rate_status();
                            switch (t_rate_status) {
                                case Record.DIALOG:
                                    HomePageRatingData ratingDataList = new HomePageRatingData();
                                    List<String> ratingDataItemName = new ArrayList<>();
                                    ratingDataItemName.add("Communication");
                                    ratingDataItemName.add("Behaviour");
                                    ratingDataList.setRatingNameList(ratingDataItemName);
                                    ratingDataList.setName(record.getStudentName());
                                    ratingDataList.setAvatar(record.getStudentDpUrl());
                                    view.testingCustomDialogue(ratingDataList, record);
                                    break;
                                case Record.BOTTOM_SHEET:
                                    HomePageRatingData currentRatingDataList = new HomePageRatingData();
                                    List<String> currentRatingDataItemName = new ArrayList<>();
                                    currentRatingDataItemName.add("Communication");
                                    currentRatingDataItemName.add("Behaviour");
                                    currentRatingDataList.setRatingNameList(currentRatingDataItemName);
                                    currentRatingDataList.setName(record.getStudentName());
                                    currentRatingDataList.setAvatar(record.getStudentDpUrl());
                                    currentRatingDataList.setRecord(record);
                                    view.showSingleBottomSheetRating(currentRatingDataList);
                                    break;
                                case Record.DONE:
                                    model.removeCompletedRating(ratingArray.get(finalI), new TeacherContract.Model.Listener<Void>() {
                                        @Override
                                        public void onSuccess(Void list) {
                                        }

                                        @Override
                                        public void onError(String msg) {
                                            view.flush(msg);
                                        }
                                    });
                                    break;
                            }
                        }

                        @Override
                        public void onError(String msg) {
                            view.flush(msg);
                        }
                    });
                }
            }
        }

    }

    @Override
    public void loadStat(TeacherContract.Model.Listener<List<Stat>> listener) {
        model.getStatList(new TeacherContract.Model.Listener<QuerySnapshot>() {
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
                    listener.onSuccess(stat);
                    teachearDataStore.setStat(stat);
                } else listener.onError("No review");
            }

            @Override
            public void onError(String msg) {
                listener.onError("Empty Response");
                Log.e(TAG, "onError: " + msg);
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
