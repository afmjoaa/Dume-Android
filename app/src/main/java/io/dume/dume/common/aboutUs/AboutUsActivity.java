package io.dume.dume.common.aboutUs;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import io.dume.dume.R;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;

import static io.dume.dume.util.DumeUtils.configureAppbar;

public class AboutUsActivity extends CustomStuAppCompatActivity implements AboutUsContact.View {

    private AboutUsContact.Presenter mPresenter;
    private static final String TAG = "AboutUsActivity";
    private static final int fromFlag = 13;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common2_activity_about_us);
        setActivityContext(this, fromFlag);
        mPresenter = new AboutUsPresenter(this, new AboutUsModel());
        mPresenter.aboutUsEnqueue();
        configureAppbar(this, "About us", true);
        findLoadView();
    }

    @Override
    public void findView() {

    }

    @Override
    public void initAboutUs() {

    }

    @Override
    public void configAboutUs() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
