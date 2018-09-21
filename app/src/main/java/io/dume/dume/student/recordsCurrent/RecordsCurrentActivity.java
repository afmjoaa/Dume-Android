package io.dume.dume.student.recordsCurrent;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import io.dume.dume.R;
import io.dume.dume.student.pojo.CusStuAppComMapActivity;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.student.pojo.MyGpsLocationChangeListener;
import io.dume.dume.util.DumeUtils;

public class RecordsCurrentActivity extends CustomStuAppCompatActivity implements RecordsCurrentContract.View,
        MyGpsLocationChangeListener{

    private RecordsCurrentContract.Presenter mPresenter;
    private static final int fromFlag = 23;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu13_activity_records_current);
        setActivityContext(this, fromFlag);
        mPresenter = new RecordsCurrentPresenter(this, new RecordsCurrentModel());
        mPresenter.recordsCurrentEnqueue();
        DumeUtils.configureAppbar(this, "Current Records");


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

    @Override
    public void onMyGpsLocationChanged(Location location) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_records_default, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_help:
                //Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
