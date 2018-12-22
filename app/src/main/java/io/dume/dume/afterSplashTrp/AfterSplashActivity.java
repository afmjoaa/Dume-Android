package io.dume.dume.afterSplashTrp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.dume.dume.R;
import io.dume.dume.afterSplashTrp.adapter.AfterSplashPagerAdapter;
import io.dume.dume.afterSplashTrp.adapter.DemoCardFragment;
import io.dume.dume.auth.auth.AuthActivity;
import li.yohan.parallax.ParallaxViewPager;
import me.relex.circleindicator.CircleIndicator;

import static io.dume.dume.util.DumeUtils.makeFullScreen;

public class AfterSplashActivity extends AppCompatActivity implements DemoCardFragment.OnActionListener {


    private static final String TAG = "Bal";
    private ParallaxViewPager mPager;
    private ViewPager pager;
    public Button afterSplashBtn;
    private static Boolean MLOCATIONPERMISSIONGRANTED = false;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_splash_parallax_layout);
        makeFullScreen(this);
        mPager = findViewById(R.id.pager);
        mPager.setAdapter(new AfterSplashPagerAdapter(this, getSupportFragmentManager()));
        CircleIndicator indicator = findViewById(R.id.indicator);
        afterSplashBtn = findViewById(R.id.after_splash_start_btn);
        indicator.setViewPager(mPager);
        afterSplashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button button = (Button) view;
               /*button.setEnabled(false);
                button.setBackgroundColor(getResources().getColor(R.color.green));*/
               switch (mPager.getCurrentItem()){
                   case 0:
                       mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);
                       break;
                   case 1:
                       mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);
                       break;
                   case 2:
                       getLocationPermission();
                       break;
                   case 3:
                       mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);
                       break;
                   case 4:
                       if(checkLocationPermission()){
                           startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                           AfterSplashActivity.this.finish();
                       }else{
                           Toast.makeText(AfterSplashActivity.this, "Please grant the permission", Toast.LENGTH_SHORT).show();
                           //getLocationPermission();
                           mPager.setCurrentItem(2, true);
                       }
                       break;
               }
               /* if (mPager.getCurrentItem() < 4) {
                    mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);
                } else if (mPager.getCurrentItem() == 4) {
                    startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                    AfterSplashActivity.this.finish();
                }*/
            }
        });
    }

    @Override
    public void onAction(int id) {

    }

    public void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            MLOCATIONPERMISSIONGRANTED = true;
            Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
            mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);
        } else {
            Toast.makeText(this, "fucked it called", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(AfterSplashActivity.this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    
    //checking the permission result here
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1234: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    MLOCATIONPERMISSIONGRANTED = true;
                    mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);
                } else {
                    Toast.makeText(this, "Please accept the permission to proceed", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
            break;
            // other 'case' lines to check for other
            // permissions this app might request
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public boolean checkLocationPermission()
    {
        String permissionOne = "android.permission.ACCESS_FINE_LOCATION";
        String permissionTwo = "android.permission.ACCESS_COARSE_LOCATION";
        int resOne = this.checkCallingOrSelfPermission(permissionOne);
        int resTwo = this.checkCallingOrSelfPermission(permissionTwo);
        return (resOne == PackageManager.PERMISSION_GRANTED && resTwo == PackageManager.PERMISSION_GRANTED);
    }
}


