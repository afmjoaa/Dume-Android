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
        switch (position) {
            case ID_DEFAULT:
                title = context.getString(R.string.sample_title_holder);
                description = context.getString(R.string.intro1);
                break;
            case ID_STYLED:
                title = context.getString(R.string.sample_title_holder);
                description = context.getString(R.string.intro2);
                break;
            case ID_CUSTOM_LAYOUT:
                title = context.getString(R.string.sample_title_holder);
                description = context.getString(R.string.intro3);
                break;
            case ID_CUSTOM_VIEW_HOLDER:
                title = context.getString(R.string.sample_title_holder);
                description = context.getString(R.string.intro4);
                break;
            case ID_CUSTOM_CONTENT:
                title = context.getString(R.string.sample_title_holder);
                description = context.getString(R.string.intro5);
                break;
        }
        return DemoCardFragment.newInstance(position, title, description);
    }

    @Override
    public int getCount() {
        return 5;
    }
}