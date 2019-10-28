package io.dume.dume.FirstTimeUser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.dume.dume.R;

public class RoleChooserActivity extends AppCompatActivity implements View.OnClickListener {
    Button asTeacher;
    Button asStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_chooser);

    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, PermissionActivity.class));
        finish();
    }
}
