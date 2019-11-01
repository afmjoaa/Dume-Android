package io.dume.dume.bootCamp.student_addvertise;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import io.dume.dume.R;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;

import static io.dume.dume.util.DumeUtils.animateImage;
import static io.dume.dume.util.DumeUtils.configureAppbar;

public class StudentAddvertise extends CustomStuAppCompatActivity implements StudentAddvertiseContact.View{
    private static final String TAG = "StudentAddvertise";
    private static final int fromFlag = 118;
    private StudentAddvertiseContact.Presenter mPresenter;
    private ImageView enhanceSkillIV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_addvertise);
        setActivityContext(this, fromFlag);
        mPresenter = new StudentAddvertisePresenter(this, new StudentAddvertiseModel());
        mPresenter.studentAddvertiseEnqueue();
        findLoadView();
        configureAppbar(this, "Enhance your skill", true);
    }

    @Override
    public void findView() {
        enhanceSkillIV = findViewById(R.id.enhance_skill_imageview);
    }

    @Override
    public void initStudentAddvertise() {

    }

    @Override
    public void configStudentAddvertise() {

    }

    @Override
    public void onAnimationImage() {
        animateImage(enhanceSkillIV);
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

    public void onStudentAdvertiseViewClicked(View view) {
        mPresenter.onStudentAddvertiseViewIntracted(view);
    }
}
