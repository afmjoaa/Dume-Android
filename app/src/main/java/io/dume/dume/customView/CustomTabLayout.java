package io.dume.dume.customView;

import android.content.Context;
import com.google.android.material.tabs.TabLayout;
import android.util.AttributeSet;
import java.lang.reflect.Field;
import io.dume.dume.util.DumeUtils;

public class CustomTabLayout extends TabLayout {

    private static final int WIDTH_INDEX = 0;
    private static final int DIVIDER_FACTOR = 3;
    private static final String SCROLLABLE_TAB_MIN_WIDTH = "mScrollableTabMinWidth";

    public CustomTabLayout(Context context) {
        super(context);
        initTabMinWidth();
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTabMinWidth();
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTabMinWidth();
    }

    private void initTabMinWidth() {
        int[] wh = DumeUtils.getScreenSize(getContext());
        int tabMinWidth = wh[WIDTH_INDEX] / DIVIDER_FACTOR;

        Field field;
        try {
            field = TabLayout.class.getDeclaredField(SCROLLABLE_TAB_MIN_WIDTH);
            field.setAccessible(true);
            field.set(this, tabMinWidth);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}