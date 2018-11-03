package io.dume.dume.afterSplashTrp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import io.dume.dume.R;
import io.dume.dume.afterSplashTrp.adapter.AfterSplashPagerAdapter;
import io.dume.dume.afterSplashTrp.adapter.DemoCardFragment;
import io.dume.dume.auth.auth.AuthActivity;
import li.yohan.parallax.ParallaxViewPager;
import me.relex.circleindicator.CircleIndicator;

public class AfterSplashActivity extends AppCompatActivity implements DemoCardFragment.OnActionListener {


    private static final String TAG = "Bal";
    private ParallaxViewPager mPager;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_splash_parallax_layout);
        makeFullScreen();
        mPager = findViewById(R.id.pager);
        mPager.setAdapter(new AfterSplashPagerAdapter(this, getSupportFragmentManager()));
        CircleIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        findViewById(R.id.after_splash_start_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button button = (Button) view;
                button.setEnabled(false);
                button.setBackgroundColor(getResources().getColor(R.color.green));
                startActivity(new Intent(getApplicationContext(), AuthActivity.class));
            }
        });
    }

    @Override
    public void onAction(int id) {

    }

    public void makeFullScreen() {
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}


/*class Adapter extends RecyclingPagerAdapter {
    private LayoutInflater inflater;
    private Context context;

    public Adapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = new TextView(container.getContext());
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup
                    .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ((TextView) convertView).setGravity(Gravity.CENTER);
            ((TextView) convertView).setTextSize(TypedValue.COMPLEX_UNIT_SP, 60);
            ((TextView) convertView).setTypeface(null, Typeface.BOLD);
            ((TextView) convertView).setTextColor(Color.parseColor("#ffffff"));
            convertView.setLayoutParams(layoutParams);
        }
        ((TextView) convertView).setText(position + "");
        return convertView;
    }

    @Override
    public int getCount() {
        return 5;
    }

}*/

