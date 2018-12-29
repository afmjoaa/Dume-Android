package io.dume.dume.teacher.crudskill;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MapStyleOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import carbon.widget.LinearLayout;
import io.dume.dume.R;
import io.dume.dume.customView.HorizontalLoadView;
import io.dume.dume.student.grabingInfo.GrabingInfoActivity;
import io.dume.dume.student.pojo.CusStuAppComMapActivity;
import io.dume.dume.student.pojo.MyGpsLocationChangeListener;
import io.dume.dume.teacher.adapters.CategoryAdapter;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.GridSpacingItemDecoration;

import static io.dume.dume.util.DumeUtils.configureAppbar;

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
    }

    @Override
    public void init() {
        configureAppbar(this, "Add New Skill");
        fromWhere = getIntent().getAction();
        if (fromWhere != null) {
            switch (fromWhere) {
                case DumeUtils.STUDENT:
                    flush("fucked it from student");
                    break;
                case DumeUtils.TEACHER:
                    flush("fucked it from Teacher");
                    break;
                case (DumeUtils.BOOTCAMP):
                    flush("fucked it from Boot-camp");
                    break;
            }
        }
        //getting the width
        int[] wh = DumeUtils.getScreenSize(this);
        spacing = (int) ((wh[0] - ((330) * (getResources().getDisplayMetrics().density))) / 4);
        Log.e(TAG, "init: " + spacing);
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
        onMapReadyGeneralConfig();
        mMap.setPadding((int) (10 * (getResources().getDisplayMetrics().density)), (int) (250 * (getResources().getDisplayMetrics().density)), 0, (int) (6 * (getResources().getDisplayMetrics().density)));
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
            //add function here
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
