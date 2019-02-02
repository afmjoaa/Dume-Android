package io.dume.dume.common.contactActivity;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import java.util.List;

import io.dume.dume.teacher.homepage.TeacherContract;

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

        mModel.readContact("", new TeacherContract.Model.Listener<List<ContactData>>() {
            @Override
            public void onSuccess(List<ContactData> list) {
                mView.loadRV(list);
            }

            @Override
            public void onError(String msg) {
                mView.flush(msg);
            }
        });
    }

    @Override
    public void onContactActivityViewIntracted(View view) {

    }
}
