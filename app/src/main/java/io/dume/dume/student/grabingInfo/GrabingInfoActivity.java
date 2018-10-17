package io.dume.dume.student.grabingInfo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
import java.util.List;
import java.util.Objects;

import io.dume.dume.R;
import io.dume.dume.common.inboxActivity.InboxActivity;
import io.dume.dume.student.grabingLocation.GrabingLocationActivity;
import io.dume.dume.student.grabingPackage.GrabingPackageActivity;
import io.dume.dume.student.pojo.CusStuAppComMapActivity;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.student.pojo.MyGpsLocationChangeListener;
import io.dume.dume.util.VisibleToggleClickListener;

public class GrabingInfoActivity extends CusStuAppComMapActivity implements GrabingInfoContract.View,
        MyGpsLocationChangeListener, OnMapReadyCallback {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private View decor;
    private ViewPager mViewPager;
    private int[] navIcons = {
            R.drawable.ic_medium,
            R.drawable.ic_class,
            R.drawable.ic_subject,
            R.drawable.ic_payment,
            R.drawable.ic_gender_preference,
            R.drawable.ic_cross_check

    };
    private int[] navLabels = {
            R.string.tob_medium,
            R.string.tab_class,
            R.string.tab_subject,
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
    private LinearLayout tabHintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu2_activity_grabing_info);
        setActivityContextMap(this, fromFlag);
        mPresenter = new GrabingInfoPresenter(this, new GrabingInfoModel());
        mPresenter.grabingInfoPageEnqueue();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        getLocationPermission(mapFragment);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);
        // loop through all navigation tabs
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            // inflate the Parent LinearLayout Container for the tab
            // from the layout nav_tab.xml file that we created 'R.layout.nav_tab
            LinearLayout tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tablayout_tab, null);

            // get child TextView and ImageView from this layout for the icon and label
            TextView tab_label = (TextView) tab.findViewById(R.id.nav_label);
            ImageView tab_icon = (ImageView) tab.findViewById(R.id.nav_icon);
            tab_label.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Cairo_Regular.ttf"));
            tab_label.setText(getResources().getString(navLabels[i]));
            tab_icon.setImageResource(navIcons[i]);

            // finally publish this custom view to navigation tab
            Objects.requireNonNull(tabLayout.getTabAt(i)).setCustomView(tab);
        }
        // finishes here ................

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                if (tabLayout.getSelectedTabPosition() == 5) {
                    gotoGrabingPackage();
                }

            }
        });

        setDarkStatusBarIcon();
    }

    @Override
    public void findView() {
        toolbar = findViewById(R.id.toolbar);
        hintIdOne = findViewById(R.id.hint_id_1);
        hintIdTwo = findViewById(R.id.hint_id_2);
        hintIdThree = findViewById(R.id.hint_id_3);
        mViewPager = (ViewPager) findViewById(R.id.container);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mAppBarLayout = findViewById(R.id.appbar);
        forMeBtn = findViewById(R.id.for_me_btn);
        viewMusk = findViewById(R.id.view_musk);
        contractLayout = findViewById(R.id.contract_layout);
        myAppBarLayout = findViewById(R.id.appbar);
        tabHintLayout = findViewById(R.id.tab_hint_layout);

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

        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_more_vert_black_24dp);
        toolbar.setOverflowIcon(drawable);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View tabView = tab.getCustomView();
                // get inflated children Views the icon and the label by their id
                if (tabView != null) {
                    TextView tab_label = (TextView) tabView.findViewById(R.id.nav_label);
                    ImageView tab_icon = (ImageView) tabView.findViewById(R.id.nav_icon);
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
                    TextView tab_label = (TextView) tabView.findViewById(R.id.nav_label);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                View tabView = tab.getCustomView();
                // get inflated children Views the icon and the label by their id
                if (tabView != null) {
                    ImageView tab_icon = (ImageView) tabView.findViewById(R.id.nav_icon);
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
                                if(!visible){
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
                    forMeBtn.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.ic_keyboard_arrow_up_black_24dp, 0);
                    viewMusk.setVisibility(View.VISIBLE);
                    forMeBtn.setText(R.string.switch_learner);
                } else {
                    tabHintLayout.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);
                    contractLayout.setVisibility(View.GONE);
                    viewMusk.setVisibility(View.GONE);
                    forMeBtn.setCompoundDrawablesWithIntrinsicBounds( R.drawable.alias_profile_icon, 0, R.drawable.ic_keyboard_arrow_down_black_24dp, 0);
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
        mMap.setPadding(0,  (int) (250 * (getResources().getDisplayMetrics().density)),0, 0);
    }

    @Override
    public void onMyGpsLocationChanged(Location location) {

    }

    public void onGrabingInfoViewClicked(View view) {
        mPresenter.onGrabingInfoViewIntracted(view);
    }


    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private RecyclerAdapter recyclerAdapter;
        private final int VERTICAL_ITEM_SPACE = -20;
        private GrabingInfoActivity myMainActivity;
        private RecyclerView mRecyclerView;
        private static final String TAG = "PlaceholderFragment";
        private View rootView;
        private TextView textView;
        private LinearLayout radioButtonLayout;
        private RadioGroup fragRadioGroupOne;
        private RadioGroup fragRadioGroupTwo;
        private RadioGroup fragRadioGroupThree;
        private RadioGroup fragRadioGroupFour;
        private RadioGroup fragRadioGroupFive;
        private String[] currentFragData5;
        private NestedScrollView fragmentBg;

        public PlaceholderFragment() {

        }


        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser) {
                // load data here
                int fragmentPosition = getArguments().getInt(ARG_SECTION_NUMBER);
                switch (fragmentPosition) {
                    case 1:
                        if (fragRadioGroupOne != null) {
                            fragRadioGroupOne.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                    // checkedId is the RadioButton selected
                                    RadioButton rb = (RadioButton) group.findViewById(checkedId);
                                    String currentSelectedBtnText = rb.getText().toString();
                                    myMainActivity.givenInfo[fragmentPosition - 1] = currentSelectedBtnText;
                                    myMainActivity.hintIdOne.setText(currentSelectedBtnText);
                                }
                            });
                        }
                        break;
                    case 2:
                        if (fragRadioGroupTwo != null) {
                            fragRadioGroupTwo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                    // checkedId is the RadioButton selected
                                    RadioButton rb = (RadioButton) group.findViewById(checkedId);
                                    String currentSelectedBtnText = rb.getText().toString();
                                    myMainActivity.givenInfo[fragmentPosition - 1] = currentSelectedBtnText;
                                    myMainActivity.hintIdTwo.setText(currentSelectedBtnText);
                                }
                            });
                        }
                        break;

                    case 3:
                        if (fragRadioGroupThree != null) {
                            fragRadioGroupThree.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                    // checkedId is the RadioButton selected
                                    RadioButton rb = (RadioButton) group.findViewById(checkedId);
                                    String currentSelectedBtnText = rb.getText().toString();
                                    myMainActivity.givenInfo[fragmentPosition - 1] = currentSelectedBtnText;
                                    myMainActivity.hintIdTwo.setText(currentSelectedBtnText);
                                }
                            });
                        }
                        break;

                    case 4:
                        if (fragRadioGroupFour != null) {
                            fragRadioGroupFour.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                    // checkedId is the RadioButton selected
                                    RadioButton rb = (RadioButton) group.findViewById(checkedId);
                                    String currentSelectedBtnText = rb.getText().toString();
                                    myMainActivity.givenInfo[fragmentPosition - 1] = currentSelectedBtnText;
                                    myMainActivity.hintIdTwo.setText(currentSelectedBtnText);
                                }
                            });
                        }
                        break;

                    case 5:
                        if (fragRadioGroupFive != null) {
                            fragRadioGroupFive.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                    // checkedId is the RadioButton selected
                                    RadioButton rb = (RadioButton) group.findViewById(checkedId);
                                    String currentSelectedBtnText = rb.getText().toString();
                                    myMainActivity.givenInfo[fragmentPosition - 1] = currentSelectedBtnText;
                                    myMainActivity.hintIdTwo.setText(currentSelectedBtnText);
                                }
                            });
                        }
                        break;

                    case 6:
                        if (getActivity() != null) {
                            recyclerAdapter = new RecyclerAdapter(getActivity(), getFinalData()) {
                                @Override
                                protected void OnButtonClicked(View v, int position) {
                                    //toolbar button clicked
                                    if (position == 0) {

                                    } else {
                                        assert myMainActivity != null;
                                        TabLayout.Tab tab = myMainActivity.tabLayout.getTabAt(position - 1);
                                        if (tab != null) {
                                            tab.select();
                                        }
                                    }

                                }
                            };
                            mRecyclerView.setAdapter(recyclerAdapter);
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        }
                        break;
                }
            } else {
                // fragment is no longer visible
                if (getArguments().getInt(ARG_SECTION_NUMBER) == 6) {
                    onDestroyView();

                }
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            myMainActivity = (GrabingInfoActivity) getActivity();
            String[] headers = {"Select your medium", "Select your class",
                    "Select the subject your want mentor", "Choose selary in offer", "Select gender preference", "Verify the given info"};
            int fragmentPosition = getArguments().getInt(ARG_SECTION_NUMBER);
            //garbage code goes here

            //  background color alpha changing here
            /*LinearLayout fragmentBg = rootView.findViewById(R.id.fragment_bg);
            fragmentBg.getBackground().setAlpha(90);*/


            //checking which fragment it is !!
            switch (fragmentPosition) {
                case 1:
                    rootView = inflater.inflate(R.layout.stu2_fragment_grabing_info, container, false);
                    textView = (TextView) rootView.findViewById(R.id.section_label);
                    // Setting the header text view
                    textView.setText(headers[fragmentPosition - 1]);
                    //setting the background color
                    fragmentBg = rootView.findViewById(R.id.fragment_bg);
                    fragmentBg.getBackground().setAlpha(90);
                    /*radioButtonLayout = rootView.findViewById(R.id.radio_button_linear_layout);
                    rootViewRadioButtonLayoutContent = inflater.inflate(R.layout.custom_grading_info_radio_group, container, false);
                    radioButtonLayout.addView(rootViewRadioButtonLayoutContent, 0);*/
                    RadioButton[] radioButtonsArr1 = {
                            rootView.findViewById(R.id.radio_btn_one),
                            rootView.findViewById(R.id.radio_btn_two),
                            rootView.findViewById(R.id.radio_btn_three),
                            rootView.findViewById(R.id.radio_btn_four),
                            rootView.findViewById(R.id.radio_btn_five),
                            rootView.findViewById(R.id.radio_btn_six),
                            rootView.findViewById(R.id.radio_btn_seven),
                            rootView.findViewById(R.id.radio_btn_eight),
                            rootView.findViewById(R.id.radio_btn_nine),
                            rootView.findViewById(R.id.radio_btn_ten),
                            rootView.findViewById(R.id.radio_btn_eleven),
                            rootView.findViewById(R.id.radio_btn_twelve),
                            rootView.findViewById(R.id.radio_btn_thirteen),
                            rootView.findViewById(R.id.radio_btn_fourteen),
                            rootView.findViewById(R.id.radio_btn_fifteen),
                    };

                    currentFragData5 = getData();
                    for (int i = 0; i < radioButtonsArr1.length; i++) {
                        if (i < currentFragData5.length) {
                            radioButtonsArr1[i].setText(currentFragData5[i]);
                        } else {
                            radioButtonsArr1[i].setVisibility(View.GONE);
                        }
                    }

                    fragRadioGroupOne = (RadioGroup) rootView.findViewById(R.id.frag_radio_group);
                    fragRadioGroupOne.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            // checkedId is the RadioButton selected
                            RadioButton rb = (RadioButton) group.findViewById(checkedId);
                            String currentSelectedBtnText = rb.getText().toString();
                            myMainActivity.givenInfo[fragmentPosition - 1] = currentSelectedBtnText;
                            myMainActivity.hintIdOne.setText(currentSelectedBtnText);

                        }
                    });
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.stu2_fragment_grabing_info_one, container, false);
                    textView = (TextView) rootView.findViewById(R.id.section_label);
                    // Setting the header text view
                    textView.setText(headers[fragmentPosition - 1]);
                    //setting the background color
                    fragmentBg = rootView.findViewById(R.id.fragment_bg);
                    fragmentBg.getBackground().setAlpha(90);
                    RadioButton[] radioButtonsArr2 = {
                            rootView.findViewById(R.id.radio_btn_one),
                            rootView.findViewById(R.id.radio_btn_two),
                            rootView.findViewById(R.id.radio_btn_three),
                            rootView.findViewById(R.id.radio_btn_four),
                            rootView.findViewById(R.id.radio_btn_five),
                            rootView.findViewById(R.id.radio_btn_six),
                            rootView.findViewById(R.id.radio_btn_seven),
                            rootView.findViewById(R.id.radio_btn_eight),
                            rootView.findViewById(R.id.radio_btn_nine),
                            rootView.findViewById(R.id.radio_btn_ten),
                            rootView.findViewById(R.id.radio_btn_eleven),
                            rootView.findViewById(R.id.radio_btn_twelve),
                            rootView.findViewById(R.id.radio_btn_thirteen),
                            rootView.findViewById(R.id.radio_btn_fourteen),
                            rootView.findViewById(R.id.radio_btn_fifteen),
                    };

                    currentFragData5 = getData();
                    for (int i = 0; i < radioButtonsArr2.length; i++) {
                        if (i < currentFragData5.length) {
                            radioButtonsArr2[i].setText(currentFragData5[i]);
                        } else {
                            radioButtonsArr2[i].setVisibility(View.GONE);
                        }
                    }

                    fragRadioGroupTwo = (RadioGroup) rootView.findViewById(R.id.frag_radio_group);
                    fragRadioGroupTwo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            // checkedId is the RadioButton selected
                            RadioButton rb = (RadioButton) group.findViewById(checkedId);
                            String currentSelectedBtnText = rb.getText().toString();
                            myMainActivity.givenInfo[fragmentPosition - 1] = currentSelectedBtnText;
                            myMainActivity.hintIdTwo.setText(currentSelectedBtnText);
                        }
                    });
                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.stu2_fragment_grabing_info_two, container, false);
                    textView = (TextView) rootView.findViewById(R.id.section_label);
                    // Setting the header text view
                    textView.setText(headers[fragmentPosition - 1]);
                    //setting the background color
                    fragmentBg = rootView.findViewById(R.id.fragment_bg);
                    fragmentBg.getBackground().setAlpha(90);
                    RadioButton[] radioButtonsArr3 = {
                            rootView.findViewById(R.id.radio_btn_one),
                            rootView.findViewById(R.id.radio_btn_two),
                            rootView.findViewById(R.id.radio_btn_three),
                            rootView.findViewById(R.id.radio_btn_four),
                            rootView.findViewById(R.id.radio_btn_five),
                            rootView.findViewById(R.id.radio_btn_six),
                            rootView.findViewById(R.id.radio_btn_seven),
                            rootView.findViewById(R.id.radio_btn_eight),
                            rootView.findViewById(R.id.radio_btn_nine),
                            rootView.findViewById(R.id.radio_btn_ten),
                            rootView.findViewById(R.id.radio_btn_eleven),
                            rootView.findViewById(R.id.radio_btn_twelve),
                            rootView.findViewById(R.id.radio_btn_thirteen),
                            rootView.findViewById(R.id.radio_btn_fourteen),
                            rootView.findViewById(R.id.radio_btn_fifteen),
                    };

                    currentFragData5 = getData();
                    for (int i = 0; i < radioButtonsArr3.length; i++) {
                        if (i < currentFragData5.length) {
                            radioButtonsArr3[i].setText(currentFragData5[i]);
                        } else {
                            radioButtonsArr3[i].setVisibility(View.GONE);
                        }
                    }

                    fragRadioGroupThree = (RadioGroup) rootView.findViewById(R.id.frag_radio_group);
                    fragRadioGroupThree.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            // checkedId is the RadioButton selected
                            RadioButton rb = (RadioButton) group.findViewById(checkedId);
                            String currentSelectedBtnText = rb.getText().toString();
                            myMainActivity.givenInfo[fragmentPosition - 1] = currentSelectedBtnText;
                            myMainActivity.hintIdTwo.setText(currentSelectedBtnText);
                        }
                    });
                    break;
                case 4:
                    rootView = inflater.inflate(R.layout.stu2_fragment_grabing_info_three, container, false);
                    textView = (TextView) rootView.findViewById(R.id.section_label);
                    // Setting the header text view
                    textView.setText(headers[fragmentPosition - 1]);
                    //setting the background color
                    fragmentBg = rootView.findViewById(R.id.fragment_bg);
                    fragmentBg.getBackground().setAlpha(90);
                    RadioButton[] radioButtonsArr4 = {
                            rootView.findViewById(R.id.radio_btn_one),
                            rootView.findViewById(R.id.radio_btn_two),
                            rootView.findViewById(R.id.radio_btn_three),
                            rootView.findViewById(R.id.radio_btn_four),
                            rootView.findViewById(R.id.radio_btn_five),
                            rootView.findViewById(R.id.radio_btn_six),
                            rootView.findViewById(R.id.radio_btn_seven),
                            rootView.findViewById(R.id.radio_btn_eight),
                            rootView.findViewById(R.id.radio_btn_nine),
                            rootView.findViewById(R.id.radio_btn_ten),
                            rootView.findViewById(R.id.radio_btn_eleven),
                            rootView.findViewById(R.id.radio_btn_twelve),
                            rootView.findViewById(R.id.radio_btn_thirteen),
                            rootView.findViewById(R.id.radio_btn_fourteen),
                            rootView.findViewById(R.id.radio_btn_fifteen),
                    };

                    currentFragData5 = getData();
                    for (int i = 0; i < radioButtonsArr4.length; i++) {
                        if (i < currentFragData5.length) {
                            radioButtonsArr4[i].setText(currentFragData5[i]);
                        } else {
                            radioButtonsArr4[i].setVisibility(View.GONE);
                        }
                    }

                    fragRadioGroupFour = (RadioGroup) rootView.findViewById(R.id.frag_radio_group);
                    fragRadioGroupFour.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            // checkedId is the RadioButton selected
                            RadioButton rb = (RadioButton) group.findViewById(checkedId);
                            String currentSelectedBtnText = rb.getText().toString();
                            myMainActivity.givenInfo[fragmentPosition - 1] = currentSelectedBtnText;
                            myMainActivity.hintIdTwo.setText(currentSelectedBtnText);
                        }
                    });
                    break;
                case 5:
                    rootView = inflater.inflate(R.layout.stu2_fragment_grabing_info_four, container, false);
                    textView = (TextView) rootView.findViewById(R.id.section_label);
                    // Setting the header text view
                    textView.setText(headers[fragmentPosition - 1]);
                    //setting the background color
                    fragmentBg = rootView.findViewById(R.id.fragment_bg);
                    fragmentBg.getBackground().setAlpha(90);
                    RadioButton[] radioButtonsArr5 = {
                            rootView.findViewById(R.id.radio_btn_one),
                            rootView.findViewById(R.id.radio_btn_two),
                            rootView.findViewById(R.id.radio_btn_three),
                            rootView.findViewById(R.id.radio_btn_four),
                            rootView.findViewById(R.id.radio_btn_five),
                            rootView.findViewById(R.id.radio_btn_six),
                            rootView.findViewById(R.id.radio_btn_seven),
                            rootView.findViewById(R.id.radio_btn_eight),
                            rootView.findViewById(R.id.radio_btn_nine),
                            rootView.findViewById(R.id.radio_btn_ten),
                            rootView.findViewById(R.id.radio_btn_eleven),
                            rootView.findViewById(R.id.radio_btn_twelve),
                            rootView.findViewById(R.id.radio_btn_thirteen),
                            rootView.findViewById(R.id.radio_btn_fourteen),
                            rootView.findViewById(R.id.radio_btn_fifteen),
                    };
                    currentFragData5 = getData();
                    for (int i = 0; i < radioButtonsArr5.length; i++) {
                        if (i < currentFragData5.length) {
                            radioButtonsArr5[i].setText(currentFragData5[i]);
                        } else {
                            radioButtonsArr5[i].setVisibility(View.GONE);
                        }
                    }

                    fragRadioGroupFive = (RadioGroup) rootView.findViewById(R.id.frag_radio_group);
                    fragRadioGroupFive.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            // checkedId is the RadioButton selected
                            RadioButton rb = (RadioButton) group.findViewById(checkedId);
                            String currentSelectedBtnText = rb.getText().toString();
                            myMainActivity.givenInfo[fragmentPosition - 1] = currentSelectedBtnText;
                            myMainActivity.hintIdTwo.setText(currentSelectedBtnText);
                        }
                    });
                    break;
                case 6:
                    //setting the background color
                    rootView = inflater.inflate(R.layout.stu2_fragment_grabing_info_five, container, false);
                    LinearLayout thisFragmentBg = rootView.findViewById(R.id.fragment_bg);
                    thisFragmentBg.getBackground().setAlpha(90);
                    mRecyclerView = rootView.findViewById(R.id.recycler_view_list);
                    recyclerAdapter = new RecyclerAdapter(getActivity(), getFinalData()) {
                        @Override
                        protected void OnButtonClicked(View v, int position) {
                            //toolbar button clicked
                            if (position == 0) {

                            } else {
                                assert myMainActivity != null;
                                TabLayout.Tab tab = myMainActivity.tabLayout.getTabAt(position - 1);
                                if (tab != null) {
                                    tab.select();
                                }
                            }

                        }
                    };
                    mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
                    mRecyclerView.setAdapter(recyclerAdapter);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    break;
            }

            return rootView;
        }

        public String[] getData() {
            String[] titles = {"can't match anything"};
            String[] mediums = {"Bangla Medium", "Bangla Version", "English Medium", "English Version", "Others"};
            String[] classes = {"University", "A-Level", "O-Level", "Class-8", "Class-7", "Class-6", "Class-5", "Class-4", "Class-3", "Class-2", "Class-1", "Others"};
            String[] subjects = {"Physics", "Chemistry", "Mathematics", "Biology", "English", "Bengali"};
            String[] salaries = {"4k-5k", "5k-6k", "6k-8k", "8k-10k", "10k-15k", "15k-20k"};
            String[] genderPreference = {"Male", "Female", "Both"};
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    return mediums;
                case 2:
                    return classes;
                case 3:
                    return subjects;
                case 4:
                    return salaries;
                case 5:
                    return genderPreference;
                default:
                    return titles;
            }
        }

        public List<RecycleData> getFinalData() {
            List<RecycleData> data = new ArrayList<>();
            String[] finalInfo = {"For :", "Medium :", "Class :", "Subject :", "Salary :", "Gender Preference :"};
            for (int i = 0; i < finalInfo.length; i++) {
                if (i == 0) {

                } else {
                    if (!(myMainActivity.givenInfo[i - 1].startsWith("Ex"))) {
                        finalInfo[i] += " " + myMainActivity.givenInfo[i - 1];
                    }
                }
            }
            for (String title : finalInfo) {
                RecycleData current = new RecycleData();
                current.options = title;
                data.add(current);
            }
            return data;
        }

        public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

            private final int verticalSpaceHeight;

            VerticalSpaceItemDecoration(int verticalSpaceHeight) {
                this.verticalSpaceHeight = verticalSpaceHeight;
            }

            public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                       RecyclerView.State state) {

                if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                    outRect.bottom = verticalSpaceHeight;
                }
            }
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 6;
        }
    }


}
