package io.dume.dume.customView;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;

@CoordinatorLayout.DefaultBehavior(MoveUpwardBehaviour.class)
public class MoveableCustomFab extends com.getbase.floatingactionbutton.FloatingActionsMenu {

    public MoveableCustomFab(Context context) {
        super(context);
    }

    public MoveableCustomFab(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveableCustomFab(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}