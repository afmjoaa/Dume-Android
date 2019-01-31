package io.dume.dume.student.searchLoading;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.varunest.loader.TheGlowingLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import carbon.widget.ImageView;
import io.dume.dume.R;
import io.dume.dume.student.homePage.HomePageActivity;
import io.dume.dume.student.pojo.CusStuAppComMapActivity;
import io.dume.dume.student.pojo.MyGpsLocationChangeListener;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.student.searchResult.SearchResultActivity;
import io.dume.dume.teacher.homepage.TeacherContract;

import static io.dume.dume.util.DumeUtils.configureAppbarWithoutColloapsing;
import static io.dume.dume.util.DumeUtils.firstFour;
import static io.dume.dume.util.DumeUtils.firstThree;
import static io.dume.dume.util.ImageHelper.getRoundedCornerBitmap;
import static io.dume.dume.util.ImageHelper.getRoundedCornerBitmapSquare;

public class SearchLoadingActivity extends CusStuAppComMapActivity implements OnMapReadyCallback,
        SearchLoadingContract.View, MyGpsLocationChangeListener {

    private static final String TAG = "SearchLoadingActivity";
    private CameraPosition cameraPosition;
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
    private SearchPackageAdapter packageRecyclerAdapter;
    private ImageView searchImageView;
    private BottomSheetDialog mCancelBottomSheetDialog;
    private View cancelsheetRootView;
    private NestedScrollView bottomSheetNSV;
    private FrameLayout alwaysViewMusk;
    private View mCustomMarkerView;
    private ImageView mMarkerImageView;
    private IconGenerator iconFactory;
    private SearchLoadingModel mModel;
    private Boolean saveDone = false;
    private String defaultUrl;
    private long tStart;
    private BottomSheetDialog mNoResultBSD;
    private View mNoResultRootView;
    private TextView noResultMainText;
    private TextView noResultSubText;
    private Button goBackEditBtn;
    private Button homePageBtn;
    private String retrivedAction;
    private TextView mainText;
    private TextView subText;
    private Button cancelYesBtn;
    private Button cancelNoBtn;

    @Override
    public void flush(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu7_activity_search_loading);
        setActivityContextMap(this, fromFlag);
        mModel = new SearchLoadingModel(this);
        mPresenter = new SearchLoadingPresenter(this, mModel);
        tStart = System.currentTimeMillis();
        mPresenter.searchLoadingEnqueue();
        settingStatusBarTransparent();
        setDarkStatusBarIcon();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        getLocationPermission(mapFragment);

        //adding the search recycler adapter
        searchDetailRecyclerAdapter = new SearchDetailAdapter(this, getSearchDetailData());
        searchDetailRecycler.setAdapter(searchDetailRecyclerAdapter);
        searchDetailRecycler.setLayoutManager(new LinearLayoutManager(this));

        //add the package recycler adapter
        packageRecyclerAdapter = new SearchPackageAdapter(this, getSearchPackageData());
        packageRecyclerView.setAdapter(packageRecyclerAdapter);
        packageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @NonNull
    private List<SearchDetailData> getSearchDetailData() {
        List<SearchDetailData> grabingInfoData = new ArrayList<>();
        SearchDetailData current = new SearchDetailData();
        current.setItemName((String) searchDataStore.getForWhom().get("name"));
        current.setItemInfo("Student name");
        if ((Boolean) searchDataStore.getForWhom().get("is_self")) {
            current.setItemChange(searchDataStore.getAvatarString());
            current.setImageSrc(null);
        } else {
            current.setImageSrc((Bitmap) searchDataStore.getForWhom().get("photo"));
            current.setItemChange(null);
        }
        grabingInfoData.add(current);
        for (int i = 0; i < searchDataStore.getQueryList().size(); i++) {
            SearchDetailData myCurrent = new SearchDetailData();
            myCurrent.setItemName((String) searchDataStore.getQueryList().get(i));
            myCurrent.setItemInfo((String) searchDataStore.getQueryListName().get(i));
            grabingInfoData.add(myCurrent);
        }
        return grabingInfoData;
    }

    @NonNull
    private List<SearchDetailData> getSearchPackageData() {
        List<SearchDetailData> reviewData = new ArrayList<>();
        SearchDetailData packageCurrent = new SearchDetailData();
        packageCurrent.setItemName((String) searchDataStore.getPackageName());
        packageCurrent.setItemInfo("Package name");
        reviewData.add(packageCurrent);
        packageCurrent = new SearchDetailData();
        String selectedDays = (String) searchDataStore.getPreferredDays().get("selected_days");
        String finalWeekOfDays = "";
        List<String> myWeekOfDays = Arrays.asList(selectedDays.split(","));
        for (int fuck = 0; fuck < myWeekOfDays.size(); fuck++) {
            if (fuck == 0) {
                finalWeekOfDays = firstThree(myWeekOfDays.get(fuck));

            } else {
                finalWeekOfDays = finalWeekOfDays + "," + firstFour(myWeekOfDays.get(fuck));
            }
        }
        packageCurrent.setItemName(finalWeekOfDays);
        packageCurrent.setItemInfo("Preferred days");
        reviewData.add(packageCurrent);
        packageCurrent = new SearchDetailData();
        packageCurrent.setItemName((String) searchDataStore.getStartDate().get("date_string"));
        packageCurrent.setItemInfo("Start date");
        reviewData.add(packageCurrent);
        packageCurrent = new SearchDetailData();
        packageCurrent.setItemName((String) searchDataStore.getStartTime().get("time_string"));
        packageCurrent.setItemInfo("Start time");
        reviewData.add(packageCurrent);
        return reviewData;
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
        bottomSheetNSV = findViewById(R.id.bottom_sheet_scroll_view);
        alwaysViewMusk = findViewById(R.id.view_musk);
        mCustomMarkerView = ((LayoutInflater) Objects.requireNonNull(getSystemService(LAYOUT_INFLATER_SERVICE))).inflate(R.layout.custom_marker_view, null);
        mMarkerImageView = mCustomMarkerView.findViewById(R.id.profile_image);
        iconFactory = new IconGenerator(this);
    }

    @Override
    public void initSearchLoading() {
        retrivedAction = getIntent().getAction();
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
        bottomSheetNSV.setNestedScrollingEnabled(true);
        bottomSheetNSV.setSmoothScrollingEnabled(true);
        //init the bottom sheet cancel
        mCancelBottomSheetDialog = new BottomSheetDialog(this);
        cancelsheetRootView = this.getLayoutInflater().inflate(R.layout.custom_bottom_sheet_dialogue_cancel, null);
        mCancelBottomSheetDialog.setContentView(cancelsheetRootView);
        mainText = mCancelBottomSheetDialog.findViewById(R.id.main_text);
        subText = mCancelBottomSheetDialog.findViewById(R.id.sub_text);
        cancelYesBtn = mCancelBottomSheetDialog.findViewById(R.id.cancel_yes_btn);
        cancelNoBtn = mCancelBottomSheetDialog.findViewById(R.id.cancel_no_btn);
        if (cancelNoBtn != null && cancelYesBtn != null) {
            cancelNoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCancelBottomSheetDialog.dismiss();
                }
            });

            cancelYesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCancelBottomSheetDialog.dismiss();
                    searchDataStore.setFirstTime(false);
                    onBackPressed();
                }
            });
        }

        //init no result
        mNoResultBSD = new BottomSheetDialog(this);
        mNoResultRootView = this.getLayoutInflater().inflate(R.layout.custom_bottom_sheet_dialogue_cancel, null);
        mNoResultBSD.setContentView(mNoResultRootView);
        noResultMainText = mNoResultBSD.findViewById(R.id.main_text);
        noResultSubText = mNoResultBSD.findViewById(R.id.sub_text);
        goBackEditBtn = mNoResultBSD.findViewById(R.id.cancel_yes_btn);
        homePageBtn = mNoResultBSD.findViewById(R.id.cancel_no_btn);
        mNoResultBSD.setCancelable(false);
        mNoResultBSD.setCanceledOnTouchOutside(false);
        if (goBackEditBtn != null && homePageBtn != null && noResultMainText != null  && noResultSubText != null) {
            noResultMainText.setText("Sorry !!!");
            noResultSubText.setText("No mentor available for your specific query...");
            homePageBtn.setText("Goto, Homepage");
            goBackEditBtn.setText("Edit, Query");

            if (retrivedAction != null) {
                switch (retrivedAction){
                    case "from_HPA":
                        homePageBtn.setVisibility(View.VISIBLE);
                        goBackEditBtn.setVisibility(View.GONE);
                        break;
                    case "from_GPA":
                        homePageBtn.setVisibility(View.VISIBLE);
                        goBackEditBtn.setVisibility(View.VISIBLE);
                        break;
                }
            }
            homePageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(retrivedAction.equals("from_HPA")){
                        searchDataStore.setFirstTime(false);
                        onBackPressed();
                        mNoResultBSD.dismiss();
                    }else{
                        Intent intent = new Intent(SearchLoadingActivity.this, HomePageActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        mNoResultBSD.dismiss();
                    }
                }
            });

            goBackEditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    searchDataStore.setFirstTime(false);
                    onBackPressed();
                    mNoResultBSD.dismiss();
                }
            });
        }
    }

    @Override
    public void configSearchLoading() {
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
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
                    bottomSheetNSV.post(new Runnable() {
                        @Override
                        public void run() {
                            bottomSheetNSV.fling(0);
                            bottomSheetNSV.scrollTo(0, 0);
                            bottomSheetNSV.fling(-10000);
                        }
                    });
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
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                llBottomSheet.animate().scaleX(1 + (slideOffset * 0.058f)).setDuration(0).start();
                viewMuskOne.animate().alpha(2 * slideOffset).setDuration(0).start();
                loaderView.animate().alpha(1 - (3 * slideOffset)).setDuration(0).start();
                secondaryAppbarLayout.animate().alpha(slideOffset).scaleX(slideOffset).scaleY(slideOffset).setDuration(0).start();

            }
        });
        //gathering the touch event here
        alwaysViewMusk.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(
                this, R.raw.loading_map_style);
        googleMap.setMapStyle(style);
        mMap = googleMap;
        onMapReadyListener(mMap);
        onMapReadyGeneralConfig();
        mMap.setPadding((int) (10 * (getResources().getDisplayMetrics().density)), 0, 0, (int) (72 * (getResources().getDisplayMetrics().density)));
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.setMyLocationEnabled(false);
        addCustomMarkerFromURL(searchDataStore.getAvatarString(), searchDataStore.getAnchorPoint());
        mMap.addCircle(new CircleOptions()
                .center(searchDataStore.getAnchorPoint())
                .radius(1400)//meter radius
                .strokeColor(0xFF0277bd)
                .fillColor(0x0c64b5f6)
                .strokeWidth(1.5f)
        );
        viewMuskOne.postDelayed(new Runnable() {
            @Override
            public void run() {
                //give anchor point here
                moveSearchLoadingCamera(searchDataStore.getAnchorPoint(), DEFAULT_ZOOM, "Device Location", mMap);
            }
        }, 50L);
    }

    @Override
    public void onMyGpsLocationChanged(Location location) {
        //testing
        //getDeviceLocation(mMap);
        //nothing to do where as the location will be the anchor point
    }

    @Override
    public void gotoSearchResult() {
        startActivity(new Intent(this, SearchResultActivity.class));
    }


    @Override
    public void noResultDialogue() {
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        long elseDelta = 3000 - tDelta;
        if (tDelta >= 3000) {
            mNoResultBSD.show();
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mNoResultBSD.show();
                        }
                    });
                }
            }, elseDelta);
        }
    }

    @Override
    public void cancelBtnClicked() {
        mCancelBottomSheetDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            } else {
                onBackPressed();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (searchDataStore.getFirstTime()) {
            cancelBtnClicked();
        } else {
            //show dialogue of discard changes
            super.onBackPressed();
            searchDataStore.setFirstTime(true);
        }
    }

    public void onSearchLoadingViewCLicked(View view) {
        mPresenter.onSearchLoadingIntracted(view);
    }

    private void addCustomMarkerFromURL(String url, LatLng lattitudeLongitude) {
        if (mMap == null) {
            return;
        }
        if (url != null && !url.equals("")) {
            Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(url)
                    .apply(new RequestOptions().override((int) (28 * (getResources().getDisplayMetrics().density)), (int) (28 * (getResources().getDisplayMetrics().density))).centerCrop().placeholder(R.drawable.alias_profile_icon))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                            mMap.addMarker(new MarkerOptions().position(lattitudeLongitude)
                                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, resource))));
                            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lattitudeLongitude, 15f));
                        }
                    });
        } else {
            if (searchDataStore.getGender().equals("Male") || searchDataStore.getGender().equals("")) {
                defaultUrl = "https://firebasestorage.googleapis.com/v0/b/dume-2d063.appspot.com/o/avatar.png?alt=media&token=801c75b7-59fe-4a13-9191-186ef50de707";
            } else {
                defaultUrl = "https://firebasestorage.googleapis.com/v0/b/dume-2d063.appspot.com/o/avatar_female.png?alt=media&token=7202ea91-4f0d-4bd6-838e-8b73d0db13eb";
            }
            Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(defaultUrl)
                    .apply(new RequestOptions().override((int) (28 * (getResources().getDisplayMetrics().density)), (int) (28 * (getResources().getDisplayMetrics().density))).centerCrop().placeholder(R.drawable.alias_profile_icon))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                            mMap.addMarker(new MarkerOptions().position(lattitudeLongitude)
                                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, resource))));
                            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lattitudeLongitude, 15f));
                        }
                    });
        }


        iconFactory.setStyle(IconGenerator.STYLE_DEFAULT);
        iconFactory.setTextAppearance(this, R.style.MyCustomInfoWindowTextApp);
        iconFactory.setBackground(getResources().getDrawable(R.drawable.custom_info_window_vector));
        iconFactory.setContentPadding((int) (27 * (getResources().getDisplayMetrics().density)), (int) (2 * (getResources().getDisplayMetrics().density)), 0, (int) (6 * (getResources().getDisplayMetrics().density)));
        addCustomInfoWindow(iconFactory, makeCharSequence("Radius",   "2 km") , lattitudeLongitude);
    }

    @Override
    public void notifyRadious(int radius) {
        addCustomInfoWindow(iconFactory, makeCharSequence("Radius", Integer.toString(radius) + " km"), searchDataStore.getAnchorPoint());
    }

    @Override
    public void showResultActivty() {
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        long elseDelta = 3000 - tDelta;
        if (tDelta >= 3000) {
            Intent intent = new Intent(SearchLoadingActivity.this, SearchResultActivity.class);
            if (retrivedAction != null) {
                intent.setAction(retrivedAction);
            }
            startActivity(intent);
            finish();
        } else {
            /*Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                }
            }, elseDelta);*/
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(SearchLoadingActivity.this, SearchResultActivity.class);
                            if (retrivedAction != null) {
                                intent.setAction(retrivedAction);
                            }
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }, elseDelta);
        }
    }

    //testing custom marker code here
    private Bitmap getMarkerBitmapFromView(View view, Bitmap bitmap) {

        mMarkerImageView.setImageBitmap(getRoundedCornerBitmap(bitmap, (int) (28 * (getResources().getDisplayMetrics().density))));
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;

    }

    @Override
    public void saveToDB() {
        if (!checkIfInDB(searchDataStore.getJizz())) {
            String preIdentify = (String) searchDataStore.getDocumentSnapshot().get("next_rs_write");
            String identify = "rs_";
            identify = identify + preIdentify;
            mModel.updateRecentSearch(identify, searchDataStore.genRetMainMap(), new TeacherContract.Model.Listener<Void>() {
                @Override
                public void onSuccess(Void list) {
                    saveDone = true;
                }

                @Override
                public void onError(String msg) {
                    Log.e(TAG, msg);
                    saveDone = true;
                }
            });
        }
    }

    @Override
    public boolean checkIfInDB(Map<String, Object> jizz) {
        boolean found = false;
        Map<String, Map<String, Object>> beforeSearched = (Map<String, Map<String, Object>>) searchDataStore.getDocumentSnapshot().get("recent_search");
        if (beforeSearched != null && beforeSearched.size() > 0) {
            for (Map.Entry<String, Map<String, Object>> entry : beforeSearched.entrySet()) {
                if (entry.getValue().get("jizz").equals(jizz)) {
                    found = true;
                }
            }
        }
        return found;
    }


}
