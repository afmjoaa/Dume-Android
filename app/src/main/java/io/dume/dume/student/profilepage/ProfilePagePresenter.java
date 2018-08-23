package io.dume.dume.student.profilepage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import io.dume.dume.student.homepage.HomePageContract;

public class ProfilePagePresenter implements ProfilePageContract.Presenter {

    private ProfilePageContract.View mView;
    private ProfilePageContract.Model mModel;
    private Context context;
    private Activity activity;

    public ProfilePagePresenter(Context context, ProfilePageContract.View mView, ProfilePageContract.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = mView;
        this.mModel = mModel;
    }

    @Override
    public void profilePageEnqueue() {
        mView.findView();
        mView.initProfilePage();
        settingStatusBarTransparent();
        mView.configProfilePage();
    }

    @Override
    public void onProfileViewIntracted(View view) {

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
