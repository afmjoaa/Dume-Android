package io.dume.dume.util;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class RatingAnimation extends Animation {
    private static final String TAG = "RatingAnimation";

    private MaterialRatingBar mDecimalRatingBars;
    private final int myProgress;

    public RatingAnimation(MaterialRatingBar mDecimalRatingBars) {
        this.mDecimalRatingBars = mDecimalRatingBars;
        setDuration(mDecimalRatingBars.getNumStars()
                * 4 * 200);
        setInterpolator(new LinearInterpolator());
        setRepeatCount(Animation.ABSOLUTE);
        myProgress = mDecimalRatingBars.getProgress();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int progress = Math.round(interpolatedTime * myProgress);
        //Log.e(TAG, "applyTransformation: " + mDecimalRatingBars.getMax() + "::" + mDecimalRatingBars.getProgress() + "::" + mDecimalRatingBars.getRating());
        mDecimalRatingBars.setProgress(progress);
        /*for (RatingBar ratingBar : mDecimalRatingBars) {
        }*/
    }

    @Override
    public boolean willChangeTransformationMatrix() {
        return false;
    }

    @Override
    public boolean willChangeBounds() {
        return false;
    }
}