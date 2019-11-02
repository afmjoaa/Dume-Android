package io.dume.dume.student.pojo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.jaeger.library.StatusBarUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Calendar;

import io.dume.dume.Google;
import io.dume.dume.R;
import io.dume.dume.auth.auth.AuthActivity;
import io.dume.dume.broadcastReceiver.MyConnectivityHandler;
import io.dume.dume.broadcastReceiver.NetworkChangeReceiver;
import io.dume.dume.customView.HorizontalLoadView;
import io.dume.dume.splash.SplashActivity;
import io.dume.dume.student.homePage.HomePageContract;
import io.dume.dume.util.MyApplication;
import io.dume.dume.util.NetworkUtil;

public class CustomStuAppCompatActivity extends AppCompatActivity implements MyConnectivityHandler {
    private View decor;
    private static final String TAG = "CustomStuAppCompatActiv";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    protected NetworkChangeReceiver networkChangeReceiver;
    protected Context context;
    protected Activity activity;
    protected Snackbar snackbar;
    protected View rootView;
    protected static int fromFlag = 0;
    protected static Boolean ISNIGHT;
    protected static int HOUR;
    private CoordinatorLayout v;
    private HorizontalLoadView loadView;
    protected SearchDataStore searchDataStore;
    protected float mDensity;

    public void setActivityContext(Context context, int i) {
        this.context = context;
        fromFlag = i;
        this.activity = (Activity) context;
        rootView = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        createNetworkCheckSnackbar();
        searchDataStore = SearchDataStore.getInstance();
        mDensity = getResources().getDisplayMetrics().density;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        int status = NetworkUtil.getConnectivityStatusString(context);

        if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
            if (snackbar != null) {
                snackbar.show();
            }
        } else {
            if (snackbar != null) {
                snackbar.dismiss();
            }
            new Thread(() -> {
                try {
                    Socket socket = new Socket();
                    socket.connect(new InetSocketAddress("8.8.8.8", 53), 2500);

                    // socket.connect(new InetSocketAddress("114.114.114.114", 53), 3000);
                    if (snackbar != null) {
                        snackbar.dismiss();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    if (snackbar != null) {
                        snackbar.show();
                    }

                }
            }).start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.activityPaused();// On Pause notify the Application
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.activityResumed();// On Resume notify the Application
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String accountMajor = Google.getInstance().getAccountMajor();
        if (FirebaseAuth.getInstance().getCurrentUser() != null && accountMajor == null) {
            Intent returnIntent = new Intent(this, SplashActivity.class);
            returnIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(returnIntent);
            //finish();
            System.exit(0);
        }
        init();
        // Create an IntentFilter instance.
        IntentFilter intentFilter = new IntentFilter();
        // Add network connectivity change action.
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        // Set broadcast receiver priority.
        intentFilter.setPriority(100);
        // Create a network change broadcast receiver.
        networkChangeReceiver = new NetworkChangeReceiver();
        //setting the listener for the network
        networkChangeReceiver.setNetworkListener(this);
        // Register the broadcast receiver with the intent filter object.
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    private void init() {

    }

    public void createNetworkCheckSnackbar() {
        v = rootView.findViewById(R.id.parent_coor_layout);
        snackbar = Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();

        TextView textView = (TextView) layout.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        LayoutInflater inflater = LayoutInflater.from(context);
        View snackView = inflater.inflate(R.layout.custom_snackbar_layout, null);

        TextView textViewStart = snackView.findViewById(R.id.custom_snackbar_text);
        textViewStart.setText(R.string.noNet_snack_text);
        textViewStart.setTextColor(Color.WHITE);

        layout.setPadding(0, 0, 0, 0);
        layout.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_red));
        CoordinatorLayout.LayoutParams parentParams = (CoordinatorLayout.LayoutParams) layout.getLayoutParams();
        parentParams.height = (int) (36 * (getResources().getDisplayMetrics().density));

        //can set those things like that ..
        //parentParams.gravity = Gravity.TOP;

        layout.setLayoutParams(parentParams);
        layout.addView(snackView, 0);

        LinearLayout retryBtn = snackView.findViewById(R.id.snackbar_btn);
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = v.findViewById(R.id.custom_snackbar_btnImage);
                if (imageView.getVisibility() == View.INVISIBLE) {
                    imageView.setVisibility(View.VISIBLE);
                }
                Drawable d = imageView.getDrawable();
                if (d instanceof Animatable) {
                    ((Animatable) d).start();
                }

                int status = NetworkUtil.getConnectivityStatusString(context);
                if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                    if (snackbar != null) {
                        snackbar.show();
                    }
                    //setTimeout(() -> imageView.setVisibility(View.INVISIBLE), 1250);

                } else {
                    if (snackbar != null) {
                        snackbar.dismiss();
                    }
                    new Thread(() -> {
                        try {
                            Socket socket = new Socket();
                            socket.connect(new InetSocketAddress("8.8.8.8", 53), 2500);

                            // socket.connect(new InetSocketAddress("114.114.114.114", 53), 3000);
                            if (snackbar != null) {
                                snackbar.dismiss();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                            if (snackbar != null) {
                                snackbar.show();
                            }

                        }
                    }).start();
                    //setTimeout(() -> imageView.setVisibility(View.INVISIBLE), 1250);
                }
            }
        });
    }

    public void settingStatusBarTransparent() {

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

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            StatusBarUtil.setTranslucent(activity, 50);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }

        //lollipop only support white status bar icon color so have to fix it by the status bar util library
        //by making translucent status bar and test thoroughly
        //StatusBarUtil.setTranslucent(this, 100);
        // StatusBarUtil.setTransparent(this);
        //StatusBarUtil.setColor(Activity activity, int color)
    }

    //make transparent status bar icon color dark
    public void setDarkStatusBarIcon() {
        decor = activity.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            StatusBarUtil.setDarkMode(activity);
            StatusBarUtil.setTranslucent(activity, 50);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
    }

    //make transparent status bar icon color light
    public void setLightStatusBarIcon() {
        decor = activity.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            decor.setSystemUiVisibility(0);
        } else {
            StatusBarUtil.setLightMode(activity);
            StatusBarUtil.setTranslucent(activity, 50);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
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

    public int getCurrentHour() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    @Override
    public void pause() {
        if (snackbar != null) {
            snackbar.show();
        }
    }

    @Override
    public void resume() {
        if (snackbar != null) {
            snackbar.dismiss();
        }

    }

    @Override
    public void onError(Exception e) {
        Log.e(TAG, "onNetworkError: Error function called" + e);
    }

    public void setIsNight() {
        HOUR = getCurrentHour();
        ISNIGHT = HOUR < 5 || HOUR > 19;
    }

    //fullscreen code
    public void makeFullScreen() {
        View decorView = activity.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    //sign_out function
    public void onSignOut() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    public void findLoadView() {
        loadView = rootView.findViewById(R.id.loadView);
    }

    public void showProgress() {
        if (loadView.getVisibility() == View.INVISIBLE || loadView.getVisibility() == View.GONE) {
            loadView.setVisibility(View.VISIBLE);
        }
        if (!loadView.isRunningAnimation()) {
            loadView.startLoading();
        }
    }

    public void hideProgress() {
        if (loadView.isRunningAnimation()) {
            loadView.stopLoading();
        }
        if (loadView.getVisibility() == View.VISIBLE) {
            loadView.setVisibility(View.INVISIBLE);
        }
    }

    public void flush(String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if (v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
