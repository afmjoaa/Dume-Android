package io.dume.dume.common.privacyPolicy;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import io.dume.dume.R;

public class PrivacyPolicyActivity extends AppCompatActivity implements PrivacyPolicyActivityContact.View {

    private PrivacyPolicyActivityContact.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common3_activity_privacy_policy);
        mPresenter = new PrivacyPolicyPresenter(this, new PrivacyPolicyModel());
        mPresenter.privacyPolicyEnqueue();
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
    public void initPrivacyPolicy() {

    }

    @Override
    public void configPrivacyPolicy() {

    }
}
