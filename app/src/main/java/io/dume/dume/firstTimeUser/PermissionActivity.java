package io.dume.dume.firstTimeUser;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.google.android.material.appbar.AppBarLayout;
import com.jaeger.library.StatusBarUtil;

import androidx.core.view.ViewCompat;
import io.dume.dume.R;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;

import static io.dume.dume.util.DumeUtils.configureAppbar;

public class PermissionActivity extends CustomStuAppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PermissionActivity";
    private static Boolean MLOCATIONPERMISSIONGRANTED = false;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final String MY_PREFS_NAME = "welcome";
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        initView();
        initDesign();
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        //if permission already granted then don't show anything at all
        if (checkPermissions()) {
            /*editor.putBoolean("isShown", true);
            editor.apply();
            editor.commit();*/
            startActivity(new Intent(getApplicationContext(), PrivacyActivity.class));
        }
    }

    public void initView(){

    }

    public void initDesign(){
        setActivityContext(this, 3637);
        setDarkStatusBarIcon();
        configureAppbar(this, "Provide Permission", true);
    }

    public void getLocationPermission() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            MLOCATIONPERMISSIONGRANTED = true;
            flush("permission granted");
        } else {
            ActivityCompat.requestPermissions(PermissionActivity.this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1234) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (checkPermissions()) {
                    MLOCATIONPERMISSIONGRANTED = true;
                    /*editor.putBoolean("isShown", true);
                    editor.apply();
                    editor.commit();*/
                    startActivity(new Intent(getApplicationContext(), PrivacyActivity.class));
                }
            } else {
                flush("Please accept the permission to proceed");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public boolean checkPermissions() {
        int resOne = this.checkCallingOrSelfPermission(FINE_LOCATION);
        int resTwo = this.checkCallingOrSelfPermission(COURSE_LOCATION);
        return (resOne == PackageManager.PERMISSION_GRANTED
                && resTwo == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onClick(View v) {
        if (checkPermissions()) {
            /*editor.putBoolean("isShown", true);
            editor.apply();
            editor.commit();*/
            startActivity(new Intent(getApplicationContext(), PrivacyActivity.class));
        } else {
            getLocationPermission();
        }
    }
}
