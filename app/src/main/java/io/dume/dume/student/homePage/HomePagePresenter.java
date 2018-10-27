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

import io.dume.dume.R;

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


    public HomePagePresenter(Context context, HomePageContract.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (HomePageContract.View) context;
        this.mModel = mModel;
    }

    @Override
    public void homePageEnqueue() {
        mView.findView();
        mView.init();
        mView.makingCallbackInterfaces();
        mView.configHomePage();
//        checkNetworkAndGps();
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

        }

    }


    @Override
    public void onMenuItemInteracted(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.student:
                //Toast.makeText(context, "texting toast", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mentor:

                break;
            case R.id.boot_camp:

                break;
            case R.id.al_display_pic:
                //mView.updateProfileBadge(mProfileChar);
                mView.gotoProfilePage();
                break;
            case R.id.al_records:
                mView.updateRecordsBadge(++mRecPendingCount, ++mRecAcceptedCount, ++mRecCurrentCount);
                mView.gotoRecordsPage();
                break;
            case R.id.al_messages:
                mView.gotoInboxActivity();
                mView.updateChatBadge(++mChatCount);
                break;
            case R.id.al_notifications:
                mView.gotoNotificationTab();
                mView.updateNotificationsBadge(++mNotificationsCount);
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

}
