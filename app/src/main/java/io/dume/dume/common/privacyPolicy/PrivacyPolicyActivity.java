package io.dume.dume.common.privacyPolicy;

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

public class PrivacyPolicyActivity extends CustomStuAppCompatActivity implements PrivacyPolicyActivityContact.View {

    private PrivacyPolicyActivityContact.Presenter mPresenter;
    private static final String TAG = "PrivacyPolicyActivity";
    private static final int fromFlag = 14;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common3_activity_privacy_policy);
        setActivityContext(this, fromFlag);
        mPresenter = new PrivacyPolicyPresenter(this, new PrivacyPolicyModel());
        mPresenter.privacyPolicyEnqueue();
        findLoadView();
        configureAppbar(this, "Privacy policy");

    }

    @Override
    public void findView() {

    }

    @Override
    public void initPrivacyPolicy() {

    }

    @Override
    public void configPrivacyPolicy() {

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
