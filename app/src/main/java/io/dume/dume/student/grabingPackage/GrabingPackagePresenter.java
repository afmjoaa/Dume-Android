package io.dume.dume.student.grabingPackage;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import io.dume.dume.R;

public class GrabingPackagePresenter implements GrabingPackageContract.Presenter {

    private GrabingPackageContract.View mView;
    private Context context;
    private Activity activity;

    public GrabingPackagePresenter(Context context) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (GrabingPackageContract.View) context;
    }

    @Override
    public void grabingPackagePageEnqueue() {
        mView.findView();
        mView.initGrabingPackagePage();
        mView.configGrabingPackagePage();
    }

    @Override
    public void onGrabingPackageViewIntracted(View view) {
        if (view.getId() == R.id.package_search_btn) {
            mView.executeSearchActivity();
        }
    }
}
