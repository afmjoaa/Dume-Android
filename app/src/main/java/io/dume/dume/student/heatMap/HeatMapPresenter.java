package io.dume.dume.student.heatMap;

import android.app.Activity;
import android.content.Context;
import android.view.View;

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

    }

    @Override
    public void onHeatMapViewIntracted(View view) {

    }
}
