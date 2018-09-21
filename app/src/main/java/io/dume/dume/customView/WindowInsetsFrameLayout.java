package io.dume.dume.customView;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.util.ObjectsCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowInsets;
import android.widget.FrameLayout;

/**
 * A FrameLayout which memorizes the window insets and propagates them to child views before they are measured.
 * You can use this layout as a fragment container in place of a standard FrameLayout to
 * propagate window insets to attached fragments.
 *
 * @author Christophe Beyls
 */
public class WindowInsetsFrameLayout extends FrameLayout {

    private Object mLastInsets;

    public WindowInsetsFrameLayout(Context context) {
        super(context);
    }

    public WindowInsetsFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WindowInsetsFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        if (!ObjectsCompat.equals(mLastInsets, insets)) {
            mLastInsets = insets;
            requestLayout();
        }
        return insets.consumeSystemWindowInsets();
    }

    @SuppressWarnings("deprecation")
    @Override
    protected boolean fitSystemWindows(Rect insets) {
        if (!ObjectsCompat.equals(mLastInsets, insets)) {
            if (mLastInsets == null) {
                mLastInsets = new Rect(insets);
            } else {
                ((Rect) mLastInsets).set(insets);
            }
            requestLayout();
        }
        return true;
    }

    @SuppressLint("DrawAllocation")
    @SuppressWarnings("deprecation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mLastInsets != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                final WindowInsets wi = (WindowInsets) mLastInsets;
                final int childCount = getChildCount();
                for (int i = 0; i < childCount; i++) {
                    final View child = getChildAt(i);
                    if (child.getVisibility() != GONE) {
                        child.dispatchApplyWindowInsets(wi);
                    }
                }
            } else {
                super.fitSystemWindows(new Rect((Rect) mLastInsets));
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}