package io.dume.dume.student.studentPayment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import io.dume.dume.R;

public class StudentPaymentActivity extends AppCompatActivity implements StudentPaymentContract.View {

    private StudentPaymentContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stunav_activity2_student_payment);
        mPresenter = new StudentPaymentPresenter(this, new StudentPaymentModel());
        mPresenter.studentPaymentEnqueue();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
