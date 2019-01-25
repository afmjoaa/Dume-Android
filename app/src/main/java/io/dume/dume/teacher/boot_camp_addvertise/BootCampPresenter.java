package io.dume.dume.teacher.boot_camp_addvertise;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import io.dume.dume.R;

public class BootCampPresenter implements BootCampContract.Presenter {
    private static final String TAG = "BootCampPresenter";
    private BootCampContract.Model mModel;
    private BootCampContract.View mView;
    private Context context;
    private Activity activity;

    public BootCampPresenter(Context context, BootCampContract.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (BootCampContract.View) context;
        this.mModel = mModel;
    }

    @Override
    public void bootCampAddEnqueue() {
        mView.findView();
        mView.initBootCampAdd();
        mView.configBootCampAdd();
    }

    @Override
    public void onBootCampViewIntracted(View view) {
        switch (view.getId()) {
            case R.id.start_mentoring_imageView:
                mView.onAnimationImage();
                break;
        }
    }
}
