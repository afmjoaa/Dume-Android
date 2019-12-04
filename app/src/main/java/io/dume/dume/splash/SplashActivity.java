package io.dume.dume.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.dume.dume.auth.AuthModel;
import io.dume.dume.auth.auth.AuthActivity;
import io.dume.dume.firstTimeUser.RoleChooserActivity;
import io.dume.dume.foreignObligation.PayActivity;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.util.DumeUtils;

public class SplashActivity extends AppCompatActivity implements SplashContract.View {
    private static final String MY_PREFS_NAME = "welcome";
    SplashContract.Presenter presenter;
    private static final String TAG = "SplashActivity";
    private SharedPreferences prefs;
    public static String updateDescription = "";
    public static String updateVersionName = "";
    public static String updateLink = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SplashPresenter(this, new AuthModel(this, this));
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        presenter.enqueue();
        //settingStatusBarTransparent();
        //makeFullScreen(this);
        presenter.init(this);
    }

    @Override
    public void foundUpdates() {
        DumeUtils.notifyDialog(this, false, false, "Mandatory Update !!", updateDescription, "Update", new TeacherContract.Model.Listener<Boolean>() {
            @Override
            public void onSuccess(Boolean yes) {
                if (yes) {
                    final String appPackageName = getPackageName();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }

                } else {
                    Toast.makeText(SplashActivity.this, "Update Ignored", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String msg) {
                Log.w(TAG, "onError: " + msg);
            }
        });
    }

    @Override
    public void foundErr(String msg) {
        DumeUtils.notifyDialog(this, true, false, "No Internet or Error...", msg, "Retry", new TeacherContract.Model.Listener<Boolean>() {
            @Override
            public void onSuccess(Boolean yes) {
                //already handled
                presenter.enqueue();
            }

            @Override
            public void onError(String msg) {
                //already handled
                Toast.makeText(SplashActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void gotoLoginActivity() {

        Boolean isShown = prefs.getBoolean("isShown", false);
        if (isShown) {
            startActivity(new Intent(this, AuthActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, RoleChooserActivity.class));
            finish();
        }
    }

    @Override
    public void gotoTeacherActivity() {
        //  startActivity(new Intent(this, TeacherActivityMock.class));
      //  startActivity(new Intent(this, TeacherActivtiy.class));
        gotoLoginActivity();
        finish();
    }

    @Override
    public void gotoStudentActivity() {
      //  startActivity(new Intent(this, HomePageActivity.class));
        gotoLoginActivity();
        finish();
    }

    @Override
    public void gotoForeignObligation() {
        startActivity(new Intent(this, PayActivity.class));
        finish();
    }

   /* public void settingStatusBarTransparent() {

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            this.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            StatusBarUtil.setTranslucent(this, 50);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }

        //lollipop only support white status bar icon color so have to fix it by the status bar util library
        //by making translucent status bar and test thoroughly
        //StatusBarUtil.setTranslucent(this, 100);
        // StatusBarUtil.setTransparent(this);
        //StatusBarUtil.setColor(Activity activity, int color)
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
*/
}