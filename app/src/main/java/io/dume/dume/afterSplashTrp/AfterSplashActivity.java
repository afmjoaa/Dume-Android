package io.dume.dume.afterSplashTrp;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import io.dume.dume.R;
import io.dume.dume.afterSplashTrp.adapter.AfterSplashPagerAdapter;
import io.dume.dume.afterSplashTrp.adapter.DemoCardFragment;
import me.relex.circleindicator.CircleIndicator;

public class AfterSplashActivity extends AppCompatActivity implements DemoCardFragment.OnActionListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trp_activity_after_splash);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new AfterSplashPagerAdapter(this, getSupportFragmentManager()));
        pager.setPageMargin((int) getResources().getDimension(R.dimen.card_padding) / 4);
        pager.setOffscreenPageLimit(3);

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);

    }

    @Override
    public void onAction(int id) {
        switch (id) {
            case AfterSplashPagerAdapter.ID_DEFAULT:
                break;
            case AfterSplashPagerAdapter.ID_STYLED:
                break;
            case AfterSplashPagerAdapter.ID_CUSTOM_LAYOUT:
                break;
            case AfterSplashPagerAdapter.ID_CUSTOM_VIEW_HOLDER:
                break;
            case AfterSplashPagerAdapter.ID_CUSTOM_CONTENT:
                //CustomMediaMessagesActivity.open(this);
                break;
        }
    }

}
