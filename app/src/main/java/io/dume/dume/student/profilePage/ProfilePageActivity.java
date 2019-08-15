package io.dume.dume.student.profilePage;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.IndicatorStayLayout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import carbon.widget.ImageView;
import id.zelory.compressor.Compressor;
import io.dume.dume.R;
import io.dume.dume.student.grabingLocation.GrabingLocationActivity;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.FileUtil;
import io.dume.dume.util.RadioBtnDialogue;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static io.dume.dume.util.DumeUtils.getAddress;
import static io.dume.dume.util.DumeUtils.getUserUID;
import static io.dume.dume.util.DumeUtils.hideKeyboard;
import static io.dume.dume.util.DumeUtils.showKeyboard;

public class ProfilePageActivity extends CustomStuAppCompatActivity implements ProfilePageContract.View,
        CompoundButton.OnCheckedChangeListener {

    ProfilePageContract.Presenter mPresenter;
    private View decor;
    private IndicatorSeekBar seekbar;
    private IndicatorStayLayout seekbarStaylayout;
    private AutoCompleteTextView email;
    private AppCompatCheckBox cgpaCheckBox;
    private AppCompatCheckBox gpaCheckBox;
    private EditText selectGenderTextView;
    private String[] genderSelcetionArr;
    protected static int fromFlag = 102;
    private EditText previousResultTextView;
    private EditText currentAddressTextView;
    private String[] gpaOptionsArr;
    private String[] cgpaOptionsArr;
    private ImageView discardImageView;
    private ImageView doneImageView;
    private Button doneBtn;
    private Button discardBtn;
    private TextView profileUserName;
    private TextView profileUserNumber;
    private carbon.widget.ImageView profileUserDP;
    private ProgressBar loadingSpiner;
    private Uri outputFileUri = null;
    private int LOCATION_REQUEST_CODE = 2222;
    private int IMAGE_RESULT_CODE = 3333;
    private AutoCompleteTextView inputLastName;
    private AutoCompleteTextView inputFirstName;
    private AutoCompleteTextView inputCurrentStatus;
    private String avatarString = null;
    private GeoPoint userLocation = null;
    public String action = "null";
    private Uri selectedImageUri = null;
    private TextView profileCompleteTextView;
    private View dividerHorizontalUnderPCT;
    private ImageView emailEmptyFound;
    private ImageView lnEmptyFound;
    private ImageView fnEmptyFound;
    private File compressedImage;
    private File actualImage = null;
    private int genderCheckedItem = 0;
    private int PResultCheckedItem = 0;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu1_activity_profile_page);
        setActivityContext(this, fromFlag);
        action = getIntent().getAction();
        settingStatusBarTransparent();
        ProfilePageModel mModel = new ProfilePageModel(this);
        mPresenter = new ProfilePagePresenter(this, this, mModel);
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

        inputFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("")) {
                    fnEmptyFound.setVisibility(View.GONE);
                } else {
                    fnEmptyFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    fnEmptyFound.setVisibility(View.VISIBLE);
                }
            }
        });

        inputLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("")) {
                    lnEmptyFound.setVisibility(View.GONE);
                } else {
                    lnEmptyFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    lnEmptyFound.setVisibility(View.VISIBLE);
                }
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("")) {
                    emailEmptyFound.setVisibility(View.GONE);
                } else {
                    emailEmptyFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    emailEmptyFound.setVisibility(View.VISIBLE);
                }
            }
        });

        currentAddressTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateChangesClicked();
            }
        });
        inputCurrentStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateChangesClicked();
            }
        });
        previousResultTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateChangesClicked();
            }
        });
        selectGenderTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateChangesClicked();
            }
        });
    }

    @Override
    public void initProfilePage() {
        decor = getWindow().getDecorView();
        cgpaCheckBox.setOnCheckedChangeListener(this);
        gpaCheckBox.setOnCheckedChangeListener(this);
        profileUserDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (avatarString == null || avatarString.equals("")) {
                    openImageIntent();
                } else {
                    PopupMenu popup = new PopupMenu(context, view);
                    popup.inflate(R.menu.menu_dp_long_click);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            int id = menuItem.getItemId();
                            switch (id) {
                                case R.id.action_update:
                                    openImageIntent();
                                    break;
                                case R.id.action_remove:
                                    avatarString = null;
                                    compressedImage = null;
                                    setAvatar(null);
                                    //profileUserDP.setImageDrawable(getResources().getDrawable(R.drawable.avatar));
                                    updateChangesClicked();
                                    flush("Display pic Removed");
                                    break;
                            }
                            return false;
                        }
                    });
                    popup.show();
                }
            }
        });
        inputCurrentStatus.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    inputCurrentStatus.setHint("RUMC HSC Candidate...");
                    showKeyboard(activity);
                } else {
                    inputCurrentStatus.setHint("");
                }
            }
        });

    }


    @Override
    public void findView() {
        seekbar = findViewById(R.id.complete_seekbar);
        seekbarStaylayout = findViewById(R.id.complete_seekbar_staylayout);
        cgpaCheckBox = findViewById(R.id.cgpa_checkbox);
        gpaCheckBox = findViewById(R.id.gpa_checkbox);
        //arrays
        genderSelcetionArr = this.getResources().getStringArray(R.array.select_gender);
        gpaOptionsArr = this.getResources().getStringArray(R.array.gpa_options);
        cgpaOptionsArr = this.getResources().getStringArray(R.array.cgpa_options);
        discardImageView = findViewById(R.id.discard_imageview);
        doneImageView = findViewById(R.id.done_imageview);
        discardBtn = findViewById(R.id.profile_discard_btn);
        doneBtn = findViewById(R.id.profile_update_btn);
        profileUserName = findViewById(R.id.Profile_page_user_name);
        profileUserNumber = findViewById(R.id.Profile_page_user_phone_no);
        profileUserDP = findViewById(R.id.imageView1);
        loadingSpiner = findViewById(R.id.loading_spinner);

        inputFirstName = findViewById(R.id.input_firstname);
        inputLastName = findViewById(R.id.input_lastname);
        email = findViewById(R.id.input_email);
        inputCurrentStatus = findViewById(R.id.input_current_status);
        currentAddressTextView = findViewById(R.id.input_current_address);
        previousResultTextView = findViewById(R.id.input_previous_result);
        selectGenderTextView = findViewById(R.id.input_gerder);
        profileCompleteTextView = findViewById(R.id.profile_complete_text);
        dividerHorizontalUnderPCT = findViewById(R.id.divider_horizontal);
        //
        fnEmptyFound = findViewById(R.id.empty_fn_found);
        lnEmptyFound = findViewById(R.id.empty_ln_found);
        emailEmptyFound = findViewById(R.id.empty_email_found);

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
        String previousResult = getPreviousResult();
        if (gpaCheckBox.isChecked()) {
            for (int i = 0; i < gpaOptionsArr.length; i++) {
                if(i== gpaOptionsArr.length-1){
                    PResultCheckedItem = 0;
                }
                if (previousResult.equals(String.format("%s (GPA)", gpaOptionsArr[i]))) {
                    PResultCheckedItem = i;
                    break;
                }
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(ProfilePageActivity.this), R.style.RadioDialogTheme);
            builder.setTitle("Select your GPA").setSingleChoiceItems(gpaOptionsArr, PResultCheckedItem, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    PResultCheckedItem = i;
                    previousResultTextView.setText(String.format("%s (GPA)", gpaOptionsArr[i]));
                }
            }).setPositiveButton("Select", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //Toast.makeText(ProfilePageActivity.this, "Set", Toast.LENGTH_SHORT).show();
                    //nothing to do here
                    if(getPreviousResult().equals("")){
                        PResultCheckedItem = 0;
                        previousResultTextView.setText(String.format("%s (GPA)", gpaOptionsArr[0]));
                    }
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }else{
            for (int i = 0; i < cgpaOptionsArr.length; i++) {
                if(i== cgpaOptionsArr.length-1){
                    PResultCheckedItem = 0;
                }
                if (previousResult.equals(String.format("%s (CGPA)", cgpaOptionsArr[i]))) {
                    PResultCheckedItem = i;
                    break;
                }

            }
            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(ProfilePageActivity.this), R.style.RadioDialogTheme);
            builder.setTitle("Select your CGPA").setSingleChoiceItems(cgpaOptionsArr, PResultCheckedItem, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    PResultCheckedItem = i;
                    previousResultTextView.setText(String.format("%s (CGPA)", cgpaOptionsArr[i]));
                }
            }).setPositiveButton("Select", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //Toast.makeText(ProfilePageActivity.this, "Set", Toast.LENGTH_SHORT).show();
                    //nothing to do here
                    if(getPreviousResult().equals("")){
                        PResultCheckedItem = 0;
                        previousResultTextView.setText(String.format("%s (CGPA)", cgpaOptionsArr[0]));
                    }
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    public void onCurrentAddressClicked() {
        startActivityForResult(new Intent(this, GrabingLocationActivity.class).setAction("fromPPA"), LOCATION_REQUEST_CODE);
    }

    @Override
    public void onGenderClicked() {
        String gender = getGender();
        for (int i = 0; i < genderSelcetionArr.length; i++) {
            if (gender.equals(genderSelcetionArr[i])) {
                genderCheckedItem = i;
                break;
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(ProfilePageActivity.this), R.style.RadioDialogTheme);
        builder.setTitle("Select your gender").setSingleChoiceItems(genderSelcetionArr, genderCheckedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                genderCheckedItem = i;
                selectGenderTextView.setText(genderSelcetionArr[i]);
            }
        }).setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(ProfilePageActivity.this, "Set", Toast.LENGTH_SHORT).show();
                //nothing to do here
                if(getGender().equals("")){
                    genderCheckedItem = 0;
                    selectGenderTextView.setText(genderSelcetionArr[0]);
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void onProfilePageClicked(View view) {
        mPresenter.onProfileViewIntracted(view);
    }

    private void openImageIntent() {
        // Determine Uri of camera image to save.
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
        root.mkdirs();
        final String fname = "stu_" + getUserUID() + ".jpg";
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));
        startActivityForResult(chooserIntent, IMAGE_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_RESULT_CODE) {
                final boolean isCamera;
                if (data == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }

                if (isCamera) {
                    selectedImageUri = outputFileUri;
                } else {
                    selectedImageUri = data == null ? null : data.getData();
                }
                if (selectedImageUri != null) {
                    showSpiner();
                    try {
                        actualImage = FileUtil.from(this, selectedImageUri);
                    } catch (Exception e) {
                        actualImage = null;
                        e.printStackTrace();
                    }
                    Glide.with(ProfilePageActivity.this).load(selectedImageUri).apply(new RequestOptions().override(100, 100).placeholder(R.drawable.set_display_pic)).into(profileUserDP);
                    if(actualImage ==null){
                        compressedImage = null;
                        hideSpiner();
                        updateChangesClicked();
                    }else {
                        compressImage(actualImage);
                    }
                }
                //Glide.with(this).load(selectedImageUri).apply(new RequestOptions().override(100, 100)).into(profileUserDP);
            } else if (requestCode == LOCATION_REQUEST_CODE) {
                LatLng selectedLocation = data.getParcelableExtra("selected_location");
                if (selectedLocation != null) {
                    GeoPoint retrivedLocation = new GeoPoint(selectedLocation.latitude, selectedLocation.longitude);
                    setCurrentAddress(retrivedLocation);
                }
            }
        }
    }
    //       Glide.with(this).load().apply()

    @SuppressLint("CheckResult")
    private void compressImage(File actualImage) {
        new Compressor(this)
                .compressToFileAsFlowable(actualImage, "student_photo")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) {
                        //flush("i am here");
                        compressedImage = file;
                        hideSpiner();
                        updateChangesClicked();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        throwable.printStackTrace();
                        flush(throwable.getMessage());
                        hideSpiner();
                    }
                });
    }

    @Override
    public String getFirstName() {
        return String.valueOf(inputFirstName.getText());
    }

    @Override
    public String getLastName() {
        return String.valueOf(inputLastName.getText());
    }

    @Override
    public String getGmail() {
        return String.valueOf(email.getText());
    }

    @Override
    public String getGender() {
        return String.valueOf(selectGenderTextView.getText());
    }

    @Override
    public String getPhone() {
        return String.valueOf(profileUserNumber.getText());
    }

    @Override
    public String getCurrentStatus() {
        return String.valueOf(inputCurrentStatus.getText());
    }

    @Override
    public String getPreviousResult() {
        return String.valueOf(previousResultTextView.getText());
    }

    //TODO
    @Override
    public String getAvatarString() {
        return avatarString;
    }

    @Override
    public Uri getAvatarUri() {
        if (compressedImage != null) {
            try{
                return Uri.fromFile(compressedImage);
            }catch (Exception e){
                e.printStackTrace();
                return selectedImageUri;
            }
        } else {
            return selectedImageUri;
        }
    }

    @Override
    public String getProfileComPercent() {
        return Integer.toString(seekbar.getProgress());
    }

    //TODO
    @Override
    public GeoPoint getCurrentAddress() {
        return userLocation;
    }

    @Override
    public void setUserName(String last, String first) {
        profileUserName.setText(String.format("%s %s", last, first));
    }

    @Override
    public void setPhone(String phone) {
        profileUserNumber.setText(phone);
    }

    @Override
    public void setFirstName(String first) {
        inputFirstName.setText(first);
    }

    @Override
    public void setLastName(String last) {
        inputLastName.setText(last);
    }

    @Override
    public void setGmail(String gmail) {
        email.setText(gmail);
    }

    //TODO
    @Override
    public void setCurrentAddress(GeoPoint geoPoint) {
        userLocation = geoPoint;
        final String address = getAddress(this, geoPoint.getLatitude(), geoPoint.getLongitude());
        currentAddressTextView.setText(address);
    }

    @Override
    public void setCurrentStatus(String currentStatus) {
        inputCurrentStatus.setText(currentStatus);
    }

    @Override
    public void setPreviousResult(String previousResult) {
        if(previousResult!= null){
            if(previousResult.endsWith("(GPA)")){
                gpaCheckBox.setChecked(true);
            }else {
                cgpaCheckBox.setChecked(true);
            }
            previousResultTextView.setText(previousResult);
        }
    }

    @Override
    public void setGender(String gender) {
        selectGenderTextView.setText(gender);
    }

    //TODO
    @Override
    public void setAvatar(String uri) {
        avatarString = uri;
        //flush("this is setAvatar");
        String gender = selectGenderTextView.getText().toString();
        if (gender.equals("Male") || gender.equals("")) {
            Glide.with(getApplicationContext()).load(uri).apply(new RequestOptions().override(100, 100).placeholder(R.drawable.avatar)).into(profileUserDP);
        } else {
            Glide.with(getApplicationContext()).load(uri).apply(new RequestOptions().override(100, 100).placeholder(R.drawable.avatar_female)).into(profileUserDP);
        }
    }

    @Override
    public void setAvatarString(String uri) {
        avatarString = uri;
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
    public void flush(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void discardChangesClicked() {
        onBackPressed();
    }

    @Override
    public void updateChangesClicked() {
        //go for the 16 way
        if (getCurrentAddress() == null && getCurrentStatus().equals("")
                && getPreviousResult().equals("") && getGender().equals("")) {
            setProfileComPercent("60");
        } else if (getCurrentAddress() != null && getCurrentStatus().equals("")
                && getPreviousResult().equals("") && getGender().equals("")) {
            setProfileComPercent("70");
        } else if (getCurrentAddress() == null && !getCurrentStatus().equals("")
                && getPreviousResult().equals("") && getGender().equals("")) {
            setProfileComPercent("70");
        } else if (getCurrentAddress() == null && getCurrentStatus().equals("")
                && !getPreviousResult().equals("") && getGender().equals("")) {
            setProfileComPercent("65");
        } else if (getCurrentAddress() == null && getCurrentStatus().equals("")
                && getPreviousResult().equals("") && !getGender().equals("")) {
            setProfileComPercent("65");
        } else if (getCurrentAddress() != null && !getCurrentStatus().equals("")
                && getPreviousResult().equals("") && getGender().equals("")) {
            setProfileComPercent("80");
        } else if (getCurrentAddress() != null && getCurrentStatus().equals("")
                && !getPreviousResult().equals("") && getGender().equals("")) {
            setProfileComPercent("75");
        } else if (getCurrentAddress() != null && getCurrentStatus().equals("")
                && getPreviousResult().equals("") && !getGender().equals("")) {
            setProfileComPercent("75");
        } else if (getCurrentAddress() == null && !getCurrentStatus().equals("")
                && !getPreviousResult().equals("") && getGender().equals("")) {
            setProfileComPercent("75");
        } else if (getCurrentAddress() == null && !getCurrentStatus().equals("")
                && getPreviousResult().equals("") && !getGender().equals("")) {
            setProfileComPercent("75");
        } else if (getCurrentAddress() == null && getCurrentStatus().equals("")
                && !getPreviousResult().equals("") && !getGender().equals("")) {
            setProfileComPercent("70");
        } else if (getCurrentAddress() == null && !getCurrentStatus().equals("")
                && !getPreviousResult().equals("") && !getGender().equals("")) {
            setProfileComPercent("80");
        } else if (getCurrentAddress() != null && getCurrentStatus().equals("")
                && !getPreviousResult().equals("") && !getGender().equals("")) {
            setProfileComPercent("80");
        } else if (getCurrentAddress() != null && !getCurrentStatus().equals("")
                && getPreviousResult().equals("") && !getGender().equals("")) {
            setProfileComPercent("85");
        } else if (getCurrentAddress() != null && !getCurrentStatus().equals("")
                && !getPreviousResult().equals("") && getGender().equals("")) {
            setProfileComPercent("85");
        } else {
            setProfileComPercent("90");
        }


        if (getAvatarUri() != null && getAvatarString() == null) {
            Float profileComPercent = Float.parseFloat(getProfileComPercent()) + 10;
            setProfileComPercent(profileComPercent.toString());
        } else if (getAvatarString() != null) {
            Float profileComPercent = Float.parseFloat(getProfileComPercent()) + 10;
            setProfileComPercent(profileComPercent.toString());
        }


    }

    @Override
    public void hideSpiner() {
        loadingSpiner.setVisibility(View.GONE);
    }

    @Override
    public void showSpiner() {
        loadingSpiner.setVisibility(View.VISIBLE);
    }

    @Override
    public void goBack() {
        searchDataStore.setProfileChanged(true);
        hideKeyboard(activity);
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void initProfileCompleteView() {
        seekbarStaylayout.setVisibility(View.GONE);
        profileCompleteTextView.setVisibility(View.VISIBLE);
        dividerHorizontalUnderPCT.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean checkIfValidUpdate() {
        return getFirstName() != null && !getFirstName().equals("") &&
                getLastName() != null && !getLastName().equals("") &&
                getGmail() != null && !getGmail().equals("");
    }

    @Override
    public void showInvalideInfo() {
        if (getFirstName() == null || getFirstName().equals("")) {
            fnEmptyFound.setVisibility(View.VISIBLE);
        }
        if (getLastName() == null || getLastName().equals("")) {
            lnEmptyFound.setVisibility(View.VISIBLE);
        }
        if (getGmail() == null || getGmail().equals("")) {
            emailEmptyFound.setVisibility(View.VISIBLE);
        }
        flush("Please fill in the mandatory field");
    }

}

