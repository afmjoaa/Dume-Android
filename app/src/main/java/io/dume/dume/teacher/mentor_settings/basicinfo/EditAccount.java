package io.dume.dume.teacher.mentor_settings.basicinfo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.transitionseverywhere.TransitionManager;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.IndicatorStayLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dume.dume.R;
import io.dume.dume.customView.HorizontalLoadView;
import io.dume.dume.student.grabingLocation.GrabingLocationActivity;
import io.dume.dume.student.pojo.BaseAppCompatActivity;
import io.dume.dume.teacher.adapters.AcAdapter;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.homepage.TeacherDataStore;
import io.dume.dume.teacher.mentor_settings.academic.AcademicActivity;
import io.dume.dume.teacher.pojo.Academic;
import io.dume.dume.util.DatePickerFragment;
import io.dume.dume.util.DumeUtils;

import static io.dume.dume.util.DumeUtils.getAddress;

public class EditAccount extends BaseAppCompatActivity implements EditContract.View, View.OnClickListener {
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
    private GeoPoint userLocation = null;
    private IndicatorSeekBar seekbar;
    private IndicatorStayLayout seekbarStaylayout;
    private EditText currentStatusET;
    private TextView profileCompleteTextView;
    private View dividerHorizontalUnderPCT;
    @BindView(R.id.academicRV)
    RecyclerView academicRV;
    @BindView(R.id.add_saved_place_layout)
    RelativeLayout addAcademicBtn;
    private AcAdapter acAdapter;
    private String databaseComPercent;
    private boolean isChanged = false;
    private RelativeLayout pHostRelative;
    private BottomSheetDialog mCancelBottomSheetDialog;
    private View cancelsheetRootView;
    private int genderCheckedItem = 0;
    private int maritalCheckedItem = 0;
    private int religionCheckedItem = 0;
    private Dialog dialog;
    private TextInputLayout emptyFirstNameLayout;
    private TextInputLayout emptyLastNameLayout;
    private TextInputLayout emptyEmailLayout;
    private TextInputLayout emptyLocationLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        setActivityContext(this, fromFlag);
        ButterKnife.bind(this);
        DumeUtils.configureAppbar(this, "Edit Account");
        presenter = new EditPresenter(this, this, EditModel.getModelInstance(this));
        presenter.enqueue();
        TransitionManager.beginDelayedTransition(pHostRelative);
        if (getIntent().getAction() != null) {
            if (getIntent().getAction().equals("scroll_down")) {
                mScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        mScrollView.fling(0);
                        //mScrollView.smoothScrollTo(0, mScrollView.getBottom());
                        mScrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        }
        testingCustomDialogue();
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
        profileCompleteTextView = findViewById(R.id.profile_complete_text);
        dividerHorizontalUnderPCT = findViewById(R.id.divider_horizontal);

        mScrollView = findViewById(R.id.editAccountScrolling);
        avatar = findViewById(R.id.profileImage);
        loadView = findViewById(R.id.loadView);
        wrapper = findViewById(R.id.parent_coor_layout);

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
        emptyFirstNameLayout = findViewById(R.id.input_layout_firstname);
        emptyLastNameLayout = findViewById(R.id.input_layout_lastname);
        emptyEmailLayout = findViewById(R.id.input_layout_email);
        emptyLocationLayout = findViewById(R.id.input_layout_c_address);
        pHostRelative = findViewById(R.id.percent_host_relative);
    }

    @Override
    public void onDataLoad(DocumentSnapshot documentSnapshot) {
        TeacherDataStore.getInstance().setDocumentSnapshot(documentSnapshot.getData());
        TeacherDataStore.getInstance().setSnapshot(documentSnapshot);
        databaseComPercent = documentSnapshot.getString("pro_com_%");
        final RequestOptions requestOptions = new RequestOptions();
        String gender = documentSnapshot.getString("gender");
        if (gender != null) {
            if (gender.equals("Male") || gender.equals("")) {
                requestOptions.placeholder(R.drawable.set_display_pic);
            } else {
                requestOptions.placeholder(R.drawable.set_display_pic);
            }
        }
        Glide.with(getApplicationContext()).load(documentSnapshot.getString("avatar")).apply(requestOptions.override(100, 100).placeholder(R.drawable.set_display_pic)).into(avatar);
        first.setText(documentSnapshot.getString("first_name") == null ? "" : documentSnapshot.getString("first_name"));
        last.setText(documentSnapshot.getString("last_name") == null ? "" : documentSnapshot.getString("last_name"));
        phone.setText(documentSnapshot.getString("phone_number") == null ? "" : documentSnapshot.getString("phone_number"));
        mail.setText(documentSnapshot.getString("email") == null ? "" : documentSnapshot.getString("email"));
        selectReligionET.setText(documentSnapshot.getString("religion") == null ? "" : documentSnapshot.getString("religion"));
        selectGenderEditText.setText(documentSnapshot.getString("gender") == null ? "" : documentSnapshot.getString("gender"));
        selectMaritalStatusET.setText(documentSnapshot.getString("marital") == null ? "" : documentSnapshot.getString("marital"));
        pickLocationET.setText(documentSnapshot.getGeoPoint("location") == null ? "" : documentSnapshot.getGeoPoint("location").toString());
        currentStatusET.setText(documentSnapshot.getString("current_status_icon") == null ? "" : documentSnapshot.getString("current_status_icon"));
        selectBirthDataET.setText(documentSnapshot.getString("birth_date") == null ? "" : documentSnapshot.getString("birth_date"));

        Log.e(TAG, "loadCarryData: " + documentSnapshot.toString());
        avatarUrl = documentSnapshot.getString("avatar") == null ? "" : documentSnapshot.getString("avatar");
        List<Academic> arrayList = getAcademics(documentSnapshot);
        acAdapter = new AcAdapter(this, arrayList);
        academicRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        academicRV.setAdapter(acAdapter);
    }

    @NonNull
    @Override
    public List<Academic> getAcademics(DocumentSnapshot documentSnapshot) {
        List<Academic> arrayList = new ArrayList<>();
        Map<String, Map<String, Object>> academicMap = (Map<String, Map<String, Object>>) documentSnapshot.get("academic");
        if (academicMap != null && academicMap.size() > 0) {
            for (Map.Entry<String, Map<String, Object>> entry : academicMap.entrySet()) {
                String level = (String) entry.getValue().get("level");
                String institution = (String) entry.getValue().get("institution");
                String degree = (String) entry.getValue().get("degree");
                String from_year = (String) entry.getValue().get("from_year");
                String to_year = (String) entry.getValue().get("to_year");
                String result = (String) entry.getValue().get("result");
                Academic academic = new Academic(level, institution, degree, from_year, to_year, result);
                arrayList.add(academic);
            }
        }
        return arrayList;
    }

    @Override
    public void configureCallback() {
        fb.setOnClickListener(this);
        seekbar.setIndicatorTextFormat("${PROGRESS}%");

        //initializing the bottomSheet dialogue
        mCancelBottomSheetDialog = new BottomSheetDialog(this);
        cancelsheetRootView = this.getLayoutInflater().inflate(R.layout.custom_bottom_sheet_dialogue_cancel, null);
        mCancelBottomSheetDialog.setContentView(cancelsheetRootView);
        TextView mainText = mCancelBottomSheetDialog.findViewById(R.id.main_text);
        TextView subText = mCancelBottomSheetDialog.findViewById(R.id.sub_text);
        Button cancelYesBtn = mCancelBottomSheetDialog.findViewById(R.id.cancel_yes_btn);
        Button cancelNoBtn = mCancelBottomSheetDialog.findViewById(R.id.cancel_no_btn);
        if (mainText != null && subText != null && cancelYesBtn != null && cancelNoBtn != null) {
            mainText.setText("Discard Changes ?");
            cancelYesBtn.setText("Yes, Discard");
            cancelNoBtn.setText("No");
            subText.setText("Discard local changes & go back ...");
            cancelNoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCancelBottomSheetDialog.dismiss();
                }
            });

            cancelYesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    enableLoad();
                    mCancelBottomSheetDialog.dismiss();
                    someThingChanged(false);
                    onBackPressed();
                }
            });
        }

        avatar.setOnClickListener(this);

        first.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("")) {
                    emptyFirstNameLayout.setError(null);
                } else {
                    emptyFirstNameLayout.setError("Field can't be Empty.");
                }
                someThingChanged(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    emptyFirstNameLayout.setError("Field can't be Empty.");
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
                    emptyLastNameLayout.setError(null);
                } else {
                    emptyLastNameLayout.setError("Field can't be Empty.");
                }
                someThingChanged(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    emptyLastNameLayout.setError("Field can't be Empty.");
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
                    emptyEmailLayout.setError(null);
                } else {
                    emptyEmailLayout.setError("Field can't be Empty.");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    emptyEmailLayout.setError("Field can't be Empty.");
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
                    emptyLocationLayout.setError(null);
                } else {
                    emptyLocationLayout.setError("Field can't be Empty.");
                }
                someThingChanged(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    emptyLocationLayout.setError("Field can't be Empty.");
                }
                generatePercent();
            }
        });

        currentStatusET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                someThingChanged(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
                generatePercent();
            }
        });

        currentStatusET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    currentStatusET.setHint("Lecturer in MIST");
                } else {
                    currentStatusET.setHint("");
                }
            }
        });
        selectGenderEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                someThingChanged(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
                generatePercent();
            }
        });
        selectReligionET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                someThingChanged(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
                generatePercent();
            }
        });
        selectMaritalStatusET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                someThingChanged(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
                generatePercent();
            }
        });
        selectBirthDataET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                someThingChanged(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
                generatePercent();
            }
        });

    }

    @Override
    public void snakbar(String msg) {
        Snackbar snak = Snackbar.make(fb, msg, Snackbar.LENGTH_LONG);
        snak.setAction("Go Back", view -> onBackPressed());
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
        Glide.with(this).load(uri).apply(new RequestOptions().override(100, 100).placeholder(R.drawable.set_display_pic)).into(avatar);
    }

    @Override
    public void setImageUrlString(String url) {
        Glide.with(getApplicationContext()).load(url).apply(new RequestOptions().override(100, 100).placeholder(R.drawable.set_display_pic)).into(avatar);
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
            loadView.setVisibility(View.VISIBLE);
            loadView.startLoading();
        }
    }

    @Override
    public void disableLoad() {
        fb.setEnabled(true);
        fb.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
        if (loadView.isRunningAnimation()) {
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
    public void onBackPressed() {
        if (isChanged) {
            discardDialogue();
        } else {
            //show dialogue of discard changes
            super.onBackPressed();
        }
    }

    @Override
    public void discardDialogue() {
        mCancelBottomSheetDialog.show();
    }

    @Override
    public void onGenderClicked() {
        String gender = gender();
        for (int i = 0; i < genderSelcetionArr.length; i++) {
            if (gender.equals(genderSelcetionArr[i])) {
                genderCheckedItem = i;
                break;
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(EditAccount.this), R.style.RadioDialogTheme);
        builder.setTitle("Select your gender").setSingleChoiceItems(genderSelcetionArr, genderCheckedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                genderCheckedItem = i;
                selectGenderEditText.setText(genderSelcetionArr[i]);
            }
        }).setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(ProfilePageActivity.this, "Set", Toast.LENGTH_SHORT).show();
                //nothing to do here
                if(gender().equals("")){
                    genderCheckedItem = 0;
                    selectGenderEditText.setText(genderSelcetionArr[0]);
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onMaritalStatusClicked() {
        String marital = maritalStatus();
        for (int i = 0; i < maritalStatusSelcetionArr.length; i++) {
            if (marital.equals(maritalStatusSelcetionArr[i])) {
                maritalCheckedItem = i;
                break;
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(EditAccount.this), R.style.RadioDialogTheme);
        builder.setTitle("Select your marital status").setSingleChoiceItems(maritalStatusSelcetionArr, maritalCheckedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                maritalCheckedItem = i;
                selectMaritalStatusET.setText(maritalStatusSelcetionArr[i]);
            }
        }).setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(ProfilePageActivity.this, "Set", Toast.LENGTH_SHORT).show();
                //nothing to do here
                if(maritalStatus().equals("")){
                    maritalCheckedItem = 0;
                    selectMaritalStatusET.setText(maritalStatusSelcetionArr[0]);
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onReligionClicked() {
        String religion = religion();
        for (int i = 0; i < religionSelcetionArr.length; i++) {
            if (religion.equals(religionSelcetionArr[i])) {
                religionCheckedItem = i;
                break;
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(EditAccount.this), R.style.RadioDialogTheme);
        builder.setTitle("Select your religion").setSingleChoiceItems(religionSelcetionArr, religionCheckedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                religionCheckedItem = i;
                selectReligionET.setText(religionSelcetionArr[i]);
            }
        }).setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(ProfilePageActivity.this, "Set", Toast.LENGTH_SHORT).show();
                //nothing to do here
                if(religion().equals("")){
                    religionCheckedItem = 0;
                    selectReligionET.setText(religionSelcetionArr[0]);
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
                emptyFirstNameLayout.setError("Field can't be empty.");
                break;
            case "last":
                emptyLastNameLayout.setError("Field can't be empty.");
                break;
            case "email":
                emptyEmailLayout.setError("Field can't be empty.");
                break;
            case "location":
                emptyLocationLayout.setError("Field can't be empty.");
                break;
        }
    }

    @Override
    public String generatePercent() {
        setProfileComPercent("40");
        if (getAvatarUrl() != null && !getAvatarUrl().equals("")) {
            Float profileComPercent = Float.parseFloat(getProfileComPercent()) + 10;
            setProfileComPercent(profileComPercent.toString());
        }
        if (getCurrentAddress() != null) {
            Float profileComPercent = Float.parseFloat(getProfileComPercent()) + 10;
            setProfileComPercent(profileComPercent.toString());
        }
        if (getCurrentStatus() != null && !getCurrentStatus().equals("")) {
            Float profileComPercent = Float.parseFloat(getProfileComPercent()) + 10;
            setProfileComPercent(profileComPercent.toString());
        }
        if (acAdapter != null && acAdapter.getRecyItemCount() > 0) {
            Float profileComPercent = Float.parseFloat(getProfileComPercent()) + 10;
            setProfileComPercent(profileComPercent.toString());
        }
        if (gender() != null && !gender().equals("")) {
            Float profileComPercent = Float.parseFloat(getProfileComPercent()) + 5;
            setProfileComPercent(profileComPercent.toString());
        }
        if (religion() != null && !religion().equals("")) {
            Float profileComPercent = Float.parseFloat(getProfileComPercent()) + 5;
            setProfileComPercent(profileComPercent.toString());
        }
        if (maritalStatus() != null && !maritalStatus().equals("")) {
            Float profileComPercent = Float.parseFloat(getProfileComPercent()) + 5;
            setProfileComPercent(profileComPercent.toString());
        }
        if (getBirthDate() != null && !getBirthDate().equals("")) {
            Float profileComPercent = Float.parseFloat(getProfileComPercent()) + 5;
            setProfileComPercent(profileComPercent.toString());
        }


        if (databaseComPercent != null && !databaseComPercent.equals(getProfileComPercent())) {
            someThingChanged(true);
        }

        String profileComPercent = getProfileComPercent();
        if (profileComPercent != null) {
            if (profileComPercent.equals("100")) {
                initProfileCompleteView();
            } else {
                disInitProfileCompleteView();
            }
        }
        return getProfileComPercent();
        //to be continued
    }

    @Override
    public void setProfileComPercent(String num) {
        if (num.equals("")) {
            seekbar.setProgress(40);
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

    @Override
    public void initProfileCompleteView() {
        seekbarStaylayout.setVisibility(View.GONE);
        profileCompleteTextView.setVisibility(View.VISIBLE);
        dividerHorizontalUnderPCT.setVisibility(View.VISIBLE);
    }

    @Override
    public void disInitProfileCompleteView() {
        seekbarStaylayout.setVisibility(View.VISIBLE);
        profileCompleteTextView.setVisibility(View.GONE);
        dividerHorizontalUnderPCT.setVisibility(View.GONE);
    }

    @Override
    public void addQualifiaction() {
        if (isChanged) {
            presenter.fabClicked(new TeacherContract.Model.Listener<Void>() {
                @Override
                public void onSuccess(Void list) {
                    disableLoad();
                    final Intent intent = new Intent(EditAccount.this, AcademicActivity.class);
                    intent.setAction("add");
                    startActivityForResult(intent, 1234);
                }

                @Override
                public void onError(String msg) {
                    toast(msg);
                }
            });
        } else {
            final Intent intent = new Intent(this, AcademicActivity.class);
            intent.setAction("add");
            startActivityForResult(intent, 1234);
        }
        //this.startActivity(intent);
    }

    @Override
    public void modifyQualification() {

    }

    @Override
    public void someThingChanged(boolean b) {
        isChanged = b;
    }

    @Override
    public void updateAcademics(DocumentSnapshot documentSnapshot) {
        acAdapter.update(getAcademics(documentSnapshot));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_info, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.action_help) {
            //show the dialogue
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void testingCustomDialogue() {
        // custom dialog
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_obligation_dialogue);

        //all find view here
        Button dismissBtn = dialog.findViewById(R.id.dismiss_btn);
        TextView dialogText = dialog.findViewById(R.id.dialog_text);
        carbon.widget.ImageView dialogImage = dialog.findViewById(R.id.dialog_image);
        dialogText.setGravity(Gravity.START);

        dialogImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_info_icon_green));
        dialogText.setText(R.string.edit_account_info);

        dismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

}
