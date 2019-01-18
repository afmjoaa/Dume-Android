package io.dume.dume.student.homePage;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.GeoPoint;

import java.util.Map;
import java.util.Objects;

import io.dume.dume.R;
import io.dume.dume.model.DumeModel;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.teacher.homepage.TeacherActivtiy;
import io.dume.dume.teacher.homepage.TeacherContract;
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
    private final SearchDataStore searchDataStore;


    public HomePagePresenter(Context context, HomePageContract.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (HomePageContract.View) context;
        this.mModel = mModel;
        searchDataStore = SearchDataStore.getInstance();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void homePageEnqueue() {
        mView.findView();
        mView.init();
        mView.makingCallbackInterfaces();
        mView.configHomePage();

        mModel.addShapShotListener((documentSnapshot, e) -> {
            if (documentSnapshot != null) {
                mView.setDocumentSnapshot(documentSnapshot);
                final String avatar = documentSnapshot.getString("avatar");
                if (avatar != null && !avatar.equals("")) {
                    mView.setAvatar(avatar);
                }
                String o = documentSnapshot.getString("last_name");
                String o1 = documentSnapshot.getString("first_name");
                mView.setUserName(o1, o);
                mView.setMsgName(mView.generateMsgName(o1, o));
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

            } else {
                mView.flush("Does not found any user");
            }
        });
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
            case R.id.profile_data:
                mView.gotoProfilePage();
                break;
            case R.id.mentor_add_layout:
                mView.gotoMentorAddvertise();
                break;
            case R.id.search_mentor_btn:
            case R.id.search_mentor_btn_nogps:
                //mView.gotoGrabingInfoPage();
                mView.gotoGrabingLocationPage();
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
                mView.testingCustomDialogue();
                break;

        }

    }


    @Override
    public void onMenuItemInteracted(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.student:
                //Toast.makeText(context, "texting toast", Toast.LENGTH_SHORT).show();
                mView.gotoStudentProfile();
                break;
            case R.id.mentor:
                mView.flush("Please Wait...");
                new DumeModel().switchAcount(DumeUtils.TEACHER, new TeacherContract.Model.Listener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mView.gotoMentorProfile();
                    }

                    @Override
                    public void onError(String msg) {
                        mView.flush("Error : " + msg);
                    }
                });

                break;
            case R.id.boot_camp:

                break;
            case R.id.al_display_pic:
                //mView.updateProfileBadge(mProfileChar);
                mView.gotoProfilePage();
                break;
            case R.id.al_records:
                //mView.updateRecordsBadge(++mRecPendingCount, ++mRecAcceptedCount, ++mRecCurrentCount);
                mView.gotoRecordsPage();
                break;
            case R.id.al_messages:
                mView.gotoInboxActivity();
                //mView.updateChatBadge(++mChatCount);
                break;
            case R.id.al_notifications:
                mView.gotoNotificationTab();
                //mView.updateNotificationsBadge(++mNotificationsCount);
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
                Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show();
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

}
