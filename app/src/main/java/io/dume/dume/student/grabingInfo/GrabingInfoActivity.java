package io.dume.dume.student.grabingInfo;

import android.Manifest;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRadioButton;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import carbon.widget.RelativeLayout;
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
    private ViewPager mViewPager;
    private int[] navIcons = {
            R.drawable.ic_payment,
            R.drawable.ic_gender_preference,
            R.drawable.ic_cross_check,
            R.drawable.ic_seven_days,
            R.drawable.ic_medium,
            R.drawable.ic_subject,
            R.drawable.ic_class,
            R.drawable.ic_preffered_day
    };
    private int[] navLabels = {
            R.string.tab_payment,
            R.string.tab_gender_preference,
            R.string.tab_cross_ckeck
    };
    private String[] givenInfo = {"Ex.Others", "Ex.O level", "Ex.Physics", "Ex.3k - 6k", "Ex.Both", "→←"};
    protected TabLayout tabLayout;
    private TextView hintIdOne;
    private TextView hintIdTwo;
    private TextView hintIdThree;
    private static final String TAG = "GrabingInfoActivity";
    private static final int fromFlag = 3;
    private GrabingInfoContract.Presenter mPresenter;
    private Toolbar toolbar;
    public FloatingActionButton fab;
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
    private String retrivedAction;
    private List<String> queryList;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu2_activity_grabing_info);
        setActivityContextMap(this, fromFlag);
        mPresenter = new GrabingInfoPresenter(this, new GrabingInfoModel());
        mPresenter.grabingInfoPageEnqueue();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        getLocationPermission(mapFragment);
        queryList = new ArrayList<>();
        retrivedAction = getIntent().getAction();
        if(retrivedAction != null){
            getAction();
        }
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), 1, getIntent().getIntExtra(DumeUtils.SELECTED_ID, 0), this);
        mSectionsPagerAdapter.newTab(new LocalDb().getLevelOne(getIntent().getIntExtra(DumeUtils.SELECTED_ID, 0)));
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        //mViewPager.setOffscreenPageLimit(10);
        //mSectionsPagerAdapter.notifyDataSetChanged();
        setDarkStatusBarIcon();
        onNewTabCreated("Enam");
        //AccessContact();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        selectFromContact = findViewById(R.id.select_other_contact);
        endOfNest = new ArrayList<>(Arrays.asList("Subject", "Field", "Software", "Language", "Flavour", "Type", "Course"));


        //testing code
        wh = DumeUtils.getScreenSize(this);
        tabMinWidthThree = ((wh[0] / 3) - (int) (24 * (getResources().getDisplayMetrics().density)));
        tabMinWidthTwo = ((wh[0] / 2) - (int) (24 * (getResources().getDisplayMetrics().density)));
        tabMinWidthOne = ((wh[0]) - (int) (24 * (getResources().getDisplayMetrics().density)));
        textParamThree = new LinearLayout.LayoutParams(tabMinWidthThree, LinearLayout.LayoutParams.WRAP_CONTENT);
        textParamTwo = new LinearLayout.LayoutParams(tabMinWidthTwo, LinearLayout.LayoutParams.WRAP_CONTENT);
        textParamOne = new LinearLayout.LayoutParams(tabMinWidthOne, LinearLayout.LayoutParams.WRAP_CONTENT);


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
                if(fab.getVisibility()== View.VISIBLE){
                    animateFab(tabPosition);
                }
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
                if(fab.getVisibility()== View.VISIBLE){
                    animateFab(tabPosition);
                }
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
                    forMeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.alias_profile_icon, 0, R.drawable.ic_keyboard_arrow_down_black_24dp, 0);
                    forMeBtn.setText(R.string.for_me);
                }
            }

        });
    }

    @Override
    public void viewMuskClicked() {
        forMeBtn.performClick();
    }

    @Override
    public void fabClicked(View view) {
        if(tabLayout.getSelectedTabPosition() == (tabLayout.getTabCount()-1)){
            String levelName = (String) tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText();
            if (endOfNest.contains(levelName)) {
                AppCompatRadioButton rd = new AppCompatRadioButton(this);
                rd.setText("endOfNest");
                onRadioButtonClick(rd,tabLayout.getSelectedTabPosition(),levelName);
            } else if(levelName.equals("Salary")) {
                AppCompatRadioButton rd = new AppCompatRadioButton(this);
                rd.setText("fromSalary");
                onRadioButtonClick(rd,tabLayout.getSelectedTabPosition(),levelName);
            }else if(levelName.equals("Cross Check")){
                //cross_check block here
                gotoGrabingPackage();
            }
        }else{
            //other general block "just go to the next one"
            Objects.requireNonNull(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()+1)).select();
        }
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
        switch (Objects.requireNonNull(retrivedAction)) {
            case DumeUtils.STUDENT:
                selected_category_position = getIntent().getIntExtra(DumeUtils.SELECTED_ID, 0);
                createTabDynamically();
                queryList.add(new LocalDb().getCategories().get(selected_category_position));
                break;
            case DumeUtils.TEACHER:
                selected_category_position = getIntent().getIntExtra(DumeUtils.SELECTED_ID, 0);
                forMeWrapper.setVisibility(View.GONE);
                toolbar.setTitle("Select Skill");
                createTabDynamically();
                queryList.add(new LocalDb().getCategories().get(selected_category_position));
                break;
            default:
                selected_category_position = getIntent().getIntExtra(DumeUtils.SELECTED_ID, 0);
                createTabDynamically();
                queryList.add(new LocalDb().getCategories().get(selected_category_position));
                break;
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
        ArrayList<String> arr = new ArrayList<>(Arrays.asList("Salary", "Gender"));
        if (arr.contains(levelName) || (endOfNest.contains(levelName) && view.getText().toString().equals("endOfNest"))) {
            fab.setVisibility(View.VISIBLE);
            switch (levelName) {
                case "Gender":
                    if (!(fragmentId < tabLayout.getTabCount() - 1)) {
                        mSectionsPagerAdapter.newTab(db.payment);
                        mViewPager.setCurrentItem(fragmentId + 1);
                    } else mViewPager.setCurrentItem(fragmentId + 1);
                    break;
                case "Salary":
                    if (!(fragmentId < tabLayout.getTabCount() - 1)) {
                        mSectionsPagerAdapter.newTab(db.crossCheck);
                        mViewPager.setCurrentItem(fragmentId + 1);
                    } else mViewPager.setCurrentItem(fragmentId + 1);
                break;
             /* case "Subject":
                case "Field":
                case "Flavour":
                case "Type":
                case "Course":
                case "Software":
                case "Language":*/

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
        //flush("End of the Nest");
        if (!(fragment < tabLayout.getTabCount() - 1)) {
            mSectionsPagerAdapter.newTab(db.getGenderPreferencesList());
            mViewPager.setCurrentItem(fragment + 1);
        } else mViewPager.setCurrentItem(fragment + 1);
    }

    //testing the contact retrival
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Log.d(TAG, "Response: " + data.toString());
            uriContact = data.getData();

            retrieveContactName();
            retrieveContactNumber();
            retrieveContactPhoto();
        }
    }

    private void retrieveContactPhoto() {
        Bitmap photo = null;
        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
//                ImageView imageView = (ImageView) findViewById(R.id.img_contact);
//                imageView.setImageBitmap(photo);
                flush("image not null here");
                inputStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
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

        for(int i = 0; i <= 20 ; i++){
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
                if(contactNumber != null){
                    Log.d(TAG, "Contact Phone Number: " + contactNumber);
                    allNumbers.add(contactNumber);
                }
            }
            cursorPhone.close();
        }
    }

    private void retrieveContactName() {
        String contactName = null;
        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);
        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
        cursor.close();
        Log.d(TAG, "Contact Name: " + contactName);

    }

    public void flush(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNewTabCreated(String tabName) {
        tabLayout.invalidate();
        int whatToDO = tabLayout.getTabCount()-1;
        if(whatToDO == 0){
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
                tab_icon.setImageResource(navIcons[i]);
                tab_label.setLayoutParams(textParamOne);
                Objects.requireNonNull(tabLayout.getTabAt(i)).setCustomView(tab);
            }
        }else if(whatToDO == 1){
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
                tab_icon.setImageResource(navIcons[i]);
                tab_label.setLayoutParams(textParamTwo);
                Objects.requireNonNull(tabLayout.getTabAt(i)).setCustomView(tab);
            }
        }else{
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
                tab_icon.setImageResource(navIcons[i]);
                tab_label.setLayoutParams(textParamThree);
                Objects.requireNonNull(tabLayout.getTabAt(i)).setCustomView(tab);
            }
        }
    }

    @Override
    public void onTabDeleted() {

    }

    @Override
    public void selectFromContactClicked() {
    }

    public void onSelectFromOtherContact(View view) {
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
    }


    final private int REQUEST_MULTIPLE_PERMISSIONS = 124;
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);

            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(GrabingInfoActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void AccessContact()
    {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS))
            permissionsNeeded.add("Read Contacts");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_CONTACTS))
            permissionsNeeded.add("Write Contacts");
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_MULTIPLE_PERMISSIONS);
            return;
        }
    }
}
