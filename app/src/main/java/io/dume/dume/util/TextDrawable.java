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

public class TextDrawable extends Drawable {

    private float mTextSize;
    private float mDensity;
    private Paint mBadgePaint;
    private Paint mTextPaint;
    private Rect mTxtRect = new Rect();
    private String mCount = "";
    private boolean mWillDraw = false;

    public TextDrawable(Context context) {
        mTextSize = context.getResources().getDimension(R.dimen.text_over_drawable_size);
        mDensity = context.getResources().getDisplayMetrics().density;

        mBadgePaint = new Paint();
        mBadgePaint.setColor(Color.RED);
        mBadgePaint.setAntiAlias(true);
        mBadgePaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Cairo-Light.ttf"));
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

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
        //float radius = ((Math.min(width, height) / 2) / 2);
        float centerX = width / 2;
        float centerY = height / 2;

        // Draw badge circle.
        //canvas.drawCircle(centerX, centerY, radius, mBadgePaint);

        // Draw badge count text inside the circle.
        mTextPaint.getTextBounds(mCount, 0, mCount.length(), mTxtRect);
        float textHeight = mTxtRect.bottom - mTxtRect.top;
        float textY = centerY + (textHeight / 2f) - (6 * mDensity);
        canvas.drawText(mCount, centerX, textY, mTextPaint);

    }

    public void setString(String str) {
        mCount = str;

        // Only draw a badge if there are notifications.
        mWillDraw = true;
        invalidateSelf();
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
