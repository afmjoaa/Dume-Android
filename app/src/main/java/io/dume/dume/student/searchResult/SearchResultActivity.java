package io.dume.dume.student.searchResult;

import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import carbon.widget.ImageView;
import io.dume.dume.R;
import io.dume.dume.student.pojo.CusStuAppComMapActivity;
import io.dume.dume.student.pojo.MyGpsLocationChangeListener;

public class SearchResultActivity extends CusStuAppComMapActivity implements OnMapReadyCallback,
        SearchResultContract.View, MyGpsLocationChangeListener {

    private GoogleMap mMap;
    private SearchResultContract.Presenter mPresenter;
    private static final int fromFlag = 6;
    private Toolbar toolbar;
    private SupportMapFragment mapFragment;
    private FrameLayout viewMusk;
    private Toolbar secondaryToolbar;
    private AppBarLayout defaultAppbarLayout;
    private AppBarLayout secondaryAppbarLayout;
    private LinearLayout llBottomSheet;
    private CoordinatorLayout coordinatorLayout;
    private BottomSheetBehavior bottomSheetBehavior;
    private LinearLayout verticalTextViewContainer;
    private ImageView mentorDisplayPic;
    private LinearLayout changingOrientationContainer;
    private LinearLayout.LayoutParams changingOrientationParams;
    private Boolean dragFirst = true;
    private TextView ageText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu6_activity_search_result);
        setActivityContextMap(this, fromFlag);
        mPresenter = new SearchResultPresenter(this, new SearchResultModel());
        mPresenter.searchResultEnqueue();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        getLocationPermission(mapFragment);

    }

    @Override
    public void findView() {
        toolbar = findViewById(R.id.toolbar);
        coordinatorLayout = findViewById(R.id.my_main_container);
        llBottomSheet = findViewById(R.id.searchBottomSheet);
        secondaryAppbarLayout = findViewById(R.id.secondary_Appbar);
        defaultAppbarLayout = findViewById(R.id.my_appbarLayout);
        secondaryToolbar = findViewById(R.id.secondary_toolbar);
        viewMusk = findViewById(R.id.view_musk);
        verticalTextViewContainer = findViewById(R.id.vertical_textview_container);
        mentorDisplayPic = findViewById(R.id.mentor_dp);
        changingOrientationContainer = findViewById(R.id.changing_orientation_layout);
        changingOrientationParams = (LinearLayout.LayoutParams) changingOrientationContainer.getLayoutParams();
        ageText = findViewById(R.id.text_two);


    }

    @Override
    public void initSearchResult() {
        setSupportActionBar(toolbar);
        settingStatusBarTransparent();
        setDarkStatusBarIcon();

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
    public void configSearchResult() {
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (BottomSheetBehavior.STATE_HIDDEN == newState) {

                } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    dragFirst = true;
                    setDarkStatusBarIcon();
                    viewMusk.setVisibility(View.GONE);
                    secondaryAppbarLayout.setVisibility(View.INVISIBLE);
                    defaultAppbarLayout.setVisibility(View.VISIBLE);
                    changingOrientationContainer.setOrientation(LinearLayout.HORIZONTAL);
                    changingOrientationParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    changingOrientationContainer.setLayoutParams(changingOrientationParams);
                    ageText.setGravity(Gravity.CENTER_VERTICAL | Gravity.START );
                    verticalTextViewContainer.animate()
                            .translationYBy((float) (-32.0f * (getResources().getDisplayMetrics().density)))
                            .setDuration(60)
                            .start();
                    mentorDisplayPic.animate()
                            .translationYBy((float) (-20.0f * (getResources().getDisplayMetrics().density)))
                            .setDuration(60)
                            .start();


                } else if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    setLightStatusBarIcon();
                    viewMusk.setVisibility(View.VISIBLE);
                    secondaryAppbarLayout.setVisibility(View.VISIBLE);
                    defaultAppbarLayout.setVisibility(View.INVISIBLE);
                    /*verticalTextViewContainer.animate()
                            .translationYBy((float) (32.0f * (getResources().getDisplayMetrics().density)))
                            .setDuration(60)
                            .start();
                    mentorDisplayPic.animate()
                            .translationYBy((float) (20.0f * (getResources().getDisplayMetrics().density)))
                            .setDuration(60)
                            .start();*/

                } else if (BottomSheetBehavior.STATE_DRAGGING == newState) {
                    viewMusk.setVisibility(View.VISIBLE);
                    secondaryAppbarLayout.setVisibility(View.VISIBLE);
                    defaultAppbarLayout.setVisibility(View.INVISIBLE);
                    changingOrientationContainer.setOrientation(LinearLayout.VERTICAL);
                    changingOrientationParams.height = (int) (150 * (getResources().getDisplayMetrics().density));
                    changingOrientationContainer.setLayoutParams(changingOrientationParams);
                    ageText.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    if (dragFirst) {
                        dragFirst = false;
                        verticalTextViewContainer.animate()
                                .translationYBy((float) (32.0f * (getResources().getDisplayMetrics().density)))
                                .setDuration(60)
                                .start();
                        mentorDisplayPic.animate()
                                .translationYBy((float) (20.0f * (getResources().getDisplayMetrics().density)))
                                .setDuration(60)
                                .start();
                    }

                } /*else if (BottomSheetBehavior.STATE_SETTLING == newState) {
                    loaderView.setVisibility(View.VISIBLE);
                }*/
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                llBottomSheet.animate().scaleX(1 + (slideOffset * 0.058f)).setDuration(0).start();
                viewMusk.animate().alpha(2 * slideOffset).setDuration(0).start();
                secondaryAppbarLayout.animate().alpha(slideOffset).scaleX(slideOffset).scaleY(slideOffset).setDuration(0).start();
                mentorDisplayPic.animate()
                        .scaleX((float) (1 + (0.55 * slideOffset)))
                        .scaleY((float) (1 + (0.55 * slideOffset)))
                        .setDuration(0).start();
                verticalTextViewContainer.animate()
                        .scaleX((float) (1 + (0.22 * slideOffset)))
                        .scaleY((float) (1 + (0.22 * slideOffset)))
                        .setDuration(0).start();

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(
                this, R.raw.map_style_default_json);
        googleMap.setMapStyle(style);
        mMap = googleMap;
        mMap.setPadding((int) (6 * (getResources().getDisplayMetrics().density)), 0, 0, (int) (150 * (getResources().getDisplayMetrics().density)));

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search_set:
                //Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_help:
                //Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_list_view:
                //Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMyGpsLocationChanged(Location location) {

    }
}
