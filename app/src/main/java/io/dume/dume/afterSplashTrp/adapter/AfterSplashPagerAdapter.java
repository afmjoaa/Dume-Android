package io.dume.dume.afterSplashTrp.adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import io.dume.dume.R;

public class AfterSplashPagerAdapter extends FragmentStatePagerAdapter {

    public static final int ID_DEFAULT = 0;
    public static final int ID_STYLED = 1;
    public static final int ID_CUSTOM_LAYOUT = 2;
    public static final int ID_CUSTOM_VIEW_HOLDER = 3;
    public static final int ID_CUSTOM_CONTENT = 4;

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
            case ID_CUSTOM_LAYOUT:
                title = titleArr[2];
                description = descriptionArr[2];
                imageSrc= imageIcons[2];
                break;
            case ID_CUSTOM_VIEW_HOLDER:
                title = titleArr[3];
                description = descriptionArr[3];
                imageSrc= imageIcons[3];
                break;
            case ID_CUSTOM_CONTENT:
                title = titleArr[4];
                description = descriptionArr[4];
                imageSrc= imageIcons[4];
                break;
        }
        return DemoCardFragment.newInstance(position, title, description, imageSrc);
    }

    @Override
    public int getCount() {
        return 5;
    }
}