package io.dume.dume.student.studentPayment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import io.dume.dume.R;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;

import static io.dume.dume.util.DumeUtils.configureAppbar;

public class StudentPaymentActivity extends CustomStuAppCompatActivity implements StudentPaymentContract.View {

    private StudentPaymentContract.Presenter mPresenter;
    private static final int fromFlag = 17;
    private static final String TAG = "StudentPaymentActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stunav_activity2_student_payment);
        setActivityContext(this, fromFlag);
        mPresenter = new StudentPaymentPresenter(this, new StudentPaymentModel());
        mPresenter.studentPaymentEnqueue();
        configureAppbar(this, "Payment");

    }

    @Override
    public void configStudentPayment() {

    }

    @Override
    public void initStudentPayment() {

    }

    @Override
    public void findView() {

    }
}
