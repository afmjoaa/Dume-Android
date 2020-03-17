package io.dume.dume.components.library;

import android.graphics.Point;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import at.wirecube.additiveanimations.additive_animator.AdditiveAnimator;

/**
 * Created by amal.chandran on 04/11/17.
 */

class ProjectionHelper {

    LatLng mLineChartCenterLatLng;
    private float x, y;
    private Point previousPoint;
    private boolean isRouteSet;
    private float previousZoomLevel = -1.0f;
    private boolean isZooming = false;
    private Projection mProjection;

    private CameraPosition mCameraPosition;

    public void setCenterlatLng(LatLng lineChartCenterLatLng) {
        mLineChartCenterLatLng = lineChartCenterLatLng;
        isRouteSet = true;
    }

    public void onCameramove(GoogleMap mMap, RouteOverlayView mRouteOverlayView) {
        mCameraPosition = mMap.getCameraPosition();
        if (previousZoomLevel != mCameraPosition.zoom) {
            isZooming = true;
        }

        previousZoomLevel = mCameraPosition.zoom;

        mProjection = mMap.getProjection();

        Point point;
        if (mLineChartCenterLatLng == null) {
            point = new Point(mRouteOverlayView.getWidth() / 2,
                    mRouteOverlayView.getHeight() / 2);
        } else {
            point = mProjection.toScreenLocation(mLineChartCenterLatLng);
        }

        if (previousPoint != null) {
            x = previousPoint.x - point.x;
            y = previousPoint.y - point.y;
            Log.i("onCameraMove", "dx,dy : (" + x + "," + y + ")");
        }

        if (isRouteSet) {
            if (isZooming) {
                mRouteOverlayView.zoom(mCameraPosition.zoom);
            }

            //FrameLayout frameLayout = (FrameLayout) mRouteOverlayView.getParent();
            AdditiveAnimator.animate(mRouteOverlayView).rotation(-mCameraPosition.bearing).setDuration(2).start();
            AdditiveAnimator.animate(mRouteOverlayView).translationXBy(-x).translationYBy(-y).setDuration(2).start();
        }

        previousPoint = point;
    }

    public Projection getProjection() {
        return mProjection;
    }

    public CameraPosition getCameraPosition() {
        return mCameraPosition;
    }
}
