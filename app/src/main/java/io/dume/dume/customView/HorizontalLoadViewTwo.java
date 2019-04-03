package io.dume.dume.customView;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import io.dume.dume.R;
import io.dume.dume.util.DumeUtils;

public class HorizontalLoadViewTwo extends View implements ValueAnimator.AnimatorUpdateListener {

    private Paint paint;
    private Rect rectangle;
    private Activity activity;
    private ValueAnimator valueAnimator;
    private boolean isRunning = false;
    private int widthRight;
    private int mDisplayWidth;
    private static final String TAG = "HorizontalLoadView";

    public HorizontalLoadViewTwo(Context context) {
        super(context);
        init(context, null);
    }

    public HorizontalLoadViewTwo(Context context, @android.support.annotation.Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attr) {
        activity = (Activity) context;
        int[] mDisplayWidth = DumeUtils.getScreenSize(context);
        int startPoint = (int) (14 * (getResources().getDisplayMetrics().density));
        int selfWidth = (int) (20 * (getResources().getDisplayMetrics().density));
        valueAnimator = ValueAnimator.ofInt(startPoint, (mDisplayWidth[0] - (startPoint+selfWidth)));
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setDuration(750);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
       /* if (attr != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attr, R.styleable.HorizontalLoadView);
            paint.setColor(typedArray.getColor(R.styleable.HorizontalLoadView_rect_color, Color.BLACK));
            typedArray.recycle();
        } else*/ paint.setColor(context.getResources().getColor(R.color.transparent));
        widthRight = (int) (15 * (getResources().getDisplayMetrics().density));
        rectangle = new Rect(0, 0, widthRight, (int) (8 * (getResources().getDisplayMetrics().density)));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(rectangle, paint);
    }


    public void startLoading() {
        valueAnimator.addUpdateListener(this);
        valueAnimator.start();
        isRunning = true;
    }

    public void stopLoading() {
        if (valueAnimator != null) {
            valueAnimator.end();
            isRunning = false;
        }
    }


    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        int intermediateValue = (int) valueAnimator.getAnimatedValue();
        this.setTranslationX((float) intermediateValue);
        this.requestLayout();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (valueAnimator != null) {
            valueAnimator.end();
            isRunning = false;
        }
    }

    public boolean isRunningAnimation() {
        return isRunning;
    }
}
