package io.dume.dume.teacher.mentor_settings.academic;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.dume.dume.R;
import io.dume.dume.customView.HorizontalLoadView;
import io.dume.dume.teacher.adapters.MentorSpinnerAdapter;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.RadioBtnDialogue;

public class AcademicActivity extends AppCompatActivity implements AcademicContract.View, View.OnClickListener {

    private HorizontalLoadView loadView;
    private FloatingActionButton fb;
    private AcademicContract.Presenter presenter = null;
    private EditText instiEdt, degreeEdt, desEdt;
    AppCompatCheckBox studyCB, gpaCB, cgpaCB;
    private SpinnerAdapter fromSpinnerAdapter;
    private List<String> fromArray;
    EditText fromET, toET, resultET;

    private List<String> toArray;
    private MentorSpinnerAdapter toSpinnerAdapter;
    private static String ACTION_EDIT = "edit", ACTION_ADD = "add";
    public static String ACTION = null;

    private String itemUid;
    public static boolean isDeleted = false;
    private String[] cgpaOptionsArr;
    private String[] gpaOptionsArr;


    @Override
    public String getAction() {
        return ACTION == null ? "" : ACTION;
    }

    @Override
    public String getResultType() {
        return null;
    }

    @Override
    public String getRestult() {
        return resultET.getText().toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic);
        if (getIntent().getAction() == ACTION_EDIT) {
            ACTION = ACTION_EDIT;
            DumeUtils.configureAppbar(this, "Edit Education");

        } else {
            ACTION = ACTION_ADD;
            DumeUtils.configureAppbar(this, "Add Education");
        }

        presenter = new AcademicPresenter(this, AcademicModel.getInstance());
        presenter.enqueue();
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void snak(String msg) {
        Snackbar snakbar = Snackbar.make(fb, msg, Snackbar.LENGTH_SHORT);
        snakbar.setAction("Go Back", view -> AcademicActivity.super.onBackPressed());
      /*  snakbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                if (isDeleted) {
                    AcademicActivity.super.onBackPressed();
                } else if (ACTION.equals("edit")) {

                } else AcademicActivity.super.onBackPressed();
            }
        });*/
        snakbar.show();
    }

    @Override
    public String getInstitution() {
        return instiEdt.getText().toString();
    }

    @Override
    public String getStartYear() {
        return fromET.toString();
    }

    @Override
    public String getEndYear() {
        return toET.toString();
    }

    @Override
    public String getDescription() {
        return desEdt.getText().toString();
    }

    @Override
    public void getBundle() {
        if (getIntent().getAction() == ACTION_EDIT) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                instiEdt.setText(bundle.getString("institution"));
                int from = fromArray.indexOf(bundle.getString("from"));
                fromET.setText(from);
                int to = toArray.indexOf(bundle.getString("to"));
                toET.setText(to);
                degreeEdt.setText(bundle.getString("degree"));
                desEdt.setText(bundle.getString("description"));
                itemUid = bundle.getString("itemUid");
            }
        }
    }

    @Override
    public String getItemUid() {
        return itemUid;
    }

    @Override
    public boolean isGraduate() {
        return false;
    }

    @Override
    public void configView() {
        loadView = findViewById(R.id.academicLoad);
        fb = findViewById(R.id.fabEdit);
        instiEdt = findViewById(R.id.institutionET);
        degreeEdt = findViewById(R.id.degreeTV);
        desEdt = findViewById(R.id.descriptionET);
        resultET = findViewById(R.id.resultET);
        fromET = findViewById(R.id.fromET);
        toET = findViewById(R.id.toET);


        studyCB = findViewById(R.id.study_checkbox);
        gpaCB = findViewById(R.id.gpa_checkbox);
        cgpaCB = findViewById(R.id.cgpa_checkbox);
        fromArray = new ArrayList<>();
        toArray = new ArrayList<>();
        String[] stringArray = getResources().getStringArray(R.array.fromYear);
        gpaOptionsArr = this.getResources().getStringArray(R.array.gpa_options);
        cgpaOptionsArr = this.getResources().getStringArray(R.array.cgpa_options);
        fromET.setOnClickListener(view -> {
            Bundle pRargs = new Bundle();
            pRargs.putString("title", "Select your gender");
            pRargs.putStringArray("radioOptions", stringArray);
            RadioBtnDialogue genderBtnDialogue = new RadioBtnDialogue();
            genderBtnDialogue.setItemChoiceListener((dialogInterface, i) -> fromET.setText(stringArray[i]));
            genderBtnDialogue.setArguments(pRargs);
            genderBtnDialogue.show(getSupportFragmentManager(), "genderDialogue");
            fromET.setText(stringArray[0]);
        });
        toET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle pRargs = new Bundle();
                pRargs.putString("title", "Select your gender");
                pRargs.putStringArray("radioOptions", stringArray);
                RadioBtnDialogue genderBtnDialogue = new RadioBtnDialogue();
                genderBtnDialogue.setItemChoiceListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        toET.setText(stringArray[i]);
                    }
                });
                genderBtnDialogue.setArguments(pRargs);
                genderBtnDialogue.show(getSupportFragmentManager(), "genderDialogue");
                toET.setText(stringArray[0]);
            }
        });
        resultET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                } else {
                    resultET.setText(String.format("%s (CGPA)", cgpaOptionsArr[0]));
                    args.putString("title", "Select your CGPA");
                    args.putStringArray("radioOptions", cgpaOptionsArr);
                    previousResultDialogue.setItemChoiceListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            resultET.setText(String.format("%s (CGPA)", cgpaOptionsArr[i]));
                        }
                    });
                }
                previousResultDialogue.setArguments(args);
                previousResultDialogue.show(getSupportFragmentManager(), "genderDialogue");
            }
        });


    }

    @Override
    public void setListener() {
        fb.setOnClickListener(this);

    }


    @Override
    public String getDegree() {
        return degreeEdt.getText().toString();
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
    public void goBack() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        presenter.onViewIntracted(view);
    }
}
