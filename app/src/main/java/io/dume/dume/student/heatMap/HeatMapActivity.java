package io.dume.dume.student.heatMap;

import android.annotation.SuppressLint;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import carbon.widget.LinearLayout;
import io.dume.dume.R;
import io.dume.dume.customView.WindowInsetsFrameLayout;
import io.dume.dume.student.pojo.CusStuAppComMapActivity;
import io.dume.dume.student.pojo.MyGpsLocationChangeListener;
import io.dume.dume.util.VisibleToggleClickListener;
import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class HeatMapActivity extends CusStuAppComMapActivity implements OnMapReadyCallback, MyGpsLocationChangeListener,
        HeatMapContract.View {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stunav_activity1_heat_map);
        setActivityContextMap(this, fromFlag);
        mPresenter = new HeatMapPresenter(this, new HeatMapModel());
        mPresenter.heatMapEnqueue();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        getLocationPermission(mapFragment);
        //setting the adapter with the recycler view
        HeatMapAccountRecyAda heatMapAccountRecyAda = new HeatMapAccountRecyAda(this, getFinalData()) {
            @Override
            protected void OnAccouItemClicked(View v, int position) {

            }
        };
        myAccountRecycler = findViewById(R.id.heatMap_account_recycler);
        myAccountRecycler.setAdapter(heatMapAccountRecyAda);
        myAccountRecycler.setLayoutManager(new LinearLayoutManager(this));

        //initializing the animation
        myAppBarLayout = findViewById(R.id.my_appbarLayout);
        chooseAccouTypeBtn = findViewById(R.id.choose_account_type_btn);
        toolbarContainer = findViewById(R.id.toolbar_container);
        viewMusk = findViewById(R.id.view_musk);
        parentCoorLayout = findViewById(R.id.parent_coor_layout);
//        toolbarContainer.bringToFront();
        myAppBarLayout.bringToFront();
        myAppBarLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));


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
                //myAccountRecycler.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
                if (visible) {
                    myAccountRecycler.setVisibility(View.VISIBLE);
                    viewMusk.setVisibility(View.VISIBLE);
                } else {
                    myAccountRecycler.setVisibility(View.INVISIBLE);
                    viewMusk.setVisibility(View.INVISIBLE);
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
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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

    public void onHeatMapViewClicked(View view) {
    }

    @Override
    public void onMyGpsLocationChanged(Location location) {

    }

    @Override
    public void configHeatMap() {

    }

    @Override
    public void initHeatMap() {

    }

    @Override
    public void findView() {

    }

    public List<AccountRecyData> getFinalData() {
        List<AccountRecyData> data = new ArrayList<>();
        String[] accountType = getResources().getStringArray(R.array.AccountType);
        int[] imageIcons = {
                R.drawable.ic_default_student_profile,
                R.drawable.ic_default_mentor_profile,
                R.drawable.ic_default_bootcamp_profile
        };

        for (int i = 0; i < accountType.length && i < imageIcons.length; i++) {
            AccountRecyData current = new AccountRecyData();
            current.accouName = accountType[i];
            current.iconId = imageIcons[i];
            data.add(current);
        }
        return data;
    }
}
