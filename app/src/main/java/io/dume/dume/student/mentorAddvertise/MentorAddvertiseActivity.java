package io.dume.dume.student.mentorAddvertise;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import io.dume.dume.R;

public class MentorAddvertiseActivity extends AppCompatActivity implements MentorAddvertiseContact.View{

    private MentorAddvertiseContact.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu15_activity_mentor_addvertise);
        mPresenter = new MentorAddvertisePresenter(this, new MentorAddvertiseModel());
        mPresenter.mentorAddvertiseEnqueue();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void findView() {

    }

    @Override
    public void initMentorAddvertise() {

    }

    @Override
    public void configMentorAddvertise() {

    }
}
