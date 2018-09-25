package io.dume.dume.student.freeCashBack;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class FreeCashBackPresenter implements FreeCashBackContact.Presenter{

    private FreeCashBackContact.View mView;
    private FreeCashBackContact.Model mModel;
    private Context context;
    private Activity activity;

    public FreeCashBackPresenter(Context context, FreeCashBackContact.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (FreeCashBackContact.View) context;
        this.mModel = mModel;
    }

    @Override
    public void freeCashBackEnqueue() {
        mView.findView();
        mView.initFreeCashBack();
        mView.configFreeCashBack();
    }

    @Override
    public void onFreeCashBackViewIntracted(View view) {

    }
}
