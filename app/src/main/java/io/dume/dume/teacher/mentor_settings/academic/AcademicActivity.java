package io.dume.dume.teacher.mentor_settings.academic;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.EditText;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.dume.dume.R;
import io.dume.dume.custom_view.HorizontalLoadView;
import io.dume.dume.teacher.adapters.MentorSpinnerAdapter;
import io.dume.dume.util.DumeUtils;

public class AcademicActivity extends AppCompatActivity implements AcademicContract.View, View.OnClickListener {

    private HorizontalLoadView loadView;
    private FloatingActionButton fb;
    private AcademicContract.Presenter presenter = null;
    private EditText instiEdt, degreeEdt, desEdt;
    private AppCompatSpinner fromSpn, toSpn;
    private SpinnerAdapter fromSpinnerAdapter;
    private List<String> fromArray;
    private List<String> toArray;
    private MentorSpinnerAdapter toSpinnerAdapter;
    private int EDIT, ADD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic);
        DumeUtils.configureAppbar(this, "Add Education");
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
        snakbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                AcademicActivity.super.onBackPressed();
            }
        });
        snakbar.show();

    }

    @Override
    public String getInstitution() {
        return instiEdt.getText().toString();
    }

    @Override
    public String getStartYear() {
        return fromSpn.getSelectedItem().toString();
    }

    @Override
    public String getEndYear() {
        return toSpn.getSelectedItem().toString();
    }

    @Override
    public String getDescription() {
        return desEdt.getText().toString();
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
        fromSpn = findViewById(R.id.fromSpn);
        toSpn = findViewById(R.id.toSpn);
        fromArray = new ArrayList<>();
        toArray = new ArrayList<>();
        String[] stringArray = getResources().getStringArray(R.array.fromYear);
        fromArray.addAll(Arrays.asList(stringArray));
        fromArray.add("2012");
        fromArray.add("2015");
        fromArray.add("2012");
        fromArray.add("2015");
        fromArray.add("2012");
        fromArray.add("2015");
        fromArray.add("From");
        toArray.addAll(Arrays.asList(stringArray));
        toArray.add("To");
        fromSpinnerAdapter = new MentorSpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, fromArray);
        toSpinnerAdapter = new MentorSpinnerAdapter(this, android.R.layout.simple_dropdown_item_1line, toArray);
        fromSpn.setAdapter(fromSpinnerAdapter);
        fromSpn.setSelection(fromSpinnerAdapter.getCount());
        toSpn.setAdapter(toSpinnerAdapter);
        toSpn.setSelection(toSpinnerAdapter.getCount());

    }

    @Override
    public void setListener() {
        fb.setOnClickListener(this);


    }

    @Override
    public int getFlag() {
        return 0;
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
