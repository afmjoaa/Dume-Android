package io.dume.dume.student.pojo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
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
import com.google.android.gms.tasks.Task;

import io.dume.dume.R;
import io.dume.dume.broadcastReceiver.NetworkChangeReceiver;

public class CusStuAppComMapActivity extends CustomStuAppCompatActivity implements
        OnCompleteListener<LocationSettingsResponse> {

    //private variable
    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    public NetworkChangeReceiver networkChangeReceiverForGps;
    private LocationRequest locationRequest;
    private carbon.widget.RelativeLayout toggleGpsControlBtn;
    private GoogleMap mMap;

    //protected variable
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    protected View MAPCONTAINER;
    protected Task<LocationSettingsResponse> result;
    protected FusedLocationProviderClient mFusedLocationProviderClient;

    //public variable
    public static final float DEFAULT_ZOOM = 15f;
    public static Boolean MLOCATIONPERMISSIONGRANTED = false;


    public void setActivityContextMap(Context context, int i) {
        setActivityContext(context, i);
        this.context = context;
        this.activity = (Activity) context;
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

        // Create an IntentFilter instance.
        IntentFilter intentFilter = new IntentFilter();
        // Add network connectivity change action.
        intentFilter.addAction("android.location.PROVIDERS_CHANGED");
        // Set broadcast receiver priority.
        intentFilter.setPriority(101);
        // Create a network change broadcast receiver.
        networkChangeReceiverForGps = new NetworkChangeReceiver();
        // Register the broadcast receiver with the intent filter object.
        registerReceiver(networkChangeReceiverForGps, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiverForGps);
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
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");

                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");

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
        if (fromFlag == 1) {
            toggleGpsControlBtn = rootView.findViewById(R.id.toggle_gps_dialogue_btn);
        }
    }


    //default map functions
    //++++++++++_________++++++++++
    public void getLocationPermission(SupportMapFragment mapFragment) {
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

    // Initializing the map here
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
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            if (currentLocation != null) {
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM
                                        , "Device Location", mMap);
                            }
                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(context, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

}
