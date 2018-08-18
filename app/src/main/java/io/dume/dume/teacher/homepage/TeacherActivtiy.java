package io.dume.dume.teacher.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import io.dume.dume.R;
import io.dume.dume.auth.auth.AuthActivity;
import io.dume.dume.teacher.mentor_settings.AccountSettings;
import io.dume.dume.teacher.model.ModelSource;

public class TeacherActivtiy extends AppCompatActivity implements TeacherContract.View, NavigationView.OnNavigationItemSelectedListener {
    private TeacherContract.Presenter presenter;
    private TextView textView;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private DrawerLayout drawerLayout;
    private Button signOutButton;
    private static final String TAG = "TeacherActivtiy";
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new TeacherPresenter(this, new ModelSource());
    }

    @Override
    public void showRandomNumber(int i) {
        textView.setText(String.valueOf(i));
    }

    @Override
    public void init() {
        /*Object Initialization From */
        textView = findViewById(R.id.my_textView);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        //signOutButton = findViewById(R.id.signOutButton);

        /*Advance Configuration of Initialized Objects*/
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.drawer_menu);
        navigationView = findViewById(R.id.navigationView);
        initAdvance();
    }

    private void initAdvance() {
        navigationView.setNavigationItemSelectedListener(this);
   /*     View view = LayoutInflater.from(this).inflate(R.layout.lorem, null, false);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();*/
    }

    public void toggle(android.view.View view) {
        presenter.onButtonClicked();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
            startActivity(intent);
        }
        Toast.makeText(this, "this is a bullshit ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //  getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.prifile:
                startActivity(new Intent(this, AccountSettings.class));
                break;
        }
        return false;
    }
}
