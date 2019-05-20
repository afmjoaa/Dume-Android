package io.dume.dume.student.pojo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.ui.IconGenerator;

import carbon.widget.Button;

import android.support.design.widget.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.dume.dume.R;
import io.dume.dume.customView.TouchableWrapper;

import static android.graphics.Typeface.ITALIC;
import static android.graphics.Typeface.NORMAL;
import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

public class CusStuAppComMapActivity extends CustomStuAppCompatActivity implements
        OnCompleteListener<LocationSettingsResponse>, TouchableWrapper.UpdateMapAfterUserInterection,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {
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
    protected boolean grabingLocationFirstLoad = true;

    //public variable
    public static final float DEFAULT_ZOOM = 16f;
    public static Boolean MLOCATIONPERMISSIONGRANTED = false;
    private ImageView myImageMarker;
    private MyGpsLocationChangeListener myGpsLocationChangeListener;
    private Button locationDoneBtn;
    private LinearLayout searchBottomSheet;
    private carbon.widget.RelativeLayout inputSearchContainer;
    private FloatingActionButton fab;
    private EditText inputSearch;
    private CameraPosition cameraPosition;
    private int time;
    private long delayTime = 100L;
    private static LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(19, 87), new LatLng(27, 93));


    public void setActivityContextMap(Context context, int i) {
        setActivityContext(context, i);
        this.context = context;
        this.activity = (Activity) context;
        LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(19, 87), new LatLng(27, 93));
        try {
            myGpsLocationChangeListener = (MyGpsLocationChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement MyGpsLocationChangeListener");
        }
        findDefaultView();
        displayLocationSettingsRequest();
        setListenerForGpsToggle();
//      result.addOnCompleteListener(this);
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
            inputSearch = findViewById(R.id.input_search);

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
        if (fromFlag == 2) {
            inputSearchContainer.requestFocus();
            searchBottomSheet.setVisibility(View.INVISIBLE);
            locationDoneBtn.setVisibility(View.VISIBLE);
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

    protected void moveSearchLoadingCamera(LatLng latLng, float zoom, String title, GoogleMap mMap) {
        cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(14f)
                .bearing(0)
                .tilt(40)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 50, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        Timer timer = new Timer();
                        timer.scheduleAtFixedRate(new TimerTask() {
                            @Override
                            public void run() {
                                // ... Calculating and sending new location, in an infinite loop with sleeps for timing
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        cameraPosition = new CameraPosition.Builder()
                                                .target(latLng)
                                                .zoom(15f)
                                                .bearing(mMap.getCameraPosition().bearing + 120)
                                                .tilt(45)
                                                .build();
                                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 9000, new GoogleMap.CancelableCallback() {
                                            @Override
                                            public void onFinish() {
                                                cameraPosition = new CameraPosition.Builder()
                                                        .target(latLng)
                                                        .zoom(16f)
                                                        .bearing(mMap.getCameraPosition().bearing + 120)
                                                        .tilt(50)
                                                        .build();
                                                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                                                    @Override
                                                    public void onMapLoaded() {
                                                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 9000, new GoogleMap.CancelableCallback() {
                                                            @Override
                                                            public void onFinish() {
                                                                cameraPosition = new CameraPosition.Builder()
                                                                        .target(latLng)
                                                                        .zoom(17f)
                                                                        .bearing(mMap.getCameraPosition().bearing + 120)
                                                                        .tilt(55)
                                                                        .build();
                                                                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                                                                    @Override
                                                                    public void onMapLoaded() {
                                                                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 9000, new GoogleMap.CancelableCallback() {
                                                                            @Override
                                                                            public void onFinish() {
                                                                                cameraPosition = new CameraPosition.Builder()
                                                                                        .target(latLng)
                                                                                        .zoom(17f)
                                                                                        .bearing(mMap.getCameraPosition().bearing)
                                                                                        .tilt(55)
                                                                                        .build();
                                                                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, new GoogleMap.CancelableCallback() {
                                                                                    @Override
                                                                                    public void onFinish() {
                                                                                        //nothing to do
                                                                                    }

                                                                                    @Override
                                                                                    public void onCancel() {

                                                                                    }
                                                                                });
                                                                            }

                                                                            @Override
                                                                            public void onCancel() {

                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                            }

                                                            @Override
                                                            public void onCancel() {

                                                            }
                                                        });
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancel() {

                                            }
                                        });
                                    }
                                });
                            }
                        }, 1000, 300000);
                    }
                });
            }

            @Override
            public void onCancel() {
                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {

                    }
                });
            }
        });
    }

    protected void moveCamera(LatLng latLng, float zoom, String title, GoogleMap mMap) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        time = 1000;
        if (fromFlag == 2) {
            //grabingLocationFirstLoad
            cameraPosition = new CameraPosition.Builder()
                    .target(latLng)
                    .zoom(DEFAULT_ZOOM)
                    .tilt(0)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
        } else if (fromFlag == 1) {
            cameraPosition = new CameraPosition.Builder()
                    .target(latLng)
                    .zoom(15f)
                    .tilt(42)
                    .build();
            time = 1000;
            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), time, new GoogleMap.CancelableCallback() {
                        @Override
                        public void onFinish() {
                        }

                        @Override
                        public void onCancel() {
                        }
                    });
                }
            });
        } else {
            cameraPosition = new CameraPosition.Builder()
                    .target(latLng)
                    .zoom(15f)
                    .tilt(0)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
            /*MAP.postDelayed(new Runnable() {
                @Override
                public void run() {
                }
            }, 700L);*/
            /*mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
                }
            });*/
        }
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
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
                                    if (fromFlag != 5) {
                                        if (mMap.getCameraPosition().zoom < 4) {
                                            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(LAT_LNG_BOUNDS, 10));
                                            moveCamera(new LatLng(location.getLatitude(), location.getLongitude()), DEFAULT_ZOOM, "Device Location", mMap);
                                        } else {
                                            moveCamera(new LatLng(location.getLatitude(), location.getLongitude()), DEFAULT_ZOOM, "Device Location", mMap);
                                        }
                                    }
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

    protected void getDeviceLocationWithZoom(GoogleMap mMap, float zoomOne) {
        Log.d(TAG, "getDeviceLocation: getting the device current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
        try {
            if (MLOCATIONPERMISSIONGRANTED) {
                mFusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    CameraPosition cameraPosition = new CameraPosition.Builder()
                                            .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                            .zoom(zoomOne)
                                            .bearing(0)
                                            .tilt(0)
                                            .build();
                                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), new GoogleMap.CancelableCallback() {
                                        @Override
                                        public void onFinish() {
                                            //nothing to do
                                        }

                                        @Override
                                        public void onCancel() {
                                        }
                                    });
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
            //TODO: centering the current location not default
            try{
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(LAT_LNG_BOUNDS, 10));
            } catch (Exception e) {
                Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
            }
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                getLocationPermission(mapFragment);
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
            //setting the toolbar for the marker click disable
            mMap.getUiSettings().setMapToolbarEnabled(false);

            if (mMap.getUiSettings().isCompassEnabled()) {
                //this works for me
                COMPASSBTN = MAP.findViewWithTag("GoogleMapCompass");
                RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) COMPASSBTN.getLayoutParams();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    rlp.addRule(RelativeLayout.ALIGN_PARENT_START, 0);
                    rlp.addRule(RelativeLayout.ALIGN_START, 0);
                    rlp.addRule(RelativeLayout.ALIGN_PARENT_END);
                    rlp.addRule(RelativeLayout.ALIGN_END);
                }
                rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
                rlp.addRule(RelativeLayout.ALIGN_LEFT, 0);
                rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                rlp.addRule(RelativeLayout.ALIGN_RIGHT);

                rlp.addRule(RelativeLayout.ALIGN_TOP, 0);
                rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                rlp.addRule(RelativeLayout.ALIGN_BOTTOM);
                rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                rlp.topMargin = (int) (20 * (getResources().getDisplayMetrics().density));
                rlp.bottomMargin = (int) (58 * (getResources().getDisplayMetrics().density));
                rlp.leftMargin = (int) (16 * (getResources().getDisplayMetrics().density));
                rlp.rightMargin = (int) (16 * (getResources().getDisplayMetrics().density));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    COMPASSBTN.setElevation((int) (6 * (getResources().getDisplayMetrics().density)));
                }

                if (fromFlag == 1604) {
                    rlp.topMargin = (int) (20 * (getResources().getDisplayMetrics().density));
                    rlp.leftMargin = (int) (16 * (getResources().getDisplayMetrics().density));
                    rlp.bottomMargin = (int) (10 * (getResources().getDisplayMetrics().density));
                    rlp.rightMargin = (int) (66 * (getResources().getDisplayMetrics().density));
                }
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
                            if (fromFlag == 1 || fromFlag == 1604) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(LAT_LNG_BOUNDS, 10));
                                moveCamera(new LatLng(location.getLatitude(), location.getLongitude()), DEFAULT_ZOOM, "Device Location", mMap);
                            } else if (fromFlag != 5) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(LAT_LNG_BOUNDS, 10));
                            }
                            //TODO do camera change here
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MapDemoActivity", "Error trying to get last GPS location");
                        e.printStackTrace();
                        if (fromFlag != 5) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(LAT_LNG_BOUNDS, 10));
                        }
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

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses.size() > 0) {
                Address obj = addresses.get(0);
                String mainAddress = obj.getAddressLine(0);
                String add = obj.getAddressLine(0);
                add = add + "\n" + obj.getCountryName();
                add = add + "\n" + obj.getCountryCode();
                add = add + "\n" + obj.getAdminArea();
                add = add + "\n" + obj.getPostalCode();
                add = add + "\n" + obj.getSubAdminArea();
                add = add + "\n" + obj.getLocality();
                add = add + "\n" + obj.getSubThoroughfare();
                Log.e("IGA", "Address" + add);
                return mainAddress;
            } else {
                Toast.makeText(context, "Address still not selected.", Toast.LENGTH_SHORT).show();
                return "";
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    //for making custom info window
    protected void addCustomInfoWindow(IconGenerator iconFactory, CharSequence text, LatLng position) {
        if (mMap == null) {
            return;
        }
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).
                position(position).
                anchor(0, 1f);
        mMap.addMarker(markerOptions);
    }

    protected CharSequence makeCharSequence(String distance, String time) {
        String sequence = distance + " \n" + time;
        SpannableStringBuilder ssb = new SpannableStringBuilder(sequence);
        ssb.setSpan(new StyleSpan(ITALIC), 0, distance.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new StyleSpan(NORMAL), distance.length(), sequence.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

    protected CharSequence makeCharSequence(String distance) {
        String sequence = distance;
        SpannableStringBuilder ssb = new SpannableStringBuilder(sequence);
        ssb.setSpan(new StyleSpan(ITALIC), 0, distance.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new StyleSpan(NORMAL), distance.length(), sequence.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

}
