package io.dume.dume.student.freeCashBack;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import io.dume.dume.R;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;

import static io.dume.dume.util.DumeUtils.animateImage;
import static io.dume.dume.util.DumeUtils.configureAppbar;

public class FreeCashBackActivity extends CustomStuAppCompatActivity implements FreeCashBackContact.View {

    private FreeCashBackContact.Presenter mPresenter;
    private static final int fromFlag = 15;
    private static final String TAG = "FreeCashBackActivity";
    private ImageView freeCashbackImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu14_activity_free_cash_back);
        setActivityContext(this, fromFlag);
        findLoadView();
        mPresenter = new FreeCashBackPresenter(this, new FreeCashBackModel());
        mPresenter.freeCashBackEnqueue();
        configureAppbar(this, "Free cash-back");
    }

    @Override
    public void findView() {
        freeCashbackImageView = findViewById(R.id.free_cashback_imageView);
    }

    @Override
    public void initFreeCashBack() {
        showProgress();
    }

    @Override
    public void configFreeCashBack() {

    }

    @Override
    public void onAnimationImage() {
        animateImage(freeCashbackImageView);
    }

    public void onFreeCashBackViewClicked(View view) {
        mPresenter.onFreeCashBackViewIntracted(view);
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
