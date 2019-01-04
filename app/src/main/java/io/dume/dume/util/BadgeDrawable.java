package io.dume.dume.util;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import io.dume.dume.R;

public class BadgeDrawable extends Drawable {

    private float mTextSize;
    private float mDensity;
    private Paint mBadgePaint;
    private Paint mTextPaint;
    private Rect mTxtRect = new Rect();
    private String mCount = "";
    private boolean mWillDraw = false;

    private float xCenter, yCenter;

    public BadgeDrawable(Context context, float x, float y) {
        mTextSize = context.getResources().getDimension(R.dimen.badge_text_size);
        mDensity = context.getResources().getDisplayMetrics().density;

        mBadgePaint = new Paint();
        mBadgePaint.setColor(Color.RED);
        mBadgePaint.setAntiAlias(true);
        mBadgePaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        xCenter = x;
        yCenter = y;
    }

    @Override
    public void draw(Canvas canvas) {
        if (!mWillDraw) {
            return;
        }

        Rect bounds = getBounds();
        float width = bounds.right - bounds.left;
        float height = bounds.bottom - bounds.top;

        // Position the badge in the top-right quadrant of the icon.
        float radius = ((Math.min(width, height) / 2) / 2);
        float centerX = width - radius + (xCenter * mDensity);
        float centerY = radius - (yCenter * mDensity);

        // Draw badge circle.
        canvas.drawCircle(centerX, centerY, radius, mBadgePaint);

        // Draw badge count text inside the circle.
        mTextPaint.getTextBounds(mCount, 0, mCount.length(), mTxtRect);
        float textHeight = mTxtRect.bottom - mTxtRect.top;
        float textY = centerY + (textHeight / 2f);
        canvas.drawText(mCount, centerX, textY, mTextPaint);
    }

    /*
    Sets the count (i.e notifications) to display.
     */
    public void setCount(int count) {
        if (count > 9) {
            mCount = "9+";
        }else {
            mCount = Integer.toString(count);
        }

        // Only draw a badge if there are notifications.
        mWillDraw = count > 0;
        invalidateSelf();
    }

    public void setChar(char character) {
        mCount = Character.toString(character);

        // Only draw a badge if there are notifications.
        mWillDraw = character == '!' || character == '?' || character == '$';
        invalidateSelf();
    }


    public void setCircleColor(int color) {
        mBadgePaint.setColor(color);
    }

    public void setCircleTextColor(int color) {
        mTextPaint.setColor(color);
    }

    @Override
    public void setAlpha(int alpha) {
        // do nothing
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        // do nothing
    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }
}