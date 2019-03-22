package io.dume.dume.student.freeCashBack;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import io.dume.dume.R;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.student.studentHelp.StudentHelpActivity;

import static io.dume.dume.util.DumeUtils.animateImage;
import static io.dume.dume.util.DumeUtils.configureAppbar;

public class FreeCashBackActivity extends CustomStuAppCompatActivity implements FreeCashBackContact.View {

    private FreeCashBackContact.Presenter mPresenter;
    private static final int fromFlag = 15;
    private static final String TAG = "FreeCashBackActivity";
    private ImageView freeCashbackImageView;
    private TextView howInviteWork;
    private Button freeCashBtn;

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
        howInviteWork = findViewById(R.id.how_invite_works);
        freeCashBtn = findViewById(R.id.free_cashback);
    }

    @Override
    public void initFreeCashBack() {

    }

    @Override
    public void configFreeCashBack() {
        howInviteWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StudentHelpActivity.class);
                intent.setAction("faq");
                startActivity(intent);
            }
        });
        freeCashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inviteAFriendCalled();
            }
        });
    }

    private void inviteAFriendCalled() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TITLE, "Dume");
            String strShareMessage = "Check out Dume, It's simple just share your skill and earn money.Get it for free from\n\n";
            strShareMessage = strShareMessage + "https://play.google.com/store/apps/details?id=" + getPackageName();
            i.putExtra(Intent.EXTRA_TEXT, strShareMessage);
            startActivity(Intent.createChooser(i, "Share via"));
        } catch (Exception e) {
            Log.e(TAG, "inviteAFriendCalled: " + e.toString());
        }
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
