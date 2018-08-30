package io.dume.dume.student.pojo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.Calendar;

import io.dume.dume.R;
import io.dume.dume.broadcastReceiver.MyConnectivityHandler;
import io.dume.dume.broadcastReceiver.NetworkChangeReceiver;
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
    protected HomePageContract.ParentCallback parentCallback;

    protected static Boolean ISNIGHT;
    protected static int HOUR;

    public void setActivityContext(Context context, int i) {
        this.context = context;
        fromFlag = i;
        this.activity = (Activity) context;
        rootView = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        createNetworkCheckSnackbar();
    }

    public void setParentCallback(HomePageContract.ParentCallback parentCallback) {
        this.parentCallback = parentCallback;
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
            if(snackbar != null){
                snackbar.show();
            }
        } else {
            if (snackbar != null) {
                snackbar.dismiss();
            }
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
        init();

        // Create an IntentFilter instance.
        IntentFilter intentFilter = new IntentFilter();
        // Add network connectivity change action.
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        // Set broadcast receiver priority.
        intentFilter.setPriority(100);
        // Create a network change broadcast receiver.
        networkChangeReceiver = new NetworkChangeReceiver();
        // Register the broadcast receiver with the intent filter object.
        registerReceiver(networkChangeReceiver, intentFilter);
        //setting the listener for the network
        networkChangeReceiver.setNetworkListener(this);

    }

    private void init() {

    }

    public void createNetworkCheckSnackbar() {
        View v = rootView.findViewById(R.id.parent_coor_layout);
        snackbar = Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();

        TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        LayoutInflater inflater = LayoutInflater.from(context);
        View snackView = inflater.inflate(R.layout.custom_snackbar_layout, null);

        TextView textViewStart = snackView.findViewById(R.id.custom_snackbar_text);
        textViewStart.setText(R.string.noNet_snack_text);
        textViewStart.setTextColor(Color.WHITE);

        layout.setPadding(0, 0, 0, 0);
        layout.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_red));
        CoordinatorLayout.LayoutParams parentParams = (CoordinatorLayout.LayoutParams) layout.getLayoutParams();
        parentParams.height = (int) (30 * (getResources().getDisplayMetrics().density));

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
                    if(snackbar != null){
                        snackbar.show();
                    }
                    //setTimeout(() -> imageView.setVisibility(View.INVISIBLE), 1250);

                } else {
                    if (snackbar != null) {
                        snackbar.dismiss();
                    }
                    //setTimeout(() -> imageView.setVisibility(View.INVISIBLE), 1250);
                }
            }
        });

        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (fromFlag == 1) {
                    parentCallback.onNetworkResume();
                }

            }

            @Override
            public void onShown(Snackbar snackbar) {
                if (fromFlag == 1) {
                    parentCallback.onNetworkPause();
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

    //make transparent status bar icon color dark
    public void setDarkStatusBarIcon() {
        decor = activity.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    //make transparent status bar icon color light
    public void setLightStatusBarIcon() {
        decor = activity.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            decor.setSystemUiVisibility(0);
        }
    }

    //    checking google service for map implementation and availability
    public boolean isServicesOK(Activity activity, Context context) {
        Log.d(TAG, "isServicesOK: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity);

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(activity, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(context, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public int getCurrentHour() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    @Override
    public void pause() {
        if(snackbar != null){
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
    //    setTimeout function homemade
    public static void setTimeout(Runnable runnable, int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            } catch (Exception e) {
                Log.e(TAG, "setTimeout: " + e);
            }
        }).start();
    }
}
