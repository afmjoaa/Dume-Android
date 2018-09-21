package io.dume.dume.student.recordsPending;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import io.dume.dume.R;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.OnSwipeTouchListener;

public class RecordsPendingActivity extends CustomStuAppCompatActivity implements RecordsPendingContract.View {

    private RecordsPendingContract.Presenter mPresenter;
    private static final int fromFlag = 20;
    private BottomSheetDialog mBottomSheetDialog;
    private View sheetView;
    private OnSwipeTouchListener onSwipeTouchListener;
    private RelativeLayout recordsHostLayout;
    private ViewPager pager;
    private SectionsPagerAdapter myPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu9_activity_records_pendding);
        setActivityContext(this, fromFlag);
        mPresenter = new RecordsPendingPresenter(this, new RecordsPendingModel());
        mPresenter.recordsPendingEnqueue();
        DumeUtils.configureAppbar(this, "Pending Requests");



    }
    @Override
    public void findView() {
        recordsHostLayout = findViewById(R.id.recordsHostLayout);

    }

    @Override
    public void initRecordsPending() {

    }

    @Override
    public void configRecordsPending() {

        mBottomSheetDialog = new BottomSheetDialog(this);
        sheetView = this.getLayoutInflater().inflate(R.layout.custom_bottom_sheet_dialogue_records, null);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();

        pager = (ViewPager) findViewById(R.id.pending_page_container);
        myPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(myPagerAdapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_records_default, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_help:
                //Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }



    //testing code goes here
    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private RecordsPendingActivity myThisActivity;

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
            myThisActivity = (RecordsPendingActivity) getActivity();
            View rootView = inflater.inflate(R.layout.stu9_viewpager_layout_pending, container, false);
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
            // from database call will get the array size
            return 4;
        }
    }

}
