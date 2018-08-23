package io.dume.dume.student.grabingLocation;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import io.dume.dume.R;
import io.dume.dume.student.pojo.CusStuAppComMapActivity;
import io.dume.dume.student.pojo.MyGpsLocationChangeListener;

public class GrabingLocationActivity extends CusStuAppComMapActivity implements OnMapReadyCallback,
        GrabingLocaitonContract.View, MyGpsLocationChangeListener {

    private GoogleMap mMap;
    private static final String TAG = "GrabingLocationActivity";
    private static final int fromFlag = 2;

    GrabingLocaitonContract.Presenter mPresenter;
    private Context mContext;
    private TextView mLocationMarkerText;
    private LatLng mCenterLatLong;
    private SupportMapFragment mapFragment;
    private FloatingActionButton fab;
    private View map;
    private Toolbar toolbar;
    private BottomSheetBehavior bottomSheetBehavior;
    private NestedScrollView llBottomSheet;
    private FloatingActionButton bottomSheetFab;
    private boolean firstTime = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu3_activity_grabing_location);
        buildGoogleApiClient();
        setActivityContextMap(this, fromFlag);
        mContext = this;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        getLocationPermission(mapFragment);
        mPresenter = new GrabingLocationPresenter(this, new GrabingLocationModel());
        mPresenter.grabingLocationPageEnqueue();
        //settingStatusBarTransparent();
        setDarkStatusBarIcon();
        setIsNight();

    }

    @Override
    public void findView() {
        fab = findViewById(R.id.fab);
        map = findViewById(R.id.map);
        mLocationMarkerText = (TextView) findViewById(R.id.locationMarkertext);
        toolbar = findViewById(R.id.toolbar);
        llBottomSheet = findViewById(R.id.searchBottomSheet);
        bottomSheetFab = findViewById(R.id.bottom_sheet_fab);
    }

    @Override
    public void initGrabingLocationPage() {
        fab.setAlpha(0.90f);

        //initializing actionbar/toolbar
        setSupportActionBar(toolbar);
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);

        /*Log.e(TAG, "initGrabingLocationPage: before" + bottomSheetBehavior.getState());

        Log.e(TAG, "initGrabingLocationPage:after " + bottomSheetBehavior.getState());*/



    }

    @Override
    public void configGrabingLocationPage() {
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });



    }

    @Override
    public void makingCallbackInterfaces() {

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        //ISNIGHT = HOUR < 4 || HOUR > 19;
        if (ISNIGHT) {
            MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.map_style_night_json);
            googleMap.setMapStyle(style);
        } else {
            MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.map_style_default_json);
            googleMap.setMapStyle(style);
        }
        mMap = googleMap;
        onMapReadyListener(mMap);
        onMapReadyGeneralConfig();

        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {

            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                // Cleaning all the markers.
                if (mMap != null) {
                    mMap.clear();
                }

                mCenterLatLong = mMap.getCameraPosition().target;
                //mZoom = mGoogleMap.getCameraPosition().zoom;
                mLocationMarkerText.setText("Lat : " + mCenterLatLong.latitude + "," + "Long : " + mCenterLatLong.longitude);

            }
        });

    }

    @Override
    public void onCenterCurrentLocation() {
        Drawable d = fab.getDrawable();
        if (d instanceof Animatable) {
            ((Animatable) d).start();
        }
        Log.d(TAG, "onClick: clicked gps icon");
        getDeviceLocation(mMap);

    }

    @Override
    public void onShowBottomSheet() {

    }

  /*  @Override
    public void onShowBottomSheet() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            fab.animate().translationYBy((float) (-60.0f * (getResources().getDisplayMetrics().density))).setDuration(100).start();
            if (COMPASSBTN != null) {
//                COMPASSBTN.animate().translationYBy((float) (-54.0f * (getResources().getDisplayMetrics().density))).setDuration(100).start();

            }
            if (bottomSheetFab.getVisibility() == View.VISIBLE) {
                bottomSheetFab.setVisibility(View.GONE);
            }
        } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            if (firstTime) {
                fab.animate().translationYBy((float) (-60.0f * (getResources().getDisplayMetrics().density))).setDuration(100).start();
//                COMPASSBTN.animate().translationYBy((float) (-54.0f * (getResources().getDisplayMetrics().density))).setDuration(100).start();
                firstTime = false;
            }
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            if (bottomSheetFab.getVisibility() == View.VISIBLE) {
                bottomSheetFab.setVisibility(View.GONE);
            }

        }
    }
*/

    public void onGrabingLocationViewClicked(View view) {
        mPresenter.onGrabingLocationViewIntracted(view);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }


    @Override
    public void onMyGpsLocationChanged(Location location) {
        /*// New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        moveCamera(latLng, DEFAULT_ZOOM, "Device Location", mMap);*/
    }
}
