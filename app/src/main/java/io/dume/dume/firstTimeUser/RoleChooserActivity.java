package io.dume.dume.firstTimeUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.dume.dume.R;
import io.dume.dume.auth.DataStore;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;

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
            local.setAccountManjor(DataStore.TEACHER );
        }
        ForwardFlowStat.ROLE.getFlow();

        startActivity(new Intent(this, PermissionActivity.class));
    }
}
