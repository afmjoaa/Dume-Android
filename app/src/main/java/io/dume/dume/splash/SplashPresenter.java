package io.dume.dume.splash;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;


import io.dume.dume.auth.AuthGlobalContract;


public class SplashPresenter implements SplashContract.Presenter {
    SplashContract.View view;
    SplashContract.Model model;
    private static final String TAG = "SplashPresenter";

    public SplashPresenter(SplashContract.View view, SplashContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void init(Activity myActivity) {
        // Status bar :: Transparent
        Window window = myActivity.getWindow();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void enqueue() {
        Log.w(TAG, "enqueue: ");
        if (model.isUserLoggedIn()) {
            model.onAccountTypeFound(model.getUser(), new AuthGlobalContract.AccountTypeFoundListener() {
                @Override
                public void onStart() {
                    Log.w(TAG, "onStart: ");
                }

                @Override
                public void onTeacherFound() {
                    view.gotoTeacherActivity();
                    Log.w(TAG, "onTeacherFound: ");
                }

                @Override
                public void onStudentFound() {
                    view.gotoStudentActivity();
                    Log.w(TAG, "onStudentFound: ");
                }

                @Override
                public void onFail(String exeption) {

                }
            });
        } else {
            view.gotoLoginActivity();
            Log.w(TAG, "enqueue: login");
        }

    }
}