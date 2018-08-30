package io.dume.dume.student.recordsRejected;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import io.dume.dume.R;

public class RecordsRejectedActivity extends AppCompatActivity implements RecordsRejectedContract.View {

    private RecordsRejectedContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu10_activity_records_rejected);
        mPresenter = new RecordsRejectedPresenter(this, new RecordsRejectedModel());
        mPresenter.recordsRejectedEnqueue();
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
    public void configRecordsRejected() {

    }

    @Override
    public void initRecordsRejected() {

    }

    @Override
    public void findView() {

    }
}
