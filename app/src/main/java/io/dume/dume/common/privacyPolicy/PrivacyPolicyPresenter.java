package io.dume.dume.common.privacyPolicy;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class PrivacyPolicyPresenter implements PrivacyPolicyActivityContact.Presenter {

    private PrivacyPolicyActivityContact.View mView;
    private PrivacyPolicyActivityContact.Model mModel;
    private Context context;
    private Activity activity;

    public PrivacyPolicyPresenter(Context context, PrivacyPolicyActivityContact.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (PrivacyPolicyActivityContact.View) context;
        this.mModel = mModel;
    }

    @Override
    public void privacyPolicyEnqueue() {
        mView.findView();
        mView.initPrivacyPolicy();
        mView.configPrivacyPolicy();
    }

    @Override
    public void onPrivacyPolicyViewIntracted(View view) {

    }
}
