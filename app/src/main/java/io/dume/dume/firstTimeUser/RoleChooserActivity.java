package io.dume.dume.firstTimeUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import io.dume.dume.R;
import io.dume.dume.auth.DataStore;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.teacher.dashboard.activities.JobBoardActivity;

public class RoleChooserActivity extends CustomStuAppCompatActivity implements View.OnClickListener {
    private DataStore local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_chooser);
        setActivityContext(this, 3636);
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
            local.setAccountManjor(DataStore.STUDENT);
        } else if (v.getId() == R.id.asTeacher) {
            local.setAccountManjor(DataStore.TEACHER);
        } else if (v.getId() == R.id.test) {

            Intent[] buddies = {
                    new Intent(this, JobBoardActivity.class)};
            startActivities(buddies);


            return;
        }
        startActivity(new Intent(this, PermissionActivity.class));
    }
}
