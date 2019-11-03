package io.dume.dume.customView;

import android.content.Context;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.util.AttributeSet;

@CoordinatorLayout.DefaultBehavior(MoveUpwardBehaviour.class)
public class MoveableBottomNav extends BottomNavigationView {

    public MoveableBottomNav(Context context) {
        super(context);
    }

    public MoveableBottomNav(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveableBottomNav(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}