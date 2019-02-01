package io.dume.dume.teacher.boot_camp_addvertise;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import io.dume.dume.R;
import io.dume.dume.student.mentorAddvertise.MentorAddvertiseModel;
import io.dume.dume.student.mentorAddvertise.MentorAddvertisePresenter;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;

import static io.dume.dume.util.DumeUtils.animateImage;
import static io.dume.dume.util.DumeUtils.configureAppbar;

public class BootCampAdd extends CustomStuAppCompatActivity implements BootCampContract.View {
    private static final String TAG = "BootCampAdd";
    private BootCampContract.Presenter mPresenter;
    private static final int fromFlag = 117;
    private ImageView startMentoringImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boot_camp_add);
        setActivityContext(this, fromFlag);
        mPresenter = new BootCampPresenter(this, new BootCampModel());
        mPresenter.bootCampAddEnqueue();
        findLoadView();
        configureAppbar(this, "Start Boot-Camp", true);
    }

    public void onBootCampViewClicked(View view) {
        mPresenter.onBootCampViewIntracted(view);
    }

    @Override
    public void findView() {
        startMentoringImageView = findViewById(R.id.start_mentoring_imageView);
    }

    @Override
    public void initBootCampAdd() {

    }

    @Override
    public void configBootCampAdd() {

    }

    @Override
    public void onAnimationImage() {
        animateImage(startMentoringImageView);
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