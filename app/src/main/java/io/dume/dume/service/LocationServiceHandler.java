package io.dume.dume.service;

import android.location.Location;

public interface LocationServiceHandler {

    void onGpsProviderDisabled();

    void onGpsProviderEnabled();

    void onNetworkDisabled();

    void onNetworkEnabled();

    void onlocationChangedByGps(Location location);

    void onlocationChangedByNetwork(Location location);

    void onLocationServiceHandlerError(Exception e);
}
