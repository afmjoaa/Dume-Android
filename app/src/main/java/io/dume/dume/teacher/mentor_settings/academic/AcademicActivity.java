package io.dume.dume.teacher.mentor_settings.academic;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.Calendar;

import io.dume.dume.R;
import io.dume.dume.customView.HorizontalLoadView;
import io.dume.dume.teacher.mentor_settings.basicinfo.EditAccount;
import io.dume.dume.util.DatePickerFragment;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.RadioBtnDialogue;

import static io.dume.dume.util.DumeUtils.showKeyboard;

public class AcademicActivity extends AppCompatActivity implements AcademicContract.View,
        CompoundButton.OnCheckedChangeListener {

    private HorizontalLoadView loadView;
    private FloatingActionButton fb;
    private AcademicContract.Presenter presenter = null;
    private EditText instiEdt, degreeEdt;
    AppCompatCheckBox studyCB, gpaCB, cgpaCB;
    EditText fromET, toET, resultET;
    private static String ACTION_EDIT = "edit", ACTION_ADD = "add", ACTION_CADD = "c_add";
    public static String ACTION = null;
    private String[] cgpaOptionsArr;
    private String[] gpaOptionsArr;
    private EditText levelET;
    private String[] levelSelectionArr;
    private carbon.widget.ImageView emptyLevelF;
    private carbon.widget.ImageView emptyDegreeF;
    private carbon.widget.ImageView emptyINstitutionF;
    private carbon.widget.ImageView emptyFromF;
    private carbon.widget.ImageView emptyToF;
    private carbon.widget.ImageView emptyResultF;
    private Dialog dialog;
    private RelativeLayout fromRelative;
    private RelativeLayout toRelative;
    private LinearLayout hostLayout;


    @Override
    public String getAction() {
        return ACTION == null ? "" : ACTION;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic);
        presenter = new AcademicPresenter(this, this, AcademicModel.getInstance());
        presenter.enqueue();
        if (getIntent().getAction() == ACTION_EDIT) {
            ACTION = ACTION_EDIT;
            DumeUtils.configureAppbar(this, "Edit Qualification", true);
            degreeEdt.setFocusable(false);
            //levelET.setFocusable(false);
            degreeEdt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toast("Degree can't be edited, once added.");
                }
            });
           /* levelET.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toast("Level can't be edited, once added.");
                }
            });*/
        } else if(getIntent().getAction().equals(ACTION_CADD)){
            ACTION = ACTION_CADD;
            DumeUtils.configureAppbar(this, "Add Qualification", true);
            degreeEdt.setFocusable(true);
        }else {
            ACTION = ACTION_ADD;
            DumeUtils.configureAppbar(this, "Add Qualification", true);
            degreeEdt.setFocusable(true);
        }
        TransitionSet set = new TransitionSet()
                .addTransition(new Fade())
                .addTransition(new Slide(Gravity.END))
                .setInterpolator(new LinearOutSlowInInterpolator())
                .addListener(new Transition.TransitionListener() {
                    @Override
                    public void onTransitionStart(@NonNull Transition transition) {

                    }

                    @Override
                    public void onTransitionEnd(@NonNull Transition transition) {
                        if (toRelative.getVisibility() == View.INVISIBLE) {
                            toRelative.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onTransitionCancel(@NonNull Transition transition) {

                    }

                    @Override
                    public void onTransitionPause(@NonNull Transition transition) {

                    }

                    @Override
                    public void onTransitionResume(@NonNull Transition transition) {

                    }
                });
        TransitionManager.beginDelayedTransition(hostLayout, set);
        testingCustomDialogue();
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void snak(String msg) {
        Snackbar snakbar = Snackbar.make(fb, msg, Snackbar.LENGTH_LONG);
        snakbar.setAction("Go Back", view -> {
            super.onBackPressed();
        });
        snakbar.show();
    }

    @Override
    public String getInstitution() {
        return instiEdt.getText().toString();
    }

    @Override
    public String getStartYear() {
        return fromET.getText().toString();
    }

    @Override
    public String getEndYear() {
        return toET.getText().toString();
    }


    @Override
    public String getRestult() {
        return resultET.getText().toString();
    }


    @Override
    public String getDegree() {
        return degreeEdt.getText().toString();
    }

    @Override
    public String getLevel() {
        return levelET.getText().toString();
    }


    @Override
    public void getBundle() {
        if (getIntent().getAction() == ACTION_EDIT) {
            Bundle bundle = getIntent().getBundleExtra("academic_data");
            if (bundle != null) {
                levelET.setText(bundle.getString("level"));
                instiEdt.setText(bundle.getString("institution"));
                degreeEdt.setText(bundle.getString("degree"));
                fromET.setText(bundle.getString("from_year"));
                toET.setText(bundle.getString("to_year"));
                resultET.setText(bundle.getString("result"));
                int resultType = bundle.getInt("resultType", 0);
                switch (resultType) {
                    case 1:
                        studyCB.setChecked(true);
                        break;
                    case 2:
                        gpaCB.setChecked(true);
                        break;
                    case 3:
                        cgpaCB.setChecked(true);
                        break;
                }
            }
        }
    }

    @Override
    public void findView() {
        loadView = findViewById(R.id.academicLoad);
        fb = findViewById(R.id.fabEdit);
        instiEdt = findViewById(R.id.institutionET);
        degreeEdt = findViewById(R.id.degreeTV);
        resultET = findViewById(R.id.levelET);
        fromET = findViewById(R.id.fromET);
        toET = findViewById(R.id.toET);
        levelET = findViewById(R.id.input_level);
        fromRelative = findViewById(R.id.from_relative);
        toRelative = findViewById(R.id.to_relative);
        hostLayout = findViewById(R.id.user_name);

        studyCB = findViewById(R.id.study_checkbox);
        gpaCB = findViewById(R.id.gpa_checkbox);
        cgpaCB = findViewById(R.id.cgpa_checkbox);
        gpaOptionsArr = this.getResources().getStringArray(R.array.gpa_options);
        cgpaOptionsArr = this.getResources().getStringArray(R.array.cgpa_options);
        levelSelectionArr = this.getResources().getStringArray(R.array.level_option);
        emptyLevelF = findViewById(R.id.empty_level_found);
        emptyDegreeF = findViewById(R.id.empty_degree_found);
        emptyINstitutionF = findViewById(R.id.empty_institution_found);
        emptyFromF = findViewById(R.id.empty_from_found);
        emptyToF = findViewById(R.id.empty_to_found);
        emptyResultF = findViewById(R.id.empty_status_found);
        resultET.setText("Studying");

    }

    @Override
    public void inValidFound(String identify) {
        switch (identify) {
            case "level":
                emptyLevelF.setVisibility(View.VISIBLE);
                break;
            case "institution":
                emptyINstitutionF.setVisibility(View.VISIBLE);
                break;
            case "degree":
                emptyDegreeF.setVisibility(View.VISIBLE);
                break;
            case "from":
                emptyFromF.setVisibility(View.VISIBLE);
                break;
            case "to":
                emptyToF.setVisibility(View.VISIBLE);
                break;
            case "result":
                emptyResultF.setVisibility(View.VISIBLE);
                break;
        }
    }


    @Override
    public void configActivity() {
        gpaCB.setOnCheckedChangeListener(this);
        cgpaCB.setOnCheckedChangeListener(this);
        studyCB.setOnCheckedChangeListener(this);
        degreeEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    degreeEdt.setHint("B.Sc. in CSE");
                    showKeyboard(AcademicActivity.this);
                } else {
                    degreeEdt.setHint("");
                }
            }
        });
        instiEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    instiEdt.setHint("MIST-Military Ins. of Sc. and Tech.");
                    showKeyboard(AcademicActivity.this);
                } else {
                    instiEdt.setHint("");
                }
            }
        });

        //empty show hide
        levelET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("")) {
                    emptyLevelF.setVisibility(View.GONE);
                } else {
                    emptyLevelF.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    emptyLevelF.setVisibility(View.VISIBLE);
                }
            }
        });

        degreeEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("")) {
                    emptyDegreeF.setVisibility(View.GONE);
                } else {
                    emptyDegreeF.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    emptyDegreeF.setVisibility(View.VISIBLE);
                }
            }
        });
        instiEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("")) {
                    emptyINstitutionF.setVisibility(View.GONE);
                } else {
                    emptyINstitutionF.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    emptyINstitutionF.setVisibility(View.VISIBLE);
                }
            }
        });
        fromET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("")) {
                    emptyFromF.setVisibility(View.GONE);
                } else {
                    emptyFromF.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    emptyFromF.setVisibility(View.VISIBLE);
                }
            }
        });
        toET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("")) {
                    emptyToF.setVisibility(View.GONE);
                } else {
                    emptyToF.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    emptyToF.setVisibility(View.VISIBLE);
                }
            }
        });
        resultET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("")) {
                    emptyResultF.setVisibility(View.GONE);
                } else {
                    emptyResultF.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    emptyResultF.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void enableLoad() {
        if (!loadView.isRunningAnimation()) {
            loadView.setVisibility(View.VISIBLE);
            loadView.startLoading();
            fb.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
            fb.setClickable(false);
        }
    }

    @Override
    public void disableLoad() {
        if (loadView.isRunningAnimation()) {
            loadView.setVisibility(View.GONE);
            loadView.stopLoading();
            fb.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
            fb.setClickable(true);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = compoundButton.getId();
        if (id == R.id.cgpa_checkbox) {
            if (compoundButton.isChecked()) {
                gpaCB.setChecked(false);
                studyCB.setChecked(false);
                toRelative.setVisibility(View.VISIBLE);
                resultET.setText("A+ (CGPA)");
            }
        } else if (id == R.id.gpa_checkbox) {
            if (compoundButton.isChecked()) {
                cgpaCB.setChecked(false);
                studyCB.setChecked(false);
                toRelative.setVisibility(View.VISIBLE);
                resultET.setText("Golden A+ (GPA)");
            }
        } else if (id == R.id.study_checkbox) {
            if (compoundButton.isChecked()) {
                cgpaCB.setChecked(false);
                gpaCB.setChecked(false);
                toRelative.setVisibility(View.GONE);
                resultET.setText("Studying");
            }
        }

        if (!studyCB.isChecked() && !gpaCB.isChecked() && !cgpaCB.isChecked()) {
            studyCB.setChecked(true);
            toRelative.setVisibility(View.GONE);
            resultET.setText("Studying");
        }
        //selectResultClicked();
    }

    @Override
    public boolean getValidationCheck() {
        return !studyCB.isChecked();
    }

    @Override
    public void gotoBackActivity() {
        if(ACTION.equals("c_add")){
            Intent goBackIntent = new Intent(this, EditAccount.class);
            setResult(RESULT_OK, goBackIntent.setAction("c_add"));
            finish();
        }else{
            Intent goBackIntent = new Intent(this, EditAccount.class);
            setResult(RESULT_OK, goBackIntent);
            finish();
        }
    }

    public void onAcademicClick(View view) {
        presenter.onViewIntracted(view);
    }

    @Override
    public void selectLevelClicked() {
        Bundle pRargs = new Bundle();
        pRargs.putString("title", "Select level");
        pRargs.putStringArray("radioOptions", levelSelectionArr);
        RadioBtnDialogue genderBtnDialogue = new RadioBtnDialogue();
        genderBtnDialogue.setItemChoiceListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                levelET.setText(levelSelectionArr[i]);
            }
        });
        genderBtnDialogue.setArguments(pRargs);
        genderBtnDialogue.show(getSupportFragmentManager(), "levelDialogue");
        levelET.setText(levelSelectionArr[0]);

    }

    @Override
    public void selectFromClicked() {
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
                fromET.setText(currentDateStr);
            }
        });
        thisDatePicker.setInitialDate(2000, 0, 1);
        thisDatePicker.show(getSupportFragmentManager(), "from_date_picker");
    }

    @Override
    public void selectToClicked() {
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
                toET.setText(currentDateStr);
            }
        });
        thisDatePicker.setInitialDate(2004, 0, 1);
        thisDatePicker.show(getSupportFragmentManager(), "to_date_picker");
    }

    @Override
    public void selectResultClicked() {
        Bundle args = new Bundle();
        RadioBtnDialogue previousResultDialogue = new RadioBtnDialogue();
        if (gpaCB.isChecked()) {
            resultET.setText(String.format("%s (GPA)", gpaOptionsArr[0]));
            args.putString("title", "Select your GPA");
            args.putStringArray("radioOptions", gpaOptionsArr);
            previousResultDialogue.setItemChoiceListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    resultET.setText(String.format("%s (GPA)", gpaOptionsArr[i]));
                }
            });
            previousResultDialogue.setArguments(args);
            previousResultDialogue.show(getSupportFragmentManager(), "result_dialogue");
        } else if (cgpaCB.isChecked()) {
            resultET.setText(String.format("%s (CGPA)", cgpaOptionsArr[0]));
            args.putString("title", "Select your CGPA");
            args.putStringArray("radioOptions", cgpaOptionsArr);
            previousResultDialogue.setItemChoiceListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    resultET.setText(String.format("%s (CGPA)", cgpaOptionsArr[i]));
                }
            });
            previousResultDialogue.setArguments(args);
            previousResultDialogue.show(getSupportFragmentManager(), "result_dialogue");
        } else {
            resultET.setText("Studying");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_grabing_info, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
        } else if (id == R.id.action_help) {
            //show the dialogue
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(loadView.isRunningAnimation()){
            toast("Please wait ...");
        }else {
            super.onBackPressed();
        }
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
        dialogText.setText(R.string.academic_info);

        dismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
