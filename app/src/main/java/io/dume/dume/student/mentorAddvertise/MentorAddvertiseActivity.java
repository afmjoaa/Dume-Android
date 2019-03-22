package io.dume.dume.student.mentorAddvertise;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import io.dume.dume.R;
import io.dume.dume.bootCamp.bootCampHomePage.BootCampHomePageActivity;
import io.dume.dume.model.DumeModel;
import io.dume.dume.obligation.foreignObli.PayActivity;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.teacher.homepage.TeacherActivtiy;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.util.DumeUtils;

import static io.dume.dume.util.DumeUtils.animateImage;
import static io.dume.dume.util.DumeUtils.configureAppbar;

public class MentorAddvertiseActivity extends CustomStuAppCompatActivity implements MentorAddvertiseContact.View {

    private MentorAddvertiseContact.Presenter mPresenter;
    private static final String TAG = "MentorAddvertiseActivit";
    private static final int fromFlag = 16;
    private ImageView startMentoringImageView;
    private Button switchToMentor;
    private BottomSheetDialog mCancelBottomSheetDialog;
    private View cancelsheetRootView;

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
        switchToMentor = findViewById(R.id.switch_to_mentor_btn);
    }

    @Override
    public void initMentorAddvertise() {
        //initializing the bottomSheet dialogue
        mCancelBottomSheetDialog = new BottomSheetDialog(this);
        cancelsheetRootView = this.getLayoutInflater().inflate(R.layout.custom_bottom_sheet_dialogue_cancel, null);
        mCancelBottomSheetDialog.setContentView(cancelsheetRootView);
    }

    @Override
    public void configMentorAddvertise() {
        switchToMentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchProfileDialog(DumeUtils.TEACHER);
            }
        });
    }

    @Override
    public void onAnimationImage() {
        animateImage(startMentoringImageView);
    }

    public void onMentorAdvertiseViewClicked(View view) {
        mPresenter.onMentorAddvertiseViewIntracted(view);
    }

    @Override
    public void switchProfileDialog(String identify) {
        TextView mainText = mCancelBottomSheetDialog.findViewById(R.id.main_text);
        TextView subText = mCancelBottomSheetDialog.findViewById(R.id.sub_text);
        Button cancelYesBtn = mCancelBottomSheetDialog.findViewById(R.id.cancel_yes_btn);
        Button cancelNoBtn = mCancelBottomSheetDialog.findViewById(R.id.cancel_no_btn);
        if (mainText != null && subText != null && cancelYesBtn != null && cancelNoBtn != null) {
            mainText.setText("Switch Profile ?");
            cancelYesBtn.setText("Yes, Switch");
            cancelNoBtn.setText("No");
            if (identify.equals(DumeUtils.TEACHER)) {
                subText.setText("Switch from student to mentor profile ...");
            } else {
                subText.setText("Switch from student to boot camp profile ...");
            }

            cancelNoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCancelBottomSheetDialog.dismiss();
                }
            });

            cancelYesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCancelBottomSheetDialog.dismiss();
                    showProgress();
                    if (identify.equals(DumeUtils.TEACHER)) {
                        new DumeModel(context).switchAcount(DumeUtils.TEACHER, new TeacherContract.Model.Listener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                gotoMentorProfile();
                            }

                            @Override
                            public void onError(String msg) {
                                hideProgress();
                                flush("Network error 101 !!");
                            }
                        });
                    } else {
                        new DumeModel(context).switchAcount(DumeUtils.BOOTCAMP, new TeacherContract.Model.Listener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                gotoBootCampHomePage();
                            }

                            @Override
                            public void onError(String msg) {
                                hideProgress();
                                flush("Network error 101 !!");
                            }
                        });
                    }
                }
            });
        }
        mCancelBottomSheetDialog.show();
    }

    public void gotoMentorProfile() {
        Intent intent = new Intent(this, TeacherActivtiy.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    public void gotoBootCampHomePage() {
        startActivity(new Intent(this, BootCampHomePageActivity.class));
        finish();
    }

    private void flush(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
