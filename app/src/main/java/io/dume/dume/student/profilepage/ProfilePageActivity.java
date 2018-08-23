package io.dume.dume.student.profilepage;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.IndicatorStayLayout;

import io.dume.dume.R;

public class ProfilePageActivity extends AppCompatActivity implements ProfilePageContract.View {

    ProfilePageContract.Presenter mPresenter;
    private View decor;
    private IndicatorSeekBar seekbar;
    private IndicatorStayLayout seekbarStaylayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu1_activity_profile_page);
        mPresenter = new ProfilePagePresenter(this, this, new ProfilePageModel());
        mPresenter.profilePageEnqueue();

    }

    @Override
    public void configProfilePage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            decor.setSystemUiVisibility(0);
        }
        seekbar.setIndicatorTextFormat("${PROGRESS}%");

    }

    @Override
    public void initProfilePage() {
        decor = getWindow().getDecorView();
    }

    @Override
    public void findView() {

        seekbar = findViewById(R.id.complete_seekbar);
        seekbarStaylayout = findViewById(R.id.complete_seekbar_staylayout);

    }
}
