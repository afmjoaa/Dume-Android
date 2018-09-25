package io.dume.dume.student.freeCashBack;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import io.dume.dume.R;

public class FreeCashBackActivity extends AppCompatActivity implements FreeCashBackContact.View {

    private FreeCashBackContact.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu14_activity_free_cash_back);
        mPresenter = new FreeCashBackPresenter(this, new FreeCashBackModel());
        mPresenter.freeCashBackEnqueue();
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
    public void findView() {

    }

    @Override
    public void initFreeCashBack() {

    }

    @Override
    public void configFreeCashBack() {

    }
}
