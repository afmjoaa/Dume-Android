package io.dume.dume.teacher.homepage;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.hanks.htextview.scale.ScaleTextView;
import com.tomergoldst.tooltips.ToolTipsManager;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import info.hoang8f.android.segmented.SegmentedGroup;
import io.dume.dume.Google;
import io.dume.dume.R;
import io.dume.dume.bootCamp.bootCampHomePage.BootCampHomePageActivity;
import io.dume.dume.common.aboutUs.AboutUsActivity;
import io.dume.dume.common.bkash_transection.BkashTransectionActivity;
import io.dume.dume.common.chatActivity.DemoModel;
import io.dume.dume.common.inboxActivity.InboxActivity;
import io.dume.dume.common.privacyPolicy.PrivacyPolicyActivity;
import io.dume.dume.customView.HorizontalLoadView;
import io.dume.dume.customView.HorizontalLoadViewTwo;
import io.dume.dume.model.DumeModel;
import io.dume.dume.student.freeCashBack.FreeCashBackActivity;
import io.dume.dume.student.heatMap.HeatMapActivity;
import io.dume.dume.student.homePage.HomePageActivity;
import io.dume.dume.student.homePage.adapter.HomePageRatingAdapter;
import io.dume.dume.student.homePage.adapter.HomePageRatingData;
import io.dume.dume.student.homePage.adapter.HomePageRecyclerAdapter;
import io.dume.dume.student.homePage.adapter.HomePageRecyclerData;
import io.dume.dume.student.pojo.CusStuAppComMapActivity;
import io.dume.dume.student.pojo.MyGpsLocationChangeListener;
import io.dume.dume.student.recordsPage.Record;
import io.dume.dume.student.recordsPage.RecordsPageActivity;
import io.dume.dume.student.studentHelp.StudentHelpActivity;
import io.dume.dume.student.studentPayment.StudentPaymentActivity;
import io.dume.dume.teacher.adapters.AcademicAdapter;
import io.dume.dume.teacher.adapters.MyTabAdapter;
import io.dume.dume.teacher.adapters.SkillAdapter;
import io.dume.dume.teacher.boot_camp_addvertise.BootCampAdd;
import io.dume.dume.teacher.homepage.fragments.AcademicFragment;
import io.dume.dume.teacher.homepage.fragments.InboxFragment;
import io.dume.dume.teacher.homepage.fragments.PayFragment;
import io.dume.dume.teacher.homepage.fragments.PerformanceFragment;
import io.dume.dume.teacher.homepage.fragments.SkillFragment;
import io.dume.dume.teacher.homepage.fragments.StatisticsFragment;
import io.dume.dume.teacher.mentor_settings.AccountSettings;
import io.dume.dume.teacher.mentor_settings.basicinfo.EditAccount;
import io.dume.dume.teacher.pojo.Skill;
import io.dume.dume.teacher.pojo.TabModel;
import io.dume.dume.teacher.skill.SkillActivity;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.NetworkUtil;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.widget.TabView;

import static io.dume.dume.util.DumeUtils.animateImage;
import static io.dume.dume.util.ImageHelper.getRoundedCornerBitmapSquare;

public class TeacherActivtiy extends CusStuAppComMapActivity implements TeacherContract.View,
        NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, MyGpsLocationChangeListener,
        RadioGroup.OnCheckedChangeListener {
    public TeacherContract.Presenter presenter;
    public TeacherDataStore teacherDataStore = null;
    private TextView textView;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private DrawerLayout drawerLayout;
    private Button signOutButton;
    private static final String TAG = "TeacherActivtiy";
    private NavigationView navigationView;
    ToolTipsManager mToolTipsManager;
    private Context context;
    private int mNotificationsCount = 0;
    private char mProfileChar = '!';
    private int mChatCount = 0;
    private int mRecPendingCount = 0, mRecAcceptedCount = 0, mRecCurrentCount = 0;
    private ActionBar supportActionBarMain;
    private ActionBar supportActionBarSecond;

    private Menu menu;
    private MenuItem home, records, payments, messages, notifications, heat_map, free_cashback, settings, forum, help, selectAccount, infoItem, studentProfile, mentorProfile, bootCampProfile;
    private Drawable less;
    private Drawable more;
    private Drawable leftDrawable;
    private Button switchAcountBtn;
    private MenuItem skills;
    private LinearLayout bottomSheet;
    private View headerView;
    private ListView listView;
    private BottomSheetBehavior bottomSheetBehavior;
    private boolean firstTime;
    private Toolbar secondaryToolbar;
    private AppBarLayout secondaryAppBarLayout;
    private CollapsingToolbarLayout secondaryCollapsableToolbar;
    private FrameLayout viewMusk;
    private AppBarLayout mainAppbar;
    private CoordinatorLayout coordinatorLayout;
    private int spacing;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    @BindView(R.id.tablayout)
    VerticalTabLayout tabLayout;
    @BindView(R.id.mViewPager)
    VerticalViewPager viewPager;
    @BindView(R.id.fragmentTitle)
    ScaleTextView fragmentTitle;
    private CoordinatorLayout mainInterface;
    private ImageView referMentorImageView;
    private ImageView enhanceSkillImageView;
    private ImageView freeCashBackImageView;
    private SegmentedGroup radioSegmentGroup;
    private RadioButton buttonActive;
    private RadioButton buttonInActive;
    private TextView userName;
    private RecyclerView hPageBSRecycler;
    private int optionMenu = R.menu.stu_homepage;
    private String[] feedbackStrings;
    private FloatingActionButton fab;
    private DocumentSnapshot documentSnapshot;
    private TextView userNameTextView;
    private TextView userAddressingTextView;
    private TextView userRatingTextView;
    private carbon.widget.ImageView userDP;
    private TextView doMoreTextView;
    private TextView doMoreDetailTextView;
    private MenuItem alProfile;
    private LayerDrawable alProfileIcon;
    private Snackbar enamSnackbar;
    private HorizontalLoadViewTwo loadViewOne;
    private NestedScrollView bottomSheetNSV;
    private BottomSheetDialog mCancelBottomSheetDialog;
    private View cancelsheetRootView;
    private static final int fromFlag = 1604;
    private carbon.widget.LinearLayout percentOffBlock;
    private ArrayList<TabModel> tabModelArrayList;
    private MyTabAdapter tabAdapter;
    private HomePageRecyclerAdapter hPageBSRcyclerAdapter;
    private TeacherModel model;
    private LinearLayout profileDataLayout;
    private LinearLayout BootCampAddLayout;
    private HorizontalLoadView loadView;
    public LinearLayout hackHeight;
    private TextView promotionTextView, promotionExpireDate;
    Integer discount = 0;
    private carbon.widget.LinearLayout headerTab;
    private TextView dumeInfo;
    private LinearLayout dumeInfoContainer;
    private Button learnMoreBtnOne;
    private Button startCouching;
    private Button startTakingCouching;
    private TextView referLearnMore;
    private Button referMentorBtn;
    private TextView how_invite_works;
    private Button freeCashBack;
    private Button startLearingBtn;
    private SharedPreferences sharedPreferences;
    private Dialog paymentDialog;
    private static String FACEBOOK_URL = "https://www.facebook.com/groups/1623868617935891/";
    private static String FACEBOOK_PAGE_ID = "1623868617935891";
    public static boolean ISSKILLDIALOGSHOWING = false;
    private Dialog dialog;

    @Override
    public void loadPromoData(HomePageRecyclerData promoData) {
        Log.w(TAG, "loadPromoData: ");
        hPageBSRcyclerAdapter.addPromoToList(promoData);
    }


    @Override
    public void loadHeadsUpPromo(HomePageRecyclerData promoData) {
        Log.e(TAG, "loadHeadsUpPromo: ");
        if (promoData.getMax_dicount_percentage() > discount) {
            discount = promoData.getMax_dicount_percentage();
            Date expirity = promoData.getExpirity();
            Date now = new Date();
            Log.e(TAG, "loadHeadsUpPromo: " + (now.getTime() > expirity.getTime()));
            if (now.getTime() > expirity.getTime()) {
                model.removeAppliedPromo(promoData, new TeacherContract.Model.Listener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean deleted) {

                    }

                    @Override
                    public void onError(String msg) {
                        Log.e(TAG, "onError: " + msg);
                    }
                });
            } else {
                long leftMillis = expirity.getTime() - now.getTime();
                int daysLeft = (int) (leftMillis / (1000 * 60 * 60 * 24));
                setHeadsUpPromo(discount.toString(), (daysLeft > 1 ? daysLeft + " days" : "less than a day"), promoData.getPackageName() == null ? "" : promoData.getPackageName());
            }
        }
    }


    public void setHeadsUpPromo(String discount, String dayLeft, String packageName) {
        percentOffBlock.setVisibility(View.VISIBLE);
        headerTab.setBackground(getResources().getDrawable(R.drawable.bg_white_bottom_round_6));
        promotionTextView.setText(discount + "% off on " + packageName);
        promotionExpireDate.setText(dayLeft);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setActivityContextMap(this, fromFlag);
        teacherDataStore = TeacherDataStore.getInstance();
        model = new TeacherModel(this);
        presenter = new TeacherPresenter(this,this, model);
        presenter.init();
        settingStatusBarTransparent();
        setDarkStatusBarIcon();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        getLocationPermission(mapFragment);

        if (hPageBSRcyclerAdapter == null) {
            hPageBSRcyclerAdapter = new HomePageRecyclerAdapter(this, new ArrayList<>());
            hPageBSRcyclerAdapter.setWindow(getWindow());
            hPageBSRecycler.setAdapter(hPageBSRcyclerAdapter);
            hPageBSRecycler.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    @Override
    public void updateBadge(String badgeNumber) {
        if (Integer.parseInt(badgeNumber) != 0) {
            tabLayout.setTabBadge(1, badgeNumber);
        }
    }

    @Override
    public void init() {
        context = this;
        ButterKnife.bind(this);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        navigationView = findViewById(R.id.navigationView);
        mToolTipsManager = new ToolTipsManager();
        menu = navigationView.getMenu();
        fab = findViewById(R.id.fab);
        home = menu.findItem(R.id.home_id);
        records = menu.findItem(R.id.records);
        payments = menu.findItem(R.id.payments);
        messages = menu.findItem(R.id.messages);
        notifications = menu.findItem(R.id.notifications);
        free_cashback = menu.findItem(R.id.free_cashback);
        heat_map = menu.findItem(R.id.heat_map);
        settings = menu.findItem(R.id.settings);
        forum = menu.findItem(R.id.forum);
        help = menu.findItem(R.id.help);
        infoItem = menu.findItem(R.id.information_item);
        selectAccount = menu.findItem(R.id.select_account);
        skills = menu.findItem(R.id.skills);
        studentProfile = menu.findItem(R.id.student);
        mentorProfile = menu.findItem(R.id.mentor);
        bootCampProfile = menu.findItem(R.id.boot_camp);
        less = this.getResources().getDrawable(R.drawable.less_icon);
        more = this.getResources().getDrawable(R.drawable.more_icon);
        leftDrawable = this.getResources().getDrawable(R.drawable.mentor_account_icon);
        switchAcountBtn = findViewById(R.id.switch_account_btn);
        switchAcountBtn.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, more, null);
        bottomSheet = findViewById(R.id.amBottomSheet);
        coordinatorLayout = findViewById(R.id.parent_coor_layout);
        listView = findViewById(R.id.messagesRV);
        secondaryToolbar = findViewById(R.id.Secondary_toolbar);
        secondaryAppBarLayout = findViewById(R.id.secondary_Appbar);
        secondaryCollapsableToolbar = findViewById(R.id.secondary_collapsing_toolbar);
        viewMusk = findViewById(R.id.view_musk);
        mainAppbar = findViewById(R.id.my_appbarLayout);
        mainInterface = findViewById(R.id.main_interface);
        percentOffBlock = findViewById(R.id.percent_off_block);
        referMentorImageView = findViewById(R.id.refer_mentor_imageView);
        enhanceSkillImageView = findViewById(R.id.enhance_skill_imageview);
        freeCashBackImageView = findViewById(R.id.free_cashback_imageView);
        radioSegmentGroup = findViewById(R.id.segmentGroup);

        buttonActive = findViewById(R.id.buttonActive);
        buttonInActive = findViewById(R.id.buttonInActive);
        userName = findViewById(R.id.user_name);
        hPageBSRecycler = findViewById(R.id.homePage_bottomSheet_recycler);
        feedbackStrings = getResources().getStringArray(R.array.review_hint_text_dependent);
        userNameTextView = findViewById(R.id.user_name);
        userAddressingTextView = findViewById(R.id.user_addressing);
        userRatingTextView = findViewById(R.id.user_rating);
        userDP = findViewById(R.id.user_dp);
        doMoreTextView = findViewById(R.id.do_more);
        doMoreDetailTextView = findViewById(R.id.make_money_mentoring);
        loadViewOne = findViewById(R.id.loadViewTwo);
        loadView = findViewById(R.id.loadView);
        bottomSheetNSV = findViewById(R.id.bottom_sheet_scroll_view);
        profileDataLayout = findViewById(R.id.profile_data);
        BootCampAddLayout = findViewById(R.id.mentor_add_layout);
        hackHeight = findViewById(R.id.hack_height);
        promotionTextView = findViewById(R.id.promotion_text);
        promotionExpireDate = findViewById(R.id.promotion_validity_text);
        headerTab = findViewById(R.id.header_fuck);
        dumeInfo = findViewById(R.id.dume_info);
        dumeInfoContainer = findViewById(R.id.dume_info_container);

        learnMoreBtnOne = findViewById(R.id.learn_more_btn_one);
        startCouching = findViewById(R.id.start_couching);
        startTakingCouching = findViewById(R.id.start_taking_couching);
        referLearnMore = findViewById(R.id.refer_learn_more_tv);
        referMentorBtn = findViewById(R.id.refer_mentor_btn);
        how_invite_works = findViewById(R.id.how_invite_works);
        freeCashBack = findViewById(R.id.free_cashback_Btn);
        startLearingBtn = findViewById(R.id.start_learing_btn);
        bottomSheetBtnCallback();
        dialog = new Dialog(context);
    }

    public void bottomSheetBtnCallback() {
        learnMoreBtnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StudentHelpActivity.class);
                intent.setAction("how_to_use");
                startActivity(intent);
            }
        });
        startCouching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StudentHelpActivity.class);
                intent.setAction("whats_new");
                startActivity(intent);
            }
        });
        startTakingCouching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StudentHelpActivity.class);
                intent.setAction("whats_new");
                startActivity(intent);
            }
        });
        referMentorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flush("Feature is under development...");
            }
        });
        referLearnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StudentHelpActivity.class);
                intent.setAction("faq");
                startActivity(intent);
            }
        });
        how_invite_works.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StudentHelpActivity.class);
                intent.setAction("faq");
                startActivity(intent);
            }
        });
        freeCashBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inviteAFriendCalled();
            }
        });
        startLearingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchProfileDialog(DumeUtils.STUDENT);
            }
        });

    }

    private void inviteAFriendCalled() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TITLE, "Dume");
            String strShareMessage = "Check out Dume, It's simple just share your skill and earn money.Get it for free from\n\n";
            strShareMessage = strShareMessage + "https://play.google.com/store/apps/details?id=" + getPackageName();
            i.putExtra(Intent.EXTRA_TEXT, strShareMessage);
            startActivity(Intent.createChooser(i, "Share via"));
        } catch (Exception e) {
            Log.e(TAG, "inviteAFriendCalled: " + e.toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Map<String, Object> documentSnapshot = TeacherDataStore.getInstance().getDocumentSnapshot();
        if (documentSnapshot != null) {
            Map<String, Object> payments = (Map<String, Object>) documentSnapshot.get("payments");
            if (payments != null) {
                String totalPaid = (String) payments.get("total_paid");
                String obligationAmount = (String) payments.get("obligation_amount");
                if (obligationAmount != null && Integer.parseInt(obligationAmount) >= 500) {
                    if (!isDialogShowing()) {
                        showPaymentDialogue();

                    }
                }
            }
        }


        sharedPreferences = context.getSharedPreferences(UNREAD_MESSAGE, MODE_PRIVATE);
        updateChatBadge(sharedPreferences.getInt("unread", 0));
        switch (viewPager.getCurrentItem()) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:

                SkillFragment skillFragment = SkillFragment.getInstance();
                ArrayList<Skill> skillArrayList = TeacherDataStore.getInstance().getSkillArrayList();
                if (skillArrayList != null) {


                    skillFragment.skillAdapter = new SkillAdapter(TeacherActivtiy.this, SkillAdapter.FRAGMENT, skillFragment.itemWidth, teacherDataStore.getSkillArrayList());
                    skillFragment.skillRV.setAdapter(skillFragment.skillAdapter);
                    if (skillArrayList.size() == 0) {
                        skillFragment.noDataBlock.setVisibility(View.VISIBLE);

                    } else {
                        skillFragment.noDataBlock.setVisibility(View.GONE);
                    }
                }
                skillFragment.changeAddSkillBtnColor();
                break;
            case 5:
                break;
        }
        //viewPager.setCurrentItem(3);
        presenter.loadProfile(new TeacherContract.Model.Listener<Void>() {
            @Override
            public void onSuccess(Void list) {

            }

            @Override
            public void onError(String msg) {
                flush(msg);
            }
        });
    }

    @Override
    public void configView() {
        /*if (Google.getInstance().getTotalStudent()>0&& Google.getInstance().getTotalMentor()>0) {
            dumeInfoContainer.setVisibility(View.VISIBLE);
            dumeInfo.setText(Google.getInstance().getTotalStudent() +" students & "+Google.getInstance().getTotalMentor() +" mentors on dume network");
        }*/
        dumeInfoContainer.setVisibility(View.GONE);
        mentorProfile.setVisible(false);
        fab.setAlpha(0.90f);
        enamSnackbar = Snackbar.make(coordinatorLayout, "Replace with your own action", Snackbar.LENGTH_LONG);
        // Toolbar :: Transparent
        mainAppbar.bringToFront();
        mainAppbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        //secondary appbar font fix
        secondaryCollapsableToolbar.setCollapsedTitleTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/Cairo-Light.ttf"));
        secondaryCollapsableToolbar.setExpandedTitleTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/Cairo-Light.ttf"));
        secondaryCollapsableToolbar.setTitle("Messages");
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_more_vert_white_24dp);
        secondaryToolbar.setOverflowIcon(drawable);
        //config the bottom sheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetCallbackConfig();
        //setting my snackbar callback
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                //network resumed make functionality available
            }

            @Override
            public void onShown(Snackbar snackbar) {
                //network unavailable make functionality unavailable
            }
        });
        initAdvance();
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.drawer_menu);
        navigationView.setNavigationItemSelectedListener(this);
        //listener for the active inactive radio btn
        radioSegmentGroup.setOnCheckedChangeListener(this);
        //initializing the bottomSheet dialogue
        mCancelBottomSheetDialog = new BottomSheetDialog(this);
        cancelsheetRootView = this.getLayoutInflater().inflate(R.layout.custom_bottom_sheet_dialogue_cancel, null);
        mCancelBottomSheetDialog.setContentView(cancelsheetRootView);
        //setting do more
        doMoreDetailTextView.setText("Start a couching center");
        //setting the onclick listener here
        profileDataLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START, true);
                gotoProfilePage();
            }
        });
        BootCampAddLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START, true);
                gotoBootCampAddvertise();
            }
        });
    }

    @Override
    public void showSnackBar(String messages, String actionName) {
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) enamSnackbar.getView();
        TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);
        LayoutInflater inflater = LayoutInflater.from(context);
        View snackView = inflater.inflate(R.layout.teachers_snakbar_layout, null);
        // layout.setBackgroundColor(R.color.red);
        TextView textViewStart = snackView.findViewById(R.id.custom_snackbar_text);
        textViewStart.setText(messages);
        TextView actionTV = snackView.findViewById(R.id.actionTV);
        actionTV.setText(actionName);
        actionTV.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), EditAccount.class));
        });
        layout.setPadding(0, 0, 0, 0);
        CoordinatorLayout.LayoutParams parentParams = (CoordinatorLayout.LayoutParams) layout.getLayoutParams();
        parentParams.height = (int) (36 * (getResources().getDisplayMetrics().density));
       /* parentParams.setAnchorId(R.id.Secondary_toolbar);
        parentParams.anchorGravity = Gravity.BOTTOM;*/
        layout.setLayoutParams(parentParams);
        layout.addView(snackView, 0);
        int status = NetworkUtil.getConnectivityStatusString(context);
        if (snackbar != null && !snackbar.isShown() && status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
            enamSnackbar.show();
        }
    }

    @Override
    public void referMentorImageViewClicked() {
        animateImage(referMentorImageView);
    }

    @Override
    public void freeCashBackImageViewClicked() {
        animateImage(freeCashBackImageView);
    }

    @Override
    public void enhanceVIewImageClicked() {
        animateImage(enhanceSkillImageView);
    }


    public void bottomSheetCallbackConfig() {
        ViewTreeObserver vto = bottomSheet.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                bottomSheet.getLayoutParams().height = (bottomSheet.getHeight() - secondaryAppBarLayout.getHeight() + (int) (0 * (getResources().getDisplayMetrics().density)));
                bottomSheet.requestLayout();
                bottomSheetBehavior.onLayoutChild(coordinatorLayout, bottomSheet, ViewCompat.LAYOUT_DIRECTION_LTR);
                ViewTreeObserver obs = bottomSheet.getViewTreeObserver();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeOnGlobalLayoutListener(this);
                } else {
                    obs.removeGlobalOnLayoutListener(this);
                }
            }

        });
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        firstTime = true;
        // set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    setDarkStatusBarIcon();
                    viewMusk.setVisibility(View.GONE);
                    secondaryAppBarLayout.setVisibility(View.GONE);
                    mainAppbar.setVisibility(View.VISIBLE);
                    bottomSheet.animate().scaleY(1).setDuration(0).start();
                    //hack
                    setSupportActionBar(toolbar);
                    supportActionBarMain = getSupportActionBar();
                    if (supportActionBarMain != null) {
                        supportActionBarMain.setDisplayHomeAsUpEnabled(true);
                        supportActionBarMain.setDisplayShowHomeEnabled(true);
                        optionMenu = R.menu.stu_homepage;
                        invalidateOptionsMenu();
                    }
                    bottomSheetNSV.post(new Runnable() {
                        @Override
                        public void run() {
                            bottomSheetNSV.fling(0);
                            bottomSheetNSV.smoothScrollTo(0, 0);
                        }
                    });
                    bottomSheetNSV.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bottomSheetNSV.fling(0);
                            bottomSheetNSV.scrollTo(0, 0);
                        }
                    }, 200L);
                } else if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    setLightStatusBarIcon();
                    viewMusk.setVisibility(View.VISIBLE);
                    secondaryAppBarLayout.setVisibility(View.VISIBLE);
                    mainAppbar.setVisibility(View.GONE);
                    //hack
                    setSupportActionBar(secondaryToolbar);
                    supportActionBarSecond = getSupportActionBar();
                    if (supportActionBarSecond != null) {
                        supportActionBarSecond.setDisplayHomeAsUpEnabled(true);
                        supportActionBarSecond.setDisplayShowHomeEnabled(true);
                        optionMenu = R.menu.menu_only_help;
                        invalidateOptionsMenu();
                    }
                } else if (BottomSheetBehavior.STATE_DRAGGING == newState) {
                    viewMusk.setVisibility(View.VISIBLE);
                    secondaryAppBarLayout.setVisibility(View.VISIBLE);
                    mainAppbar.setVisibility(View.GONE);
//                    secondaryAppBarLayout.setExpanded(false);

                } /*else if (BottomSheetBehavior.STATE_SETTLING == newState) {
                    mainAppbar.setVisibility(View.VISIBLE);
                }*/

            }


            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (slideOffset > 0.0f && slideOffset < 1.0f) {
                    fab.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
                    bottomSheet.animate().scaleX(1 + (slideOffset * 0.058f)).setDuration(0).start();
                    viewMusk.animate().alpha(2 * slideOffset).setDuration(0).start();
                    mainInterface.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
                    //percentOffBlock.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
                    secondaryAppBarLayout.animate().alpha(slideOffset).scaleX(slideOffset).scaleY(slideOffset).setDuration(0).start();
                }
            }
        });

    }

    @Override
    public void onCenterCurrentLocation() {
        Drawable d = fab.getDrawable();
        if (d instanceof Animatable) {
            ((Animatable) d).start();
        }
        Log.d(TAG, "onClick: clicked gps icon");
        getDeviceLocation(mMap);

    }

    @Override
    public void flush(String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if (v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }

    private void initAdvance() {
        drawerLayout.setScrimColor(getResources().getColor(R.color.black_overlay));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                setDarkStatusBarIcon();
                switchAcountBtn.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, more, null);
                mainMenu();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setLightStatusBarIcon();
            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        ButterKnife.bind(this);
        tabModelArrayList = new ArrayList<>();
        tabModelArrayList.add(new TabModel(R.drawable.performance, R.drawable.performance_selected, 0, "Performance"));
        tabModelArrayList.add(new TabModel(R.drawable.inbox, R.drawable.inbox_selected, 0, "Inbox"));
        tabModelArrayList.add(new TabModel(R.drawable.pay, R.drawable.pay_selceted, 0, "Pay"));
        tabModelArrayList.add(new TabModel(R.drawable.ic_statistics, R.drawable.ic_statistics_selected, 0, "Statistics"));
        tabModelArrayList.add(new TabModel(R.drawable.skills, R.drawable.skills_selected, 0, "Manage Skills"));
        tabModelArrayList.add(new TabModel(R.drawable.academics_icon, R.drawable.academics_icon_selected, 0, "Academic"));
        tabAdapter = new MyTabAdapter(tabModelArrayList);
        tabLayout.setTabAdapter(tabAdapter);

        tabLayout.addOnTabSelectedListener(new VerticalTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabView tab, int position) {
                viewPager.setCurrentItem(position);
                fragmentTitle.animateText(tabModelArrayList.get(position).getTabName());

            }

            @Override
            public void onTabReselected(TabView tab, int position) {

            }
        });
        viewPager.setAdapter(new pager(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                tabLayout.setTabSelected(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        fragmentTitle.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Cairo_Regular.ttf"));
        fragmentTitle.animateText(tabModelArrayList.get(tabLayout.getSelectedTabPosition()).getTabName());


    }

    public void toggle(android.view.View view) {
        presenter.onButtonClicked();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(optionMenu, menu);
        if (optionMenu == R.menu.stu_homepage) {
            alProfile = menu.findItem(R.id.al_display_pic);
            alProfileIcon = (LayerDrawable) alProfile.getIcon();
            DumeUtils.setBadgeChar(this, alProfileIcon, 0xfff56161, Color.BLACK, mProfileChar, 3.0f, 3.0f);

            MenuItem alNoti = menu.findItem(R.id.al_notifications);
            LayerDrawable alNotiIcon = (LayerDrawable) alNoti.getIcon();
            DumeUtils.setBadgeCount(this, alNotiIcon, R.id.ic_badge, 0xfface0ac, Color.BLACK, mNotificationsCount, 3.0f, 3.0f);

            MenuItem alChat = menu.findItem(R.id.al_messages);
            LayerDrawable alChatIcon = (LayerDrawable) alChat.getIcon();
            DumeUtils.setBadgeCount(this, alChatIcon, R.id.ic_badge, 0xfface0ac, Color.BLACK, mChatCount, 3.0f, 3.0f);

            MenuItem alRecords = menu.findItem(R.id.al_records);
            LayerDrawable alRecordsIcon = (LayerDrawable) alRecords.getIcon();
            if (mRecPendingCount != 0 && mRecAcceptedCount == 0 && mRecCurrentCount == 0) {
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeTwo, 0xfff56161, Color.BLACK, mRecPendingCount, 3.0f, 3.0f);
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeOne, 0xfff4f094, Color.BLACK, mRecAcceptedCount, 8.0f, -3.4f);
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badge, 0xfface0ac, Color.BLACK, mRecCurrentCount, 12.0f, 3.0f);
            } else if (mRecPendingCount == 0 && mRecAcceptedCount != 0 && mRecCurrentCount == 0) {
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeTwo, 0xfff56161, Color.BLACK, mRecPendingCount, 8.0f, -3.4f);
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeOne, 0xfff4f094, Color.BLACK, mRecAcceptedCount, 3.0f, 3.0f);
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badge, 0xfface0ac, Color.BLACK, mRecCurrentCount, 12.0f, 3.0f);
            } else if (mRecPendingCount == 0 && mRecAcceptedCount == 0 && mRecCurrentCount != 0) {
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeTwo, 0xfff56161, Color.BLACK, mRecPendingCount, 12.0f, 3.0f);
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeOne, 0xfff4f094, Color.BLACK, mRecAcceptedCount, 8.0f, -3.4f);
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badge, 0xfface0ac, Color.BLACK, mRecCurrentCount, 3.0f, 3.0f);
            } else if (mRecPendingCount != 0 && mRecAcceptedCount != 0 && mRecCurrentCount == 0) {
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeTwo, 0xfff56161, Color.BLACK, mRecPendingCount, 3.0f, 3.0f);
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeOne, 0xfff4f094, Color.BLACK, mRecAcceptedCount, 8.0f, -3.4f);
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badge, 0xfface0ac, Color.BLACK, mRecCurrentCount, 12.0f, 3.0f);
            } else if (mRecPendingCount != 0 && mRecAcceptedCount == 0 && mRecCurrentCount != 0) {
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeTwo, 0xfff56161, Color.BLACK, mRecPendingCount, 3.0f, 3.0f);
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeOne, 0xfff4f094, Color.BLACK, mRecAcceptedCount, 12.0f, 3.0f);
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badge, 0xfface0ac, Color.BLACK, mRecCurrentCount, 8.0f, -3.4f);
            } else if (mRecPendingCount == 0 && mRecAcceptedCount != 0 && mRecCurrentCount != 0) {
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeTwo, 0xfff56161, Color.BLACK, mRecPendingCount, 12.0f, 3.0f);
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeOne, 0xfff4f094, Color.BLACK, mRecAcceptedCount, 3.0f, 3.0f);
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badge, 0xfface0ac, Color.BLACK, mRecCurrentCount, 8.0f, -3.4f);
            } else {
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeTwo, 0xfff56161, Color.BLACK, mRecPendingCount, 3.0f, 3.0f);
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeOne, 0xfff4f094, Color.BLACK, mRecAcceptedCount, 8.0f, -3.4f);
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badge, 0xfface0ac, Color.BLACK, mRecCurrentCount, 12.0f, 3.0f);
            }

            if (teacherDataStore.gettAvatarString() != null) {
                setAvatarForMenu(teacherDataStore.gettAvatarString());
            } else {
                viewMusk.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (teacherDataStore.gettAvatarString() != null) {
                            setAvatarForMenu(teacherDataStore.gettAvatarString());
                        }
                    }
                }, 1000L);
            }
        } else if (optionMenu == R.menu.menu_only_help) {

        }
        return super.onCreateOptionsMenu(menu);
    }

    //method to get the right URL to use in the intent
    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.al_display_pic:
                startActivity(new Intent(getApplicationContext(), EditAccount.class));
                break;
            case R.id.al_records:
                startActivity(new Intent(this, RecordsPageActivity.class).setAction(DumeUtils.TEACHER));
                break;
            case R.id.al_messages:
                startActivity(new Intent(this, InboxActivity.class));
                break;
            case R.id.al_notifications:
                Intent notificationTabIntent = new Intent(this, InboxActivity.class);
                notificationTabIntent.putExtra("notiTab", 1);
                startActivity(notificationTabIntent);
                break;
            case android.R.id.home:
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START, true);
                    //super.onBackPressed();
                }
                break;
            case R.id.action_help:
                startActivity(new Intent(this, StudentHelpActivity.class));
                break;
        }
        Drawable drawableGeneral = item.getIcon();
        if (drawableGeneral instanceof Animatable) {
            ((Animatable) drawableGeneral).start();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(this, AccountSettings.class));
                break;
            case R.id.skills:
                startActivity(new Intent(this, SkillActivity.class));
                break;
            case R.id.about_us:
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
            case R.id.help:
                startActivity(new Intent(this, StudentHelpActivity.class));
                break;
            case R.id.payments:
                startActivity(new Intent(this, StudentPaymentActivity.class).setAction(DumeUtils.TEACHER));
                break;
            case R.id.forum:
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(context);
                facebookIntent.setData(Uri.parse(facebookUrl));
                context.startActivity(facebookIntent);
                //Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.messages:
                startActivity(new Intent(this, InboxActivity.class));
                break;
            case R.id.notifications:
                Intent notificationTabIntent = new Intent(this, InboxActivity.class);
                notificationTabIntent.putExtra("notiTab", 1);
                startActivity(notificationTabIntent);
                break;
            case R.id.free_cashback:
                startActivity(new Intent(this, FreeCashBackActivity.class));
                break;
            case R.id.privacy_policy:
                startActivity(new Intent(this, PrivacyPolicyActivity.class));
                break;
            case R.id.records:
                startActivity(new Intent(this, RecordsPageActivity.class).setAction(DumeUtils.TEACHER));
                break;
            case R.id.heat_map:
                startActivity(new Intent(this, HeatMapActivity.class));
                break;
            case R.id.student:
                switchProfileDialog(DumeUtils.STUDENT);
                break;
            case R.id.mentor:
                break;
            case R.id.boot_camp:
                flush("Boot Camp Service is coming soon...");
                //switchProfileDialog(DumeUtils.BOOTCAMP);
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START, true);
        return true;
    }

    public void onHomePageViewClicked(View view) {
        presenter.onViewInteracted(view);
    }

    public void onNavigationHeaderClick(View view) {
        presenter.onViewInteracted(view);
    }

    public void subMenu() {
        home.setVisible(false);
        records.setVisible(false);
        payments.setVisible(false);
        messages.setVisible(false);
        notifications.setVisible(false);
        heat_map.setVisible(false);
        free_cashback.setVisible(false);
        forum.setVisible(false);
        help.setVisible(false);
        settings.setVisible(false);
        infoItem.setVisible(false);
        selectAccount.setVisible(true);
        skills.setVisible(false);

    }

    public void mainMenu() {
        home.setVisible(true);
        records.setVisible(true);
        payments.setVisible(true);
        messages.setVisible(true);
        notifications.setVisible(true);
        heat_map.setVisible(true);
        free_cashback.setVisible(true);
        forum.setVisible(true);
        help.setVisible(true);
        settings.setVisible(true);
        skills.setVisible(true);
        infoItem.setVisible(true);
        selectAccount.setVisible(false);
    }

    public void onSwitchAccount() {
        if (home.isVisible()) {
            switchAcountBtn.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, less, null);
            subMenu();
        } else {
            switchAcountBtn.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, more, null);
            mainMenu();
        }

        if (leftDrawable instanceof Animatable) {
            ((Animatable) leftDrawable).start();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        setDarkStatusBarIcon();
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_default_no_landmarks);
        googleMap.setMapStyle(style);
        mMap = googleMap;
        onMapReadyListener(mMap);
        onMapReadyGeneralConfig();
        mMap.setPadding((int) (20 * (getResources().getDisplayMetrics().density)), 0, 0, (int) (72 * (getResources().getDisplayMetrics().density)));
        mMap.setMyLocationEnabled(false);
        getDeviceLocation(mMap);
    }

    @Override
    public void onMyGpsLocationChanged(Location location) {
    }

    /*
      Updates the count of notifications in the ActionBar starts here.
    */
    public void updateProfileBadge(char character) {
        mProfileChar = character;
        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        invalidateOptionsMenu();
    }

    public void updateNotificationsBadge(int count) {
        mNotificationsCount = count;
        invalidateOptionsMenu();
    }

    public void updateChatBadge(int count) {
        mChatCount = count;
        invalidateOptionsMenu();
    }

    public void updateRecordsBadge(int penCount, int acptCount, int curCount) {
        mRecPendingCount = penCount;
        mRecAcceptedCount = acptCount;
        mRecCurrentCount = curCount;
        invalidateOptionsMenu();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        String o = documentSnapshot.getString("last_name");
        String o1 = documentSnapshot.getString("first_name");
        switch (checkedId) {
            case R.id.buttonActive:
                // Toast.makeText(this, "You are active on Dume network now", Toast.LENGTH_SHORT).show();
                userNameTextView.setText(String.format("%s %s", o1, o));
                userNameTextView.setText(o1 + " " + o + "- Active");
                buttonActive.setCompoundDrawablesWithIntrinsicBounds(R.drawable.state_active_active, 0, 0, 0);
                buttonInActive.setCompoundDrawablesWithIntrinsicBounds(R.drawable.state_inactive_inactive, 0, 0, 0);
                switchStatus(true);

                break;
            case R.id.buttonInActive:
                //Toast.makeText(this, "You are inactive on Dume network now", Toast.LENGTH_SHORT).show();
                userName.setText(o1 + " " + o + "- Inactive");
                buttonActive.setCompoundDrawablesWithIntrinsicBounds(R.drawable.state_active_inactive, 0, 0, 0);
                buttonInActive.setCompoundDrawablesWithIntrinsicBounds(R.drawable.state_inactive_active, 0, 0, 0);
                boolean accountActive = (boolean) teacherDataStore.getDocumentSnapshot().get("account_active");
                if (accountActive) {
                    switchStatus(false);
                } else {
                    Toast toast = Toast.makeText(context, "Your account status is inactive. While you are inactive your skills are excluded from Dume queries...", Toast.LENGTH_LONG);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    if (v != null) v.setGravity(Gravity.CENTER);
                    toast.show();
                }
                break;
            default:
                // Nothing to do
                break;
        }
    }

    /*
      Updates the count of notifications in the ActionBar finishes here.
    */

    class pager extends FragmentPagerAdapter {

        public pager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = null;
            if (i == 0) {
                fragment = PerformanceFragment.getInstance();
            } else if (i == 1) {
                fragment = InboxFragment.getInstance();
            } else if (i == 2) {
                fragment = PayFragment.getInstance();
            } else if (i == 3) {
                fragment = StatisticsFragment.getInstance();
            } else if (i == 4) {
                fragment = SkillFragment.getInstance();
            } else if (i == 5) {
                fragment = AcademicFragment.getInstance();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 6;
        }
    }

    @Override
    public boolean isDialogShowing() {
        if (paymentDialog != null) {
            return paymentDialog.isShowing();
        } else return false;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void showPaymentDialogue() {
        paymentDialog = new Dialog(context);
        paymentDialog.setContentView(R.layout.payment_dialogue);
        paymentDialog.setCanceledOnTouchOutside(false);
        TextView primaryText = paymentDialog.findViewById(R.id.rating_primary_text);
        TextView subText = paymentDialog.findViewById(R.id.sub_text);
        Button bkashTransection = paymentDialog.findViewById(R.id.bkashTransection);
        primaryText.setText("Dear " + TeacherDataStore.getInstance().gettUserName() + ", it's time to pay...");
        subText.setText("You have crossed your threshold(500 ৳) obligation. Please pay your due obligation to continue uninterrupted dume service... ");
        TextView obligationAmountTV = paymentDialog.findViewById(R.id.afterDiscount);
        TextView totalPaidTV = paymentDialog.findViewById(R.id.afterDiscount_one);

        Map<String, Object> payments = (Map<String, Object>) TeacherDataStore.getInstance().getDocumentSnapshot().get("payments");
        if (payments != null) {
            String totalPaid = (String) payments.get("total_paid");
            String obligationAmount = (String) payments.get("obligation_amount");
            if (obligationAmount != null && totalPaid != null) {
                obligationAmountTV.setText(Integer.parseInt(obligationAmount) + " ৳");
                totalPaidTV.setText(totalPaid + " ৳");
                if (Integer.parseInt(obligationAmount) >= 1000) {
                    if (!Google.getInstance().isObligation()) {
                        model.toggleObligation(true, new TeacherContract.Model.Listener<Void>() {
                            @Override
                            public void onSuccess(Void list) {
                                //handled already
                            }

                            @Override
                            public void onError(String msg) {
                                //handled already
                            }
                        });
                    }
                    paymentDialog.setCancelable(false);
                    paymentDialog.setCanceledOnTouchOutside(false);
                    if (!buttonActive.isChecked()) {
                        buttonInActive.setChecked(true);
                    }
                } else {
                    paymentDialog.setCancelable(true);
                    paymentDialog.setCanceledOnTouchOutside(true);

                }
            }

        }


        bkashTransection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), BkashTransectionActivity.class));
            }
        });
        paymentDialog.show();


    }

    //testing the customDialogue
    @Override
    public void testingCustomDialogue(HomePageRatingData myData, Record record) {
        // custom dialog
        String keyToChange = "t_rate_status";
        //dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_rating_dialogue);
        dialog.setCanceledOnTouchOutside(false);
        DocumentSnapshot snapshot = record.getRecordSnap();
        //all find view here
        MaterialRatingBar mDecimalRatingBars = dialog.findViewById(R.id.rated_mentor_rating_bar);
        RecyclerView itemRatingRecycleView = dialog.findViewById(R.id.rating_item_recycler);
        carbon.widget.ImageView ratedMentorDP = dialog.findViewById(R.id.rated_mentor_dp);
        TextView ratingPrimaryText = dialog.findViewById(R.id.rating_primary_text);
        TextView ratingSecondaryText = dialog.findViewById(R.id.rating_secondary_text);
        TextInputLayout feedbackTextViewLayout = dialog.findViewById(R.id.input_layout_firstname);
        AutoCompleteTextView feedbackTextView = dialog.findViewById(R.id.feedback_textview);
        Button dismissBtn = (Button) dialog.findViewById(R.id.skip_btn);
        Button dismissBtnOne = (Button) dialog.findViewById(R.id.skip_btn_two);
        Button nextSubmitBtn = dialog.findViewById(R.id.next_btn);
        RelativeLayout dialogHostingLayout = dialog.findViewById(R.id.dialog_hosting_layout);
        Button SubmitBtn = dialog.findViewById(R.id.submit_btn);
        RelativeLayout firstLayout = dialog.findViewById(R.id.first_layout);
        RelativeLayout secondLayout = dialog.findViewById(R.id.second_layout);

        ratingPrimaryText.setText("How was your experience with " + myData.getName());
        Glide.with(getApplicationContext()).load(myData.getAvatar()).into(ratedMentorDP);

        //testing the recycle view here
        HomePageRatingAdapter itemRatingRecycleAdapter = new HomePageRatingAdapter(this, myData);
        itemRatingRecycleView.setAdapter(itemRatingRecycleAdapter);
        itemRatingRecycleView.setLayoutManager(new LinearLayoutManager(this));
        feedbackTextView.setVisibility(View.GONE);
        feedbackTextViewLayout.setVisibility(View.GONE);

        mDecimalRatingBars.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                nextSubmitBtn.performClick();
            }
        });

        dismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.changeRecordStatus(snapshot.getId(), keyToChange, Record.BOTTOM_SHEET);
                dialog.dismiss();
            }
        });
        dismissBtnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.changeRecordStatus(snapshot.getId(), keyToChange, Record.BOTTOM_SHEET);
                dialog.dismiss();
            }
        });

        nextSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionSet set = new TransitionSet()
                        .addTransition(new Fade())
                        .addTransition(new Slide(Gravity.START))
                        .setInterpolator(new FastOutLinearInInterpolator());
                TransitionManager.beginDelayedTransition(dialogHostingLayout, set);
                if (nextSubmitBtn.getText().equals("Next") && mDecimalRatingBars.getProgress() != 0) {
                    firstLayout.setVisibility(View.GONE);
                    secondLayout.setVisibility(View.VISIBLE);

                } else {
                    flush("please rate your experience");
                }
            }
        });


        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemRatingRecycleAdapter.getInputRating() == null) {
                    flush("Make sure you hit the like or dislike thumb");
                } else {
                    SubmitBtn.setEnabled(false);
                    showProgress();
                    model.submitRating(snapshot.getId(), snapshot.getString("skill_uid"), new DemoModel(context).opponentUid((List<String>) snapshot.get("participants")),
                            Google.getInstance().getAccountMajor(), itemRatingRecycleAdapter.getInputRating(), mDecimalRatingBars.getRating(), feedbackTextView.getText().toString(), new TeacherContract.Model.Listener<Void>() {
                                @Override
                                public void onSuccess(Void list) {
                                    hideProgress();
                                    SubmitBtn.setEnabled(true);
                                    flush("Thanks for your review...");
                                }

                                @Override
                                public void onError(String msg) {
                                    flush(msg);
                                    hideProgress();
                                    SubmitBtn.setEnabled(true);
                                }
                            });
                    dialog.dismiss();
                }
            }
        });
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void setDocumentSnapshot(DocumentSnapshot documentSnapshot) {
        this.documentSnapshot = documentSnapshot;
        teacherDataStore.settUserName(getUserName());
        teacherDataStore.settUserNumber(documentSnapshot.getString("phone_number"));
        teacherDataStore.settUserMail(documentSnapshot.getString("email"));
        teacherDataStore.settUserUid(documentSnapshot.getId());
        teacherDataStore.settAvatarString(getAvatarString());
    }

    @Override
    public void switchStatus(boolean active) {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
        radioSegmentGroup.setEnabled(false);
        showProgress();
        new DumeModel(this).switchAccountStatus(active, new TeacherContract.Model.Listener<Void>() {
            @Override
            public void onSuccess(Void list) {
                radioSegmentGroup.setEnabled(true);
                hideProgress();
                hideProgressTwo();
                String foo = active ? "Active" : "Inactive";
                flush("Account Status Changed To : " + foo);
            }

            @Override
            public void onError(String msg) {
                radioSegmentGroup.setEnabled(true);
                hideProgress();
                hideProgressTwo();
                flush(msg);
            }
        });


    }

    @Override
    public String getAvatarString() {
        return documentSnapshot.getString("avatar");
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getSelfRating() {
        return (Map<String, Object>) documentSnapshot.get("self_rating");
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getUnreadRecords() {
        return (Map<String, Object>) documentSnapshot.get("unread_records");
    }

    @Override
    public String unreadMsg() {
        return documentSnapshot.getString("unread_msg");
    }

    @Override
    public String unreadNoti() {
        return documentSnapshot.getString("unread_noti");
    }

    @Override
    public String getProfileComPercent() {
        return documentSnapshot.getString("pro_com_%");
    }

    @SuppressWarnings("unchecked")
    @Override
    public ArrayList<String> getAppliedPromo() {
        return (ArrayList<String>) documentSnapshot.get("applied_promo");
    }

    @SuppressWarnings("unchecked")
    @Override
    public ArrayList<String> getAvailablePromo() {
        return (ArrayList<String>) documentSnapshot.get("applied_promo");
    }

    @Override
    public String generateMsgName(String first, String last) {
        return "@" + first + last;
    }

    @Override
    public String getUserName() {
        return userNameTextView.getText().toString();
    }

    @Override
    public void setUserName(String first, String last) {
        userNameTextView.setText(String.format("%s %s", first, last));
    }

    @Override
    public void setAvatar(String avatarString) {
        Glide.with(getApplicationContext()).load(avatarString).apply(new RequestOptions().override(100, 100).placeholder(R.drawable.demo_alias_dp)).into(userDP);
    }

    @Override
    public void setAvatarForMenu(String avatar) {
        Glide.with(getApplicationContext()).asBitmap().load(avatar)
                .apply(new RequestOptions().override((int) (20 * (getResources().getDisplayMetrics().density)), (int) (20 * (getResources().getDisplayMetrics().density))).centerCrop().placeholder(R.drawable.alias_profile_icon))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                        final Bitmap roundedCornerBitmap = getRoundedCornerBitmapSquare(resource, (int) (10 * (getResources().getDisplayMetrics().density)), (int) (20 * (getResources().getDisplayMetrics().density)));
                        Drawable drawable = new BitmapDrawable(getResources(), roundedCornerBitmap);
                        if (drawable != null) {
                            alProfileIcon.setDrawableByLayerId(R.id.ic_al_profile_pic, drawable);
                            alProfile.setIcon(alProfileIcon);
                        }
                    }
                });
    }

    @Override
    public void setRating(Map<String, Object> selfRating) {
        userRatingTextView.setText(selfRating.get("star_rating") + "  ★");
    }

    @Override
    public void setMsgName(String msgName) {
        userAddressingTextView.setText(msgName);
        referMentorBtn.setText(msgName.toLowerCase());
        freeCashBack.setText(msgName.toLowerCase());
    }

    //TODO
    @Override
    public void setAvailablePromo(ArrayList<String> availablePromo) {

    }

    //TODO
    @Override
    public void setAppliedPromo(ArrayList<String> appliedPromo) {

    }


    @Override
    public void setProfileComPercent(String num) {
        if (Integer.parseInt(num) < 100) {
            updateProfileBadge('!');
        } else {
            updateProfileBadge('%');
        }
    }

    public static String UNREAD_MESSAGE = "unread_message";

    @Override
    public void setUnreadMsg(String unreadMsg) {
        updateChatBadge(Integer.parseInt(unreadMsg));
        sharedPreferences = context.getSharedPreferences(UNREAD_MESSAGE, MODE_PRIVATE);
        updateChatBadge(sharedPreferences.getInt("unread", 0));
    }

    @Override
    public void setUnreadNoti(String unreadNoti) {
        updateNotificationsBadge(Integer.parseInt(unreadNoti));
    }

    @Override
    public void setUnreadRecords(Map<String, Object> unreadRecords) {
        updateRecordsBadge(Integer.parseInt((String) unreadRecords.get("pending_count")),
                Integer.parseInt((String) unreadRecords.get("accepted_count")),
                Integer.parseInt((String) unreadRecords.get("current_count")));
    }

    @Override
    public void showPercentSnackBar(String completePercent) {
        Snackbar mySnackbar = Snackbar.make(coordinatorLayout, "Replace with your own action", Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) mySnackbar.getView();
        TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);
        LayoutInflater inflater = LayoutInflater.from(context);
        View snackView = inflater.inflate(R.layout.custom_snackbar_layout_one, null);

        TextView textViewStart = snackView.findViewById(R.id.custom_snackbar_text);
        textViewStart.setText("Profile only " + completePercent + "% complete");


        layout.setPadding(0, 0, 0, 0);
        if (Integer.parseInt(completePercent) < 90) {
            layout.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_yellow));
            textViewStart.setTextColor(Color.BLACK);
        } else {
            layout.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_green));
            textViewStart.setTextColor(Color.WHITE);
        }
        CoordinatorLayout.LayoutParams parentParams = (CoordinatorLayout.LayoutParams) layout.getLayoutParams();
        parentParams.height = (int) (36 * (getResources().getDisplayMetrics().density));

        layout.setLayoutParams(parentParams);
        layout.addView(snackView, 0);
        int status = NetworkUtil.getConnectivityStatusString(context);
        if (snackbar != null && !snackbar.isShown() && status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED && enamSnackbar != null && !enamSnackbar.isShown()) {
            mySnackbar.show();
        }
    }

    @Override
    public void gotoBootCampAddvertise() {
        startActivity(new Intent(this, BootCampAdd.class));

    }

    @Override
    public void gotoStudentHomePage() {
        startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
        finish();
    }

    @Override
    public void gotoBootCamPHomePage() {
        startActivity(new Intent(getApplicationContext(), BootCampHomePageActivity.class));
        finish();
    }

    @Override
    public void gotoProfilePage() {
        startActivity(new Intent(this, EditAccount.class));
    }

    @Override
    public void showProgress() {
        if (loadViewOne.getVisibility() == View.INVISIBLE || loadViewOne.getVisibility() == View.GONE) {
            loadViewOne.setVisibility(View.VISIBLE);
        }
        if (!loadViewOne.isRunningAnimation()) {
            loadViewOne.startLoading();
        }
    }

    @Override
    public void hideProgress() {
        if (loadViewOne.isRunningAnimation()) {
            loadViewOne.stopLoading();
        }
        if (loadViewOne.getVisibility() == View.VISIBLE) {
            loadViewOne.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void switchProfileDialog(String identify) {
        TextView mainText = mCancelBottomSheetDialog.findViewById(R.id.main_text);
        TextView subText = mCancelBottomSheetDialog.findViewById(R.id.sub_text);
        Button cancelYesBtn = mCancelBottomSheetDialog.findViewById(R.id.cancel_yes_btn);
        Button cancelNoBtn = mCancelBottomSheetDialog.findViewById(R.id.cancel_no_btn);
        if (mainText != null && subText != null && cancelYesBtn != null && cancelNoBtn != null) {
            mainText.setText("Switch Profile ?");
            cancelYesBtn.setText("Yes, Switch");
            cancelNoBtn.setText("No");
            if (identify.equals(DumeUtils.STUDENT)) {
                subText.setText("Switch from mentor to student profile ...");
            } else {
                subText.setText("Switch from mentor to boot camp profile ...");
            }

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
                    showProgress();
                    if (identify.equals(DumeUtils.STUDENT)) {
                        new DumeModel(context).switchAcount(DumeUtils.STUDENT, new TeacherContract.Model.Listener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                gotoStudentHomePage();
                            }

                            @Override
                            public void onError(String msg) {
                                hideProgress();
                                flush("Network error 101 !!");
                            }
                        });
                    } else {
                        new DumeModel(context).switchAcount(DumeUtils.BOOTCAMP, new TeacherContract.Model.Listener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                gotoBootCamPHomePage();
                            }

                            @Override
                            public void onError(String msg) {
                                hideProgress();
                                flush("Network error 101 !!");
                            }
                        });
                    }
                }
            });
        }
        mCancelBottomSheetDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1234:
                if (resultCode == EditAccount.RESULT_OK) {
                    presenter.loadProfile(new TeacherContract.Model.Listener<Void>() {
                        @Override
                        public void onSuccess(Void list) {
                            final AcademicFragment academicFragment = AcademicFragment.getInstance();
                            if (academicFragment.academicRV.getAdapter() != null) {
                                academicFragment.loadData();
                            } else {
                                academicFragment.academicAdapter = new AcademicAdapter(TeacherActivtiy.this, academicFragment.getAcademics(teacherDataStore.getDocumentSnapshot()));
                                academicFragment.academicRV.setAdapter(academicFragment.academicAdapter);
                            }
                        }

                        @Override
                        public void onError(String msg) {
                            flush(msg);
                        }
                    });
                    //viewPager.setCurrentItem(3);
                }
                break;
        }
    }

    @Override
    public void showProgressTwo() {
        if (loadView.getVisibility() == View.INVISIBLE || loadView.getVisibility() == View.GONE) {
            loadView.setVisibility(View.VISIBLE);
        }
        if (!loadView.isRunningAnimation()) {
            loadView.startLoading();
        }
    }

    @Override
    public void hideProgressTwo() {
        if (loadView.isRunningAnimation()) {
            loadView.stopLoading();
        }
        if (loadView.getVisibility() == View.VISIBLE) {
            loadView.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void showSingleBottomSheetRating(HomePageRatingData currentRatingDataList) {
        List<HomePageRecyclerData> promoData = new ArrayList<>();
        if (hPageBSRcyclerAdapter == null) {
            hPageBSRcyclerAdapter = new HomePageRecyclerAdapter(this, promoData);
            hPageBSRecycler.setAdapter(hPageBSRcyclerAdapter);
            hPageBSRecycler.setLayoutManager(new LinearLayoutManager(this));
        }
        hPageBSRcyclerAdapter.addNewData(currentRatingDataList);

    }

    @Override
    public void updateAccountActive(boolean acountActive) {
        if (acountActive) {
            buttonActive.setChecked(true);
        } else {
            buttonInActive.setChecked(true);
        }
    }

}
