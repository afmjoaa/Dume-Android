package io.dume.dume.student.recordsAccepted;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import io.dume.dume.R;

public class RecordsAcceptedActivity extends AppCompatActivity implements RecordsAcceptedContract.View {

    private RecordsAcceptedContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu12_activity_records_accepted);
        mPresenter = new RecordsAcceptedPresenter(this, new RecordsAcceptedModel());
        mPresenter.recordsAcceptedEnqueue();
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
    public void configRecordsAccepted() {

    }

    @Override
    public void initRecordsAccepted() {

    }

    @Override
    public void findView() {

    }
}
