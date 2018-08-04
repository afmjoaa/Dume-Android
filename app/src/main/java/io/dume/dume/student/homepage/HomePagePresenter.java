package io.dume.dume.student.homepage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

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


    public HomePagePresenter(Context context, HomePageContract.View mView, HomePageContract.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = mView;
        this.mModel = mModel;
    }

    @Override

    public void homePageEnqueue() {
        mView.findView();
        mView.init();
        settingStatusBarTransparent();
        mView.configHomePage();
    }

    @Override
    public void onViewIntracted(View view) {
        switch (view.getId()) {
            case R.id.switch_account_btn:
                mView.onSwitchAccount();
                break;
            case R.id.user_dp:
                break;
            case R.id.bottom_sheet_fab:
                mView.onShowBottomSheet();
                break;
            case R.id.fab:
                mView.onCenterCurrentLocation();
                break;
            case R.id.bottom_sheet:
                mView.onBottomSheetClicked();
                break;
        }

    }


    @Override
    public void onMenuItemInteracted(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.student:

                break;
            case R.id.mentor:

                break;
            case R.id.boot_camp:

                break;
            case R.id.al_display_pic:
                mView.updateProfileBadge(mProfileChar);
                break;
            case R.id.al_records:
                mView.updateRecordsBadge(++mRecPendingCount, ++mRecAcceptedCount, ++mRecCurrentCount);
                break;
            case R.id.al_messages:
                mView.updateChatBadge(++mChatCount);
                break;
            case R.id.al_notifications:
                mView.updateNotificationsBadge(++mNotificationsCount);
                break;

        }

    }

    private void settingStatusBarTransparent() {

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
