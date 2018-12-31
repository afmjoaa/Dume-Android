package io.dume.dume.student.profilePage;

import android.accounts.Account;
import android.accounts.AccountManager;
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
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.PopupMenu;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.GeoPoint;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.IndicatorStayLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import carbon.widget.ImageView;
import io.dume.dume.R;
import io.dume.dume.student.grabingLocation.GrabingLocationActivity;
import io.dume.dume.student.homePage.HomePageActivity;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.RadioBtnDialogue;

import static io.dume.dume.util.DumeUtils.getAddress;
import static io.dume.dume.util.DumeUtils.getUserUID;

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
    private ImageView discardImageView;
    private ImageView doneImageView;
    private Button doneBtn;
    private Button discardBtn;
    private TextView profileUserName;
    private TextView profileUserNumber;
    private android.widget.ImageView profileUserDP;
    private ProgressBar loadingSpiner;
    private Uri outputFileUri;
    private  int LOCATION_REQUEST_CODE = 2222;
    private int IMAGE_RESULT_CODE = 3333;
    private AutoCompleteTextView inputLastName;
    private AutoCompleteTextView inputFirstName;
    private AutoCompleteTextView inputCurrentStatus;

    Map<String, Object> localData;
    private String avatarUrl = null;
    private GeoPoint userLocation = null;
    public String action = "null";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

       /* outState.putString("first_name", getFirstName());
        outState.putString("last_name", getLastName());
        outState.putString("email", getGmail());
        outState.putParcelable("current_address", new LatLng(getCurrentAddress().getLatitude(), getCurrentAddress().getLongitude()));
        outState.putString("current_status", getCurrentStatus());
        outState.putString("previous_result", getPreviousResult());
        outState.putString("gender", getGender());
        outState.putString("pro_com_%", getProfileComPercent());
        outState.putParcelable("avatar", outputFileUri);*/
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        localData = new HashMap<>();
        localData.put("first_name", savedInstanceState.getString("first_name"));
        localData.put("last_name", savedInstanceState.getString("first_name"));
        localData.put("email", savedInstanceState.getString("first_name"));
        localData.put("current_address", (GeoPoint) savedInstanceState.getParcelable("current_address"));
        localData.put("current_status", savedInstanceState.getString("current_status"));
        localData.put("previous_result", savedInstanceState.getString("previous_result"));
        localData.put("gender", savedInstanceState.getString("gender"));
        localData.put("pro_com_%", savedInstanceState.getString("pro_com_%"));
        localData.put("avatar", savedInstanceState.getString("avatar"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu1_activity_profile_page);
        setActivityContext(this, fromFlag);
        action = getIntent().getAction();

        settingStatusBarTransparent();
        mPresenter = new ProfilePagePresenter(this, this, new ProfilePageModel(this, this));
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

        //LatLng objLatLng = Objects.requireNonNull(getIntent().getExtras()).getParcelable("selected_location");

    }

    @Override
    public void initProfilePage() {
        decor = getWindow().getDecorView();
        cgpaCheckBox.setOnCheckedChangeListener(this);
        gpaCheckBox.setOnCheckedChangeListener(this);
        profileUserDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                Toast.makeText(context, "fucked it.....", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
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
        if (gpaCheckBox.isChecked()) {
            args.putString("title", "Select your GPA");
            args.putStringArray("radioOptions", gpaOptionsArr);
            previousResultDialogue.setItemChoiceListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    previousResultTextView.setText(String.format("%s (GPA)", gpaOptionsArr[i]));
                }
            });
        } else {
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
        startActivityForResult(new Intent(this, GrabingLocationActivity.class).setAction("fromPPA"),LOCATION_REQUEST_CODE );
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

    private void openImageIntent() {
        // Determine Uri of camera image to save.
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
        root.mkdirs();
        final String fname = getUserUID() + ".jpg";
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

                Uri selectedImageUri;
                if (isCamera) {
                    selectedImageUri = outputFileUri;
                } else {
                    selectedImageUri = data == null ? null : data.getData();
                }
                Glide.with(this).load(selectedImageUri).apply(new RequestOptions().override(100, 100)).into(profileUserDP);
            }else if(requestCode == LOCATION_REQUEST_CODE){
                LatLng selectedLocation = data.getParcelableExtra("selected_location");
                if(selectedLocation != null){
                    GeoPoint retrivedLocation = new GeoPoint(selectedLocation.latitude, selectedLocation.longitude);
                    setCurrentAddress(retrivedLocation);
                    //currentAddressTextView.setText(getAddress(this, selectedLocation.latitude, selectedLocation.longitude));
                    //Toast.makeText(this, selectedLocation.latitude +":" + selectedLocation.longitude, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    //       Glide.with(this).load().apply()

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
    public String getAvatarUrl() {
        return avatarUrl;
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
        previousResultTextView.setText(previousResult);
    }

    @Override
    public void setGender(String gender) {
        selectGenderTextView.setText(gender);
    }

    //TODO
    @Override
    public void setAvatar(String uri) {
        avatarUrl = uri;
        Glide.with(this).load(uri).apply(new RequestOptions().override(100, 100)).into(profileUserDP);
    }

    @Override
    public void setProfileComPercent(String num) {
        seekbar.setProgress(Float.parseFloat(num));
    }

    @Override
    public void flush(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void discardChangesClicked() {
        startActivity(new Intent(this, HomePageActivity.class));
    }

    @Override
    public void updateChangesClicked() {

    }

    @Override
    public void setResortedBundle() {
        if (action.equals("toPPA")) {
            if (!localData.get("first_name").toString().equals("")) {
                setFirstName(localData.get("first_name").toString());
            }
            if (!localData.get("last_name").toString().equals("")) {
                setLastName(localData.get("last_name").toString());
            }
            if (!localData.get("email").toString().equals("")) {
                setGmail(localData.get("email").toString());
            }
            if (localData.get("current_address") != null) {
                setCurrentAddress((GeoPoint) localData.get("current_address"));
            }
            if (!localData.get("current_status").toString().equals("")) {
                setCurrentStatus(localData.get("current_status").toString());
            }
            if (!localData.get("previous_result").toString().equals("")) {
                setPreviousResult(localData.get("previous_result").toString());
            }
            if (!localData.get("gender").toString().equals("")) {
                setGender(localData.get("gender").toString());
            }
            if (!localData.get("avatar").toString().equals("")) {
                setAvatar(localData.get("avatar").toString());
            }
            if (!localData.get("pro_com_%").toString().equals("")) {
                setProfileComPercent(localData.get("pro_com_%").toString());
            }

        }
    }
}
