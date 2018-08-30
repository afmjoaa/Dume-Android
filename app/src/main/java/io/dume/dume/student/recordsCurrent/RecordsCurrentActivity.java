package io.dume.dume.student.recordsCurrent;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import io.dume.dume.R;

public class RecordsCurrentActivity extends AppCompatActivity implements RecordsCurrentContract.View {

    private RecordsCurrentContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_current);
        mPresenter = new RecordsCurrentPresenter(this, new RecordsCurrentModel());
        mPresenter.recordsCurrentEnqueue();
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
    public void configRecordsCurrent() {

    }

    @Override
    public void initRecordsCurrent() {

    }

    @Override
    public void findView() {

    }
}
