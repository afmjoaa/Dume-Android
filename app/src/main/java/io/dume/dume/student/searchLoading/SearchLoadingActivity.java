package io.dume.dume.student.searchLoading;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.varunest.loader.TheGlowingLoader;

import java.util.ArrayList;
import java.util.List;

import carbon.widget.ImageView;
import io.dume.dume.R;
import io.dume.dume.student.pojo.CusStuAppComMapActivity;
import io.dume.dume.student.pojo.MyGpsLocationChangeListener;
import io.dume.dume.student.searchResult.SearchResultActivity;

import static io.dume.dume.util.DumeUtils.configureAppbarWithoutColloapsing;

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
    private CollapsingToolbarLayout secondaryCollapsableLayout;
    private ActionBar supportActionBarMain;
    private ActionBar supportActionBarSecond;
    private RecyclerView searchDetailRecycler;
    private SearchDetailAdapter searchDetailRecyclerAdapter;
    private RecyclerView packageRecyclerView;
    private SearchDetailAdapter packageRecyclerAdapter;
    private ImageView searchImageView;
    private BottomSheetDialog mCancelBottomSheetDialog;
    private View cancelsheetRootView;


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

        //adding the search recycler adapter
        List<SearchDetailData> reviewData = new ArrayList<>();
        searchDetailRecyclerAdapter = new SearchDetailAdapter(this, reviewData);
        searchDetailRecycler.setAdapter(searchDetailRecyclerAdapter);
        searchDetailRecycler.setLayoutManager(new LinearLayoutManager(this));
        //add the package recycler adapter
        packageRecyclerAdapter = new SearchDetailAdapter(this, reviewData);
        packageRecyclerView.setAdapter(packageRecyclerAdapter);
        packageRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void findView() {

        loaderView = findViewById(R.id.loading_view);
        coordinatorLayout = findViewById(R.id.my_main_container);
        llBottomSheet = findViewById(R.id.searchBottomSheet);
        secondaryAppbarLayout = findViewById(R.id.secondary_Appbar);
        defaultAppbarLayout = findViewById(R.id.my_appbarLayout);
        defaultToolbar = findViewById(R.id.toolbar);
        viewMuskOne = findViewById(R.id.secondary_view_musk);
        loadingCancelBtn = findViewById(R.id.loading_cancel_btn);
        secondaryCollapsableLayout = findViewById(R.id.secondary_collapsing_toolbar);
        secondaryToolbar = findViewById(R.id.secondary_toolbar);
        searchDetailRecycler = findViewById(R.id.search_details_recycler);
        packageRecyclerView = findViewById(R.id.package_detail_recycler);
        searchImageView = findViewById(R.id.searching_imageView);
    }

    @Override
    public void initSearchLoading() {
        //bottom sheet height fix
        configureAppbarWithoutColloapsing(this, "");

        secondaryCollapsableLayout.setCollapsedTitleTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/Cairo-Light.ttf"));
        secondaryCollapsableLayout.setExpandedTitleTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/Cairo-Light.ttf"));
        secondaryCollapsableLayout.setTitle("Requesting");
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_more_vert_white_24dp);
        secondaryToolbar.setOverflowIcon(drawable);

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

                    //hack
                    setSupportActionBar(defaultToolbar);
                    supportActionBarMain = getSupportActionBar();
                    if (supportActionBarMain != null) {
                        supportActionBarMain.setDisplayHomeAsUpEnabled(true);
                        supportActionBarMain.setDisplayShowHomeEnabled(true);
                    }

                } else if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    setLightStatusBarIcon();
                    viewMuskOne.setVisibility(View.VISIBLE);
                    //loaderView.setVisibility(View.INVISIBLE);
                    secondaryAppbarLayout.setVisibility(View.VISIBLE);
                    defaultAppbarLayout.setVisibility(View.INVISIBLE);
                    //hack
                    setSupportActionBar(secondaryToolbar);
                    supportActionBarSecond = getSupportActionBar();
                    if (supportActionBarSecond != null) {
                        supportActionBarSecond.setDisplayHomeAsUpEnabled(true);
                        supportActionBarSecond.setDisplayShowHomeEnabled(true);
                    }

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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }else{
                super.onBackPressed();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(
                this, R.raw.map_style_default_no_landmarks);
        googleMap.setMapStyle(style);

        mMap = googleMap;
        mMap.setPadding((int) (10 * (getResources().getDisplayMetrics().density)), 0, 0, (int) (72 * (getResources().getDisplayMetrics().density)));

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

    @Override
    public void cancelBtnClicked() {
        mCancelBottomSheetDialog = new BottomSheetDialog(this);
        cancelsheetRootView = this.getLayoutInflater().inflate(R.layout.custom_bottom_sheet_dialogue_cancel, null);
        mCancelBottomSheetDialog.setContentView(cancelsheetRootView);
        mCancelBottomSheetDialog.show();
    }

    public void onSearchLoadingViewCLicked(View view) {
        mPresenter.onSearchLoadingIntracted(view);
    }

    public List<SearchDetailData> getFinalData() {
        List<SearchDetailData> data = new ArrayList<>();
        String[] searchTextMain = getResources().getStringArray(R.array.SearchDetailMain);
        String[] searchTextSub = getResources().getStringArray(R.array.SearchDetailSub);
        String[] searchTextChange = getResources().getStringArray(R.array.SearchDetailSub);
        int[] imageIcons = {
                R.drawable.ic_set_location_on_map,
                R.drawable.ic_arrow_forward_black_24dp,
                R.drawable.ic_category,
                R.drawable.ic_medium,
                R.drawable.ic_class,
                R.drawable.ic_subject,
                R.drawable.ic_payment,
                R.drawable.ic_gender_preference,
                R.drawable.dume_gang_image,
                R.drawable.ic_seven_days,
                R.drawable.ic_preffered_day,
                R.drawable.ic_time

        };

   /*     for (int i = 0; i < primaryText.length && i < secondaryText.length && i < imageIcons.length; i++) {
            SearchDetailData current = new SearchDetailData();
            current.primaryText = primaryText[i];
            current.secondaryText = secondaryText[i];
            current.imageSrc = imageIcons[i];
            data.add(current);
        }*/
        return data;
    }
}
