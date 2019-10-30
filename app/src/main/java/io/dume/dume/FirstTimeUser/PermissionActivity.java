package io.dume.dume.FirstTimeUser;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import io.dume.dume.R;
import io.dume.dume.auth.auth.AuthActivity;
import li.yohan.parallax.ParallaxViewPager;

public class PermissionActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Bal";

    public Button afterSplashBtn;
    private static Boolean MLOCATIONPERMISSIONGRANTED = false;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    private static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String RECEIVE_SMS = Manifest.permission.RECEIVE_SMS;
    private static final String READ_PHONE_NUMBERS = Manifest.permission.READ_PHONE_NUMBERS;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final String MY_PREFS_NAME = "welcome";
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        setupObjects();
        getLocationPermission();
        if (checkPermissions()) {
            editor.putBoolean("isShown", true);
            editor.apply();
            editor.commit();
            startActivity(new Intent(getApplicationContext(), PrivacyActivity.class));

        } else {
            flush("Please grant the permissions");
        }


    }

    private void setupObjects(){
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
    }
    public void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(), READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(), READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(), WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            MLOCATIONPERMISSIONGRANTED = true;
            flush("permission granted");
           // mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);
        } else {
            ActivityCompat.requestPermissions(PermissionActivity.this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
// , Manifest.permission.RECEIVE_SMS
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1234: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPermissions()) {
                        MLOCATIONPERMISSIONGRANTED = true;
                      //  mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);

                    }
                } else {
                    flush("Please accept the permission to proceed");
                }
            }
            break;
            // other 'case' lines to check for other
            // permissions this app might request
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public boolean checkPermissions() {
        int resOne = this.checkCallingOrSelfPermission(FINE_LOCATION);
        int resTwo = this.checkCallingOrSelfPermission(COURSE_LOCATION);
        int resPhone = this.checkCallingOrSelfPermission(READ_PHONE_STATE);
        int resRead = this.checkCallingOrSelfPermission(READ_EXTERNAL_STORAGE);
        int resWrite = this.checkCallingOrSelfPermission(WRITE_EXTERNAL_STORAGE);
        int resSms = this.checkCallingOrSelfPermission(RECEIVE_SMS);
        return (resOne == PackageManager.PERMISSION_GRANTED
                && resTwo == PackageManager.PERMISSION_GRANTED
                && resWrite == PackageManager.PERMISSION_GRANTED
                && resRead == PackageManager.PERMISSION_GRANTED);
        //this is flexible
        //&& resSms == PackageManager.PERMISSION_GRANTED
        /*&& resPhone == PackageManager.PERMISSION_GRANTED
         */
    }
    public void flush(String msg){
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if( v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }

    @Override
    public void onClick(View v) {
        if (checkPermissions()) {
            editor.putBoolean("isShown", true);
            editor.apply();
            editor.commit();
            startActivity(new Intent(getApplicationContext(), PrivacyActivity.class));

        } else {
            flush("Please grant the permissions");
            //getLocationPermission();
           // mPager.setCurrentItem(2, true);
        }
    }
}
