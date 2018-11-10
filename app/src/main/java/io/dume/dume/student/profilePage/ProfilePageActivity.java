package io.dume.dume.student.profilePage;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.IndicatorStayLayout;

import java.util.ArrayList;

import io.dume.dume.R;
import io.dume.dume.student.grabingLocation.GrabingLocationActivity;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.RadioBtnDialogue;

public class ProfilePageActivity extends CustomStuAppCompatActivity implements ProfilePageContract.View,
        CompoundButton.OnCheckedChangeListener {

    ProfilePageContract.Presenter mPresenter;
    private View decor;
    private IndicatorSeekBar seekbar;
    private IndicatorStayLayout seekbarStaylayout;

    AutoCompleteTextView email;
    private AppCompatCheckBox cgpaCheckBox;
    private AppCompatCheckBox gpaCheckBox;
    private EditText selectGenderTextView;
    private String[] genderSelcetionArr;
    protected static int fromFlag = 102;
    private EditText previousResultTextView;
    private EditText currentAddressTextView;
    private String[] gpaOptionsArr;
    private String[] cgpaOptionsArr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu1_activity_profile_page);
        setActivityContext(this, fromFlag);
        settingStatusBarTransparent();
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
        cgpaCheckBox.setOnCheckedChangeListener(this);
        gpaCheckBox.setOnCheckedChangeListener(this);

    }

    @Override
    public void findView() {
        email = findViewById(R.id.input_email);
        seekbar = findViewById(R.id.complete_seekbar);
        seekbarStaylayout = findViewById(R.id.complete_seekbar_staylayout);
        cgpaCheckBox = findViewById(R.id.cgpa_checkbox);
        gpaCheckBox = findViewById(R.id.gpa_checkbox);
        selectGenderTextView = findViewById(R.id.input_gerder);
        currentAddressTextView = findViewById(R.id.input_current_address);
        previousResultTextView = findViewById(R.id.input_previous_result);
        //arrays
        genderSelcetionArr = this.getResources().getStringArray(R.array.select_gender);
        gpaOptionsArr = this.getResources().getStringArray(R.array.gpa_options);
        cgpaOptionsArr = this.getResources().getStringArray(R.array.cgpa_options);

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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = compoundButton.getId();
        if (id == R.id.cgpa_checkbox) {
            if (compoundButton.isChecked()) {
                gpaCheckBox.setChecked(false);
            } else {
                gpaCheckBox.setChecked(true);
            }
        } else if (id == R.id.gpa_checkbox) {
            if (compoundButton.isChecked()) {
                cgpaCheckBox.setChecked(false);
            } else {
                cgpaCheckBox.setChecked(true);
            }
        }
    }

    @Override
    public void onPreviousResultClicked() {
        Bundle args = new Bundle();
        RadioBtnDialogue previousResultDialogue = new RadioBtnDialogue();
        if(gpaCheckBox.isChecked()){
            args.putString("title", "Select your GPA");
            args.putStringArray("radioOptions", gpaOptionsArr);
            previousResultDialogue.setItemChoiceListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    previousResultTextView.setText(String.format("%s (GPA)", gpaOptionsArr[i]));
                }
            });
        }else{
            args.putString("title", "Select your CGPA");
            args.putStringArray("radioOptions", cgpaOptionsArr);
            previousResultDialogue.setItemChoiceListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    previousResultTextView.setText(String.format("%s (CGPA)", cgpaOptionsArr[i]));
                }
            });
        }
        previousResultDialogue.setArguments(args);
        previousResultDialogue.show(getSupportFragmentManager(), "genderDialogue");
    }

    @Override
    public void onCurrentAddressClicked() {
        startActivity(new Intent(this, GrabingLocationActivity.class).setAction("fromPPA"));
    }

    @Override
    public void onGenderClicked() {
        Bundle pRargs = new Bundle();
        pRargs.putString("title", "Select your GPA");
        pRargs.putStringArray("radioOptions", genderSelcetionArr);
        RadioBtnDialogue genderBtnDialogue = new RadioBtnDialogue();
        genderBtnDialogue.setItemChoiceListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectGenderTextView.setText(genderSelcetionArr[i]);
            }
        });
        genderBtnDialogue.setArguments(pRargs);
        genderBtnDialogue.show(getSupportFragmentManager(), "genderDialogue");
    }


    public void onProfilePageClicked(View view) {
        mPresenter.onProfileViewIntracted(view);
    }
}
