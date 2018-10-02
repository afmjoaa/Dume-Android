package io.dume.dume.common.inboxActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.dume.dume.R;
import io.dume.dume.common.contactActivity.ContactActivity;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.student.searchResultTabview.SearchResultTabData;
import io.dume.dume.student.searchResultTabview.SearchResultTabRecyAda;

import static io.dume.dume.util.DumeUtils.configureAppbarWithoutColloapsing;

public class InboxActivity extends CustomStuAppCompatActivity implements InboxActivityContact.View {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private InboxActivityContact.Presenter mPresenter;
    private static final String TAG = "InboxActivity";
    private static final int fromFlag = 30;
    private AppBarLayout myAppbarLayout;
    private FloatingActionButton fab;
    int[] iconIntArray = {R.drawable.ic_inbox_chat,
            R.drawable.ic_inbox_notification_creater,
            R.drawable.ic_inbox_call};
    private TabLayout tabLayout;
    private int currentTabPosition = -1;
    private Intent contactFromIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common1_activity_inbox);
        setActivityContext(this, fromFlag);
        mPresenter = new InboxActivityPresenter(this, new InboxActivityModel());
        mPresenter.inboxEnqueue();
        configureAppbarWithoutColloapsing(this, "Inbox");
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // loop through all navigation tabs
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            // inflate the Parent LinearLayout Container for the tab
            // from the layout nav_tab.xml file that we created 'R.layout.nav_tab
            LinearLayout tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab_layout_inbox, null);

            // get child TextView and ImageView from this layout for the icon and label
            String[] tab_label_name = getResources().getStringArray(R.array.inboxHeader);
            TextView tab_label = (TextView) tab.findViewById(R.id.nav_label);
            TextView tab_state = (TextView) tab.findViewById(R.id.active_state_dot);
            tab_label.setText(tab_label_name[i]);
            tab_state.setText("");//"" or "*"

            // finally publish this custom view to navigation tab
            Objects.requireNonNull(tabLayout.getTabAt(i)).setCustomView(tab);
        }
        // finishes here ................

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentTabPosition) {
                    case 0:
                        contactFromIntent.putExtra("contactFrom", 0);
                        startActivity(contactFromIntent);
                        break;
                    case 1:
                        Toast.makeText(InboxActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        contactFromIntent.putExtra("contactFrom", 2);
                        startActivity(contactFromIntent);
                        break;
                }
            }
        });


        //testing code here
        Intent notiIntent = getIntent();
        int tabToOpen = notiIntent.getIntExtra("notiTab", -1);
        if (tabToOpen != -1) {
            // Open the right tab
            TabLayout.Tab tab = tabLayout.getTabAt(tabToOpen);
            Objects.requireNonNull(tab).select();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inbox, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void findView() {
        myAppbarLayout = findViewById(R.id.app_bar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.container);

    }

    @Override
    public void initInbox() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                animateFab(tab.getPosition());
                currentTabPosition = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @Override
    public void configInbox() {
        contactFromIntent = new Intent(InboxActivity.this, ContactActivity.class);
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
                // Change FAB color and
                fab.setImageDrawable(ContextCompat.getDrawable(InboxActivity.this, iconIntArray[position]));
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

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private RecyclerView inboxRecyclerView;
        private InboxActivity myThisActivity;


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
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            myThisActivity = (InboxActivity) getActivity();
            View rootView = inflater.inflate(R.layout.common1_fragment_inbox, container, false);
            inboxRecyclerView = rootView.findViewById(R.id.inbox_recycler_view);
            /*TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));*/
            //setting the recycler view
            List<SearchResultTabData> recordData = new ArrayList<>();
            SearchResultTabRecyAda recordsRecyAda = new SearchResultTabRecyAda(myThisActivity, recordData);
            inboxRecyclerView.setAdapter(recordsRecyAda);
            inboxRecyclerView.setLayoutManager(new LinearLayoutManager(myThisActivity));

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
