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
import com.google.firebase.auth.FirebaseAuth;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.IndicatorStayLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import carbon.widget.ImageView;
import io.dume.dume.R;
import io.dume.dume.student.grabingLocation.GrabingLocationActivity;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.RadioBtnDialogue;

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
    private int IMAGE_RESULT_CODE = 3333;


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
        profileUserDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, view);
                popup.inflate(R.menu.menu_dp_long_click);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        switch (id){
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
        discardImageView = findViewById(R.id.discard_imageview);
        doneImageView = findViewById(R.id.done_imageview);
        discardBtn = findViewById(R.id.profile_discard_btn);
        doneBtn = findViewById(R.id.profile_update_btn);
        profileUserName = findViewById(R.id.Profile_page_user_name);
        profileUserNumber = findViewById(R.id.Profile_page_user_phone_no);
        profileUserDP = findViewById(R.id.imageView1);
        loadingSpiner = findViewById(R.id.loading_spinner);
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
        for(ResolveInfo res : listCam) {
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
                Glide.with(this).load(selectedImageUri).apply(new RequestOptions()).into(profileUserDP);
            }
        }
    }
    //       Glide.with(this).load().apply()
}
