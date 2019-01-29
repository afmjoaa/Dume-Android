package io.dume.dume.obligation.foreignObli;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;
import java.util.Objects;

import io.dume.dume.R;
import io.dume.dume.teacher.homepage.TeacherContract;

public class PayPresenter implements PayContact.Presenter {

    private PayContact.View mView;
    private PayContact.Model mModel;
    private Context context;
    private Activity activity;
    private static final String TAG = "ProfilePagePresenter";

    public PayPresenter(Context context, PayContact.View mView, PayContact.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = mView;
        this.mModel = mModel;
        mModel.setInstance((PayModel) mModel);
    }

    @Override
    public void payActivityEnqueue() {
        mView.findView();
        mView.initPayActivity();
        mView.configPayActivity();

        mModel.addShapShotListener((documentSnapshot, e) -> {
            if (documentSnapshot != null) {
                mView.setDocumentSnapshot(documentSnapshot);
                Boolean foreignObligation = documentSnapshot.getBoolean("foreign_obligation");
                if (foreignObligation) {
                    mView.obligationFound();
                } else {
                    mView.noObligationFound();
                }
            } else {
                mView.flush("Does not found any user");
            }
        });

    }

    @Override
    public void onPayActivityViewIntracted(View view) {
        switch (view.getId()) {
            case R.id.previous_login:
                mView.signOutAndLoginPrevious();
                break;
            case R.id.recheck_obligation:
                mView.showProgress();
                mModel.getResultSnapShot(new TeacherContract.Model.Listener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot list) {
                        mView.hideProgress();
                        Boolean foreignObligation = list.getBoolean("foreign_obligation");
                        if (foreignObligation) {
                            mView.obligationFound();
                        } else {
                            mView.noObligationFound();
                        }
                    }
                    @Override
                    public void onError(String msg) {
                        mView.hideProgress();
                        mView.flush("Network error !!");
                    }
                });
                break;
            case R.id.sign_out:
                mView.signOut();
                break;
        }
    }
}
