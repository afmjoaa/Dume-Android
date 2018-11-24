package io.dume.dume.student.grabingInfo;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.dume.dume.inter_face.OnTabModificationListener;
import io.dume.dume.teacher.model.LocalDb;

public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
    private boolean doNotifyDataSetChangedOnce = false;
    private int count;
    private final LocalDb db;
    private int selectedPosition;
    private OnTabModificationListener listener;
    private List<String> list;
    private List<Fragment> fragmentList;
    private List<String> titleList;
    private String TAG = "Bal";


    public SectionsPagerAdapter(FragmentManager fm, int count, int selectedPosition, OnTabModificationListener listener) {
        super(fm);
        this.selectedPosition = selectedPosition;
        this.listener = listener;
        db = new LocalDb();
        this.count = count;
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();


    }

    public void newTab(List<String> list) {

        fragmentList.add(DataHolderFragment.newInstance(fragmentList.size(), list));
        titleList.add(list.toString());
        notifyDataSetChanged();
        listener.onNewTabCreated(list.toString());
    }

    public void removeTabs(int from) {
        if (fragmentList.size() > from) {
            Log.w(TAG, "From " + from + " Nothing to clear " + fragmentList.size() + " " + titleList.size());
            int length = fragmentList.size();
            for (int i = from; i < length; i++) {
                fragmentList.remove(fragmentList.size() - 1);
                titleList.remove(titleList.size() - 1);
                Log.w(TAG, "removeTabs: Removed : " + i);
            }
            Log.w(TAG, "From " + from + " Nothing to clear " + fragmentList.size());

        } else {
            Log.w(TAG, "removeTabs: " + "Nothing to clear : start " + from + " end : " + fragmentList.size());
        }
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    public void sync() {
        this.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    public void setListener(OnTabModificationListener listener) {
        this.listener = listener;
    }
}

