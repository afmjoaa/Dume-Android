package io.dume.dume.commonActivity.aboutUs;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class AboutUsPresenter implements AboutUsContact.Presenter {

    private AboutUsContact.View mView;
    private AboutUsContact.Model mModel;
    private Context context;
    private Activity activity;

    public AboutUsPresenter(Context context, AboutUsContact.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (AboutUsContact.View) context;
        this.mModel = mModel;
    }

    @Override
    public void aboutUsEnqueue() {
        mView.findView();
        mView.initAboutUs();
        mView.configAboutUs();
    }

    @Override
    public void onAboutUsViewIntracted(View view) {

    }
}
