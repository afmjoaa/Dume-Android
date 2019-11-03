package io.dume.dume.student.grabingLocation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataBufferUtils;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import carbon.widget.Button;
import carbon.widget.ImageView;
import carbon.widget.RelativeLayout;
import io.dume.dume.R;
import io.dume.dume.student.pojo.CusStuAppComMapActivity;
import io.dume.dume.student.pojo.MyGpsLocationChangeListener;
import io.dume.dume.student.profilePage.ProfilePageActivity;
import io.dume.dume.student.studentSettings.SavedPlacesAdaData;
import io.dume.dume.student.studentSettings.StudentSettingsActivity;
import io.dume.dume.teacher.crudskill.CrudSkillActivity;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.mentor_settings.basicinfo.EditAccount;
import io.dume.dume.util.DumeUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static io.dume.dume.util.DumeUtils.configToolbarTittle;
import static io.dume.dume.util.DumeUtils.hideKeyboard;
import static io.dume.dume.util.DumeUtils.showKeyboard;

public class GrabingLocationActivity extends CusStuAppComMapActivity implements OnMapReadyCallback,
        GrabingLocaitonContract.View, MyGpsLocationChangeListener, View.OnClickListener {

    private GrabingLocaitonContract.Presenter mPresenter;
    private static final CharacterStyle STYLE_NORMAL = new StyleSpan(Typeface.NORMAL);
    private GoogleMap mMap;
    private static final String TAG = "GrabingLocationActivity";
    private static final int fromFlag = 2;
    private Context mContext;
    private LatLng mCenterLatLong;
    private SupportMapFragment mapFragment;
    private FloatingActionButton fab;
    private View map;
    private Toolbar toolbar;
    private BottomSheetBehavior bottomSheetBehavior;
    private LinearLayout llBottomSheet;
    private FloatingActionButton bottomSheetFab;
    private AppBarLayout myAppbarlayout;
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView autoCompleteRecyView;
    private RecyclerView menualCompleteRecyView;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(19, 87), new LatLng(27, 93));
    //india new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));
    //new LatLng(-40, -168), new LatLng(71, 136)
    protected EditText inputSearch;
    private CompositeDisposable compositeDisposable;
    private PlaceAutoRecyAda recyclerAutoAdapter;
    private PlaceMenualRecyAda recyclerMenualAdapter;
    private ArrayList<AutocompletePrediction> initAdapterData;
    private ImageView discardImage;
    private Button locationDoneBtn;
    private RelativeLayout inputSearchContainer;
    private String retrivedAction;
    private LatLng queriedLocation;
    private LinearLayout hackHeight;
    private GeoPoint userLocation = null;
    private String addressName;
    private Intent fromIntent;
    private static Map<String, Map<String, Object>> favorites;
    private static Map<String, Map<String, Object>> saved_places;
    private static Map<String, Map<String, Object>> recently_used;
    private int ADD_HOME_LOCATION = 1001;
    private int ADD_WORK_LOCATION = 1002;
    private int ADD_RECENT_PLACES = 1004;
    private int ADD_PARMANENT_ADDRESS = 1005;
    private GrabingLocationModel mModel;
    private LinearLayout hackSetLocationOnMap;
    private RecyclerView searchFoundRecycleView;
    private SearchFoundRecyAda searchFoundRecyAda;
    private DocumentSnapshot documentSnapshot;
    private NestedScrollView bottomSheetNSV;


    //queriedLocation for text to geopoint
    //mCenterLatLong for geopoint to text


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu3_activity_grabing_location);
        buildGoogleApiClient();
        setActivityContextMap(this, fromFlag);
        findLoadView();
        mContext = this;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        getLocationPermission(mapFragment);
        mModel = new GrabingLocationModel(this);
        mPresenter = new GrabingLocationPresenter(this, mModel);
        fromIntent = getIntent();
        retrivedAction = fromIntent.getAction();
        mPresenter.grabingLocationPageEnqueue();
        setDarkStatusBarIcon();
        setIsNight();
        compositeDisposable = new CompositeDisposable();
        //auto one
        initAdapterData = new ArrayList<AutocompletePrediction>();
        recyclerAutoAdapter = new PlaceAutoRecyAda(this, initAdapterData, mGoogleApiClient) {
            @Override
            void OnItemClicked(View v, AutocompletePrediction clickedPrediction) {
                inputSearch.setText(clickedPrediction.getPrimaryText(STYLE_NORMAL));
                Places.GeoDataApi.getPlaceById(mGoogleApiClient, clickedPrediction.getPlaceId())
                        .setResultCallback(new ResultCallback<PlaceBuffer>() {
                            //TODO not complete
                            @Override
                            public void onResult(PlaceBuffer places) {
                                if (places.getStatus().isSuccess()) {
                                    Place myPlace = places.get(0);
                                    queriedLocation = myPlace.getLatLng();
                                    Log.e(TAG, " " + queriedLocation.latitude);
                                    Log.e(TAG, " " + queriedLocation.longitude);
                                    moveCamera(new LatLng(queriedLocation.latitude, queriedLocation.longitude), DEFAULT_ZOOM
                                            , "typed location", mMap);
                                }
                                places.release();
                            }
                        });
                hideBSShowDB();
            }
        };
        autoCompleteRecyView.setLayoutManager(new LinearLayoutManager(this));
        autoCompleteRecyView.setAdapter(recyclerAutoAdapter);

        //search found one
        List<MenualRecyclerData> data = new ArrayList<>();
        searchFoundRecyAda = new SearchFoundRecyAda(this, data) {
            @Override
            void OnItemClicked(View v, MenualRecyclerData clickedData) {
                if (clickedData.identify.equals("set_location")) {
                    hideBSShowDB();
                } else {
                    hideBSShowDB();
                    moveCamera(new LatLng(clickedData.location.getLatitude(), clickedData.location.getLongitude()), DEFAULT_ZOOM, clickedData.identify, mMap);
                }
            }
        };
        searchFoundRecycleView.setLayoutManager(new LinearLayoutManager(this));
        searchFoundRecycleView.setAdapter(searchFoundRecyAda);
        setProperTittle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_HOME_LOCATION) {
                LatLng selectedLocation = data.getParcelableExtra("selected_location");
                if (selectedLocation != null) {
                    distributeResult(selectedLocation, "Home");
                }
            } else if (requestCode == ADD_WORK_LOCATION) {
                LatLng selectedLocation = data.getParcelableExtra("selected_location");
                if (selectedLocation != null) {
                    distributeResult(selectedLocation, "Work");
                }
            } else if (requestCode == ADD_PARMANENT_ADDRESS) {
                LatLng selectedLocation = data.getParcelableExtra("selected_location");
                GeoPoint location = new GeoPoint(selectedLocation.latitude, selectedLocation.longitude);
                if (selectedLocation != null) {
                    showProgress();
                    recyclerMenualAdapter.updateFav("Permanent address", generateCAAdapterData(location));
                    mModel.updatePermanentAddress(location, new TeacherContract.Model.Listener() {
                        @Override
                        public void onSuccess(Object list) {
                            flush("Successfully Added");
                            hideProgress();
                        }

                        @Override
                        public void onError(String msg) {
                            flush(msg);
                            hideProgress();
                        }
                    });
                }
            }
        }
    }

    private void distributeResult(LatLng selectedLocation, String Name) {
        showProgress();
        SavedPlacesAdaData current = new SavedPlacesAdaData();
        GeoPoint location = new GeoPoint(selectedLocation.latitude, selectedLocation.longitude);
        String secondaryText = getAddress(selectedLocation.latitude, selectedLocation.longitude);
        current.primary_text = Name;
        current.secondary_text = secondaryText;
        current.location = location;
        recyclerMenualAdapter.updateFav(Name, current);

        Map<String, Object> myMap = new HashMap<>();
        myMap.put("location", location);
        myMap.put("primary_text", Name);
        myMap.put("secondary_text", secondaryText);

        mModel.updateFavoritePlaces(Name, myMap, new TeacherContract.Model.Listener<Void>() {
            @Override
            public void onSuccess(Void list) {
                flush("Successfully Added");
                hideProgress();
            }

            @Override
            public void onError(String msg) {
                flush(msg);
                hideProgress();
            }
        });
    }


    @Override
    public void findView() {
        fab = findViewById(R.id.fab);
        map = findViewById(R.id.map);
        //mLocationMarkerText = (TextView) findViewById(R.id.locationMarkertext);
        toolbar = findViewById(R.id.accountToolbar);
        llBottomSheet = findViewById(R.id.searchBottomSheet);
        myAppbarlayout = findViewById(R.id.my_appbarLayout_cardView);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.my_main_container);
        autoCompleteRecyView = findViewById(R.id.recycler_view_autoPlaces);
        menualCompleteRecyView = findViewById(R.id.recycler_view_manual);
        inputSearch = findViewById(R.id.input_search);
        discardImage = findViewById(R.id.discard_image);
        locationDoneBtn = findViewById(R.id.location_done_btn);
        inputSearchContainer = findViewById(R.id.input_search_container);
        hackHeight = findViewById(R.id.hack_height);
        hackSetLocationOnMap = findViewById(R.id.hack_set_location_on_map);
        searchFoundRecycleView = findViewById(R.id.recycle_view_search_found);
        bottomSheetNSV = findViewById(R.id.bottom_sheet_scroll_view);
        hackSetLocationOnMap.setOnClickListener(this);
        discardImage.setOnClickListener(this);
        locationDoneBtn.setOnClickListener(this);
        fab.setOnClickListener(this);
    }

    @Override
    public void initGrabingLocationPage() {
        fab.setAlpha(0.90f);
        //initializing actionbar/toolbar
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_more_vert_black_24dp);
        toolbar.setOverflowIcon(drawable);

        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        hideBSShowDB();
        llBottomSheet.animate().scaleX(1 + (1 * 0.058f)).setDuration(0).start();
        ViewTreeObserver vto = llBottomSheet.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                llBottomSheet.getLayoutParams().height = (llBottomSheet.getHeight() - myAppbarlayout.getHeight() - (int) (5 * (getResources().getDisplayMetrics().density)));
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

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showProgress();
                getAutocomplete(s);
                updateViewOnMatch(s.toString());
                if (discardImage.getVisibility() == View.INVISIBLE && !s.equals("")) {
                    discardImage.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    discardImage.setVisibility(View.INVISIBLE);
                }
            }
        });

        inputSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    llBottomSheet.setVisibility(View.VISIBLE);
                    locationDoneBtn.setVisibility(View.INVISIBLE);
                    //setMargins(fab,16, 16, 16, 10);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    Log.e(TAG, "onFocusChange: has focus");
                } else {
                    Log.e(TAG, "onFocusChange: nonononono focus");
                }
            }
        });
    }

    @Override
    public void updateViewOnMatch(String s) {
        List<MenualRecyclerData> newMenualData = new ArrayList<>();
        if (favorites != null && favorites.size() > 0) {
            for (Map.Entry<String, Map<String, Object>> entry : favorites.entrySet()) {
                if (entry.getKey().equals(s)) {
                    MenualRecyclerData savedPlacesAdaData = new MenualRecyclerData();
                    savedPlacesAdaData.setPrimaryText((String) entry.getValue().get("primary_text"));
                    savedPlacesAdaData.setSecondaryText((String) entry.getValue().get("secondary_text"));
                    savedPlacesAdaData.setIdentify((String) entry.getValue().get("primary_text"));
                    savedPlacesAdaData.setLocation((GeoPoint) entry.getValue().get("location"));
                    newMenualData.add(savedPlacesAdaData);
                }
            }
        }
        if (saved_places != null && saved_places.size() > 0) {
            for (Map.Entry<String, Map<String, Object>> entry : saved_places.entrySet()) {
                if (entry.getKey().equals(s)) {
                    MenualRecyclerData savedPlacesAdaData = new MenualRecyclerData();
                    savedPlacesAdaData.setPrimaryText((String) entry.getValue().get("primary_text"));
                    savedPlacesAdaData.setSecondaryText((String) entry.getValue().get("secondary_text"));
                    savedPlacesAdaData.setLocation((GeoPoint) entry.getValue().get("location"));
                    savedPlacesAdaData.setIdentify("saved_one");
                    newMenualData.add(savedPlacesAdaData);
                }
            }
        }
        if (recently_used != null && recently_used.size() > 0) {
            for (Map.Entry<String, Map<String, Object>> entry : recently_used.entrySet()) {
                String cmp = (String) entry.getValue().get("primary_text");
                if (cmp != null && cmp.equals(s)) {
                    MenualRecyclerData savedPlacesAdaData = new MenualRecyclerData();
                    savedPlacesAdaData.setPrimaryText((String) entry.getValue().get("primary_text"));
                    savedPlacesAdaData.setSecondaryText((String) entry.getValue().get("secondary_text"));
                    savedPlacesAdaData.setLocation((GeoPoint) entry.getValue().get("location"));
                    savedPlacesAdaData.setIdentify("back_in_time");
                    newMenualData.add(savedPlacesAdaData);
                }
            }
        }
        if (s.equals("Permanent address") || s.equals("permanent") || s.equals("Permanent") || s.equals("permanent address")) {
            if (documentSnapshot != null) {
                GeoPoint current_address = documentSnapshot.getGeoPoint("current_address");
                if (current_address != null) {
                    MenualRecyclerData currentSavedPlace = new MenualRecyclerData();
                    String address = getAddress(current_address.getLatitude(), current_address.getLongitude());
                    currentSavedPlace.setPrimaryText("Permanent address");
                    currentSavedPlace.setIdentify("Permanent address");
                    currentSavedPlace.setSecondaryText(address);
                    currentSavedPlace.setLocation(current_address);
                    newMenualData.add(currentSavedPlace);
                }
            }
        }
        if (s.equals("set") || s.equals("Set") || s.equals("set ") || s.equals("Set ") || s.startsWith("set l") ||
                s.startsWith("Set l")) {
            MenualRecyclerData currentSavedPlace = new MenualRecyclerData();
            currentSavedPlace.setPrimaryText(getResources().getString(R.string.set_location_on_map));
            currentSavedPlace.setIdentify("set_location");
            currentSavedPlace.setSecondaryText("");
            currentSavedPlace.setLocation(null);
            hackSetLocationOnMap.setVisibility(View.GONE);
            newMenualData.add(currentSavedPlace);
        } else if (Objects.requireNonNull(retrivedAction).startsWith("from")) {
            hackSetLocationOnMap.setVisibility(View.VISIBLE);
        }
        if (Objects.requireNonNull(retrivedAction).startsWith("from")) {
            if (newMenualData.size() > 0) {
                searchFoundRecyAda.update(newMenualData);
                searchFoundRecycleView.setVisibility(View.VISIBLE);
            } else {
                searchFoundRecycleView.setVisibility(View.GONE);
            }
        } else {
            if (newMenualData.size() > 0) {
                searchFoundRecyAda.update(newMenualData);
                menualCompleteRecyView.setVisibility(View.GONE);
                searchFoundRecycleView.setVisibility(View.VISIBLE);
            } else {
                menualCompleteRecyView.setVisibility(View.VISIBLE);
                searchFoundRecycleView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean checkIfInDB(GeoPoint geoPoint) {
        boolean found = false;
        if (favorites != null && favorites.size() > 0) {
            for (Map.Entry<String, Map<String, Object>> entry : favorites.entrySet()) {
                if (entry.getValue().get("location").equals(geoPoint)) {
                    found = true;
                }
            }
        }
        if (saved_places != null && saved_places.size() > 0) {
            for (Map.Entry<String, Map<String, Object>> entry : saved_places.entrySet()) {
                if (entry.getValue().get("location").equals(geoPoint)) {
                    found = true;
                }
            }
        }
        if (recently_used != null && recently_used.size() > 0) {
            for (Map.Entry<String, Map<String, Object>> entry : recently_used.entrySet()) {
                String targetAddress = getAddress(geoPoint.getLatitude(), geoPoint.getLongitude());
                String primary = "";
                try {
                    String[] targetAddressParts = targetAddress.split("\\s*,\\s*", 2);
                    primary = targetAddressParts[0];
                } catch (Exception err) {
                    primary = targetAddress;
                }
                if (entry.getValue().get("location").equals(geoPoint)) {
                    found = true;
                }
                if (entry.getValue().get("primary_text").equals(primary)) {
                    found = true;
                }
            }
        }
        if (documentSnapshot != null) {
            GeoPoint current_address = documentSnapshot.getGeoPoint("current_address");
            if (current_address != null) {
                if (geoPoint.equals(current_address)) {
                    found = true;
                }
            }
        }
        return found;
    }

    @Override
    public void configGrabingLocationPage() {
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    if (fab.getVisibility() == View.INVISIBLE) {
                        //fab.setVisibility(View.VISIBLE);
                        fab.show();
                    }
                    hideKeyboard(GrabingLocationActivity.this);
                    hackHeight.setVisibility(View.GONE);
                    inputSearch.clearFocus();
                    inputSearchContainer.requestFocus();
                    bottomSheetNSV.post(new Runnable() {
                        @Override
                        public void run() {
                            bottomSheetNSV.fling(0);
                            bottomSheetNSV.scrollTo(0, 0);
                            bottomSheetNSV.fling(-10000);
                        }
                    });
                } else if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    showKeyboard(GrabingLocationActivity.this);
                    hackHeight.setVisibility(View.VISIBLE);
                } else if (BottomSheetBehavior.STATE_DRAGGING == newState) {

                } else if (BottomSheetBehavior.STATE_SETTLING == newState) {

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                llBottomSheet.animate().scaleX(1 + (slideOffset * 0.058f)).setDuration(0).start();
                fab.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
                if (COMPASSBTN != null) {
                    COMPASSBTN.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
                }

            }
        });
    }

    @Override
    public void makingCallbackInterfaces() {

    }


    @Override
    public void retriveSavedData() {
        showProgress();
        mPresenter.retriveSavedPlacesData(new TeacherContract.Model.Listener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot list) {
                setDocumentSnapshot(list);
                List<SavedPlacesAdaData> favoriteAdaData = new ArrayList<>();
                List<SavedPlacesAdaData> savedAdaData = new ArrayList<>();
                List<MenualRecyclerData> recentAdaData = new ArrayList<>();

                favorites = (Map<String, Map<String, Object>>) list.get("favourite_places");
                if (favorites != null && favorites.size() > 0) {
                    for (Map.Entry<String, Map<String, Object>> entry : favorites.entrySet()) {
                        if (entry.getKey().equals("home") || entry.getKey().equals("work")) {
                            SavedPlacesAdaData savedPlacesAdaData = new SavedPlacesAdaData();
                            savedPlacesAdaData.setPrimary_text((String) entry.getValue().get("primary_text"));
                            savedPlacesAdaData.setSecondary_text((String) entry.getValue().get("secondary_text"));
                            savedPlacesAdaData.setLocation((GeoPoint) entry.getValue().get("location"));
                            favoriteAdaData.add(savedPlacesAdaData);
                        }
                    }
                    if (list.getGeoPoint("current_address") != null) {
                        favoriteAdaData.add(generateCAAdapterData(list.getGeoPoint("current_address")));
                    }
                }
                saved_places = (Map<String, Map<String, Object>>) list.get("saved_places");
                if (saved_places != null && saved_places.size() > 0) {
                    for (Map.Entry<String, Map<String, Object>> entry : saved_places.entrySet()) {
                        SavedPlacesAdaData savedPlacesAdaData = new SavedPlacesAdaData();
                        savedPlacesAdaData.setPrimary_text((String) entry.getValue().get("primary_text"));
                        savedPlacesAdaData.setSecondary_text((String) entry.getValue().get("secondary_text"));
                        savedPlacesAdaData.setLocation((GeoPoint) entry.getValue().get("location"));
                        savedAdaData.add(savedPlacesAdaData);
                    }
                }
                recently_used = (Map<String, Map<String, Object>>) list.get("recent_places");
                if (recently_used != null && recently_used.size() > 0) {
                    for (Map.Entry<String, Map<String, Object>> entry : recently_used.entrySet()) {
                        MenualRecyclerData savedPlacesAdaData = new MenualRecyclerData();
                        savedPlacesAdaData.setPrimaryText((String) entry.getValue().get("primary_text"));
                        savedPlacesAdaData.setSecondaryText((String) entry.getValue().get("secondary_text"));
                        savedPlacesAdaData.setLocation((GeoPoint) entry.getValue().get("location"));
                        savedPlacesAdaData.setIdentify((String) entry.getKey());
                        recentAdaData.add(savedPlacesAdaData);
                    }
                }
                String preIdentifyOne = documentSnapshot.getString("next_rp_write");
                recyclerMenualAdapter = new PlaceMenualRecyAda(GrabingLocationActivity.this, favoriteAdaData, savedAdaData, recentAdaData, preIdentifyOne) {
                    @Override
                    void OnItemClicked(View v, int position, String identify) {
                        SavedPlacesAdaData current = new SavedPlacesAdaData();
                        current.primary_text = "foo";
                        if (position == 0) {
                            //current location block
                            if (identify.equals("Permanent address")) {
                                for (SavedPlacesAdaData foo : favoriteAdaData) {
                                    if (foo.primary_text.equals("Permanent address")) {
                                        current = foo;
                                    }
                                }
                                hideBSShowDB();
                                moveCamera(new LatLng(current.location.getLatitude(), current.location.getLongitude()), DEFAULT_ZOOM, "Permanent_address", mMap);
                            } else {
                                startActivityForResult(new Intent(context, GrabingLocationActivity.class).setAction("fromGLAP"), ADD_PARMANENT_ADDRESS);
                            }
                        } else if (position == 1) {
                            //home block
                            if (identify.equals("Home")) {
                                for (SavedPlacesAdaData foo : favoriteAdaData) {
                                    if (foo.primary_text.equals("Home")) {
                                        current = foo;
                                    }
                                }
                                hideBSShowDB();
                                moveCamera(new LatLng(current.location.getLatitude(), current.location.getLongitude()), DEFAULT_ZOOM, "home", mMap);
                            } else {
                                startActivityForResult(new Intent(context, GrabingLocationActivity.class).setAction("fromGLAH"), ADD_HOME_LOCATION);
                            }

                        } else if (position == 2) {
                            //work block
                            if (identify.equals("Work")) {
                                for (SavedPlacesAdaData foo : favoriteAdaData) {
                                    if (foo.primary_text.equals("Work")) {
                                        current = foo;
                                    }
                                }
                                hideBSShowDB();
                                moveCamera(new LatLng(current.location.getLatitude(), current.location.getLongitude()), DEFAULT_ZOOM, "work", mMap);
                            } else {
                                activity.startActivityForResult(new Intent(context, GrabingLocationActivity.class).setAction("fromGLAW"), ADD_WORK_LOCATION);
                            }

                        } else if (position > 2 && position <= (2 + savedAdaData.size())) {
                            //saved block
                            for (SavedPlacesAdaData foo : savedAdaData) {
                                if (foo.primary_text.equals(identify)) {
                                    current = foo;
                                }
                            }
                            hideBSShowDB();
                            moveCamera(new LatLng(current.location.getLatitude(), current.location.getLongitude()), DEFAULT_ZOOM, "saved_places", mMap);
                        } else if (position > (2 + savedAdaData.size()) && position <= (2 + savedAdaData.size() + recentAdaData.size())) {
                            //back in time block
                            GeoPoint cMLocation = null;
                            switch (preIdentifyOne) {
                                case "1":
                                    switch (identify) {
                                        case "one":
                                            //3
                                            cMLocation = (GeoPoint) recently_used.get("rp_3").get("location");
                                            break;
                                        case "two":
                                            //2
                                            cMLocation = (GeoPoint) recently_used.get("rp_2").get("location");
                                            break;
                                        case "three":
                                            //1
                                            cMLocation = (GeoPoint) recently_used.get("rp_1").get("location");
                                            break;
                                    }
                                    break;
                                case "2":
                                    switch (identify) {
                                        case "one":
                                            //1
                                            cMLocation = (GeoPoint) recently_used.get("rp_1").get("location");
                                            break;
                                        case "two":
                                            //3
                                            cMLocation = (GeoPoint) recently_used.get("rp_3").get("location");
                                            break;
                                        case "three":
                                            //2
                                            cMLocation = (GeoPoint) recently_used.get("rp_2").get("location");
                                            break;
                                    }
                                    break;
                                case "3":
                                    switch (identify) {
                                        case "one":
                                            //2
                                            cMLocation = (GeoPoint) recently_used.get("rp_2").get("location");
                                            break;
                                        case "two":
                                            //1
                                            cMLocation = (GeoPoint) recently_used.get("rp_1").get("location");
                                            break;
                                        case "three":
                                            //3
                                            cMLocation = (GeoPoint) recently_used.get("rp_3").get("location");
                                            break;
                                    }
                                    break;
                            }
                            hideBSShowDB();
                            if (cMLocation != null) {
                                moveCamera(new LatLng(cMLocation.getLatitude(), cMLocation.getLongitude()), DEFAULT_ZOOM, "back_in_time", mMap);
                            }
                        } else if (position == (3 + savedAdaData.size() + recentAdaData.size())) {
                            //set location on map block
                            hideBSShowDB();
                        }
                    }
                };
                menualCompleteRecyView.setLayoutManager(new LinearLayoutManager(GrabingLocationActivity.this));
                menualCompleteRecyView.setAdapter(recyclerMenualAdapter);

            }

            @Override
            public void onError(String msg) {
                flush(msg);
            }
        });
    }

    private void hideBSShowDB() {
        hideKeyboard(GrabingLocationActivity.this);
        hackHeight.setVisibility(View.GONE);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        inputSearch.clearFocus();
        inputSearchContainer.requestFocus();
        llBottomSheet.setVisibility(View.INVISIBLE);
        locationDoneBtn.setVisibility(View.VISIBLE);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        /*ISNIGHT = HOUR < 4 || HOUR > 24;
        if (ISNIGHT) {
            MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.map_style_night_json);
            googleMap.setMapStyle(style);
        } else {}*/
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(
                this, R.raw.map_style_default_json);
        googleMap.setMapStyle(style);

        mMap = googleMap;
        onMapReadyListener(mMap);
        mMap.setPadding((int) (10 * (getResources().getDisplayMetrics().density)), 0, 0, (int) (72 * (getResources().getDisplayMetrics().density)));
        /*mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

            }
        });*/

        onMapReadyGeneralConfig();
        getDeviceLocation(mMap);
        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                locationDoneBtn.setEnabled(false);
                locationDoneBtn.setBackgroundColor(getResources().getColor(R.color.disable_color));
                showProgress();
                inputSearch.setText(R.string.LoadingText);
            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                // Cleaning all the markers.
                if (mMap != null) {
                    mMap.clear();
                }

                mCenterLatLong = mMap.getCameraPosition().target;
                //mZoom = mGoogleMap.getCameraPosition().zoom;
                //mLocationMarkerText.setText("Lat : " + mCenterLatLong.latitude + "," + "Long : " + mCenterLatLong.longitude);
                String mainAddress = getAddress(mCenterLatLong.latitude, mCenterLatLong.longitude);
                inputSearch.setText(mainAddress);
                locationDoneBtn.setEnabled(true);
                locationDoneBtn.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                hideProgress();

            }
        });

    }

    @Override
    public void onDiscardSearchClicked() {
        inputSearch.getText().clear();
        discardImage.setVisibility(View.INVISIBLE);
    }

    //not interested right now
    @Override
    public void onLocationDoneBtnClicked() {

        if (Objects.requireNonNull(retrivedAction).equals("fromPPA")) {
            //testing
            if (mCenterLatLong == null) {
                flush("Please move map marker to select location...");
                return;
            }
            Intent goBackToPPAIntent = new Intent(this, ProfilePageActivity.class);
            goBackToPPAIntent.putExtra("selected_location", mCenterLatLong);
            setResult(RESULT_OK, goBackToPPAIntent);
            finish();
            //GeoPoint selectedLocation = new GeoPoint(mCenterLatLong.latitude, mCenterLatLong.longitude);
        } else if (Objects.requireNonNull(retrivedAction).equals("fromMPA")) {
            if (mCenterLatLong == null) {
                flush("Please move map marker to select location...");
                return;
            }
            Intent mpaIntent = new Intent(this, EditAccount.class);
            mpaIntent.putExtra("selected_location", mCenterLatLong);
            setResult(RESULT_OK, mpaIntent);
            finish();
        } else if (Objects.requireNonNull(retrivedAction).startsWith("fromSPA")) {
            if (mCenterLatLong == null) {
                flush("Please move map marker to select location...");
                return;
            }
            Intent goBackToPPAIntent = new Intent(this, StudentSettingsActivity.class);
            goBackToPPAIntent.putExtra("selected_location", mCenterLatLong);
            if (Objects.requireNonNull(retrivedAction).equals("fromSPASN")) {
                goBackToPPAIntent.putExtra("addressName", addressName);
            }
            setResult(RESULT_OK, goBackToPPAIntent);
            finish();
        } else if (Objects.requireNonNull(retrivedAction).startsWith("fromGLA")) {
            if (mCenterLatLong == null) {
                flush("Please move map marker to select location...");
                return;
            }
            Intent goBackToGLAIntent = new Intent(this, StudentSettingsActivity.class);
            goBackToGLAIntent.putExtra("selected_location", mCenterLatLong);
            setResult(RESULT_OK, goBackToGLAIntent);
            finish();
        } else if (Objects.requireNonNull(retrivedAction).startsWith("fromSA")) {
            if (mCenterLatLong == null) {
                flush("Please move map marker to select location...");
                return;
            }
            Intent intent = new Intent();
            intent.putExtra("selected_location", mCenterLatLong);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            showProgress();
            locationDoneBtn.setEnabled(false);
            locationDoneBtn.setBackgroundColor(getResources().getColor(R.color.disable_color));
            LatLng target = mMap.getCameraPosition().target;

            if (target == null) {
                flush("Please move map marker to select location...");
                return;
            }
            //TSP testing share preference
            SharedPreferences.Editor editor = getSharedPreferences("DUME", MODE_PRIVATE).edit();
            Gson gson = new Gson();
            String mLoc = gson.toJson(target);
            editor.putString("location", mLoc);
            editor.apply();
            searchDataStore.setAnchorPoint(target);
            GeoPoint targetGeopoint = new GeoPoint(target.latitude, target.longitude);
            if (!checkIfInDB(targetGeopoint)) {
                String targetAddress = getAddress(target.latitude, target.longitude);
                MenualRecyclerData current = new MenualRecyclerData();
                String primary = "";
                String secondary = "";
                try {
                    String[] targetAddressParts = targetAddress.split("\\s*,\\s*", 2);
                    primary = targetAddressParts[0];
                    secondary = targetAddressParts[1];
                } catch (Exception e) {
                    primary = targetAddress;
                    secondary = "";
                }

                current.setPrimaryText(primary);
                current.setSecondaryText(secondary);
                current.setLocation(targetGeopoint);
                Map<String, Object> myMap = new HashMap<>();
                myMap.put("location", targetGeopoint);
                myMap.put("primary_text", primary);
                myMap.put("secondary_text", secondary);
                if (documentSnapshot != null) {
                    String preIdentify = documentSnapshot.getString("next_rp_write");
                    String identify = "rp_";
                    identify = identify + preIdentify;
                    current.setIdentify(identify);
                    recyclerMenualAdapter.updateRecent(identify, current, preIdentify);

                    mModel.updateRecentPlaces(identify, myMap, new TeacherContract.Model.Listener<Void>() {
                        @Override
                        public void onSuccess(Void list) {
                            //locationDoneBtn.setEnabled(true);
                            //locationDoneBtn.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                            hideProgress();
                            startActivity(new Intent(GrabingLocationActivity.this, CrudSkillActivity.class).setAction(DumeUtils.STUDENT));
                        }

                        @Override
                        public void onError(String msg) {
                            Log.e(TAG, msg);
                            hideProgress();
                            startActivity(new Intent(GrabingLocationActivity.this, CrudSkillActivity.class).setAction(DumeUtils.STUDENT));
                        }
                    });
                }
            } else {
                hideProgress();
                startActivity(new Intent(this, CrudSkillActivity.class).setAction(DumeUtils.STUDENT));
            }
        }
    }


    /*public void onGrabingLocationViewClicked(View view) {
        mPresenter.onGrabingLocationViewIntracted(view);
    }*/

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .enableAutoManage(this, this)
                .build();

    }

    @Override
    public void onMyGpsLocationChanged(Location location) {

    }

    @SuppressLint("CheckResult")
    private void getAutocomplete(CharSequence constraint) {
        if (mGoogleApiClient.isConnected()) {
            Log.i(TAG, "Starting autocomplete query for: " + constraint);

            // Submit the query to the autocomplete API and retrieve a PendingResult that will
            // contain the results when the query completes.
            PendingResult<AutocompletePredictionBuffer> results =
                    Places.GeoDataApi
                            .getAutocompletePredictions(mGoogleApiClient, constraint.toString(),
                                    LAT_LNG_BOUNDS, null);

            // This method should have been called off the main UI thread. Block and wait for at most 60s
            // for a result from the API.
            Callable<AutocompletePredictionBuffer> callable = () -> {
                AutocompletePredictionBuffer autocompletePredictions = results.await(60, TimeUnit.SECONDS);
                Status status = autocompletePredictions.getStatus();
                if (!status.isSuccess()) {
                    //Toast.makeText(GrabingLocationActivity.this, "Error contacting API: " + status.toString(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error getting autocomplete prediction API call: " + status.toString());
                    autocompletePredictions.release();
                    return null;
                }
                return autocompletePredictions;
            };

            Observable<AutocompletePredictionBuffer> observable = Observable.fromCallable(callable);

            Disposable disposable = observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((AutocompletePredictionBuffer item) -> {
                                ArrayList<AutocompletePrediction> autocompletePredictions = DataBufferUtils.freezeAndClose(item);

                                Log.i(TAG, "Query completed. Received final " + autocompletePredictions);
                                publishTheAutoResults(autocompletePredictions);
                            },
                            error -> Log.e(TAG, "error ", new RuntimeException(error)),
                            () -> Log.e(TAG, "DONE "));
            compositeDisposable.add(disposable);

        } else {
            Log.e(TAG, "Google API client is not connected for autocomplete query.");
        }
    }

    public void publishTheAutoResults(ArrayList<AutocompletePrediction> results) {
        if (results == null) {
            Log.e(TAG, "publishTheAutoResults: null found");
            autoCompleteRecyView.invalidate();
            hideProgress();
        } else {
            Log.e(TAG, "publishTheAutoResults: " + results);
            recyclerAutoAdapter.update(results);
            hideProgress();
            //recyclerAutoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
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

    private void setProperTittle() {
        switch (Objects.requireNonNull(retrivedAction)) {
            case "fromPPA":
            case "fromMPA":
            case "fromGLAP":
            case "fromSA":
                configToolbarTittle(this, "Select permanent address");
                break;
            case "fromSPAH":
            case "fromGLAH":
                configToolbarTittle(this, "Select home address");
                break;
            case "fromSPAW":
            case "fromGLAW":
                configToolbarTittle(this, "Select work address");
                break;
            case "fromSPAS":
                configToolbarTittle(this, "Select saving address");
                break;
            case "fromSPASN":
                addressName = fromIntent.getStringExtra("addressName");
                if (addressName != null) {
                    if (!addressName.equals("")) {
                        configToolbarTittle(this, "Select " + addressName + " address");
                    } else {
                        configToolbarTittle(this, "Select saving address");
                    }
                } else {
                    configToolbarTittle(this, "Select saving address");
                }
                break;
        }

        if (Objects.requireNonNull(retrivedAction).startsWith("from")) {
            menualCompleteRecyView.setVisibility(View.GONE);
            hackSetLocationOnMap.setVisibility(View.VISIBLE);
        } else {
            menualCompleteRecyView.setVisibility(View.VISIBLE);
            hackSetLocationOnMap.setVisibility(View.GONE);
        }
    }

    @Override
    public GeoPoint getCurrentAddress() {
        return null;
    }

    @Override
    public Map<String, Object> getHomeAddress() {
        return null;
    }

    @Override
    public Map<String, Object> getWorkAddress() {
        return null;
    }

    @Override
    public ArrayList<Map<String, Object>> getSavedPlaces() {
        return null;
    }

    @Override
    public ArrayList<Map<String, Object>> getBackInTime() {
        return null;
    }

    @Override
    public void setCurrentAddress(GeoPoint currentAddress) {
        userLocation = currentAddress;
        String address = getAddress(currentAddress.getLatitude(), currentAddress.getLongitude());
        //secondaryText[1] = "fuck fuck";
        //recyclerMenualAdapter.update(getFinalData());
    }

    @Override
    public void setHomeAddress(Map<String, Object> homeAddress) {


    }

    @Override
    public void setWorkAddress(Map<String, Object> workAddress) {

    }

    @Override
    public void setSavedPlaces(ArrayList<Map<String, Object>> savedPlaces) {

    }

    @Override
    public void setBackInTimePlaces(ArrayList<Map<String, Object>> backInTimePlaces) {

    }

    @Override
    public void flush(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if (v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }

    @Override
    public SavedPlacesAdaData generateCAAdapterData(GeoPoint geoPoint) {
        SavedPlacesAdaData currentSavedPlace = new SavedPlacesAdaData();
        String address;
        if (geoPoint != null) {
            address = getAddress(geoPoint.getLatitude(), geoPoint.getLongitude());
        } else {
            address = "null";
        }
        currentSavedPlace.setPrimary_text("Permanent address");
        currentSavedPlace.setSecondary_text(address);
        currentSavedPlace.setLocation(geoPoint);
        return currentSavedPlace;
    }

    @Override
    public void hackSetLocaOnMapClicked() {
        hideBSShowDB();
    }

    @Override
    public void setDocumentSnapshot(DocumentSnapshot documentSnapshot) {
        this.documentSnapshot = documentSnapshot;
    }

    @Override
    public void onClick(View view) {
        mPresenter.onGrabingLocationViewIntracted(view);
    }

    @Override
    public void onCenterCurrentLocation() {
        Drawable d = fab.getDrawable();
        if (d instanceof Animatable) {
            ((Animatable) d).start();
        }
        //Log.d(TAG, "onClick: clicked gps icon");
        getDeviceLocation(mMap);
    }
}
