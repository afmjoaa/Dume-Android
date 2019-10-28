package io.dume.dume.FirstTimeUser.adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import io.dume.dume.R;

public class AfterSplashPagerAdapter extends FragmentStatePagerAdapter {

    public static final int ID_DEFAULT = 0;
    public static final int ID_STYLED = 1;

    private Context context;

    public AfterSplashPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        String title = null;
        String description = null;
        int imageSrc = 0;
        String[] titleArr = context.getResources().getStringArray(R.array.after_splash_title);
        String[] descriptionArr = context.getResources().getStringArray(R.array.after_splash_data);
        int[] imageIcons = {
                R.drawable.intro_bg,
                R.drawable.real_time_bg,
                R.drawable.permission_bg,
                R.drawable.profiles_bg,
                R.drawable.package_bg,
        };
        switch (position) {
            case ID_DEFAULT:
                title = titleArr[0];
                description = descriptionArr[0];
                imageSrc= imageIcons[0];
                break;
            case ID_STYLED:
                title = titleArr[1];
                description = descriptionArr[1];
                imageSrc= imageIcons[1];
                break;
        }
        return DemoCardFragment.newInstance(position, title, description, imageSrc);
    }

    @Override
    public int getCount() {
        return 2;
    }
}