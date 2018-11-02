package io.dume.dume.common.contactActivity;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class ContactActivityPresenter implements ContactActivityContact.Presenter {

    private ContactActivityContact.View mView;
    private ContactActivityContact.Model mModel;
    private Context context;
    private Activity activity;

    public ContactActivityPresenter(Context context, ContactActivityContact.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (ContactActivityContact.View) context;
        this.mModel = mModel;
    }

    @Override
    public void contactActivityEnqueue() {
        mView.findView();
        mView.initContactActivity();
        mView.configContactActivity();
    }

    @Override
    public void onContactActivityViewIntracted(View view) {

    }
}
