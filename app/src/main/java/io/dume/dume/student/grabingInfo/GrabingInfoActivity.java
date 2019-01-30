package io.dume.dume.student.grabingInfo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.google.firebase.firestore.GeoPoint;
import com.google.maps.android.ui.IconGenerator;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import carbon.widget.RelativeLayout;
import id.zelory.compressor.Compressor;
import io.dume.dume.R;
import io.dume.dume.inter_face.OnTabModificationListener;
import io.dume.dume.inter_face.usefulListeners;
import io.dume.dume.model.DumeModel;
import io.dume.dume.model.TeacherModel;
import io.dume.dume.student.grabingPackage.GrabingPackageActivity;
import io.dume.dume.student.homePage.HomePageActivity;
import io.dume.dume.student.homePage.adapter.HomePageRatingAdapter;
import io.dume.dume.student.pojo.CusStuAppComMapActivity;
import io.dume.dume.student.pojo.MyGpsLocationChangeListener;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.homepage.TeacherDataStore;
import io.dume.dume.teacher.model.LocalDb;
import io.dume.dume.teacher.pojo.Skill;
import io.dume.dume.teacher.skill.SkillActivity;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.FileUtil;
import io.dume.dume.util.OnViewClick;
import io.dume.dume.util.RadioBtnDialogue;
import io.dume.dume.util.VisibleToggleClickListener;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static io.dume.dume.util.ImageHelper.getRoundedCornerBitmap;
import static io.dume.dume.util.ImageHelper.getRoundedCornerBitmapSquare;

public class GrabingInfoActivity extends CusStuAppComMapActivity implements GrabingInfoContract.View,
        MyGpsLocationChangeListener, OnMapReadyCallback, OnViewClick, OnTabModificationListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private int[] navIcons = {
            R.drawable.xxx_level_vector,
            R.drawable.ic_medium,
            R.drawable.ic_class,
            R.drawable.ic_subject,
            R.drawable.xxx_field_vector,
            R.drawable.xxx_division_branch_vector,
            R.drawable.xxx_language_vector,
            R.drawable.xxx_programing_lang_vector,
            R.drawable.xxx_software_vector,
            R.drawable.xxx_music_vector,
            R.drawable.xxx_item_vector,
            R.drawable.xxx_flavour_type_vector,
            R.drawable.xxx_degree_vector,
            R.drawable.ic_cross_check,
            R.drawable.ic_gender_preference,
            R.drawable.ic_payment,
            R.drawable.xxx_item_capacity
    };
    private int[] navLabels = {
            R.string.tab_payment,
            R.string.tab_gender_preference,
            R.string.tab_cross_ckeck
    };
    private String[] givenInfo = {"Ex.Others", "Ex.O level", "Ex.Physics", "Ex.3k - 6k", "Ex.Both", "→←"};
    protected TabLayout tabLayout;
    protected TextView hintIdOne;
    protected TextView hintIdTwo;
    protected TextView hintIdThree;
    private static final String TAG = "GrabingInfoActivity";
    private static final int fromFlag = 3;
    private GrabingInfoContract.Presenter mPresenter;
    private Toolbar toolbar;
    public FloatingActionButton fab;
    private ActionBar supportActionBar;
    private AppBarLayout mAppBarLayout;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    public Button forMeBtn;
    private FrameLayout viewMusk;
    private LinearLayout contractLayout;
    private AppBarLayout myAppBarLayout;
    private LinearLayout tabHintLayout, forMeWrapper;
    private int selected_category_position;
    private int dynamicTab;
    private LocalDb db;
    public String retrivedAction;
    protected List<String> queryList;
    protected List<String> queryListName;
    private int[] wh;
    private int tabMinWidthThree;
    private int tabMinWidthTwo;
    private int tabMinWidthOne;
    private LinearLayout.LayoutParams textParamThree;
    private LinearLayout.LayoutParams textParamTwo;
    private LinearLayout.LayoutParams textParamOne;
    private RelativeLayout selectFromContact;
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;     // contacts unique ID
    private ArrayList<String> endOfNest;
    private String tempThreeHint;
    private Boolean contactPermissionGranted = false;
    private RelativeLayout secondContactLayout;
    private carbon.widget.ImageView secondContactImageView;
    private TextView secondContactPerson;
    private TextView secondContactPersonNum;
    TeacherModel teacherModel;
    public String forName = "Me";
    private ImageView secondContactSelectImage;
    private RelativeLayout firstContactLayout;
    private carbon.widget.ImageView firstContactImageView;
    private TextView firstContactPerson;
    private TextView firstContactPersonNum;
    private ImageView firstContactSelectImage;
    private Drawable imgKeyBoardDown;
    private Drawable imgKeyBoardUp;
    private Bitmap contactBitmap = null;
    private String contactName;
    private boolean forMySelf = true;
    private Bitmap photo = null;
    private FrameLayout alwaysViewMusk;
    private String defaultUrl;
    private View mCustomMarkerView;
    private carbon.widget.ImageView mMarkerImageView;
    private IconGenerator iconFactory;
    private GrabingInfoModel mModel;
    private String contactAvatarString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu2_activity_grabing_info);
        setActivityContextMap(this, fromFlag);
        findLoadView();
        final GrabingInfoModel mModel = new GrabingInfoModel(this);
        mPresenter = new GrabingInfoPresenter(this, mModel);
        teacherModel = new DumeModel();
        mPresenter.grabingInfoPageEnqueue();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        getLocationPermission(mapFragment);
        queryList = new ArrayList<>();
        queryListName = new ArrayList<>();
        retrivedAction = getIntent().getAction();
        if (retrivedAction != null) {
            getAction();
        }
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), 1, getIntent().getIntExtra(DumeUtils.SELECTED_ID, 0), this);
        mSectionsPagerAdapter.newTab(new LocalDb().getLevelOne(getIntent().getIntExtra(DumeUtils.SELECTED_ID, 0)));
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(15);
        //mSectionsPagerAdapter.notifyDataSetChanged();
        setDarkStatusBarIcon();
        onNewTabCreated("Enam");
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        alwaysViewMusk = findViewById(R.id.always_view_musk);
        contractLayout = findViewById(R.id.contract_layout);
        tabHintLayout = findViewById(R.id.tab_hint_layout);
        forMeWrapper = findViewById(R.id.formeWrapper);
        selectFromContact = findViewById(R.id.select_other_contact);
        endOfNest = new ArrayList<>(Arrays.asList("Subject", "Field", "Software", "Language", "Flavour", "Type", "Course", " Language "));

        secondContactLayout = findViewById(R.id.second_contact);
        secondContactImageView = findViewById(R.id.account_icon_two);
        secondContactPerson = findViewById(R.id.account_type_textview_two);
        secondContactPersonNum = findViewById(R.id.account_type_textview_two_value);
        secondContactSelectImage = findViewById(R.id.account_selected_icon_container_two);

        firstContactLayout = findViewById(R.id.first_contact);
        firstContactImageView = findViewById(R.id.account_icon);
        firstContactPerson = findViewById(R.id.account_type_textview);
        firstContactPersonNum = findViewById(R.id.account_type_textview_value);
        firstContactSelectImage = findViewById(R.id.account_selected_icon_container);

        //init code
        wh = DumeUtils.getScreenSize(this);
        tabMinWidthThree = ((wh[0] / 3) - (int) (24 * (getResources().getDisplayMetrics().density)));
        tabMinWidthTwo = ((wh[0] / 2) - (int) (24 * (getResources().getDisplayMetrics().density)));
        tabMinWidthOne = ((wh[0]) - (int) (24 * (getResources().getDisplayMetrics().density)));
        textParamThree = new LinearLayout.LayoutParams(tabMinWidthThree, LinearLayout.LayoutParams.WRAP_CONTENT);
        textParamTwo = new LinearLayout.LayoutParams(tabMinWidthTwo, LinearLayout.LayoutParams.WRAP_CONTENT);
        textParamOne = new LinearLayout.LayoutParams(tabMinWidthOne, LinearLayout.LayoutParams.WRAP_CONTENT);
        imgKeyBoardDown = getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_black_24dp);
        imgKeyBoardUp = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
        mCustomMarkerView = ((LayoutInflater) Objects.requireNonNull(getSystemService(LAYOUT_INFLATER_SERVICE))).inflate(R.layout.custom_marker_view, null);
        mMarkerImageView = mCustomMarkerView.findViewById(R.id.profile_image);
        iconFactory = new IconGenerator(this);

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
                if (fab.getVisibility() == View.VISIBLE) {
                    animateFab(tabPosition);
                }
                //setting the hint layout text
                if (tabLayout.getTabAt(tabLayout.getTabCount() - 1).getText().equals("Cross Check")) {
                    if (tabPosition == tabLayout.getTabCount() - 1) {
                        //cross_check block last one
                        if (queryList.size() > tabPosition) {
                            hintIdOne.setText(queryList.get(tabPosition - 1));
                            hintIdTwo.setText(queryList.get(tabPosition));
                            hintIdThree.setText("→←");
                        }

                    } else if (tabPosition == tabLayout.getTabCount() - 2) {
                        //salary block
                        if (queryList.size() > tabPosition + 1) {
                            hintIdOne.setText(queryList.get(tabPosition));
                            hintIdTwo.setText(queryList.get(tabPosition + 1));
                            hintIdThree.setText("→←");
                        }
                    } else if (tabPosition == 0) {
                        //first block
                        if (queryList.size() > tabPosition + 3) {
                            hintIdOne.setText(queryList.get(tabPosition + 1));
                            hintIdTwo.setText(queryList.get(tabPosition + 2));
                            hintIdThree.setText(queryList.get(tabPosition + 3));
                        }
                    } else {
                        if (queryList.size() > tabPosition + 1 + 1) {
                            hintIdOne.setText(queryList.get(tabPosition + 1 - 1));
                            hintIdTwo.setText(queryList.get(tabPosition + 1));
                            hintIdThree.setText(queryList.get(tabPosition + 1 + 1));
                        }
                    }
                } else {
                    if (tabLayout.getTabCount() > 3) {
                        if (tabPosition == tabLayout.getTabCount() - 1) {
                            if (queryList.size() > tabPosition) {
                                if (hintIdThree.getText().toString().startsWith("Ex.")) {
                                    tempThreeHint = hintIdThree.getText().toString();
                                }
                                hintIdOne.setText(queryList.get(tabPosition - 1));
                                hintIdTwo.setText(queryList.get(tabPosition));
                                //hintIdThree.setText("Temporary");
                                if (tempThreeHint != null) {
                                    hintIdThree.setText(tempThreeHint);
                                }
                            }
                        } else if (tabPosition == tabLayout.getTabCount() - 2) {
                            if (queryList.size() > tabPosition + 1) {
                                if (hintIdThree.getText().toString().startsWith("Ex.")) {
                                    tempThreeHint = hintIdThree.getText().toString();
                                }
                                hintIdOne.setText(queryList.get(tabPosition));
                                hintIdTwo.setText(queryList.get(tabPosition + 1));
                                //hintIdThree.setText("Temporary");
                                if (tempThreeHint != null) {
                                    hintIdThree.setText(tempThreeHint);
                                }
                            }
                        } else if (tabPosition == 0) {
                            if (queryList.size() > tabPosition + 3) {
                                hintIdOne.setText(queryList.get(tabPosition + 1));
                                hintIdTwo.setText(queryList.get(tabPosition + 2));
                                hintIdThree.setText(queryList.get(tabPosition + 3));
                            }
                        } else {
                            if (queryList.size() > tabPosition + 1 + 1) {
                                hintIdOne.setText(queryList.get(tabPosition + 1 - 1));
                                hintIdTwo.setText(queryList.get(tabPosition + 1));
                                hintIdThree.setText(queryList.get(tabPosition + 1 + 1));
                            }
                        }
                    }
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
                if (fab.getVisibility() == View.VISIBLE) {
                    animateFab(tabPosition);
                }

                //setting the hint layout text
                if (tabLayout.getTabAt(tabLayout.getTabCount() - 1).getText().equals("Cross Check")) {
                    if (tabPosition == tabLayout.getTabCount() - 1) {
                        //cross_check block last one
                        if (queryList.size() > tabPosition) {
                            hintIdOne.setText(queryList.get(tabPosition - 1));
                            hintIdTwo.setText(queryList.get(tabPosition));
                            hintIdThree.setText("→←");
                        }

                    } else if (tabPosition == tabLayout.getTabCount() - 2) {
                        //salary block
                        if (queryList.size() > tabPosition + 1) {
                            Toast.makeText(GrabingInfoActivity.this, "salary block", Toast.LENGTH_SHORT).show();
                            hintIdOne.setText(queryList.get(tabPosition));
                            hintIdTwo.setText(queryList.get(tabPosition + 1));
                            hintIdThree.setText("→←");
                        }
                    } else if (tabPosition == 0) {
                        //first block
                        if (queryList.size() > tabPosition + 3) {
                            hintIdOne.setText(queryList.get(tabPosition + 1));
                            hintIdTwo.setText(queryList.get(tabPosition + 2));
                            hintIdThree.setText(queryList.get(tabPosition + 3));
                        }
                    } else {
                        if (queryList.size() > tabPosition + 1 + 1) {
                            hintIdOne.setText(queryList.get(tabPosition + 1 - 1));
                            hintIdTwo.setText(queryList.get(tabPosition + 1));
                            hintIdThree.setText(queryList.get(tabPosition + 1 + 1));
                        }
                    }
                } else {
                    if (tabLayout.getTabCount() > 3) {
                        if (tabPosition == tabLayout.getTabCount() - 1) {
                            if (queryList.size() > tabPosition) {
                                if (hintIdThree.getText().toString().startsWith("Ex.")) {
                                    tempThreeHint = hintIdThree.getText().toString();
                                }
                                hintIdOne.setText(queryList.get(tabPosition - 1));
                                hintIdTwo.setText(queryList.get(tabPosition));
                                //hintIdThree.setText("Temporary");
                                if (tempThreeHint != null) {
                                    hintIdThree.setText(tempThreeHint);
                                }
                            }
                        } else if (tabPosition == tabLayout.getTabCount() - 2) {
                            if (queryList.size() > tabPosition + 1) {
                                if (hintIdThree.getText().toString().startsWith("Ex.")) {
                                    tempThreeHint = hintIdThree.getText().toString();
                                }
                                hintIdOne.setText(queryList.get(tabPosition));
                                hintIdTwo.setText(queryList.get(tabPosition + 1));
                                //hintIdThree.setText("Temporary");
                                if (tempThreeHint != null) {
                                    hintIdThree.setText(tempThreeHint);
                                }
                            }
                        } else if (tabPosition == 0) {
                            if (queryList.size() > tabPosition + 3) {
                                hintIdOne.setText(queryList.get(tabPosition + 1));
                                hintIdTwo.setText(queryList.get(tabPosition + 2));
                                hintIdThree.setText(queryList.get(tabPosition + 3));
                            }
                        } else {
                            if (queryList.size() > tabPosition + 1 + 1) {
                                hintIdOne.setText(queryList.get(tabPosition + 1 - 1));
                                hintIdTwo.setText(queryList.get(tabPosition + 1));
                                hintIdThree.setText(queryList.get(tabPosition + 1 + 1));
                            }
                        }
                    }
                }
            }
        });
        //init the view here
        Glide.with(this).load(searchDataStore.getAvatarString()).apply(new RequestOptions().override(100, 100).placeholder(R.drawable.alias_profile_icon)).into(firstContactImageView);
        Glide.with(context).asBitmap().load(searchDataStore.getAvatarString())
                .apply(new RequestOptions().override((int) (20 * (getResources().getDisplayMetrics().density)), (int) (20 * (getResources().getDisplayMetrics().density))).centerCrop().placeholder(R.drawable.alias_profile_icon))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                        final Bitmap roundedCornerBitmap = getRoundedCornerBitmapSquare(resource, (int) (10 * (getResources().getDisplayMetrics().density)), (int) (20 * (getResources().getDisplayMetrics().density)));
                        Drawable drawable = new BitmapDrawable(getResources(), roundedCornerBitmap);
                        if (drawable != null) {
                            forMeBtn.setCompoundDrawablesWithIntrinsicBounds(drawable, null, imgKeyBoardDown, null);
                        }
                    }
                });
        firstContactPerson.setText(searchDataStore.getUserName() + "-(Me)");
        firstContactPersonNum.setText(searchDataStore.getUserNumber());
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
                                if (!visible) {
                                    contractLayout.setVisibility(View.GONE);
                                }
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
                TransitionManager.beginDelayedTransition(tabHintLayout, set);
                TransitionManager.beginDelayedTransition(contractLayout, set);
                TransitionManager.beginDelayedTransition(viewMusk, set1);
                if (visible) {
                    tabHintLayout.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);
                    contractLayout.setVisibility(View.VISIBLE);
                    viewMusk.setVisibility(View.VISIBLE);
                    forMeBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_up_black_24dp, 0);
                    forMeBtn.setText(R.string.switch_learner);
                } else {
                    contractLayout.setVisibility(View.GONE);
                    viewMusk.setVisibility(View.INVISIBLE);
                    tabHintLayout.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);
                    if (!forMySelf) {
                        if (contactBitmap != null) {
                            Glide.with(context).asBitmap().load(contactBitmap)
                                    .apply(new RequestOptions().override((int) (20 * (getResources().getDisplayMetrics().density)), (int) (20 * (getResources().getDisplayMetrics().density))).centerCrop().placeholder(R.drawable.alias_profile_icon))
                                    .into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                                            final Bitmap roundedCornerBitmap = getRoundedCornerBitmapSquare(resource, (int) (10 * (getResources().getDisplayMetrics().density)), (int) (20 * (getResources().getDisplayMetrics().density)));
                                            Drawable drawable = new BitmapDrawable(getResources(), roundedCornerBitmap);
                                            if (drawable != null) {
                                                forMeBtn.setCompoundDrawablesWithIntrinsicBounds(drawable, null, imgKeyBoardDown, null);
                                            }
                                        }
                                    });
                        } else {
                            Glide.with(context).asBitmap().load(SearchDataStore.DEFAULTUSERAVATER)
                                    .apply(new RequestOptions().override((int) (20 * (getResources().getDisplayMetrics().density)), (int) (20 * (getResources().getDisplayMetrics().density))).centerCrop().placeholder(R.drawable.alias_profile_icon))
                                    .into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                                            final Bitmap roundedCornerBitmap = getRoundedCornerBitmapSquare(resource, (int) (10 * (getResources().getDisplayMetrics().density)), (int) (20 * (getResources().getDisplayMetrics().density)));
                                            Drawable drawable = new BitmapDrawable(getResources(), roundedCornerBitmap);
                                            if (drawable != null) {
                                                forMeBtn.setCompoundDrawablesWithIntrinsicBounds(drawable, null, imgKeyBoardDown, null);
                                            }
                                        }
                                    });
                        }
                        forMeBtn.setText("For " + secondContactPerson.getText());
                    } else {
                        if (searchDataStore.getAvatarString() == null || searchDataStore.getAvatarString().equals("")) {
                            String gender = searchDataStore.getGender();
                            if (gender == null || gender.equals("") || gender.equals("Male")) {
                                Glide.with(getApplicationContext()).asBitmap().load(SearchDataStore.DEFAULTMALEAVATER)
                                        .apply(new RequestOptions().override((int) (20 * (getResources().getDisplayMetrics().density)), (int) (20 * (getResources().getDisplayMetrics().density))).centerCrop().placeholder(R.drawable.alias_profile_icon))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                                                final Bitmap roundedCornerBitmap = getRoundedCornerBitmapSquare(resource, (int) (10 * (getResources().getDisplayMetrics().density)), (int) (20 * (getResources().getDisplayMetrics().density)));
                                                Drawable drawable = new BitmapDrawable(getResources(), roundedCornerBitmap);
                                                if (drawable != null) {
                                                    forMeBtn.setCompoundDrawablesWithIntrinsicBounds(drawable, null, imgKeyBoardDown, null);
                                                }
                                            }
                                        });
                            } else {
                                Glide.with(getApplicationContext()).asBitmap().load(SearchDataStore.DEFAULTFEMALEAVATER)
                                        .apply(new RequestOptions().override((int) (20 * (getResources().getDisplayMetrics().density)), (int) (20 * (getResources().getDisplayMetrics().density))).centerCrop().placeholder(R.drawable.alias_profile_icon))
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                                                final Bitmap roundedCornerBitmap = getRoundedCornerBitmapSquare(resource, (int) (10 * (getResources().getDisplayMetrics().density)), (int) (20 * (getResources().getDisplayMetrics().density)));
                                                Drawable drawable = new BitmapDrawable(getResources(), roundedCornerBitmap);
                                                if (drawable != null) {
                                                    forMeBtn.setCompoundDrawablesWithIntrinsicBounds(drawable, null, imgKeyBoardDown, null);
                                                }
                                            }
                                        });
                            }
                        } else {
                            Glide.with(getApplicationContext()).asBitmap().load(searchDataStore.getAvatarString())
                                    .apply(new RequestOptions().override((int) (20 * (getResources().getDisplayMetrics().density)), (int) (20 * (getResources().getDisplayMetrics().density))).centerCrop().placeholder(R.drawable.alias_profile_icon))
                                    .into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                                            final Bitmap roundedCornerBitmap = getRoundedCornerBitmapSquare(resource, (int) (10 * (getResources().getDisplayMetrics().density)), (int) (20 * (getResources().getDisplayMetrics().density)));
                                            Drawable drawable = new BitmapDrawable(getResources(), roundedCornerBitmap);
                                            if (drawable != null) {
                                                forMeBtn.setCompoundDrawablesWithIntrinsicBounds(drawable, null, imgKeyBoardDown, null);
                                            }
                                        }
                                    });
                        }
                        forMeBtn.setText(R.string.for_me);
                    }
                }
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
    public void viewMuskClicked() {
        forMeBtn.performClick();
    }

    @Override
    public void fabClicked(View view) {
        if (tabLayout.getSelectedTabPosition() == (tabLayout.getTabCount() - 1)) {
            String levelName = (String) tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText();
            if (endOfNest.contains(levelName)) {
                //flush("this should work : " + tabLayout.getSelectedTabPosition());
                AppCompatRadioButton rd = new AppCompatRadioButton(this);
                rd.setText("null");
                onRadioButtonClick(rd, tabLayout.getSelectedTabPosition(), levelName);
            } else if (levelName.equals("Salary")) {
                AppCompatRadioButton rd = new AppCompatRadioButton(this);
                rd.setText("null");
                onRadioButtonClick(rd, tabLayout.getSelectedTabPosition(), levelName);
            } else if (levelName.equals("Capacity")) {
                AppCompatRadioButton rd = new AppCompatRadioButton(this);
                rd.setText("null");
                onRadioButtonClick(rd, tabLayout.getSelectedTabPosition(), levelName);
            } else if (levelName.equals("Cross Check")) {
                switch (retrivedAction) {
                    case DumeUtils.TEACHER:
                    case DumeUtils.BOOTCAMP:
                        HashMap<String, Object> queryMap = new HashMap<>();
                        for (int i = 0; i < queryList.size(); i++) {
                            queryMap.put(queryListName.get(i), queryList.get(i));
                        }
                        showProgress();
                        fab.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                        fab.setEnabled(false);
                        String queryString = DumeUtils.generateQueryString(SearchDataStore.REGULAR_DUME, queryList, queryListName);
                        ArrayList<Skill> skillArrayList = TeacherDataStore.getInstance().getSkillArrayList();

                        for (Skill item : skillArrayList) {
                            if (item.getQuery_string().equals(queryString)) {
                                flush("Skill Already Exists");
                                hideProgress();
                                startActivity(new Intent(GrabingInfoActivity.this, SkillActivity.class));
                                return;
                            }
                        }

                        Skill skill = new Skill(true, queryMap.get("Gender").toString(), Float.parseFloat(queryMap.get("Salary").toString().replace("k", "")), new Date(), queryMap, queryString
                                , new GeoPoint(84.9, 180), 5, 10, "", 4.6f);
                        teacherModel.saveSkill(skill, new TeacherContract.Model.Listener<Void>() {
                            @Override
                            public void onSuccess(Void list) {
                                hideProgress();
                                startActivity(new Intent(GrabingInfoActivity.this, SkillActivity.class));
                            }

                            @Override
                            public void onError(String msg) {
                                flush(msg);
                                hideProgress();
                                fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent)));
                                fab.setEnabled(true);
                            }
                        });
                        break;
                    case DumeUtils.STUDENT:
                        showProgress();
                        fab.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                        fab.setEnabled(false);
                        searchDataStore.genSetRetJizz(queryList, queryListName);
                        if (forMySelf) {
                            searchDataStore.genSetRetForWhom(searchDataStore.getUserName(), searchDataStore.getUserNumber(), searchDataStore.getUserUid(), searchDataStore.getAvatarString(), forMySelf);
                        } else {
                            String name = secondContactPerson.getText().toString();
                            String phoneNum = secondContactPersonNum.getText().toString();
                            searchDataStore.genSetRetForWhom(name, phoneNum, searchDataStore.getUserUid(), getContactAvatarUri(), forMySelf);
                        }
                        gotoGrabingPackage();
                        break;
                }
            }
        } else {
            //other general block "just go to the next one"
            Objects.requireNonNull(tabLayout.getTabAt(tabLayout.getSelectedTabPosition() + 1)).select();
        }
    }

    @Override
    public String getContactAvatarUri() {
        if (contactAvatarString != null) {
            return contactAvatarString;
        } else {
            return SearchDataStore.BOYSTUDENT;
        }
        //no way to determine female or male student
    }

    @Override
    public void setContactAvatar(String uri) {
        contactAvatarString = uri;
    }

    private void gotoGrabingPackage() {
        fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent)));
        fab.setEnabled(true);
        hideProgress();
        startActivity(new Intent(this, GrabingPackageActivity.class));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_grabing_info, menu);
        return true;
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
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.setMyLocationEnabled(false);

        switch (Objects.requireNonNull(getIntent().getAction())) {
            case DumeUtils.STUDENT:
                addCustomMarkerFromURL(searchDataStore.getAvatarString(), searchDataStore.getAnchorPoint());
                moveCamera(searchDataStore.getAnchorPoint(), DEFAULT_ZOOM, "Device Location", mMap);
                break;
            case DumeUtils.TEACHER:
            case DumeUtils.BOOTCAMP:
                GeoPoint geoPointTeacher = (GeoPoint) TeacherDataStore.getInstance().getDocumentSnapshot().get("location");
                LatLng teacherLatLng = new LatLng(geoPointTeacher.getLatitude(), geoPointTeacher.getLongitude());
                addCustomMarkerFromURL(searchDataStore.getAvatarString(), teacherLatLng);
                moveCamera(teacherLatLng, DEFAULT_ZOOM, "Device Location", mMap);
                break;
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

    public void onGrabingInfoViewClicked(View view) {
        mPresenter.onGrabingInfoViewIntracted(view);
    }

    public void getAction() {
        switch (Objects.requireNonNull(retrivedAction)) {
            case DumeUtils.STUDENT:
                selected_category_position = getIntent().getIntExtra(DumeUtils.SELECTED_ID, 0);
                queryList.add(new LocalDb().getCategories().get(selected_category_position));
                queryListName.add("Category");
                break;
            case DumeUtils.TEACHER:
                selected_category_position = getIntent().getIntExtra(DumeUtils.SELECTED_ID, 0);
                forMeWrapper.setVisibility(View.GONE);
                toolbar.setTitle("Select Skill");
                queryList.add(new LocalDb().getCategories().get(selected_category_position));
                queryListName.add("Category");
                break;
            case DumeUtils.BOOTCAMP:
                selected_category_position = getIntent().getIntExtra(DumeUtils.SELECTED_ID, 0);
                forMeWrapper.setVisibility(View.GONE);
                toolbar.setTitle("Select Skill");
                queryList.add(new LocalDb().getCategories().get(selected_category_position));
                queryListName.add("Category");
                break;
            default:
                selected_category_position = getIntent().getIntExtra(DumeUtils.SELECTED_ID, 0);
                queryList.add(new LocalDb().getCategories().get(selected_category_position));
                queryListName.add("Category");
                break;
        }

    }

    @Override
    public void onRadioButtonClick(CompoundButton view, int fragmentId, String levelName) {
        if (!view.getText().equals("null")) {
            //for query list
            if (!levelName.equals("justForData")) {
                if (!levelName.equals("Gender")) {
                    if (queryList.size() > fragmentId + 1) {
                        int length = queryList.size();
                        for (int i = fragmentId + 1; i < length; i++) {
                            queryList.remove(queryList.size() - 1);
                        }
                    }
                }
            }
            if (queryList.size() <= fragmentId + 1) {
                queryList.add(view.getText().toString());
            } else {
                queryList.set(fragmentId + 1, view.getText().toString());
            }
            //setting the hint layout text
            if (fragmentId == 0) {
                hintIdOne.setText(queryList.get(fragmentId + 1));
            } else if (fragmentId == 1) {
                hintIdTwo.setText(queryList.get(fragmentId + 1));
            } else if (fragmentId == (tabLayout.getTabCount() - 1)) {
                hintIdThree.setText(queryList.get(fragmentId + 1));
            } else if (fragmentId == (tabLayout.getTabCount() - 2)) {
                hintIdTwo.setText(queryList.get(fragmentId + 1));
            }
        }

        if (!levelName.equals("justForData")) {
            //for query list name
            if (!levelName.equals("Gender")) {
                if (queryListName.size() > fragmentId + 1) {
                    int length = queryListName.size();
                    for (int i = fragmentId + 1; i < length; i++) {
                        queryListName.remove(queryListName.size() - 1);
                    }
                }
            }
            if (queryListName.size() <= fragmentId + 1) {
                queryListName.add(fragmentId + 1, levelName);
            } else {
                queryListName.set(fragmentId + 1, levelName);
            }
        }
        Log.e(TAG, queryList.toString());
        Log.e(TAG, queryListName.toString());
        db = new LocalDb();
        ArrayList<String> arr = new ArrayList<>(Arrays.asList("Salary", "Gender", "justForData", "Capacity"));
        if (arr.contains(levelName)) {
            fab.show();
            switch (levelName) {
                case "Gender":
                    if (!(fragmentId < tabLayout.getTabCount() - 1)) {
                        if (retrivedAction.equals(DumeUtils.BOOTCAMP)) {
                            flush("now bootcamp will work");
                            mSectionsPagerAdapter.newTab(db.capacity);
                            mViewPager.setCurrentItem(fragmentId + 1);
                        } else {
                            mSectionsPagerAdapter.newTab(db.payment);
                            mViewPager.setCurrentItem(fragmentId + 1);
                        }
                    } else mViewPager.setCurrentItem(fragmentId + 1);
                    return;
                case "Salary":
                    if (!(fragmentId < tabLayout.getTabCount() - 1)) {
                        mSectionsPagerAdapter.newTab(db.crossCheck);
                        mViewPager.setCurrentItem(fragmentId + 1);
                    } else mViewPager.setCurrentItem(fragmentId + 1);
                    return;
                case "Capacity":
                    if (!(fragmentId < tabLayout.getTabCount() - 1)) {
                        mSectionsPagerAdapter.newTab(db.payment);
                        mViewPager.setCurrentItem(fragmentId + 1);
                    } else mViewPager.setCurrentItem(fragmentId + 1);
                    return;
             /* case "Subject":
                case "Field":
                case "Flavour":
                case "Type":
                case "Course":
                case "Software":
                case "Language":*/
                case "justForData":
                    return;
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
        int whatToDO = tabLayout.getTabCount() - 1;
        if (whatToDO == 0) {
            hintIdOne.setVisibility(View.VISIBLE);
            hintIdTwo.setVisibility(View.GONE);
            hintIdThree.setVisibility(View.GONE);
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                LinearLayout tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tablayout_tab, null);
                TextView tab_label = (TextView) tab.findViewById(R.id.nav_label);
                ImageView tab_icon = (ImageView) tab.findViewById(R.id.nav_icon);
                tab_label.setTranslationY((int) (2 * (getResources().getDisplayMetrics().density)));
                //tab_icon.setTranslationY((int) (-1 * (getResources().getDisplayMetrics().density)));
                tab_label.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Cairo-Light.ttf"));
                tab_label.setText(Objects.requireNonNull(tabLayout.getTabAt(i)).getText());
                tab_icon.setImageResource(navIcons[giveIconOnTabName((String) tabLayout.getTabAt(i).getText())]);
                tab_label.setLayoutParams(textParamOne);
                Objects.requireNonNull(tabLayout.getTabAt(i)).setCustomView(tab);
            }
        } else if (whatToDO == 1) {
            hintIdOne.setVisibility(View.VISIBLE);
            hintIdTwo.setVisibility(View.VISIBLE);
            hintIdThree.setVisibility(View.GONE);
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                LinearLayout tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tablayout_tab, null);
                TextView tab_label = (TextView) tab.findViewById(R.id.nav_label);
                ImageView tab_icon = (ImageView) tab.findViewById(R.id.nav_icon);
                tab_label.setTranslationY((int) (2 * (getResources().getDisplayMetrics().density)));
                //tab_icon.setTranslationY((int) (-1 * (getResources().getDisplayMetrics().density)));
                tab_label.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Cairo-Light.ttf"));
                tab_label.setText(Objects.requireNonNull(tabLayout.getTabAt(i)).getText());
                tab_icon.setImageResource(navIcons[giveIconOnTabName((String) tabLayout.getTabAt(i).getText())]);
                tab_label.setLayoutParams(textParamTwo);
                Objects.requireNonNull(tabLayout.getTabAt(i)).setCustomView(tab);
            }
        } else {
            hintIdOne.setVisibility(View.VISIBLE);
            hintIdTwo.setVisibility(View.VISIBLE);
            hintIdThree.setVisibility(View.VISIBLE);
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                LinearLayout tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tablayout_tab, null);
                TextView tab_label = (TextView) tab.findViewById(R.id.nav_label);
                ImageView tab_icon = (ImageView) tab.findViewById(R.id.nav_icon);
                tab_label.setTranslationY((int) (2 * (getResources().getDisplayMetrics().density)));
                //tab_icon.setTranslationY((int) (-1 * (getResources().getDisplayMetrics().density)));
                tab_label.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Cairo-Light.ttf"));
                tab_label.setText(Objects.requireNonNull(tabLayout.getTabAt(i)).getText());
                tab_icon.setImageResource(navIcons[giveIconOnTabName((String) tabLayout.getTabAt(i).getText())]);
                tab_label.setLayoutParams(textParamThree);
                Objects.requireNonNull(tabLayout.getTabAt(i)).setCustomView(tab);
            }
        }
    }


    private int giveIconOnTabName(String TabName) {
        switch (TabName) {
            case "Level":
                return 0;
            case "Medium":
                return 1;
            case "Class":
                return 2;
            case "Subject":
                return 3;
            case "Field":
                return 4;
            case "Division":
                return 5;
            case "Language":
                return 6;
            case " Language ":
                return 7;
            case "Software":
                return 8;
            case "Music":
                return 9;
            case "Item":
                return 10;
            case "Flavour":
                return 11;
            case "Degree":
                return 12;
            case "Branch":
                return 5;
            case "Type":
                return 11;
            case "Gender":
                return 14;
            case "Salary":
                return 15;
            case "Cross Check":
                return 13;
            case "Capacity":
                return 16;
            default:
                return 5;
        }
    }

    @Override
    public void onTabDeleted() {

    }

    @Override
    public void selectFromContactClicked() {
        if (contactPermissionGranted) {
            startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
        } else {
            AccessContact();
        }
    }

    @Override
    public void firstContactClicked() {
        firstContactSelectImage.setVisibility(View.VISIBLE);
        secondContactSelectImage.setVisibility(View.GONE);
        forMySelf = true;
    }

    @Override
    public void secondContactClicked() {
        firstContactSelectImage.setVisibility(View.GONE);
        secondContactSelectImage.setVisibility(View.VISIBLE);
        forMySelf = false;
    }

    final private int REQUEST_MULTIPLE_PERMISSIONS = 124;

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        }
        return true;
    }

    private void AccessContact() {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS)) {
            permissionsNeeded.add("Read Contacts");
        }
        if (permissionsList.size() > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_MULTIPLE_PERMISSIONS);
            }
            return;
        } else {
            contactPermissionGranted = true;
            selectFromContactClicked();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_MULTIPLE_PERMISSIONS) {
            if (permissions[0].equals(Manifest.permission.READ_CONTACTS) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                contactPermissionGranted = true;
                selectFromContactClicked();
            } else if (permissions[0].equals(Manifest.permission.READ_CONTACTS) && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                contactPermissionGranted = false;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //testing the contact retrival
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Log.d(TAG, "Response: " + data.toString());
            uriContact = data.getData();
            contactName = retrieveContactName();
            retrieveContactNumber();
            contactBitmap = retrieveContactPhoto();
            String contactNum = secondContactPersonNum.getText().toString();
            if (contactNum != null && !contactNum.equals("") && !contactNum.equals("null")) {
                //TODO update view
                secondContactLayout.setVisibility(View.VISIBLE);
                secondContactClicked();
            } else {
                secondContactLayout.setVisibility(View.GONE);
                firstContactClicked();
            }
        }
    }

    private Bitmap retrieveContactPhoto() {
        photo = null;
        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
                secondContactImageView.setImageBitmap(photo);
                flush("image not null here");
                inputStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return photo;
    }

    private void retrieveContactNumber() {
        String contactNumber = null;
        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {
            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }
        cursorID.close();
        Log.d(TAG, "Contact ID: " + contactID);
        ArrayList<String> allNumbers = new ArrayList<>();

        for (int i = 0; i <= 20; i++) {
            contactNumber = null;
            // Using the contact ID now we will get contact phone number
            Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                            ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                            i,

                    new String[]{contactID},
                    null);

            //cursorPhone.moveToFirst();
            while (cursorPhone.moveToNext()) {
                contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                if (contactNumber != null) {
                    Log.d(TAG, "Contact Phone Number: " + contactNumber);
                    allNumbers.add(contactNumber);
                }
            }
            cursorPhone.close();
        }
        String[] simpleArray = new String[allNumbers.size()];
        allNumbers.toArray(simpleArray);
        if (allNumbers.size() > 1) {
            selectOneNum(simpleArray);
        } else {
            secondContactPersonNum.setText(simpleArray[0]);
        }
    }

    public void selectOneNum(String[] allNumber) {
        Bundle pRargs = new Bundle();
        String finalTitle = "Select the number for contact ";
        finalTitle = finalTitle + secondContactPerson.getText();
        pRargs.putString("title", finalTitle);
        pRargs.putStringArray("radioOptions", allNumber);
        RadioBtnDialogue selectOneNumDialogue = new RadioBtnDialogue();
        selectOneNumDialogue.setItemChoiceListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                secondContactPersonNum.setText(allNumber[i]);
            }
        });
        selectOneNumDialogue.setCancelListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                secondContactPersonNum.setText("null");
                secondContactLayout.setVisibility(View.GONE);
                firstContactClicked();
            }
        });
        selectOneNumDialogue.setSelectListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                secondContactLayout.setVisibility(View.VISIBLE);
                secondContactClicked();
            }
        });
        selectOneNumDialogue.setCancelOnOutPress(false);
        selectOneNumDialogue.setArguments(pRargs);
        selectOneNumDialogue.show(getSupportFragmentManager(), "selectOneNumDialogue");
        secondContactPersonNum.setText(allNumber[0]);
    }

    private String retrieveContactName() {
        String contactName = null;
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
        cursor.close();
        //Log.d(TAG, "Contact Name: " + contactName);
        secondContactPerson.setText(contactName);
        return contactName;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_help) {
            //add function here
        } else if (id == android.R.id.home) {
            if (viewMusk.getVisibility() == View.VISIBLE) {
                forMeBtn.performClick();
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

    //convert bitmap to uri
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}
