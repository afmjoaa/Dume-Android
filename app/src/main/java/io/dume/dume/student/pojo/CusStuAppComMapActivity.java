package io.dume.dume.student.pojo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import carbon.widget.Button;
import android.support.design.widget.FloatingActionButton;
import io.dume.dume.R;
import io.dume.dume.customView.TouchableWrapper;

public class CusStuAppComMapActivity extends CustomStuAppCompatActivity implements
        OnCompleteListener<LocationSettingsResponse>, TouchableWrapper.UpdateMapAfterUserInterection,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener{
    //onCompleteListener is for checking the gsm gps connectivity dialogue for auto action
    //TouchableWrapper is for touch action detection on the map
    //connectionCallback and onConnectionFailedListener is for onConnect moving camera to the last known location
    //LocationListener is for listening gps location change

    //private variable
    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private LocationRequest locationRequest;
    private carbon.widget.RelativeLayout toggleGpsControlBtn;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;

    //protected variable
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final int RC_RESOLUTION = 9123;
    protected View MAPCONTAINER;
    protected View MAP;
    protected View COMPASSBTN;
    protected Task<LocationSettingsResponse> result;
    protected FusedLocationProviderClient mFusedLocationProviderClient;
    protected GoogleApiClient mGoogleApiClient;
    protected boolean touchedFirstTime = false;

    //public variable
    public static final float DEFAULT_ZOOM = 15f;
    public static Boolean MLOCATIONPERMISSIONGRANTED = false;
    private ImageView myImageMarker;
    private MyGpsLocationChangeListener myGpsLocationChangeListener;
    private Button locationDoneBtn;
    private LinearLayout searchBottomSheet;
    private carbon.widget.RelativeLayout inputSearchContainer;
    private FloatingActionButton fab;


    public void setActivityContextMap(Context context, int i) {
        setActivityContext(context, i);
        this.context = context;
        this.activity = (Activity) context;
        try {
            myGpsLocationChangeListener = (MyGpsLocationChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement MyGpsLocationChangeListener");
        }
        findDefaultView();
        displayLocationSettingsRequest();
        setListenerForGpsToggle();
//        result.addOnCompleteListener(this);
    }

    public void onMapReadyListener(GoogleMap mMap) {
        this.mMap = mMap;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
        try {
            LocationSettingsResponse response =
                    task.getResult(ApiException.class);
        } catch (ApiException ex) {
            switch (ex.getStatusCode()) {
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    try {
                        ResolvableApiException resolvableApiException =
                                (ResolvableApiException) ex;
                        resolvableApiException
                                .startResolutionForResult(activity,
                                        REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException e) {
                        Log.e(TAG, "onComplete: " + e);
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    Toast.makeText(context, "GPS sensor unavailable", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_RESOLUTION) {

        }
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        getDeviceLocation(mMap);
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");

                        break;
                }
                break;
            case RC_RESOLUTION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required play service update.");
                        mGoogleApiClient.connect();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required play service update.");
                        break;
                }
                break;
        }
    }

    //setting the location service on by the gcm dialogue
    private void displayLocationSettingsRequest() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder settingsBuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        settingsBuilder.setAlwaysShow(true);
        result = LocationServices.getSettingsClient(activity)
                .checkLocationSettings(settingsBuilder.build());
    }

    private void setListenerForGpsToggle() {
        if (fromFlag == 1 && toggleGpsControlBtn != null) {
            toggleGpsControlBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.e(TAG, "onClick: the fucking toggle gps btn clicked ");
                    LocationSettingsRequest.Builder settingsBuilder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    settingsBuilder.setAlwaysShow(true);
                    result = LocationServices.getSettingsClient(activity)
                            .checkLocationSettings(settingsBuilder.build());
                    result.addOnCompleteListener(CusStuAppComMapActivity.this);
                }
            });
        }
    }

    public void findDefaultView() {
        MAPCONTAINER = rootView.findViewById(R.id.map_container);
        MAP = MAPCONTAINER.findViewById(R.id.map);
        if (fromFlag == 1) {
            toggleGpsControlBtn = rootView.findViewById(R.id.toggle_gps_dialogue_btn);
        } else if (fromFlag == 2) {
            myImageMarker = MAPCONTAINER.findViewById(R.id.imageMarker);
            searchBottomSheet = rootView.findViewById(R.id.searchBottomSheet);
            locationDoneBtn = rootView.findViewById(R.id.location_done_btn);
            inputSearchContainer = rootView.findViewById(R.id.input_search_container);
            fab = rootView.findViewById(R.id.fab);

        }
    }


    @Override
    public void onUpdateMapAfterUserInterection() {
        //Toast.makeText(context, "interacted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapTouchStart() {
        //Toast.makeText(context, "action down", Toast.LENGTH_SHORT).show();
        if (myImageMarker != null) {
            myImageMarker.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_current_location_marker_start));
            Drawable d = myImageMarker.getDrawable();
            if (d instanceof Animatable) {
                ((Animatable) d).start();
            }
        }
        if(fromFlag == 2){
            inputSearchContainer.requestFocus();
            searchBottomSheet.setVisibility(View.INVISIBLE);
            locationDoneBtn.setVisibility(View.VISIBLE);
            if(touchedFirstTime){
                fab.animate().translationYBy((float) (6.0f * (getResources().getDisplayMetrics().density))).setDuration(60).start();
                touchedFirstTime = false;
            }
        }

    }

    @Override
    public void onMapTouchEnd() {
        //Toast.makeText(context, "action up", Toast.LENGTH_SHORT).show();
        if (myImageMarker != null) {
            myImageMarker.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_current_location_marker_end));
            Drawable d = myImageMarker.getDrawable();
            if (d instanceof Animatable) {
                ((Animatable) d).start();
            }
        }

    }

    //default map functions
    //++++++++++_________++++++++++
    public void getLocationPermission(SupportMapFragment mapFragment) {
        this.mapFragment = mapFragment;
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                MLOCATIONPERMISSIONGRANTED = true;
                initMap(context, mapFragment);
            } else {
                ActivityCompat.requestPermissions(activity,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(activity,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void initMap(Context context, SupportMapFragment mapFragment) {
        Log.d(TAG, "initMap: initializing map");
        mapFragment.getMapAsync((OnMapReadyCallback) context);
    }

    protected void moveCamera(LatLng latLng, float zoom, String title, GoogleMap mMap) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        // dropping pin here which will be customized later on
        if (!title.equals("Device Location")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }
    }

    protected void getDeviceLocation(GoogleMap mMap) {
        Log.d(TAG, "getDeviceLocation: getting the device current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
        try {
            if (MLOCATIONPERMISSIONGRANTED) {
                mFusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // GPS location can be null if GPS is switched off
                                if (location != null) {
                                    moveCamera(new LatLng(location.getLatitude(), location.getLongitude()), DEFAULT_ZOOM
                                            , "Device Location", mMap);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("MapDemoActivity", "Error trying to get last GPS location");
                                Toast.makeText(context, "unable to get current location", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    protected void onMapReadyGeneralConfig() {
        //TODO: setting up the parent map activity
        if (MLOCATIONPERMISSIONGRANTED) {
            //TODO: centering the current location
            getDeviceLocation(mMap);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                getLocationPermission(mapFragment);
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setIndoorLevelPickerEnabled(true);

            if (mMap.getUiSettings().isCompassEnabled()) {
                //this works for me
                COMPASSBTN = MAP.findViewWithTag("GoogleMapCompass");
                RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) COMPASSBTN.getLayoutParams();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    rlp.addRule(RelativeLayout.ALIGN_PARENT_START);
                    rlp.addRule(RelativeLayout.ALIGN_START);
                }
                rlp.addRule(RelativeLayout.ALIGN_TOP, 0);
                rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                rlp.addRule(RelativeLayout.ALIGN_BOTTOM);
                rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                rlp.topMargin = (int) (20 * (getResources().getDisplayMetrics().density));
                rlp.bottomMargin = (int) (20 * (getResources().getDisplayMetrics().density));
                rlp.leftMargin = (int) (16 * (getResources().getDisplayMetrics().density));
                rlp.rightMargin = (int) (16 * (getResources().getDisplayMetrics().density));
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            getLocationPermission(mapFragment);
            return;
        }
        FusedLocationProviderClient myFusedeLocationProvider = LocationServices.getFusedLocationProviderClient(this);
        myFusedeLocationProvider.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            moveCamera(new LatLng(location.getLatitude(), location.getLongitude()), DEFAULT_ZOOM, "Device Location", mMap);
                            //TODO do camera change here
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MapDemoActivity", "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
        try {
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            // new Google API SDK v11 uses getFusedLocationProviderClient(this)
            LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            // do work here
                            onLocationChanged(locationResult.getLastLocation());
                        }
                    },
                    Looper.myLooper());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Toast.makeText(this, "Connection failed:\n" + connectionResult.toString(), Toast.LENGTH_LONG ).show();
        try {
            connectionResult.startResolutionForResult(activity, RC_RESOLUTION);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onLocationChanged(Location location) {
       myGpsLocationChangeListener.onMyGpsLocationChanged(location);
    }


}