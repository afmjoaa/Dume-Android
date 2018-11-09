package io.dume.dume.student.heatMap;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import carbon.widget.LinearLayout;
import io.dume.dume.R;
import io.dume.dume.student.pojo.CusStuAppComMapActivity;
import io.dume.dume.student.pojo.MyGpsLocationChangeListener;
import io.dume.dume.util.VisibleToggleClickListener;

public class HeatMapActivity extends CusStuAppComMapActivity implements OnMapReadyCallback, MyGpsLocationChangeListener,
        HeatMapContract.View {

    private static final String TAG = "HeatMapActivity";
    private GoogleMap mMap;
    private static final int fromFlag = 101;
    private SupportMapFragment mapFragment;
    private HeatMapContract.Presenter mPresenter;
    private RecyclerView myAccountRecycler;
    private AppBarLayout myAppBarLayout;
    private Button chooseAccouTypeBtn;
    private LinearLayout toolbarContainer;
    private FrameLayout viewMusk;
    private CoordinatorLayout parentCoorLayout;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    int[] imageIcons = {
            R.drawable.ic_default_student_profile,
            R.drawable.ic_default_mentor_profile,
            R.drawable.ic_default_bootcamp_profile
    };
    private String[] accountTypeArr;
    private int thisPosition = 0;

    //testing for the heat map code here
    private static final int[] ALT_HEATMAP_GRADIENT_COLORS = {
            Color.argb(0, 0, 255, 255),// transparent
            Color.argb(255 / 3 * 2, 0, 255, 255),
            Color.rgb(0, 191, 255),
            Color.rgb(0, 0, 127),
            Color.rgb(255, 0, 0)
    };

    public static final float[] ALT_HEATMAP_GRADIENT_START_POINTS = {
            0.0f, 0.10f, 0.20f, 0.60f, 1.0f
    };

    public static final Gradient ALT_HEATMAP_GRADIENT = new Gradient(ALT_HEATMAP_GRADIENT_COLORS,
            ALT_HEATMAP_GRADIENT_START_POINTS);
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;
    private HashMap<String, DataSet> mLists = new HashMap<String, DataSet>();
    private HeatMapAccountRecyAda heatMapAccountRecyAda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stunav_activity1_heat_map);
        setActivityContextMap(this, fromFlag);
        mPresenter = new HeatMapPresenter(this, new HeatMapModel());
        mPresenter.heatMapEnqueue();
        setSupportActionBar(toolbar);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        getLocationPermission(mapFragment);

        //setting the adapter with the recycler view
        //chooseAccouTypeBtn.setCompoundDrawablesWithIntrinsicBounds( imageIcons[position], 0, R.drawable.ic_keyboard_arrow_down_black_24dp, 0);
        heatMapAccountRecyAda = new HeatMapAccountRecyAda(this, getFinalData(0)) {
            @Override
            protected void OnAccouItemClicked(View v, int position) {
                thisPosition = position;
                //chooseAccouTypeBtn.setCompoundDrawablesWithIntrinsicBounds( imageIcons[position], 0, R.drawable.ic_keyboard_arrow_down_black_24dp, 0);
                chooseAccouTypeBtn.setText(accountTypeArr[position]);
                chooseAccouTypeBtn.performClick();
                heatMapAccountRecyAda.update(getFinalData(position));
            }
        };
        myAccountRecycler.setAdapter(heatMapAccountRecyAda);
        myAccountRecycler.setLayoutManager(new LinearLayoutManager(this));

        //testing the demo heat map code here
        try {
            mLists.put(getString(R.string.police_stations), new DataSet(readItems(R.raw.police),
                    getString(R.string.police_stations_url)));
            mLists.put(getString(R.string.medicare), new DataSet(readItems(R.raw.medicare),
                    getString(R.string.medicare_url)));
        } catch (JSONException e) {
            Toast.makeText(this, "Problem reading list of markers.", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void findView() {
        myAppBarLayout = findViewById(R.id.my_appbarLayout);
        chooseAccouTypeBtn = findViewById(R.id.choose_account_type_btn);
        toolbarContainer = findViewById(R.id.toolbar_container);
        viewMusk = findViewById(R.id.view_musk);
        parentCoorLayout = findViewById(R.id.parent_coor_layout);
        myAccountRecycler = findViewById(R.id.heatMap_account_recycler);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        accountTypeArr = getResources().getStringArray(R.array.AccountType);

    }

    @Override
    public void initHeatMap() {
        //elevation and shadow transparent hack
        myAppBarLayout.bringToFront();
        myAppBarLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        ActionBar supportActionBar = this.getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_more_vert_black_24dp);
        toolbar.setOverflowIcon(drawable);

    }

    @Override
    public void configHeatMap() {
        chooseAccouTypeBtn.setOnClickListener(new VisibleToggleClickListener() {

            @SuppressLint("CheckResult")
            @Override
            protected void changeVisibility(boolean visible) {
                TransitionSet set = new TransitionSet()
                        .addTransition(new Fade())
                        .addTransition(new Slide(Gravity.TOP))
                        .setInterpolator(visible ? new LinearOutSlowInInterpolator() : new FastOutLinearInInterpolator())
                        .addListener(new Transition.TransitionListener() {
                            @Override
                            public void onTransitionStart(@NonNull Transition transition) {

                            }

                            @Override
                            public void onTransitionEnd(@NonNull Transition transition) {
                                if(!visible){
                                    myAccountRecycler.setVisibility(View.GONE);
                                    viewMusk.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onTransitionCancel(@NonNull Transition transition) {

                            }

                            @Override
                            public void onTransitionPause(@NonNull Transition transition) {

                            }

                            @Override
                            public void onTransitionResume(@NonNull Transition transition) {

                            }
                        });
                TransitionSet set1 = new TransitionSet()
                        .addTransition(new Fade())
                        .setInterpolator(visible ? new LinearOutSlowInInterpolator() : new FastOutLinearInInterpolator());
                TransitionManager.beginDelayedTransition(myAppBarLayout, set);
                TransitionManager.beginDelayedTransition(viewMusk, set1);
                if (visible) {
                    myAccountRecycler.setVisibility(View.VISIBLE);
                    chooseAccouTypeBtn.setCompoundDrawablesWithIntrinsicBounds( imageIcons[thisPosition], 0, R.drawable.ic_keyboard_arrow_up_black_24dp, 0);
                    viewMusk.setVisibility(View.VISIBLE);
                } else {
                    myAccountRecycler.setVisibility(View.INVISIBLE);
                    viewMusk.setVisibility(View.INVISIBLE);
                    chooseAccouTypeBtn.setCompoundDrawablesWithIntrinsicBounds( imageIcons[thisPosition], 0, R.drawable.ic_keyboard_arrow_down_black_24dp, 0);
                    /*myAccountRecycler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                        }
                    }, 500L);*/
                }
                //.addTransition(new Scale(0.7f))
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
        startHeatMap(mMap);

    }

    public void startHeatMap(GoogleMap mMap){
        if (mProvider == null) {
            mProvider = new HeatmapTileProvider.Builder().data(
                    mLists.get(getString(R.string.police_stations)).getData()).build();
            mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
        } else {
            /*mProvider.setData(mLists.get(dataset).getData());
            mOverlay.clearTileCache();*/
        }
        mProvider.setGradient(ALT_HEATMAP_GRADIENT);
        mOverlay.clearTileCache();
        moveCamera(new LatLng(-38.3282, 143), 6, "Device Location", mMap);
    }


    @Override
    public void onCenterCurrentLocation() {
        Drawable d = fab.getDrawable();
        if (d instanceof Animatable) {
            ((Animatable) d).start();
        }
        Log.d(TAG, "onClick: clicked gps icon");
        //testing here as well
        //getDeviceLocation(mMap);
        moveCamera(new LatLng(-38.3282, 143), 6, "Device Location", mMap);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_grabing_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMyGpsLocationChanged(Location location) {

    }

    @Override
    public void viewMuskClicked() {
        chooseAccouTypeBtn.performClick();
    }

    public List<AccountRecyData> getFinalData(int selectedItem) {
        List<AccountRecyData> data = new ArrayList<>();

        for (int i = 0; i < accountTypeArr.length && i < imageIcons.length; i++) {
            AccountRecyData current = new AccountRecyData();
            current.accouName = accountTypeArr[i];
            current.iconId = imageIcons[i];
            current.selectedOne = selectedItem;
            data.add(current);
        }
        return data;
    }

    public void onHeatMapViewClicked(View view) {
        mPresenter.onHeatMapViewIntracted(view);
    }

    //sending data set to mlist
    private class DataSet {
        private ArrayList<LatLng> mDataset;
        private String mUrl;

        public DataSet(ArrayList<LatLng> dataSet, String url) {
            this.mDataset = dataSet;
            this.mUrl = url;
        }

        public ArrayList<LatLng> getData() {
            return mDataset;
        }
        public String getUrl() {
            return mUrl;
        }
    }

    //reading the raw file and converting it into a list
    private ArrayList<LatLng> readItems(int resource) throws JSONException {
        ArrayList<LatLng> list = new ArrayList<LatLng>();
        InputStream inputStream = getResources().openRawResource(resource);
        String json = new Scanner(inputStream).useDelimiter("\\A").next();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            double lat = object.getDouble("lat");
            double lng = object.getDouble("lng");
            list.add(new LatLng(lat, lng));
        }
        return list;
    }

}
