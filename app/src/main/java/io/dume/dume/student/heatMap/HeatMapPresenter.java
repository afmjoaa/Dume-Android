package io.dume.dume.student.heatMap;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import io.dume.dume.R;

public class HeatMapPresenter implements HeatMapContract.Presenter {

    private HeatMapContract.View mView;
    private HeatMapContract.Model mModel;
    private Context context;
    private Activity activity;

    public HeatMapPresenter(Context context, HeatMapContract.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (HeatMapContract.View) context;
        this.mModel = mModel;
    }

    @Override
    public void heatMapEnqueue() {
        mView.findView();
        mView.initHeatMap();
        mView.configHeatMap();

    }

    @Override
    public void onHeatMapViewIntracted(View view) {
        switch (view.getId()) {
            case R.id.view_musk:
                mView.viewMuskClicked();
                break;
            case R.id.fab:
                mView.onCenterCurrentLocation();
                break;


        }
    }
}
