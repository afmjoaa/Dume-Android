package io.dume.dume.student.mentorAddvertise;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import io.dume.dume.R;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;

import static io.dume.dume.util.DumeUtils.animateImage;
import static io.dume.dume.util.DumeUtils.configureAppbar;

public class MentorAddvertiseActivity extends CustomStuAppCompatActivity implements MentorAddvertiseContact.View {

    private MentorAddvertiseContact.Presenter mPresenter;
    private static final String TAG = "MentorAddvertiseActivit";
    private static final int fromFlag = 16;
    private ImageView startMentoringImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu15_activity_mentor_addvertise);
        setActivityContext(this, fromFlag);
        mPresenter = new MentorAddvertisePresenter(this, new MentorAddvertiseModel());
        mPresenter.mentorAddvertiseEnqueue();
        findLoadView();
        configureAppbar(this, "Start mentoring", true);

    }

    @Override
    public void findView() {
        startMentoringImageView = findViewById(R.id.start_mentoring_imageView);
    }

    @Override
    public void initMentorAddvertise() {

    }

    @Override
    public void configMentorAddvertise() {

    }

    @Override
    public void onAnimationImage() {
        animateImage(startMentoringImageView);
    }

    public void onMentorAdvertiseViewClicked(View view) {
        mPresenter.onMentorAddvertiseViewIntracted(view);
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
