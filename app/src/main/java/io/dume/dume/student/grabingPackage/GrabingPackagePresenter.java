package io.dume.dume.student.grabingPackage;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class GrabingPackagePresenter implements GrabingPackageContract.Presenter {

    private GrabingPackageContract.View mView;
    private GrabingPackageContract.Model mModel;
    private Context context;
    private Activity activity;

    public GrabingPackagePresenter(Context context, GrabingPackageContract.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (GrabingPackageContract.View) context;
        this.mModel = mModel;
    }

    @Override
    public void grabingPackagePageEnqueue() {

    }

    @Override
    public void onGrabingPackageViewIntracted(View view) {

    }
}
