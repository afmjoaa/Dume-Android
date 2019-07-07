package io.dume.dume.student.homePage;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.dume.dume.R;
import io.dume.dume.model.DumeModel;
import io.dume.dume.student.homePage.adapter.HomePageRatingData;
import io.dume.dume.student.homePage.adapter.HomePageRecyclerData;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.student.recordsPage.Record;
import io.dume.dume.teacher.homepage.TeacherActivtiy;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.homepage.TeacherDataStore;
import io.dume.dume.util.DumeUtils;

public class HomePagePresenter implements HomePageContract.Presenter {

    private static final String TAG = "homePageActivity";
    private HomePageContract.View mView;
    private HomePageContract.Model mModel;
    private Context context;
    private Activity activity;
    private int mNotificationsCount = 0;
    private char mProfileChar = '!';
    private int mChatCount = 0;
    private int mRecPendingCount = 0, mRecAcceptedCount = 0, mRecCurrentCount = 0;
    private Map<String, Object> documentSnapshot;
    private int percentage;
    private static String FACEBOOK_URL = "https://www.facebook.com/groups/1623868617935891/";
    private static String FACEBOOK_PAGE_ID = "1623868617935891";

    //method to get the right URL to use in the intent
    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

    public HomePagePresenter(Context context, HomePageContract.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (HomePageContract.View) context;
        this.mModel = mModel;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void homePageEnqueue() {
        mView.findView();
        mView.init();
        mView.makingCallbackInterfaces();
        mView.configHomePage();
        getDataFromDB();
    }

    protected boolean isProfileOK() {
        documentSnapshot = SearchDataStore.getInstance().getDocumentSnapshot();

        if (documentSnapshot != null) {
            String beh = (String) documentSnapshot.get("pro_com_%");
            percentage = Integer.parseInt(beh);
            if (percentage >= 90) {
                return true;
            }

        }
        mView.flush("Profile should be at least 90% completed");
        String snackString = "Profile only " + percentage + "% complete";
        mView.showPercentSnak(snackString, "GO TO PROFILE");
        return false;

    }

    @Override
    public void onViewIntracted(View view) {
        switch (view.getId()) {
            case R.id.switch_account_btn:
                mView.onSwitchAccount();
                break;
            case R.id.user_dp:
                break;
            case R.id.fab:
                mView.onCenterCurrentLocation();
                break;
            case R.id.search_mentor_btn:
            case R.id.search_mentor_btn_nogps:
                if (isProfileOK()) {
                    mView.gotoGrabingLocationPage();
                }
                //mView.gotoGrabingInfoPage();
                break;
            case R.id.refer_mentor_imageView:
                mView.referMentorImageViewClicked();
                break;
            case R.id.free_cashback_imageView:
                mView.freeCashBackImageViewClicked();
                break;
            case R.id.start_mentoring_imageView:
                mView.startMentoringImageViewClicked();
                break;
            //temporary code goes here
            case R.id.promotion_validity_text:
                break;

            case R.id.search_filter_image_view:
                mView.searchFilterClicked();
                break;

        }

    }


    @Override
    public void onMenuItemInteracted(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.student:
                mView.gotoStudentProfile();
                break;
            case R.id.mentor:
                mView.switchProfileDialog(DumeUtils.TEACHER);
                break;
            case R.id.boot_camp:
                //add boot camp here
                //mView.switchProfileDialog(DumeUtils.BOOTCAMP);
                mView.gotoBootCampActivity();
                break;
            case R.id.al_display_pic:
                mView.gotoProfilePage();
                break;
            case R.id.al_records:
                mView.gotoRecordsPage();
                break;
            case R.id.al_messages:
                mView.gotoInboxActivity();
                break;
            case R.id.al_notifications:
                mView.gotoNotificationTab();
                break;
            case R.id.heat_map:
                mView.gotoHeatMapActivity();
                break;
            case R.id.settings:
                mView.gotoSettingActivity();
                break;
            case R.id.help:
                mView.gotoHelpActivity();
                break;
            case R.id.payments:
                mView.gotoPaymentActivity();
                break;
            case R.id.forum:
                //TODO
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(context);
                facebookIntent.setData(Uri.parse(facebookUrl));
                context.startActivity(facebookIntent);
                //Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.messages:
                mView.gotoInboxActivity();
                break;
            case R.id.notifications:
                //will be added later
                mView.gotoNotificationTab();
                break;
            case R.id.free_cashback:
                mView.gotoFreeCashBackActivity();
                break;
            case R.id.about_us:
                mView.gotoAboutUsActivity();
                break;
            case R.id.privacy_policy:
                mView.gotoPrivacyPolicyActivity();
                break;
            case R.id.records:
                mView.gotoRecordsPage();
                break;

        }

    }

    @Override
    public void checkNetworkAndGps() {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            Log.e(TAG, "checkNetworkAndGps: ", ex);
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            Log.e(TAG, "checkNetworkAndGps: ", ex);
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage(context.getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(context.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton(context.getString(R.string.Cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        }
    }

    @Override
    public void defaultOptionMenuCreated() {
        mModel.addShapShotListener((documentSnapshot, e) -> {
            if (documentSnapshot != null) {
                final String avatar = documentSnapshot.getString("avatar");
                if (avatar != null && !avatar.equals("")) {
                    mView.setAvatarForMenu(avatar);
                }

            } else {
                mView.flush("Does not found any user");
                Log.w(TAG, "onAccountTypeFound: document is not null");
            }
        });
    }


    @Override
    public void appliedPromo(DocumentSnapshot documentSnapshot) {
        ArrayList<String> applied_promo = null;
        try {
            applied_promo = (ArrayList<String>) documentSnapshot.get("applied_promo");
        } catch (Exception err) {
            Log.w(TAG, err.getLocalizedMessage());
        } finally {
            if (applied_promo != null) {
                for (String applied : applied_promo) {
                    Log.w(TAG, "appliedPromo: " + applied);
                    Map<String, Object> promo_item = (Map<String, Object>) documentSnapshot.get(applied);
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(promo_item);
                    try {
                        HomePageRecyclerData homePageRecyclerData = gson.fromJson(jsonElement, HomePageRecyclerData.class);
                        if (homePageRecyclerData != null) {
                            mView.loadHeadsUpPromo(homePageRecyclerData);
                        }
                    } catch (Exception e) {
                        mView.flush(e.getMessage());
                    }
                }
            }
        }
    }

    @Override
    public void getDataFromDB() {
        mModel.addShapShotListener((documentSnapshot, e) -> {
            if (documentSnapshot != null) {
                ArrayList<String> available_promo = (ArrayList<String>) documentSnapshot.get("available_promo");
                appliedPromo(documentSnapshot);
                ArrayList<String> tempList = new ArrayList<>();

                //if more then once available then take only one
                for (String promoCode : available_promo) {
                    if (!tempList.contains(promoCode)) {
                        tempList.add(promoCode);
                    }
                }
                available_promo = tempList;

                for (String promoCode : available_promo) {
                    mModel.getPromo(promoCode, new TeacherContract.Model.Listener<HomePageRecyclerData>() {
                        @Override
                        public void onSuccess(HomePageRecyclerData list) {
                            mView.loadPromoData(list);
                        }

                        @Override
                        public void onError(String msg) {
                            Log.w(TAG, "PromoPromoErr" + msg);
                        }
                    });
                }
                if (mView.checkNull()) {
                    final String avatar = documentSnapshot.getString("avatar");
                    if (avatar != null && !avatar.equals("")) {
                        mView.setAvatar(avatar);
                    }
                    String o = documentSnapshot.getString("last_name");
                    String o1 = documentSnapshot.getString("first_name");
                    mView.setUserName(o1, o);
                    mView.setMsgName(mView.generateMsgName(o1, o));

                    mView.setDocumentSnapshot(documentSnapshot);
                    mView.initRecentSearchRecycler(documentSnapshot);

                    mView.setRating((Map<String, Object>) documentSnapshot.get("self_rating"));
                    mView.setUnreadMsg(documentSnapshot.getString("unread_msg"));
                    mView.setUnreadNoti(documentSnapshot.getString("unread_noti"));
                    mView.setUnreadRecords((Map<String, Object>) documentSnapshot.get("unread_records"));

                    if (Objects.requireNonNull(documentSnapshot.getString("pro_com_%")).equals("100")) {
                        mView.setProfileComPercent(documentSnapshot.getString("pro_com_%"));
                    } else {
                        mView.setProfileComPercent(documentSnapshot.getString("pro_com_%"));
                        mView.showSnackBar(documentSnapshot.getString("pro_com_%"));
                    }
                    //testing fucking code here
                    List<String> ratingArray = (List<String>) documentSnapshot.get("rating_array");
                    if (ratingArray != null && ratingArray.size() > 0) {
                        for (int i = 0; i < ratingArray.size(); i++) {
                            int finalI = i;
                            mModel.getSingleRecords(ratingArray.get(i), new TeacherContract.Model.Listener<Record>() {
                                @Override
                                public void onSuccess(Record record) {

                                    String s_rate_status = record.getS_rate_status();
                                    switch (s_rate_status) {
                                        case Record.DIALOG:
                                            HomePageRatingData ratingDataList = new HomePageRatingData();
                                            List<String> ratingDataItemName = new ArrayList<>();
                                            ratingDataItemName.add("Expertise");
                                            ratingDataItemName.add("Experience");
                                            ratingDataItemName.add("Communication");
                                            ratingDataItemName.add("Behaviour");
                                            String subjectExchange[] = record.getSubjectExchange().split("\\s*(=>|,|\\s)\\s*");
                                            for (int j = 0; j < subjectExchange.length; j++) {
                                                ratingDataItemName.add(subjectExchange[j]);
                                            }
                                            ratingDataList.setRatingNameList(ratingDataItemName);
                                            ratingDataList.setName(record.getMentorName());
                                            ratingDataList.setAvatar(record.getMentorDpUrl());
                                            mView.testingCustomDialogue(ratingDataList, record);
                                            break;
                                        case Record.BOTTOM_SHEET:
                                            HomePageRatingData currentRatingDataList = new HomePageRatingData();
                                            List<String> currentRatingDataItemName = new ArrayList<>();
                                            currentRatingDataItemName.add("Expertise");
                                            currentRatingDataItemName.add("Experience");
                                            currentRatingDataItemName.add("Communication");
                                            currentRatingDataItemName.add("Behaviour");
                                            String newSubjectExchange[] = record.getSubjectExchange().split("\\s*(=>|,|\\s)\\s*");
                                            currentRatingDataItemName.addAll(Arrays.asList(newSubjectExchange));
                                            currentRatingDataList.setRatingNameList(currentRatingDataItemName);
                                            currentRatingDataList.setName(record.getMentorName());
                                            currentRatingDataList.setAvatar(record.getMentorDpUrl());
                                            currentRatingDataList.setRecord(record);
                                            mView.showSingleBottomSheetRating(currentRatingDataList);
                                            break;
                                        case Record.DONE:
                                            mModel.removeCompletedRating(ratingArray.get(finalI), new TeacherContract.Model.Listener<Void>() {
                                                @Override
                                                public void onSuccess(Void list) {

                                                }

                                                @Override
                                                public void onError(String msg) {
                                                    mView.flush(msg);
                                                }
                                            });
                                            break;

                                    }
                                }

                                @Override
                                public void onError(String msg) {
                                    mView.flush(msg);

                                }
                            });
                        }
                    }


                }
            } else {
                mView.flush("Does not found any user");
            }
        });


    }

}

/*  public List<HomePageRatingData> getFinalRatingData() {
        List<HomePageRatingData> data = new ArrayList<>();
        String[] primaryText = getResources().getStringArray(R.array.rating_demo_data);
        for (String aPrimaryText : primaryText) {
            HomePageRatingData current = new HomePageRatingData();
            current.ratingAboutName = aPrimaryText;
            data.add(current);
        }
        return data;
    }*/