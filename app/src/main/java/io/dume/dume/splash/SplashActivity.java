package io.dume.dume.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import io.dume.dume.R;
import io.dume.dume.afterSplashTrp.AfterSplashActivity;
import io.dume.dume.auth.AuthModel;
import io.dume.dume.auth.auth.AuthActivity;
import io.dume.dume.obligation.foreignObli.PayActivity;
import io.dume.dume.student.homePage.HomePageActivity;
import io.dume.dume.teacher.homepage.TeacherActivtiy;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.util.DumeUtils;

import static io.dume.dume.util.DumeUtils.makeFullScreen;

public class SplashActivity extends AppCompatActivity implements SplashContract.View {
    private static final String MY_PREFS_NAME = "welcome";
    SplashContract.Presenter presenter;
    private static final String TAG = "SplashActivity";
    private SharedPreferences prefs;
    public static String updateDescription = "";
    public static String updateVersionName = "";
    public static String updateLink="";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SplashPresenter(this, new AuthModel(this, this));
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        presenter.enqueue();
        makeFullScreen(this);
        presenter.init(this);

    }

    @Override
    public void foundUpdates() {
        DumeUtils.notifyDialog(this, false,false, "Mandatory Update", updateDescription, "Update", new TeacherContract.Model.Listener<Boolean>() {
            @Override
            public void onSuccess(Boolean yes) {
                if (yes) {
                    final String appPackageName = getPackageName();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }

                }else {
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
    public void foundErr(String msg){
        DumeUtils.notifyDialog(this, true,false, "No Internet...", msg, "Retry", new TeacherContract.Model.Listener<Boolean>() {
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
            startActivity(new Intent(this, AfterSplashActivity.class));
            finish();
        }
    }

    @Override
    public void gotoTeacherActivity() {
        //  startActivity(new Intent(this, TeacherActivityMock.class));
        startActivity(new Intent(this, TeacherActivtiy.class));
        finish();
    }

    @Override
    public void gotoStudentActivity() {
        startActivity(new Intent(this, HomePageActivity.class));
        finish();
    }

    @Override
    public void gotoForeignObligation() {
        startActivity(new Intent(this, PayActivity.class));
        finish();
    }
}
