package io.dume.dume.teacher.homepage;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import io.dume.dume.R;
import io.dume.dume.teacher.homepage.fragments.PerformanceFragment;
import io.dume.dume.teacher.homepage.fragments.StatisticsFragment;
import io.dume.dume.teacher.pojo.TabModel;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.adapter.TabAdapter;
import q.rorbin.verticaltablayout.widget.ITabView;
import q.rorbin.verticaltablayout.widget.QTabView;
import q.rorbin.verticaltablayout.widget.TabView;

public class TeacherActivityMock extends AppCompatActivity {
    @BindView(R.id.tablayout)
    VerticalTabLayout tabLayout;
    @BindView(R.id.mViewPager)
    VerticalViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_mock);
        ButterKnife.bind(this);
        ArrayList<TabModel> tabModelArrayList = new ArrayList<>();
        tabModelArrayList.add(new TabModel(R.drawable.performance, R.drawable.performance_selected, 0, "Performance"));
        tabModelArrayList.add(new TabModel(R.drawable.inbox, R.drawable.inbox, 0, "Inbox"));
        tabModelArrayList.add(new TabModel(R.drawable.pay, R.drawable.pay, 0, "Pay"));
        tabModelArrayList.add(new TabModel(R.drawable.ic_cross_check, R.drawable.ic_cross_check, 3, "Statistics"));
        tabModelArrayList.add(new TabModel(R.drawable.skills, R.drawable.skills, 0, "Manage Skills"));
        tabModelArrayList.add(new TabModel(R.drawable.education, R.drawable.education, 0, "Academic"));
        // tabLayout.setBackgroundTintList();

        tabLayout.setTabAdapter(new TabAdapter() {

            @Override
            public int getCount() {
                return tabModelArrayList.size();
            }

            @Override
            public ITabView.TabBadge getBadge(int position) {


                if (tabModelArrayList.get(position).getBadge() > 0) {

                    return new ITabView.TabBadge.Builder().setBadgeNumber(2).setBadgeText(tabModelArrayList.get(position).getBadge() + "").build();
                }
                return null;

            }

            @Override
            public ITabView.TabIcon getIcon(int position) {
                int normalIcon = tabModelArrayList.get(position).getNormalIcon();
                int selected = tabModelArrayList.get(position).getSelectedIcon();

                return new ITabView.TabIcon.Builder().setIcon(selected, normalIcon).build();


            }

            @Override
            public ITabView.TabTitle getTitle(int position) {
                return null;
            }

            @Override
            public int getBackground(int position) {
                return 0;
            }
        });
        tabLayout.addOnTabSelectedListener(new VerticalTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabView tab, int position) {
                viewPager.setCurrentItem(position);
                tab.setActivated(true);

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


    }

    class pager extends FragmentPagerAdapter {

        public pager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = null;
            if (i == 0) {
                fragment = new PerformanceFragment();
            } else {
                fragment = new StatisticsFragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 6;
        }
    }
}
