package io.dume.dume.student.studentPayment;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import io.dume.dume.R;

public class StudentPaymentPresenter implements StudentPaymentContract.Presenter {

    private StudentPaymentContract.View mView;
    private StudentPaymentContract.Model mModel;
    private Context context;
    private Activity activity;

    public StudentPaymentPresenter(Context context, StudentPaymentContract.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (StudentPaymentContract.View) context;
        this.mModel = mModel;
    }

    @Override
    public void studentPaymentEnqueue() {
        mView.findView();
        mView.configurePaymentInformation();
        mView.initStudentPayment();
        mView.configStudentPayment();
    }

    @Override
    public void onStudentPaymentIntracted(View view) {
        switch (view.getId()) {
            case R.id.add_promotion_layout:
                mView.onAddPromoCodeApplied();
                break;
            case R.id.promotion_relative_layout:
                mView.onViewPromotionsClicked();
                break;
            case R.id.recycler_container_layout:
                mView.onTransactionHistoryClicked();
                break;
            case R.id.obligation_or_claim_layout:
                mView.onObligtionClaimClicked();
                break;
        }
    }
}
