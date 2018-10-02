package io.dume.dume.student.searchLoading;

import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.varunest.loader.TheGlowingLoader;

import io.dume.dume.R;
import io.dume.dume.student.pojo.CusStuAppComMapActivity;
import io.dume.dume.student.pojo.MyGpsLocationChangeListener;
import io.dume.dume.student.searchResult.SearchResultActivity;

public class SearchLoadingActivity extends CusStuAppComMapActivity implements OnMapReadyCallback,
        SearchLoadingContract.View, MyGpsLocationChangeListener {

    private GoogleMap mMap;
    private SearchLoadingContract.Presenter mPresenter;
    private static final int fromFlag = 5;
    private SupportMapFragment mapFragment;
    private android.widget.LinearLayout llBottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private CoordinatorLayout coordinatorLayout;
    private AppBarLayout secondaryAppbarLayout;
    private Toolbar secondaryToolbar;
    private FrameLayout viewMuskOne;
    private TheGlowingLoader loaderView;
    private Toolbar defaultToolbar;
    private AppBarLayout defaultAppbarLayout;
    private Button loadingCancelBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu7_activity_search_loading);
        setActivityContextMap(this, fromFlag);
        mPresenter = new SearchLoadingPresenter(this, new SearchLoadingModel());
        mPresenter.searchLoadingEnqueue();
        settingStatusBarTransparent();
        setDarkStatusBarIcon();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        getLocationPermission(mapFragment);
    }

    @Override
    public void findView() {

        loaderView = findViewById(R.id.loading_view);
        coordinatorLayout = findViewById(R.id.my_main_container);
        llBottomSheet = findViewById(R.id.searchBottomSheet);
        secondaryAppbarLayout = findViewById(R.id.secondary_Appbar);
        defaultAppbarLayout = findViewById(R.id.my_appbarLayout);
        defaultToolbar = findViewById(R.id.toolbar);
        secondaryToolbar = findViewById(R.id.secondary_toolbar);
        viewMuskOne = findViewById(R.id.secondary_view_musk);
        loadingCancelBtn = findViewById(R.id.loading_cancel_btn);
    }

    @Override
    public void initSearchLoading() {
        //bottom sheet height fix
        setSupportActionBar(defaultToolbar);

        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);

        ViewTreeObserver vto = llBottomSheet.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                llBottomSheet.getLayoutParams().height = (llBottomSheet.getHeight() - secondaryAppbarLayout.getHeight() - (int) (1 * (getResources().getDisplayMetrics().density)));
                llBottomSheet.requestLayout();
                bottomSheetBehavior.onLayoutChild(coordinatorLayout, llBottomSheet, ViewCompat.LAYOUT_DIRECTION_LTR);
                ViewTreeObserver obs = llBottomSheet.getViewTreeObserver();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeOnGlobalLayoutListener(this);
                } else {
                    obs.removeGlobalOnLayoutListener(this);
                }
            }

        });

    }

    @Override
    public void configSearchLoading() {
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (BottomSheetBehavior.STATE_HIDDEN == newState) {

                } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    setDarkStatusBarIcon();
                    viewMuskOne.setVisibility(View.GONE);
                    loaderView.setVisibility(View.VISIBLE);
                    secondaryAppbarLayout.setVisibility(View.INVISIBLE);
                    defaultAppbarLayout.setVisibility(View.VISIBLE);

                } else if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    setLightStatusBarIcon();
                    viewMuskOne.setVisibility(View.VISIBLE);
                    //loaderView.setVisibility(View.INVISIBLE);
                    secondaryAppbarLayout.setVisibility(View.VISIBLE);
                    defaultAppbarLayout.setVisibility(View.INVISIBLE);


                } else if (BottomSheetBehavior.STATE_DRAGGING == newState) {
                    viewMuskOne.setVisibility(View.VISIBLE);
                    secondaryAppbarLayout.setVisibility(View.VISIBLE);
                    defaultAppbarLayout.setVisibility(View.INVISIBLE);

                } /*else if (BottomSheetBehavior.STATE_SETTLING == newState) {
                    loaderView.setVisibility(View.VISIBLE);
                }*/
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                llBottomSheet.animate().scaleX(1 + (slideOffset * 0.058f)).setDuration(0).start();
                viewMuskOne.animate().alpha(2 * slideOffset).setDuration(0).start();
                loaderView.animate().alpha(1 - (3 * slideOffset)).setDuration(0).start();
                secondaryAppbarLayout.animate().alpha(slideOffset).scaleX(slideOffset).scaleY(slideOffset).setDuration(0).start();

            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onMyGpsLocationChanged(Location location) {

    }

    @Override
    public void gotoSearchResult() {
        startActivity(new Intent(this, SearchResultActivity.class));
    }

    public void onSearchLoadingViewCLicked(View view) {
        mPresenter.onSearchLoadingIntracted(view);
    }
}
