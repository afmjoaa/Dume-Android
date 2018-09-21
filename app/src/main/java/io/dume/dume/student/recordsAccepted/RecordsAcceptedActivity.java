package io.dume.dume.student.recordsAccepted;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import io.dume.dume.R;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.util.DumeUtils;

public class RecordsAcceptedActivity extends CustomStuAppCompatActivity implements RecordsAcceptedContract.View {

    private RecordsAcceptedContract.Presenter mPresenter;
    private static final int fromFlag = 22;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu12_activity_records_accepted);
        setActivityContext(this, fromFlag);
        mPresenter = new RecordsAcceptedPresenter(this, new RecordsAcceptedModel());
        mPresenter.recordsAcceptedEnqueue();
        DumeUtils.configureAppbar(this, "Accepted Requests");


    }

    @Override
    public void findView() {

    }

    @Override
    public void initRecordsAccepted() {

    }

    @Override
    public void configRecordsAccepted() {

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
