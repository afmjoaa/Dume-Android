package io.dume.dume.student.grabingInfo;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import io.dume.dume.R;
import io.dume.dume.inter_face.OnTabModificationListener;
import io.dume.dume.student.grabingLocation.GrabingLocationActivity;
import io.dume.dume.student.grabingPackage.GrabingPackageActivity;
import io.dume.dume.student.pojo.CusStuAppComMapActivity;
import io.dume.dume.student.pojo.MyGpsLocationChangeListener;
import io.dume.dume.teacher.model.LocalDb;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.OnViewClick;
import io.dume.dume.util.VisibleToggleClickListener;

public class GrabingInfoActivity extends CusStuAppComMapActivity implements GrabingInfoContract.View,
        MyGpsLocationChangeListener, OnMapReadyCallback, OnViewClick, OnTabModificationListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private View decor;
    private ViewPager mViewPager;
    private int[] navIcons = {
            R.drawable.ic_payment,
            R.drawable.ic_gender_preference,
            R.drawable.ic_cross_check,
            R.drawable.ic_action_down,
            R.drawable.ic_medium,
            R.drawable.ic_subject,
            R.drawable.ic_class,
            R.drawable.ic_action_update

    };
    int lastPostion = 0;

    private int[] navLabels = {
            R.string.tab_payment,
            R.string.tab_gender_preference,
            R.string.tab_cross_ckeck
    };
    private String[] givenInfo = {"Ex.Others", "Ex.O level", "Ex.Physics", "Ex.3k - 6k", "Ex.Both", "→←"};
    private TabLayout tabLayout;
    private TextView hintIdOne;
    private TextView hintIdTwo;
    private TextView hintIdThree;
    private static final String TAG = "GrabingInfoActivity";
    private static final int fromFlag = 3;
    private GrabingInfoContract.Presenter mPresenter;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private ActionBar supportActionBar;
    private AppBarLayout mAppBarLayout;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private Button forMeBtn;
    private FrameLayout viewMusk;
    private LinearLayout contractLayout;
    private AppBarLayout myAppBarLayout;
    private LinearLayout tabHintLayout, forMeWrapper;
    private int selected_category_position;
    private int dynamicTab;
    private LocalDb db;

    private List<String> queryList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu2_activity_grabing_info);
        setActivityContextMap(this, fromFlag);
        mPresenter = new GrabingInfoPresenter(this, new GrabingInfoModel());
        queryList = new ArrayList<>();
        mPresenter.grabingInfoPageEnqueue();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        getLocationPermission(mapFragment);
        getAction();
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), 1, getIntent().getIntExtra(DumeUtils.SELECTED_ID, 0), this);
        mSectionsPagerAdapter.newTab(new LocalDb().getLevelOne(getIntent().getIntExtra(DumeUtils.SELECTED_ID, 0)));

        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(10);
        Log.w(TAG, "onCreate: " + tabLayout.getTabCount());
        //    mSectionsPagerAdapter.notifyDataSetChanged();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        setDarkStatusBarIcon();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void createTabDynamically() {

    }

    @Override
    public void findView() {
        toolbar = findViewById(R.id.toolbar);
        hintIdOne = findViewById(R.id.hint_id_1);
        hintIdTwo = findViewById(R.id.hint_id_2);
        hintIdThree = findViewById(R.id.hint_id_3);
        mViewPager = findViewById(R.id.container);
        fab = findViewById(R.id.fab);
        tabLayout = findViewById(R.id.tabs);
        mAppBarLayout = findViewById(R.id.appbar);
        forMeBtn = findViewById(R.id.for_me_btn);
        viewMusk = findViewById(R.id.view_musk);
        contractLayout = findViewById(R.id.contract_layout);
        tabHintLayout = findViewById(R.id.tab_hint_layout);
        forMeWrapper = findViewById(R.id.formeWrapper);

    }

    @Override
    public void viewMuskClicked() {
        forMeBtn.performClick();
    }

    @Override
    public void initGrabingInfoPage() {
        setSupportActionBar(toolbar);
        supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }

        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_more_vert_black_24dp);
        toolbar.setOverflowIcon(drawable);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View tabView = tab.getCustomView();
                // get inflated children Views the icon and the label by their id
                if (tabView != null) {
                    TextView tab_label = tabView.findViewById(R.id.nav_label);
                    ImageView tab_icon = tabView.findViewById(R.id.nav_icon);
                    Drawable drawableIcon = tab_icon.getDrawable();

                    if (drawableIcon instanceof Animatable) {
                        ((Animatable) drawableIcon).start();
                    }
                }
                int tabPosition = tab.getPosition();
                animateFab(tabPosition);
                switch (tabPosition) {
                    case 0:
                        hintIdOne.setText(givenInfo[tabPosition]);
                        hintIdTwo.setText(givenInfo[tabPosition + 1]);
                        hintIdThree.setText(givenInfo[tabPosition + 2]);
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        hintIdOne.setText(givenInfo[tabPosition - 1]);
                        hintIdTwo.setText(givenInfo[tabPosition]);
                        hintIdThree.setText(givenInfo[tabPosition + 1]);
                        break;
                    case 5:
                        hintIdOne.setText(givenInfo[tabPosition - 2]);
                        hintIdTwo.setText(givenInfo[tabPosition - 1]);
                        hintIdThree.setText(givenInfo[tabPosition]);
                        break;
                    default:
                        break;
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View tabView = tab.getCustomView();
                // get inflated children Views the icon and the label by their id
                if (tabView != null) {
                    TextView tab_label = tabView.findViewById(R.id.nav_label);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                View tabView = tab.getCustomView();
                // get inflated children Views the icon and the label by their id
                if (tabView != null) {
                    ImageView tab_icon = tabView.findViewById(R.id.nav_icon);
                    Drawable drawableIcon = tab_icon.getDrawable();

                    if (drawableIcon instanceof Animatable) {
                        ((Animatable) drawableIcon).start();
                    }
                }
                int tabPosition = tab.getPosition();
                animateFab(tabPosition);
                switch (tabPosition) {
                    case 0:
                        hintIdOne.setText(givenInfo[tabPosition]);
                        hintIdTwo.setText(givenInfo[tabPosition + 1]);
                        hintIdThree.setText(givenInfo[tabPosition + 2]);
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        hintIdOne.setText(givenInfo[tabPosition - 1]);
                        hintIdTwo.setText(givenInfo[tabPosition]);
                        hintIdThree.setText(givenInfo[tabPosition + 1]);
                        break;
                    case 5:
                        hintIdOne.setText(givenInfo[tabPosition - 2]);
                        hintIdTwo.setText(givenInfo[tabPosition - 1]);
                        hintIdThree.setText(givenInfo[tabPosition]);
                        break;
                    default:
                        break;
                }
            }
        });

    }

    @Override
    public void configGrabingInfoPage() {
        forMeBtn.setOnClickListener(new VisibleToggleClickListener() {

            @Override
            protected void changeVisibility(boolean visible) {
                TransitionSet set = new TransitionSet()
                        .addTransition(new Fade())
                        .addTransition(new Slide(Gravity.START))
                        .setInterpolator(visible ? new LinearOutSlowInInterpolator() : new FastOutLinearInInterpolator())
                        .addListener(new Transition.TransitionListener() {
                            @Override
                            public void onTransitionStart(@NonNull Transition transition) {

                            }

                            @Override
                            public void onTransitionEnd(@NonNull Transition transition) {
                                if (!visible) {
                                    contractLayout.setVisibility(View.GONE);
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
                    tabHintLayout.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);
                    contractLayout.setVisibility(View.VISIBLE);
                    forMeBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_up_black_24dp, 0);
                    viewMusk.setVisibility(View.VISIBLE);
                    forMeBtn.setText(R.string.switch_learner);
                } else {
                    tabHintLayout.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);
                    contractLayout.setVisibility(View.GONE);
                    viewMusk.setVisibility(View.INVISIBLE);
                    forMeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.alias_profile_icon, 0, R.drawable.ic_keyboard_arrow_down_black_24dp, 0);
                    forMeBtn.setText(R.string.for_me);

                }
            }

        });
    }

    //unused function here
    private void gotoGrabingLocation() {
        Log.d(TAG, "gotoGrabingLocation: fucking function called");
        startActivity(new Intent(this, GrabingLocationActivity.class));
    }

    private void gotoGrabingPackage() {
        startActivity(new Intent(this, GrabingPackageActivity.class));
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void animateFab(int position) {
        fab.clearAnimation();
        // Scale down animation
        ScaleAnimation shrink = new ScaleAnimation(1f, 0.2f, 1f, 0.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        shrink.setDuration(150);     // animation duration in milliseconds
        shrink.setInterpolator(new DecelerateInterpolator());
        shrink.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Scale up animation
                ScaleAnimation expand = new ScaleAnimation(0.2f, 1f, 0.2f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                expand.setDuration(100);     // animation duration in milliseconds
                expand.setInterpolator(new AccelerateInterpolator());
                fab.startAnimation(expand);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        fab.startAnimation(shrink);
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

    public void onGrabingInfoViewClicked(View view) {
        mPresenter.onGrabingInfoViewIntracted(view);
    }


    public void getAction() {
        selected_category_position = getIntent().getIntExtra(DumeUtils.SELECTED_ID, 0);
        forMeWrapper.setVisibility(View.GONE);
        toolbar.setTitle("Select Skill");
        createTabDynamically();
        queryList.add(new LocalDb().getCategories().get(selected_category_position));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (fab != null) {
            onNewTabCreated("Enam");
        }
    }

    @Override
    public void onRadioButtonClick(RadioButton view, int fragmentId, String levelName) {
        boolean finished = false;
        //'  flush("Level  " + fragmentId);
        if (queryList.size() > fragmentId + 1) {
            int length = queryList.size();

            for (int i = fragmentId + 1; i < length; i++) {
                queryList.remove(queryList.size() - 1);
            }
        }
        queryList.add(fragmentId + 1, view.getText().toString());
        //  flush(queryList.toString());
        db = new LocalDb();
        ArrayList<String> arr = new ArrayList<>(Arrays.asList("Salary", "Gender", "Cross Check"));
        if (arr.contains(levelName)) {
            switch (levelName) {
                case "Salary":
                    if (!(fragmentId < tabLayout.getTabCount() - 1)) {
                        mSectionsPagerAdapter.newTab(db.crossCheck);
                        mViewPager.setCurrentItem(fragmentId + 1);
                    } else mViewPager.setCurrentItem(fragmentId + 1);
                    break;
                case "Gender":
                    if (!(fragmentId < tabLayout.getTabCount() - 1)) {
                        mSectionsPagerAdapter.newTab(db.payment);
                        mViewPager.setCurrentItem(fragmentId + 1);
                    } else mViewPager.setCurrentItem(fragmentId + 1);
                    break;
                case "Cross Check":

                    break;
            }
        } else if (fragmentId == 0) {
            mSectionsPagerAdapter.removeTabs(fragmentId + 1);
            List<String> levelTwo = db.getLevelTwo(view.getText().toString(), levelName);
            if (levelTwo != null) {
                mSectionsPagerAdapter.newTab(levelTwo);
            } else {
                generateNextTabs(fragmentId);
                return;
            }


        } else if (fragmentId == 1) {
            mSectionsPagerAdapter.removeTabs(fragmentId + 1);
            List<String> levelThree = db.getLevelThree(view.getText().toString(), queryList.get(fragmentId));
            if (levelThree != null) {
                mSectionsPagerAdapter.newTab(levelThree);
            } else {
                generateNextTabs(fragmentId);
                return;
            }

        } else if (fragmentId == 2) {
            mSectionsPagerAdapter.removeTabs(fragmentId + 1);
            List<String> levelFour = db.getLevelFour(view.getText().toString(), queryList.get(fragmentId), queryList.get(fragmentId - 1));
            if (levelFour != null) {
                mSectionsPagerAdapter.newTab(levelFour);
            } else {
                generateNextTabs(fragmentId);
                return;
            }
        } else if (fragmentId == 3) {
            generateNextTabs(fragmentId);
            return;
        }


        mViewPager.setCurrentItem(tabLayout.getTabCount() - 1);
    }


    public void generateNextTabs(int fragment) {
        flush("End of the Nest");
        if (!(fragment < tabLayout.getTabCount() - 1)) {
            mSectionsPagerAdapter.newTab(db.getGenderPreferencesList());
            mViewPager.setCurrentItem(fragment + 1);
        } else mViewPager.setCurrentItem(fragment + 1);


    }

    public void flush(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNewTabCreated(String tabName) {
        tabLayout.invalidate();
        int[] wh = DumeUtils.getScreenSize(this);
        int tabMinWidth = ((wh[0] / 3) - (int) (24 * (getResources().getDisplayMetrics().density)));
        LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams
                (tabMinWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        textParam.setMargins(0, 0, 0, 3 * (int) (getResources().getDisplayMetrics().density));
        Log.e(TAG, "enam what this to be called " + tabLayout.getTabCount());
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            LinearLayout tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tablayout_tab, null);
            TextView tab_label = (TextView) tab.findViewById(R.id.nav_label);
            ImageView tab_icon = (ImageView) tab.findViewById(R.id.nav_icon);
            tab_label.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Cairo-Light.ttf"));
            tab_label.setText(tabLayout.getTabAt(i).getText());
            tab_icon.setImageResource(navIcons[i]);
            tab_label.setLayoutParams(textParam);
            Objects.requireNonNull(tabLayout.getTabAt(i)).setCustomView(tab);
        }
    }

    @Override
    public void onTabDeleted() {

    }
}
