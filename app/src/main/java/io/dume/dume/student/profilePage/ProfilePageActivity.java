package io.dume.dume.student.profilePage;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.IndicatorStayLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import carbon.widget.AutoCompleteEditText;
import io.dume.dume.R;
import io.dume.dume.util.DumeUtils;

public class ProfilePageActivity extends AppCompatActivity implements ProfilePageContract.View {

    ProfilePageContract.Presenter mPresenter;
    private View decor;
    private IndicatorSeekBar seekbar;
    private IndicatorStayLayout seekbarStaylayout;

    AutoCompleteTextView email;

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


        ArrayList<String> emailAddress = getEmailAddress();
        if (emailAddress.size() != 0) {
            email.setThreshold(1);
            email.setAdapter(new ArrayAdapter<String>(this, R.layout.item_layout_suggestion, R.id.suggetionTextView, emailAddress));
        }

    }

    @Override
    public void initProfilePage() {
        decor = getWindow().getDecorView();
    }

    @Override
    public void findView() {
        email = findViewById(R.id.input_email);
        seekbar = findViewById(R.id.complete_seekbar);
        seekbarStaylayout = findViewById(R.id.complete_seekbar_staylayout);


    }

    private ArrayList<String> getEmailAddress() {
        ArrayList<String> emailArray = new ArrayList<>();
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (DumeUtils.isValidEmailAddress(account.name)) {
                emailArray.add(account.name);
            }
        }
        return emailArray;

    }
}
