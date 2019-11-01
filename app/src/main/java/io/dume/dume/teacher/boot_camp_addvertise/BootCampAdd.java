package io.dume.dume.teacher.boot_camp_addvertise;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import io.dume.dume.R;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.student.studentHelp.StudentHelpActivity;

import static io.dume.dume.util.DumeUtils.animateImage;
import static io.dume.dume.util.DumeUtils.configureAppbar;

public class BootCampAdd extends CustomStuAppCompatActivity implements BootCampContract.View {
    private static final String TAG = "BootCampAdd";
    private BootCampContract.Presenter mPresenter;
    private static final int fromFlag = 117;
    private ImageView startMentoringImageView;
    private Button startCounching;
    private Button startTakingCounching;


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
        startCounching = findViewById(R.id.start_couching);
        startTakingCounching = findViewById(R.id.start_taking_couching);
    }

    @Override
    public void initBootCampAdd() {

    }

    @Override
    public void configBootCampAdd() {
        startCounching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StudentHelpActivity.class);
                intent.setAction("whats_new");
                startActivity(intent);
            }
        });
        startTakingCounching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StudentHelpActivity.class);
                intent.setAction("whats_new");
                startActivity(intent);
            }
        });
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
