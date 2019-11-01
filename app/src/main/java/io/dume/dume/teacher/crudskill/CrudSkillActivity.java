package io.dume.dume.teacher.crudskill;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.maps.android.ui.IconGenerator;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import carbon.widget.LinearLayout;
import io.dume.dume.R;
import io.dume.dume.customView.HorizontalLoadView;
import io.dume.dume.student.grabingInfo.GrabingInfoActivity;
import io.dume.dume.student.pojo.CusStuAppComMapActivity;
import io.dume.dume.student.pojo.MyGpsLocationChangeListener;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.student.studentHelp.StudentHelpActivity;
import io.dume.dume.teacher.adapters.CategoryAdapter;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.GridSpacingItemDecoration;

import static io.dume.dume.util.DumeUtils.configureAppbar;
import static io.dume.dume.util.ImageHelper.getRoundedCornerBitmap;

public class CrudSkillActivity extends CusStuAppComMapActivity implements CrudContract.View,
        MyGpsLocationChangeListener, OnMapReadyCallback {
    @BindView(R.id.crudLoad)
    HorizontalLoadView loadView;
    private CrudContract.Presenter presenter;
    @BindView(R.id.categoryRV)
    RecyclerView categoryGrid;
    private String fromWhere;
    private static final String TAG = "CrudSkillActivity";
    private int spacing;
    private static int fromFlag = 0;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private NestedScrollView mainScrollingContainer;
    private AppBarLayout appBarLayout;
    private LinearLayout hackElevation;
    private FrameLayout alwaysViewMusk;
    private String defaultUrl;
    private View mCustomMarkerView;
    private carbon.widget.ImageView mMarkerImageView;
    private IconGenerator iconFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_skill);
        setActivityContextMap(this, fromFlag);
        ButterKnife.bind(this);
        presenter = new CrudPresent(new CrudModel(), this);
        presenter.enqueue();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        getLocationPermission(mapFragment);

    }

    @Override
    public void findView() {
        mainScrollingContainer = findViewById(R.id.crudScroll);
        appBarLayout = findViewById(R.id.settingsAppbar);
        hackElevation = findViewById(R.id.hack_elevation);
        alwaysViewMusk = findViewById(R.id.always_view_musk);
        mCustomMarkerView = ((LayoutInflater) Objects.requireNonNull(getSystemService(LAYOUT_INFLATER_SERVICE))).inflate(R.layout.custom_marker_view, null);
        mMarkerImageView = mCustomMarkerView.findViewById(R.id.profile_image);
        iconFactory = new IconGenerator(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void init() {
        //configureAppbar(this, "Add New Skill");
        fromWhere = getIntent().getAction();
        //getting the width
        int[] wh = DumeUtils.getScreenSize(this);
        spacing = (int) ((wh[0] - ((330) * (getResources().getDisplayMetrics().density))) / 4);
        float mDensity = getResources().getDisplayMetrics().density;
        //initializing the title
        configureAppbar(this, "Select category");
        mainScrollingContainer.getBackground().setAlpha(90);
        //testing code here for elevation
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                //  Collapsed
                hackElevation.setVisibility(View.GONE);
            } else {
                //Expanded
                hackElevation.setVisibility(View.VISIBLE);
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
    public void showLoading() {
        if (!loadView.isRunningAnimation()) {
            loadView.setVisibility(View.VISIBLE);
            loadView.startLoading();
        }
    }

    @Override
    public void hideLoading() {
        if (loadView.isRunningAnimation()) {
            loadView.setVisibility(View.GONE);
            loadView.stopLoading();
        }
    }

    @Override
    public void flush(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void setUpRecyclerView(List<String> categoryList, List<Integer> drawableList) {
        categoryGrid.setLayoutManager(new GridLayoutManager(this, 3));
        categoryGrid.addItemDecoration(new GridSpacingItemDecoration(3, spacing, true));
        categoryGrid.setAdapter(new CategoryAdapter(categoryList, drawableList) {
            @Override
            protected void onCategoryItemClick(View view, int position) {
                if (fromWhere.equals(DumeUtils.STUDENT)) {
                    startActivity(new Intent(getApplicationContext(), GrabingInfoActivity.class).setAction(DumeUtils.STUDENT).putExtra(DumeUtils.SELECTED_ID, position));
                } else if (fromWhere.equals(DumeUtils.TEACHER)) {
                    startActivity(new Intent(getApplicationContext(), GrabingInfoActivity.class).setAction(DumeUtils.TEACHER).putExtra(DumeUtils.SELECTED_ID, position));
                } else if (fromWhere.equals(DumeUtils.BOOTCAMP)) {
                    startActivity(new Intent(getApplicationContext(), GrabingInfoActivity.class).setAction(DumeUtils.BOOTCAMP).putExtra(DumeUtils.SELECTED_ID, position));
                } else if (fromWhere.equals("frag_" + DumeUtils.TEACHER)) {
                    startActivity(new Intent(getApplicationContext(), GrabingInfoActivity.class).setAction("frag_" + DumeUtils.TEACHER).putExtra(DumeUtils.SELECTED_ID, position));
                    finish();
                } else if (fromWhere.equals("frag_" + DumeUtils.BOOTCAMP)) {
                    startActivity(new Intent(getApplicationContext(), GrabingInfoActivity.class).setAction("frag_" + DumeUtils.BOOTCAMP).putExtra(DumeUtils.SELECTED_ID, position));
                    finish();
                } else {
                    flush("this is else working ");
                    startActivity(new Intent(getApplicationContext(), GrabingInfoActivity.class).setAction(DumeUtils.STUDENT).putExtra(DumeUtils.SELECTED_ID, position));
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(
                this, R.raw.map_style_default_no_landmarks);
        googleMap.setMapStyle(style);
        mMap = googleMap;
        onMapReadyListener(mMap);
        mMap.setPadding((int) (10 * (getResources().getDisplayMetrics().density)), (int) (250 * (getResources().getDisplayMetrics().density)), 0, (int) (6 * (getResources().getDisplayMetrics().density)));
        mMap.getUiSettings().setCompassEnabled(false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(false);
        }
        onMapReadyGeneralConfig();
        if (fromWhere != null) {
            switch (fromWhere) {
                case DumeUtils.STUDENT:
                    //addCustomMarkerFromURL(searchDataStore.getAvatarString(), searchDataStore.getAnchorPoint());
                    alwaysViewMusk.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //give anchor point here
                            if (searchDataStore.getAnchorPoint() == null) {
                                SharedPreferences prefs = getSharedPreferences("DUME", MODE_PRIVATE);
                                String restoredText = prefs.getString("location", null);
                                if (restoredText != null) {
                                    Gson gson1 = new Gson();
                                    LatLng latLng = gson1.fromJson(restoredText, LatLng.class);
                                    moveCamera(latLng, DEFAULT_ZOOM, "Device Location", mMap);
                                }
                            } else {
                                moveCamera(searchDataStore.getAnchorPoint(), DEFAULT_ZOOM, "Device Location", mMap);
                            }
                        }
                    }, 0L);
                    break;
                case DumeUtils.TEACHER:
                case ("frag_" + DumeUtils.TEACHER):
                    getDeviceLocation(mMap);
                    break;
                case (DumeUtils.BOOTCAMP):
                case ("frag_" + DumeUtils.BOOTCAMP):
                    getDeviceLocation(mMap);
                    break;
            }
        }
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
        addCustomInfoWindow(iconFactory, makeCharSequence("Radius", Integer.toString(SearchDataStore.SHORTRADIUS)) + " m", lattitudeLongitude);
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
    public void onMyGpsLocationChanged(Location location) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_grabing_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_help) {
            Intent intent = new Intent(context, StudentHelpActivity.class);
            intent.setAction("how_to_use");
            startActivity(intent);
        } else if (id == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
