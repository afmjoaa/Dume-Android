package io.dume.dume.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.Nullable;
import android.util.Log;

public class MyLocationService extends Service {
    private static final String TAG = "BOOMBOOMTESTEPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    private final MyLocationService self = this;
    LocationServiceHandler mLocationServiceHandler;

    public void setLocationServiceHandler(LocationServiceHandler mLocationServiceHandler) {
        this.mLocationServiceHandler = mLocationServiceHandler;
    }

    public class LocalBinder extends Binder {
        public MyLocationService getService() {
            Log.d(TAG, "getService()");
            return MyLocationService.this;
        }
    }

    private final IBinder mBinder = new LocalBinder();

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            if (mLocationServiceHandler != null) {
                if (location.getProvider().equals("gps")) {
                    mLocationServiceHandler.onlocationChangedByGps(location);
                }
                if (location.getProvider().equals("network")) {
                    mLocationServiceHandler.onlocationChangedByNetwork(location);
                }
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
            if (mLocationServiceHandler == null) {
                Log.e(TAG, "onProviderEnabled: null found");
            } else {
                Log.e(TAG, "onProviderEnabled:not null found");
            }
            if (mLocationServiceHandler != null) {
                if (provider.equals("gps")) {
                    mLocationServiceHandler.onGpsProviderDisabled();
                }
                if (provider.equals("network")) {
                    mLocationServiceHandler.onNetworkDisabled();
                }
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
            if (mLocationServiceHandler == null) {
                Log.e(TAG, "onProviderEnabled: null found");
            } else {
                Log.e(TAG, "onProviderEnabled:not null found");
            }
            if (mLocationServiceHandler != null) {
                if (provider.equals("gps")) {
                    mLocationServiceHandler.onGpsProviderEnabled();
                }
                if (provider.equals("network")) {
                    mLocationServiceHandler.onNetworkEnabled();
                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.e(TAG, "onStartCommand");
        return START_STICKY;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        Log.e(TAG, "onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind()");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind()");
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "onRebind()");
        super.onRebind(intent);
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

}