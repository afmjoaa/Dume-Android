package io.dume.dume.util;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.ProgressBar;

public class ProgressAnimation extends Animation {
    private static final String TAG = "RatingAnimation";

    private ProgressBar mDecimalRatingBars;
    private final int myProgress;

    public ProgressAnimation(ProgressBar mDecimalRatingBars) {
        this.mDecimalRatingBars = mDecimalRatingBars;
        setDuration(2000);
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