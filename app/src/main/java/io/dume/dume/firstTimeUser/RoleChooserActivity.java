package io.dume.dume.firstTimeUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import io.dume.dume.R;
import io.dume.dume.auth.DataStore;
import io.dume.dume.student.DashBoard.StudentDashBoard;
import io.dume.dume.student.pojo.BaseAppCompatActivity;
import io.dume.dume.teacher.DashBoard.TeacherDashboard;

public class RoleChooserActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private DataStore local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_chooser);
        setActivityContext(this, 2001);
        local = DataStore.getInstance();
        init();
    }

    private void init() {
        settingStatusBarTransparent();
        setDarkStatusBarIcon();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.asStudent) {
            local.accountManjor = DataStore.STUDENT;
            startActivity(new Intent(this, StudentDashBoard.class));
        } else if (v.getId() == R.id.asTeacher) {
            local.accountManjor = DataStore.TEACHER;
            startActivity(new Intent(this, TeacherDashboard.class));
        } else if (v.getId() == R.id.testEnam) {
            startActivity(new Intent(this, StudentDashBoard.class));
            return;
        } else if (v.getId() == R.id.testJoaa) {
            startActivity(new Intent(this, ForwardFlowHostActivity.class));
            return;
        } else if (v.getId() == R.id.testSumon) {
            startActivity(new Intent(this, TeacherDashboard.class));
            return;
        }
        startActivity(new Intent(this, TeacherDashboard.class));
    }
}
