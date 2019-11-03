package io.dume.dume.student.heatMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.appbar.AppBarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.content.ContextCompat;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.firebase.firestore.FirebaseFirestore;
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
import io.dume.dume.Google;
import io.dume.dume.R;
import io.dume.dume.customView.HorizontalLoadView;
import io.dume.dume.splash.SplashActivity;
import io.dume.dume.student.pojo.CusStuAppComMapActivity;
import io.dume.dume.student.pojo.DataSet;
import io.dume.dume.student.pojo.MyGpsLocationChangeListener;
import io.dume.dume.student.studentHelp.StudentHelpActivity;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.util.DumeUtils;
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
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(19, 87), new LatLng(27, 93));

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

    public Gradient ALT_HEATMAP_GRADIENT = new Gradient(ALT_HEATMAP_GRADIENT_COLORS,
            ALT_HEATMAP_GRADIENT_START_POINTS);
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;
    private HashMap<String, DataSet> mLists = new HashMap<String, DataSet>();
    private HeatMapAccountRecyAda heatMapAccountRecyAda;
    private HorizontalLoadView myLoadView;
    private HeatMapModel heatMapModel;
    private HeatMapModel mModel;
    private Google google;
    private int selectedItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stunav_activity1_heat_map);
        setActivityContextMap(this, fromFlag);
        findLoadView();
        mModel = new HeatMapModel();
        mPresenter = new HeatMapPresenter(this, mModel);
        mPresenter.heatMapEnqueue();
        setSupportActionBar(toolbar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        getLocationPermission(mapFragment);

        //setting the adapter with the recycler view
        //chooseAccouTypeBtn.setCompoundDrawablesWithIntrinsicBounds( imageIcons[position], 0, R.drawable.ic_keyboard_arrow_down_black_24dp, 0);
        google = Google.getInstance();
        String accountMajor = google.getAccountMajor();
        switch (accountMajor) {
            case DumeUtils.STUDENT:
                selectedItem = 1;
                thisPosition = 1;
                break;
            case DumeUtils.TEACHER:
                selectedItem = 0;
                thisPosition = 0;
                break;
            default:
                selectedItem = 0;
                thisPosition = 0;
                break;
        }
        heatMapAccountRecyAda = new HeatMapAccountRecyAda(this, getFinalData(selectedItem)) {
            @Override
            protected void OnAccouItemClicked(View v, int position) {
                thisPosition = position;
                showProgress();
                if (position == 2) {
                    flush("BootCamp is under development...");
                    hideProgress();
                } else {
                    chooseAccouTypeBtn.setText(accountTypeArr[position]);
                    chooseAccouTypeBtn.performClick();
                    heatMapAccountRecyAda.update(getFinalData(position));
                    switch (position) {
                        case 0:
                            if (mMap != null) {
                                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                                    @Override
                                    public void onMapLoaded() {
                                        if (mLists.get(DumeUtils.STUDENT) != null) {
                                            startHeatMap(mMap, DumeUtils.STUDENT);
                                            hideProgress();
                                        } else {
                                            mModel.getStuLocData(new TeacherContract.Model.Listener<DataSet>() {
                                                @Override
                                                public void onSuccess(DataSet list) {
                                                    mLists.put(DumeUtils.STUDENT, list);
                                                    mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                                                        @Override
                                                        public void onMapLoaded() {
                                                            startHeatMap(mMap, DumeUtils.STUDENT);
                                                            hideProgress();
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onError(String msg) {
                                                    Log.w(TAG, "onError: " + msg);
                                                    flush(msg);
                                                    hideProgress();
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                            break;
                        case 1:
                            if (mMap != null) {
                                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                                    @Override
                                    public void onMapLoaded() {
                                        if (mLists.get(DumeUtils.TEACHER) != null) {
                                            startHeatMap(mMap, DumeUtils.TEACHER);
                                            hideProgress();
                                        } else {
                                            mModel.getMentorLocData(new TeacherContract.Model.Listener<DataSet>() {
                                                @Override
                                                public void onSuccess(DataSet list) {
                                                    mLists.put(DumeUtils.TEACHER, list);
                                                    mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                                                        @Override
                                                        public void onMapLoaded() {
                                                            startHeatMap(mMap, DumeUtils.TEACHER);
                                                            hideProgress();
                                                        }
                                                    });
                                                }
                                                @Override
                                                public void onError(String msg) {
                                                    Log.w(TAG, "onError: " + msg);
                                                    flush(msg);
                                                    hideProgress();
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                            break;
                        case 2:
                            Toast.makeText(HeatMapActivity.this, "BootCamp is coming soon...", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(LAT_LNG_BOUNDS, 10));
                }
            }
        };
        myAccountRecycler.setAdapter(heatMapAccountRecyAda);
        myAccountRecycler.setLayoutManager(new LinearLayoutManager(this));


        FirebaseFirestore.getInstance().collection("app").document("dume_utils").get().addOnSuccessListener(documentSnapshot -> {
            //Log.w(TAG, "hasUpdate: ");
            //Number currentVersion = (Number) documentSnapshot.get("version_code");
            //String updateVersionName = (String) documentSnapshot.get("version_name");
            //String updateDescription = (String) documentSnapshot.get("version_description");
            Number totalStudent = (Number) documentSnapshot.get("total_students");
            Number totalMentors = (Number) documentSnapshot.get("total_mentors");
            Google.getInstance().setTotalStudent(totalStudent == null ? 0 : totalStudent.intValue());
            Google.getInstance().setTotalMentor(totalMentors == null ? 0 : totalMentors.intValue());
            heatMapAccountRecyAda.update(getFinalData(thisPosition));
        }).addOnFailureListener(e -> Log.e(TAG, "err: "+ e.getLocalizedMessage()));
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
        myLoadView = findViewById(R.id.loadView);
        google = Google.getInstance();
    }

    @Override
    public void initHeatMap() {
        fab.setAlpha(0.90f);
        //elevation and shadow transparent hack
        myAppBarLayout.bringToFront();
        myAppBarLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        ActionBar supportActionBar = this.getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_more_vert_black_24dp);
        toolbar.setOverflowIcon(drawable);

        showProgress();
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
                                if (!visible) {
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
                    chooseAccouTypeBtn.setCompoundDrawablesWithIntrinsicBounds(imageIcons[thisPosition], 0, R.drawable.ic_keyboard_arrow_up_black_24dp, 0);
                    viewMusk.setVisibility(View.VISIBLE);
                    myLoadView.setTranslationY(2 * getResources().getDisplayMetrics().density);
                } else {
                    myAccountRecycler.setVisibility(View.INVISIBLE);
                    viewMusk.setVisibility(View.INVISIBLE);
                    myLoadView.setTranslationY(-8 * getResources().getDisplayMetrics().density);
                    chooseAccouTypeBtn.setCompoundDrawablesWithIntrinsicBounds(imageIcons[thisPosition], 0, R.drawable.ic_keyboard_arrow_down_black_24dp, 0);

                }
                chooseAccouTypeBtn.setEnabled(false);
                Drawable[] compoundDrawables = chooseAccouTypeBtn.getCompoundDrawables();
                Drawable d = compoundDrawables[0];
                Drawable d1 = compoundDrawables[2];
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (d instanceof Animatable) {
                        ((Animatable) d).start();
                    }
                    ((Animatable2) d).registerAnimationCallback(new Animatable2.AnimationCallback() {
                        public void onAnimationEnd(Drawable drawable) {
                            //Do something
                            if (visible) {
                                chooseAccouTypeBtn.setCompoundDrawablesWithIntrinsicBounds(imageIcons[thisPosition], 0, R.drawable.ic_keyboard_arrow_up_black_24dp, 0);
                            } else {
                                chooseAccouTypeBtn.setCompoundDrawablesWithIntrinsicBounds(imageIcons[thisPosition], 0, R.drawable.ic_keyboard_arrow_down_black_24dp, 0);
                            }
                            chooseAccouTypeBtn.setEnabled(true);
                        }
                    });
                } else {
                    if (visible) {
                        chooseAccouTypeBtn.setCompoundDrawablesWithIntrinsicBounds(imageIcons[thisPosition], 0, R.drawable.ic_keyboard_arrow_up_black_24dp, 0);
                        chooseAccouTypeBtn.setEnabled(true);
                    } else {
                        chooseAccouTypeBtn.setCompoundDrawablesWithIntrinsicBounds(imageIcons[thisPosition], 0, R.drawable.ic_keyboard_arrow_down_black_24dp, 0);
                        chooseAccouTypeBtn.setEnabled(true);
                    }
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
        /*mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

            }
        });*/
        String accountMajor = google.getAccountMajor();
        if (accountMajor == null) {
            Intent returnIntent = new Intent(HeatMapActivity.this, SplashActivity.class);
            returnIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(returnIntent);
            finish();
        } else {
            switch (accountMajor) {
                case DumeUtils.STUDENT:
                    chooseAccouTypeBtn.setText(accountTypeArr[1]);
                    chooseAccouTypeBtn.setCompoundDrawablesWithIntrinsicBounds(imageIcons[1], 0, R.drawable.ic_keyboard_arrow_down_black_24dp, 0);
                    mModel.getMentorLocData(new TeacherContract.Model.Listener<DataSet>() {
                        @Override
                        public void onSuccess(DataSet list) {
                            mLists.put(DumeUtils.TEACHER, list);
                            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                                @Override
                                public void onMapLoaded() {
                                    startHeatMap(mMap, DumeUtils.TEACHER);
                                    hideProgress();
                                }
                            });
                        }

                        @Override
                        public void onError(String msg) {
                            Log.w(TAG, "onError: " + msg);
                            flush(msg);
                            hideProgress();
                        }
                    });
                    break;
                case DumeUtils.TEACHER:
                case DumeUtils.BOOTCAMP:
                default:
                    chooseAccouTypeBtn.setText(accountTypeArr[0]);
                    mModel.getStuLocData(new TeacherContract.Model.Listener<DataSet>() {
                        @Override
                        public void onSuccess(DataSet list) {
                            mLists.put(DumeUtils.STUDENT, list);
                            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                                @Override
                                public void onMapLoaded() {
                                    startHeatMap(mMap, DumeUtils.STUDENT);
                                    hideProgress();
                                }
                            });
                        }

                        @Override
                        public void onError(String msg) {
                            Log.w(TAG, "onError: " + msg);
                            flush(msg);
                            hideProgress();
                        }
                    });
                    break;
            }
        }
    }

    public void startHeatMap(GoogleMap mMap, String identify) {
        if (mProvider == null && mOverlay == null) {
            mProvider = new HeatmapTileProvider.Builder().data(
                    mLists.get(identify).getData()).build();
            mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
        } else {
            if (mOverlay.isVisible()) {
                mOverlay.setVisible(false);
            }
            mOverlay.remove();
            mProvider = new HeatmapTileProvider.Builder().data(
                    mLists.get(identify).getData()).build();
            mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
            //mProvider.setData(mLists.get(identify).getData());
        }

        int[] ALT_HEATMAP_GRADIENT_COLORS = {
                Color.argb(0, 0, 255, 255),// transparent
                Color.argb(255 / 3 * 2, 0, 255, 255),
                Color.rgb(0, 191, 255),
                Color.rgb(0, 0, 127),
                Color.rgb(255, 0, 0)
        };

        float[] ALT_HEATMAP_GRADIENT_START_POINTS = {
                0.0f, 0.10f, 0.20f, 0.60f, 1.0f
        };

        Gradient ALT_HEATMAP_GRADIENT = new Gradient(ALT_HEATMAP_GRADIENT_COLORS,
                ALT_HEATMAP_GRADIENT_START_POINTS);
        mProvider.setGradient(ALT_HEATMAP_GRADIENT);
        //mOverlay.clearTileCache();
    }

    @Override
    public void flush(String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if (v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }

    @Override
    public void onCenterCurrentLocation() {
        Drawable d = fab.getDrawable();
        if (d instanceof Animatable) {
            ((Animatable) d).start();
        }
        Log.d(TAG, "onClick: clicked gps icon");
        if (mMap != null) {
            if (mMap.getCameraPosition().zoom < 4) {
                getDeviceLocationWithZoom(mMap, 7.99f);
            } else if (mMap.getCameraPosition().zoom >= 4 && mMap.getCameraPosition().zoom < 8) {
                getDeviceLocationWithZoom(mMap, 11.99f);
            } else if (mMap.getCameraPosition().zoom >= 8 && mMap.getCameraPosition().zoom < 12) {
                getDeviceLocationWithZoom(mMap, 14.99f);
            } else if (mMap.getCameraPosition().zoom >= 12 && mMap.getCameraPosition().zoom < 15) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(LAT_LNG_BOUNDS, 10));
            }
        } else {
            flush("Wait a bit...");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_grabing_info, menu);
        return true;
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
            switch (i) {
                case 0://+ " ጰ"
                    current.accouName = accountTypeArr[i] + " : " + Google.getInstance().getTotalStudent();
                    break;
                case 1:
                    current.accouName = accountTypeArr[i] + " : " + Google.getInstance().getTotalMentor();
                    break;
                case 2:
                    current.accouName = accountTypeArr[i];
                    break;
            }
            current.iconId = imageIcons[i];
            current.selectedOne = selectedItem;
            data.add(current);
        }
        return data;
    }

    public void onHeatMapViewClicked(View view) {
        mPresenter.onHeatMapViewIntracted(view);
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

    //view_musk
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_help) {
            Intent intent = new Intent(context, StudentHelpActivity.class);
            intent.setAction("how_to_use");
            startActivity(intent);
        } else if (id == android.R.id.home) {
            if (viewMusk.getVisibility() == View.VISIBLE) {
                chooseAccouTypeBtn.performClick();
            } else {
                super.onBackPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
