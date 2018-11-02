package io.dume.dume.common.inboxActivity;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class InboxActivityPresenter implements InboxActivityContact.Presenter {

    private InboxActivityContact.View mView;
    private InboxActivityContact.Model mModel;
    private Context context;
    private Activity activity;

    public InboxActivityPresenter(Context context, InboxActivityContact.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (InboxActivityContact.View) context;
        this.mModel = mModel;
    }

    @Override
    public void inboxEnqueue() {
        mView.findView();
        mView.initInbox();
        mView.configInbox();
    }

    @Override
    public void onInboxViewIntracted(View view) {

    }
}
