package io.dume.dume.student.recordsPending;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import io.dume.dume.R;

public class RecordsPendingActivity extends AppCompatActivity implements RecordsPendingContract.View{

    private RecordsPendingContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu9_activity_records_pendding);
        mPresenter = new RecordsPendingPresenter(this, new RecordsPendingModel());
        mPresenter.recordsPendingEnqueue();
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
    public void configRecordsPending() {

    }

    @Override
    public void initRecordsPending() {

    }

    @Override
    public void findView() {

    }
}
