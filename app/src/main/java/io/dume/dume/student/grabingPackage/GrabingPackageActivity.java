package io.dume.dume.student.grabingPackage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.touchboarder.weekdaysbuttons.WeekdaysDataItem;
import com.touchboarder.weekdaysbuttons.WeekdaysDataSource;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import biz.laenger.android.vpbs.BottomSheetUtils;
import biz.laenger.android.vpbs.ViewPagerBottomSheetBehavior;
import carbon.widget.Button;
import io.dume.dume.R;
import io.dume.dume.customView.HorizontalLoadView;
import io.dume.dume.student.pojo.CusStuAppComMapActivity;
import io.dume.dume.student.pojo.MyGpsLocationChangeListener;
import io.dume.dume.student.searchLoading.SearchLoadingActivity;

public class GrabingPackageActivity extends CusStuAppComMapActivity implements GrabingPackageContract.View,
        MyGpsLocationChangeListener, OnMapReadyCallback {

    private GrabingPackageContract.Presenter mPresenter;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private static final int fromFlag = 4;
    private ViewPagerBottomSheetBehavior bottomSheetBehavior;
    private HorizontalLoadView loadView;
    private carbon.widget.LinearLayout llBottomSheet;
    private CoordinatorLayout coordinatorLayout;
    private AppBarLayout myAppBarLayout;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;

    private int[] navIcons = {
            R.drawable.ic_seven_days,
            R.drawable.ic_preffered_day,
            R.drawable.ic_time
    };
    private Button packageSearchBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu4_activity_grabing_package);
        setActivityContextMap(this, fromFlag);
        mPresenter = new GrabingPackagePresenter(this, new GrabingPackageModel());
        mPresenter.grabingPackagePageEnqueue();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        getLocationPermission(mapFragment);


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        BottomSheetUtils.setupViewPager(mViewPager);

        // loop through all navigation tabs
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            // inflate the Parent LinearLayout Container for the tab
            // from the layout nav_tab.xml file that we created 'R.layout.nav_tab
            LinearLayout tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tablayout_tab, null);
            String navLabels[] = getResources().getStringArray(R.array.GrabingPackageTabtext);

            // get child TextView and ImageView from this layout for the icon and label
            TextView tab_label = (TextView) tab.findViewById(R.id.nav_label);
            ImageView tab_icon = (ImageView) tab.findViewById(R.id.nav_icon);
            tab_label.setText(navLabels[i]);
            tab_icon.setImageResource(navIcons[i]);

            // finally publish this custom view to navigation tab
            Objects.requireNonNull(tabLayout.getTabAt(i)).setCustomView(tab);
        }
        // finishes here ................

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    @Override
    public void findView() {
        loadView = findViewById(R.id.loadView);
        llBottomSheet = findViewById(R.id.packageBottomSheet);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.my_main_container);
        myAppBarLayout = findViewById(R.id.my_appbarLayout);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.container);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        packageSearchBtn = findViewById(R.id.package_search_btn);

    }

    @Override
    public void executeSearchActivity() {
        startActivity(new Intent(this, SearchLoadingActivity.class));
    }

    @Override
    public void initGrabingPackagePage() {
        //setting the support action bar
        setSupportActionBar(toolbar);
        //status bar and action bar transparent
        settingStatusBarTransparent();
        setDarkStatusBarIcon();
        myAppBarLayout.bringToFront();
        myAppBarLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        if (!loadView.isRunningAnimation()) {
            loadView.startLoading();
        }

        //bottom sheet height fix
        ViewTreeObserver vto = llBottomSheet.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                llBottomSheet.getLayoutParams().height = (llBottomSheet.getHeight() - myAppBarLayout.getHeight() - (int) (2 * (getResources().getDisplayMetrics().density)));
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
    }

    @Override
    public void configGrabingPackagePage() {
        bottomSheetBehavior = ViewPagerBottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new ViewPagerBottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //llBottomSheet.animate().scaleX(1 + (slideOffset * 0.058f)).setDuration(0).start();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_grabing_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMyGpsLocationChanged(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setPadding(0, 0, 0, (int) (400 * (getResources().getDisplayMetrics().density)));

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void onGrabingPackageViewCLicked(View view) {
        mPresenter.onGrabingPackageViewIntracted(view);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private View rootView;
        private View mainRootView;
        private TextView textView;
        private WeekdaysDataSource weekdaysDataSource3;
        private GrabingPackageActivity myMainActivity;
        private CoordinatorLayout coordinatorLayout;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            myMainActivity = (GrabingPackageActivity) getActivity();
            int fragmentPosition = getArguments().getInt(ARG_SECTION_NUMBER);
            switch (fragmentPosition) {
                case 1:
                    rootView = inflater.inflate(R.layout.stu4_fragment_seven_days, container, false);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.stu4_fragment_preferred_days, container, false);
                    WeekdaysDataSource.Callback callback3 = new WeekdaysDataSource.Callback() {
                        @Override
                        public void onWeekdaysItemClicked(int i, WeekdaysDataItem weekdaysDataItem) {

                        }

                        @Override
                        public void onWeekdaysSelected(int i, ArrayList<WeekdaysDataItem> items) {
                            String selectedDays = getSelectedDaysFromWeekdaysData(items);
                            if (!TextUtils.isEmpty(selectedDays))
                                showSnackbarShort(selectedDays);
                        }
                    };
                    weekdaysDataSource3 = new WeekdaysDataSource(myMainActivity, R.id.weekdays_sample_3, rootView)
                            .setFirstDayOfWeek(Calendar.SUNDAY)
                            .setSelectedDays(Calendar.MONDAY, Calendar.WEDNESDAY)
                            .setTextColorSelected(Color.WHITE)
                            .setFillWidth(true)
                            .setTextColorUnselectedRes(R.color.textColorPrimary)
                            .setUnselectedColor(Color.TRANSPARENT)
                            .setWeekdayItemLayoutRes(R.layout.custom_weekdays_image_view)
                            .setNumberOfLetters(1)
                            .start(callback3);


                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.stu4_fragment_time, container, false);
                    break;
            }
            return rootView;
        }

        private String getSelectedDaysFromWeekdaysData(ArrayList<WeekdaysDataItem> items) {
            StringBuilder stringBuilder = new StringBuilder();
            boolean selected = false;
            for (WeekdaysDataItem dataItem : items) {
                if (dataItem.isSelected()) {
                    selected = true;
                    stringBuilder.append(dataItem.getLabel());
                    stringBuilder.append(", ");
                }
            }
            if (selected) {
                String result = stringBuilder.toString();
                return result.substring(0, result.lastIndexOf(","));
            } else return "No days selected";
        }

        public void showSnackbarShort(String message) {
            mainRootView = ((Activity) myMainActivity).getWindow().getDecorView().findViewById(android.R.id.content);
            coordinatorLayout = (CoordinatorLayout) mainRootView.findViewById(R.id.parent_coor_layout);
            if (coordinatorLayout != null) {
                showSnackbar(coordinatorLayout, message, null, Snackbar.LENGTH_SHORT, null);
            }
        }

        public Snackbar showSnackbar(@NonNull ViewGroup viewGroup, String message, String action, int length, View.OnClickListener onActionListener) {
            Snackbar snackbar = Snackbar.make(viewGroup, message, length);
            if (!TextUtils.isEmpty(action) && onActionListener != null)
                snackbar.setAction(action, onActionListener);
            snackbar.show();
            return snackbar;
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
            // Show 3 total pages.
            return 3;
        }
    }
}
