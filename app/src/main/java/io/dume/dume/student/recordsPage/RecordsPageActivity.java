package io.dume.dume.student.recordsPage;

import android.graphics.Typeface;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.dume.dume.R;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.util.DumeUtils;

import static io.dume.dume.util.DumeUtils.configureAppbarWithoutColloapsing;

public class RecordsPageActivity extends CustomStuAppCompatActivity implements RecordsPageContract.View {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private RecordsPageContract.Presenter mPresenter;
    private static final int fromFlag = 25;
    private TabLayout tabLayout;

    private int[] navIcons = {
            R.drawable.ic_task_pending,
            R.drawable.ic_task_accepted,
            R.drawable.ic_task_current,
            R.drawable.ic_task_completed,
            R.drawable.ic_task_rejected
    };
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu8_activity_records_page);
        setActivityContext(this, fromFlag);
        mPresenter = new RecordsPagePresenter(this, new RecordsPageModel());
        mPresenter.recordsPageEnqueue();
        configureAppbarWithoutColloapsing(this, "Records");

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        // loop through all navigation tabs
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            // inflate the Parent LinearLayout Container for the tab
            // from the layout nav_tab.xml file that we created 'R.layout.nav_tab
            LinearLayout tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tablayout_tab, null);
            String navLabels[] = getResources().getStringArray(R.array.RecordsPageTabtext);

            // get child TextView and ImageView from this layout for the icon and label
            TextView tab_label = (TextView) tab.findViewById(R.id.nav_label);
            ImageView tab_icon = (ImageView) tab.findViewById(R.id.nav_icon);
            tab_label.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Cairo-Light.ttf"));
            /*tab_label.setTextColor(getResources().getColorStateList(R.color.tab_colorstate_light));
            */
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


    }

    @Override
    public void initRecordsPage() {

    }

    @Override
    public void configRecordsPage() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_records_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sync_now) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        private RecyclerView recordRecyclerView;
        private RecordsPageActivity myThisActivity;
        private SwipeRefreshLayout swipeRefreshLayout;


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
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            myThisActivity = (RecordsPageActivity) getActivity();
            View rootView = inflater.inflate(R.layout.stu8_fragment_records_page, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            recordRecyclerView = rootView.findViewById(R.id.records_page_recycle_view);
            swipeRefreshLayout = rootView.findViewById(R.id.swipeToRefreshRecords);

            swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_light, android.R.color.holo_green_light,
                    android.R.color.holo_red_light, android.R.color.holo_blue_light);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }, 5000);
                }
            });

            //setting the recycler view
            List<RecordsRecyData> recordData = new ArrayList<>();
            RecordsRecyAdapter recordsRecyAda = new RecordsRecyAdapter(myThisActivity, recordData);
            recordRecyclerView.setAdapter(recordsRecyAda);
            recordRecyclerView.setLayoutManager(new LinearLayoutManager(myThisActivity));
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
            return 5;
        }
    }
}
