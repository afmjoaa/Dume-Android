package io.dume.dume.teacher.mentor_settings.basicinfo;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.GeoPoint;
import com.transitionseverywhere.TransitionManager;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.IndicatorStayLayout;

import java.util.Calendar;

import io.dume.dume.R;
import io.dume.dume.customView.HorizontalLoadView;
import io.dume.dume.student.grabingLocation.GrabingLocationActivity;
import io.dume.dume.util.DatePickerFragment;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.RadioBtnDialogue;

import static io.dume.dume.util.DumeUtils.getAddress;

public class EditAccount extends AppCompatActivity implements EditContract.View, View.OnClickListener {
    private FloatingActionButton fb;
    private EditContract.Presenter presenter;
    private NestedScrollView mScrollView;
    private int oldScrollYPostion = 0;
    private static final String TAG = "EditAccount";
    private EditText first, last, phone, mail;
    private carbon.widget.ImageView avatar;
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
    private carbon.widget.ImageView emptyFirstNameFound;
    private carbon.widget.ImageView emptyLastNameFound;
    private carbon.widget.ImageView emptyEmailFound;
    private carbon.widget.ImageView emptyLocationFound;
    private GeoPoint userLocation = null;
    private IndicatorSeekBar seekbar;
    private IndicatorStayLayout seekbarStaylayout;
    private EditText currentStatusET;


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
        seekbar = findViewById(R.id.complete_seekbar);
        seekbarStaylayout = findViewById(R.id.complete_seekbar_staylayout);
        currentStatusET = findViewById(R.id.input_current_status);

        mScrollView = findViewById(R.id.editAccountScrolling);
        avatar = findViewById(R.id.profileImage);
        loadView = findViewById(R.id.loadView);
        wrapper = findViewById(R.id.wrapperAccountEdit);

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
        emptyFirstNameFound = findViewById(R.id.empty_fn_found);
        emptyLastNameFound = findViewById(R.id.empty_ln_found);
        emptyEmailFound = findViewById(R.id.empty_email_found);
        emptyLocationFound = findViewById(R.id.empty_address_found);
        loadCarryData();
    }

    private void loadCarryData() {
        Bundle bundle = getIntent().getBundleExtra("user_data");
        if (bundle != null) {
            final RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.avatar);
            Glide.with(this).load(bundle.getString("avatar")).apply(requestOptions.override(100, 100)).into(avatar);
            first.setText(bundle.getString("first_name"));
            last.setText(bundle.getString("last_name"));
            phone.setText(bundle.getString("phone_number"));
            mail.setText(bundle.getString("email"));

            selectReligionET.setText(bundle.getString("religion"));
            selectGenderEditText.setText(bundle.getString("gender"));
            selectMaritalStatusET.setText(bundle.getString("marital"));
            Log.e(TAG, "loadCarryData: " + bundle.toString());
            avatarUrl = bundle.getString("avatar");
            //TODO get the location data as well
        }
    }


    @Override
    public void configureCallback() {
        fb.setOnClickListener(this);
        seekbar.setIndicatorTextFormat("${PROGRESS}%");
        /*mScrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if (mScrollView.getScrollY() > oldScrollYPostion) {
                fb.show();
            } else if (mScrollView.getScrollY() < oldScrollYPostion || mScrollView.getScrollY() <= 0) {
                fb.hide();
            }
            oldScrollYPostion = mScrollView.getScrollY();
            Log.w(TAG, "onScrollChanged: " + oldScrollYPostion);
        });*/
        avatar.setOnClickListener(this);

        first.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("")) {
                    emptyFirstNameFound.setVisibility(View.GONE);
                } else {
                    emptyFirstNameFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    emptyFirstNameFound.setVisibility(View.VISIBLE);
                }
            }
        });
        last.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("")) {
                    emptyLastNameFound.setVisibility(View.GONE);
                } else {
                    emptyLastNameFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    emptyLastNameFound.setVisibility(View.VISIBLE);
                }
            }
        });
        mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("")) {
                    emptyEmailFound.setVisibility(View.GONE);
                } else {
                    emptyEmailFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    emptyEmailFound.setVisibility(View.VISIBLE);
                }
            }
        });
        pickLocationET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("")) {
                    emptyLocationFound.setVisibility(View.GONE);
                } else {
                    emptyLocationFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    emptyLocationFound.setVisibility(View.VISIBLE);
                }
            }
        });
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
        return selectMaritalStatusET.getText().toString();
    }

    @Override
    public String gmail() {
        return mail.getText().toString();
    }

    @Override
    public String getLocation() {
        return pickLocationET.getText().toString();
    }

    @Override
    public String religion() {
        return selectReligionET.getText().toString();
    }

    @Override
    public String gender() {
        return selectGenderEditText.getText().toString();
    }

    @Override
    public String phone() {
        return "+88" + phone.getText().toString();
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
    public GeoPoint getCurrentAddress() {
        return userLocation;
    }

    @Override
    public void setCurrentAddress(GeoPoint geoPoint) {
        userLocation = geoPoint;
        final String address = getAddress(this, geoPoint.getLatitude(), geoPoint.getLongitude());
        pickLocationET.setText(address);
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
        thisDatePicker.setInitialDate(1997, 1, 1);
        thisDatePicker.show(getSupportFragmentManager(), "Package_date_picker");
    }

    @Override
    public String getBirthDate() {
        return selectBirthDataET.getText().toString();
    }

    @Override
    public void invalidFound(String Name) {
        switch (Name) {
            case "first":
                emptyFirstNameFound.setVisibility(View.VISIBLE);
                break;
            case "last":
                emptyLastNameFound.setVisibility(View.VISIBLE);
                break;
            case "email":
                emptyEmailFound.setVisibility(View.VISIBLE);
                break;
            case "location":
                emptyLocationFound.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void generatePercent() {
        setProfileComPercent("40");
        if (getAvatarUrl() != null && !getAvatarUrl().equals("")){
            Float profileComPercent = Float.parseFloat(getProfileComPercent()) + 10;
            setProfileComPercent(profileComPercent.toString());
        }
        if(getCurrentAddress() != null){
            Float profileComPercent = Float.parseFloat(getProfileComPercent()) + 10;
            setProfileComPercent(profileComPercent.toString());
        }
        if(getCurrentStatus() != null && !getCurrentStatus().equals("")){
            Float profileComPercent = Float.parseFloat(getProfileComPercent()) + 10;
            setProfileComPercent(profileComPercent.toString());
        }
       /* if(qualification){
            Float profileComPercent = Float.parseFloat(getProfileComPercent()) + 10;
            setProfileComPercent(profileComPercent.toString());
        }*/

       //to be continued
    }

    @Override
    public void setProfileComPercent(String num) {
        if (num.equals("")) {
            seekbar.setProgress(60);
        } else {
            seekbar.setProgress(Float.parseFloat(num));
        }
    }


    @Override
    public String getProfileComPercent() {
        return Integer.toString(seekbar.getProgress());
    }

    @Override
    public String getCurrentStatus() {
        return String.valueOf(currentStatusET.getText());
    }

    @Override
    public void setCurrentStatus(String currentStatus) {
        currentStatusET.setText(currentStatus);
    }

}
