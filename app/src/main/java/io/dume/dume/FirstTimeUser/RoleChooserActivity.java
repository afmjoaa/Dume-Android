package io.dume.dume.FirstTimeUser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.dume.dume.Google;
import io.dume.dume.R;
import io.dume.dume.auth.DataStore;

public class RoleChooserActivity extends AppCompatActivity implements View.OnClickListener {
    Button asTeacher;
    Button asStudent;
    private DataStore local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_chooser);
        local = DataStore.getInstance();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.asStudent) {
            local.setAccountManjor(DataStore.STUDENT);
        } else if (v.getId() == R.id.asTeacher) {
            local.setAccountManjor(DataStore.STUDENT);
        }

        startActivity(new Intent(this, PermissionActivity.class));
        finish();
    }
}
