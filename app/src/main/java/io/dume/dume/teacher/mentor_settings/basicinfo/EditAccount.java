package io.dume.dume.teacher.mentor_settings.basicinfo;

import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.transitionseverywhere.TransitionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import io.dume.dume.R;
import io.dume.dume.customView.HorizontalLoadView;
import io.dume.dume.student.grabingLocation.GrabingLocationActivity;
import io.dume.dume.util.DatePickerFragment;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.RadioBtnDialogue;

import static io.dume.dume.util.DumeUtils.getUserUID;

public class EditAccount extends AppCompatActivity implements EditContract.View, View.OnClickListener {
    private FloatingActionButton fb;
    private EditContract.Presenter presenter;
    private NestedScrollView mScrollView;
    private int oldScrollYPostion = 0;
    private static final String TAG = "EditAccount";
    private EditText first, last, phone, mail;
    private Spinner gender, religion, marital;
    private ImageView avatar;
    private String avatarUrl = null;
    private HorizontalLoadView loadView;
    private CoordinatorLayout wrapper;
    private EditText pickLocationET;
    public static int REQUEST_LOCATION = 2;
    private String[] genderSelcetionArr;
    private EditText selectGenderEditText;
    private String[] maritalStatusSelcetionArr;
    private String[] religionSelcetionArr;
    private EditText selectMaritalStatusET;
    private EditText selectReligionET;
    private EditText selectBirthDataET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        DumeUtils.configureAppbar(this, "Edit Account");
        presenter = new EditPresenter(this, EditModel.getModelInstance());
        presenter.enqueue();
    }


    @Override
    public void onClick(View view) {
        presenter.onClick(view);
    }

    @Override
    public void configureView() {
        fb = findViewById(R.id.fabEdit);
        first = findViewById(R.id.firstNameEt);
        last = findViewById(R.id.lastNameEdt);
        mail = findViewById(R.id.mailEt);
        phone = findViewById(R.id.phoneEdt);
        gender = findViewById(R.id.genderSp);
        marital = findViewById(R.id.maritalSp);
        religion = findViewById(R.id.religionSp);
        mScrollView = findViewById(R.id.editAccountScrolling);
        avatar = findViewById(R.id.profileImage);
        loadView = findViewById(R.id.loadView);
        wrapper = findViewById(R.id.wrapperAccountEdit);
        loadCarryData();
        pickLocationET = findViewById(R.id.pickAddressET);
        pickLocationET.setOnClickListener(view -> {
            final Intent intent = new Intent(getApplicationContext(), GrabingLocationActivity.class);
            intent.setAction("fromMPA");
            startActivityForResult(intent, REQUEST_LOCATION);
        });
        genderSelcetionArr = this.getResources().getStringArray(R.array.select_gender);
        maritalStatusSelcetionArr = this.getResources().getStringArray(R.array.marital_status);
        religionSelcetionArr = this.getResources().getStringArray(R.array.religion);
        selectGenderEditText = findViewById(R.id.input_gender);
        selectMaritalStatusET = findViewById(R.id.input_marital_status);
        selectReligionET = findViewById(R.id.input_religion);
        selectBirthDataET = findViewById(R.id.input_birth_date);

    }

    private void loadCarryData() {
        Bundle bundle = getIntent().getBundleExtra("user_data");
        if (bundle != null) {
            Glide.with(this).load(bundle.getString("avatar")).apply(new RequestOptions().override(100, 100)).into(avatar);
            first.setText(bundle.getString("first_name"));
            last.setText(bundle.getString("last_name"));
            phone.setText(bundle.getString("phone_number"));
            mail.setText(bundle.getString("email"));
            //selectGenderEditText.setText(bundle.getString("gender"));
            //selectReligionET.setText(bundle.getString("religion"));
            //selectMaritalStatusET.setText(bundle.getString("marital"));
            //selectBirthDataET.setText(bundle.getString("marital"));

            religion.setSelection(Arrays.asList(getResources().getStringArray(R.array.religion)).indexOf(bundle.getString("religion")));
            gender.setSelection(Arrays.asList(getResources().getStringArray(R.array.gender)).indexOf(bundle.getString("gender")));
            marital.setSelection(Arrays.asList(getResources().getStringArray(R.array.marital_status)).indexOf(bundle.getString("marital")));
            Log.e(TAG, "loadCarryData: " + bundle.toString());
            avatarUrl = bundle.getString("avatar");
        }
    }


    @Override
    public void configureCallback() {
        fb.setOnClickListener(this);
        mScrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if (mScrollView.getScrollY() > oldScrollYPostion) {
                fb.show();
            } else if (mScrollView.getScrollY() < oldScrollYPostion || mScrollView.getScrollY() <= 0) {
                fb.hide();
            }
            oldScrollYPostion = mScrollView.getScrollY();
            Log.w(TAG, "onScrollChanged: " + oldScrollYPostion);
        });
        avatar.setOnClickListener(this);
    }

    @Override
    public void snakbar(String msg) {
        Snackbar snak = Snackbar.make(fb, msg, Snackbar.LENGTH_SHORT);
        snak.setAction("Go Back", view -> EditAccount.super.onBackPressed());
        snak.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
        snak.show();
    }

    @Override
    public void toast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public String firstName() {
        return first.getText().toString();
    }

    @Override
    public String lastName() {
        return last.getText().toString();
    }


    @Override
    public String maritalStatus() {
        return marital.getSelectedItem().toString();
    }

    @Override
    public String gmail() {
        return mail.getText().toString();
    }

    @Override
    public String religion() {
        return religion.getSelectedItem().toString();
    }

    @Override
    public String gender() {
        return gender.getSelectedItem().toString();
    }

    @Override
    public String phone() {
        return "+88" + phone.getText().toString();
    }

    @Override
    public void updateImage() {


    }

    @Override
    public void setImage(Uri uri) {
        Glide.with(this).load(uri).into(avatar);
    }

    @Override
    public void setAvatarUrl(String url) {
        this.avatarUrl = url;
    }

    @Override
    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    @Override
    public void enableLoad() {
        fb.setEnabled(false);
        fb.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        if (!loadView.isRunningAnimation()) {
            TransitionManager.beginDelayedTransition(wrapper);
            loadView.setVisibility(View.VISIBLE);
            loadView.startLoading();
        }
    }

    @Override
    public void disableLoad() {
        fb.setEnabled(true);
        fb.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
        if (loadView.isRunningAnimation()) {
            TransitionManager.beginDelayedTransition(wrapper);
            loadView.setVisibility(View.GONE);
            loadView.stopLoading();
        }
    }

    @Override
    public void onLocationUpdate(String location) {
        if (location != null) {
            pickLocationET.setText(location);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public Context getActivtiyContext() {
        return this;
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

    @Override
    public void onGenderClicked() {
        Bundle pRargs = new Bundle();
        pRargs.putString("title", "Select your gender");
        pRargs.putStringArray("radioOptions", genderSelcetionArr);
        RadioBtnDialogue genderBtnDialogue = new RadioBtnDialogue();
        genderBtnDialogue.setItemChoiceListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectGenderEditText.setText(genderSelcetionArr[i]);
            }
        });
        genderBtnDialogue.setArguments(pRargs);
        genderBtnDialogue.show(getSupportFragmentManager(), "genderDialogue");
        selectGenderEditText.setText(genderSelcetionArr[0]);
    }

    @Override
    public void onMaritalStatusClicked() {
        Bundle pRargs = new Bundle();
        pRargs.putString("title", "Select your marital status");
        pRargs.putStringArray("radioOptions", maritalStatusSelcetionArr);
        RadioBtnDialogue genderBtnDialogue = new RadioBtnDialogue();
        genderBtnDialogue.setItemChoiceListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectMaritalStatusET.setText(maritalStatusSelcetionArr[i]);
            }
        });
        genderBtnDialogue.setArguments(pRargs);
        genderBtnDialogue.show(getSupportFragmentManager(), "marital_status_d");
        selectMaritalStatusET.setText(maritalStatusSelcetionArr[0]);
    }

    @Override
    public void onReligionClicked() {
        Bundle pRargs = new Bundle();
        pRargs.putString("title", "Select your religion");
        pRargs.putStringArray("radioOptions", religionSelcetionArr);
        RadioBtnDialogue genderBtnDialogue = new RadioBtnDialogue();
        genderBtnDialogue.setItemChoiceListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectReligionET.setText(religionSelcetionArr[i]);
            }
        });
        genderBtnDialogue.setArguments(pRargs);
        genderBtnDialogue.show(getSupportFragmentManager(), "religion_d");
        selectReligionET.setText(religionSelcetionArr[0]);
    }

    @Override
    public void onBirthDateClicked() {
        //testing the date picker here
        DatePickerFragment thisDatePicker = new DatePickerFragment();
        thisDatePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String currentDateStr = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM).format(c.getTime());
                selectBirthDataET.setText(currentDateStr);
            }
        });
        thisDatePicker.setInitialDate(1997 , 1, 1);
        thisDatePicker.show(getSupportFragmentManager(), "Package_date_picker");
    }


}
