package io.dume.dume.student.grabingPackage;

import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.ViewTreeObserver;
import android.widget.TextView;

import carbon.beta.AppBarLayout;
import carbon.widget.LinearLayout;
import io.dume.dume.R;
import io.dume.dume.custom_view.HorizontalLoadView;
import io.dume.dume.student.pojo.CusStuAppComMapActivity;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.student.pojo.MyGpsLocationChangeListener;

public class GrabingPackageActivity extends CusStuAppComMapActivity implements GrabingPackageContract.View,
        MyGpsLocationChangeListener {

    private GrabingPackageContract.Presenter mPresenter;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    protected static int fromFlag = 4;
    private BottomSheetBehavior bottomSheetBehavior;
    private HorizontalLoadView loadView;
    private carbon.widget.LinearLayout llBottomSheet;
    private CoordinatorLayout coordinatorLayout;
    private AppBarLayout myAppBarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu4_activity_grabing_package);
        setActivityContextMap(this, fromFlag);
        mPresenter = new GrabingPackagePresenter(this, new GrabingPackageModel());
        mPresenter.grabingPackagePageEnqueue();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        loadView = findViewById(R.id.loadView);
        llBottomSheet = findViewById(R.id.packageBottomSheet);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.my_main_container);
        myAppBarLayout = findViewById(R.id.my_appbarLayout);


        if (!loadView.isRunningAnimation()) {
            loadView.startLoading();
        }
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);

        ViewTreeObserver vto = llBottomSheet.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                llBottomSheet.getLayoutParams().height = (llBottomSheet.getHeight() - myAppBarLayout.getHeight() - (int) (10 * (getResources().getDisplayMetrics().density)));
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

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                llBottomSheet.animate().scaleX(1 + (slideOffset * 0.058f)).setDuration(0).start();
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
    public void configGrabingPackagePage() {

    }

    @Override
    public void initGrabingPackagePage() {

    }

    @Override
    public void findView() {

    }

    @Override
    public void onMyGpsLocationChanged(Location location) {

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
            View rootView = inflater.inflate(R.layout.stu4_fragment_grabing_package, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
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
