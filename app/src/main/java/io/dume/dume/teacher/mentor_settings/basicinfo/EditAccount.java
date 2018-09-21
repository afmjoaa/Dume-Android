package io.dume.dume.teacher.mentor_settings.basicinfo;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.transitionseverywhere.TransitionManager;

import java.util.Arrays;

import io.dume.dume.R;
import io.dume.dume.custom_view.HorizontalLoadView;
import io.dume.dume.util.DumeUtils;

public class EditAccount extends AppCompatActivity implements EditContract.View, View.OnClickListener {
    private FloatingActionButton fb;
    private EditContract.Presenter presenter;
    private NestedScrollView mScrollView;
    private int oldScrollYPostion = 0;
    private static final String TAG = "EditAccount";
    private EditText first, last, phone, mail;
    private Spinner gender, religion, marital;
    private ImageView avatar;
    private String avatarUrl = null;
    private HorizontalLoadView loadView;
    private CoordinatorLayout wrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        DumeUtils.configureAppbar(this, "Edit Account");
        presenter = new EditPresenter(this, EditModel.getModelInstance());
        presenter.enqueue();
    }


    @Override
    public void onClick(View view) {
        presenter.onClick(view);
    }

    @Override
    public void configureView() {
        fb = findViewById(R.id.fabEdit);
        first = findViewById(R.id.firstNameEt);
        last = findViewById(R.id.lastNameEdt);
        mail = findViewById(R.id.mailEt);
        phone = findViewById(R.id.phoneEdt);
        gender = findViewById(R.id.genderSp);
        marital = findViewById(R.id.maritalSp);
        religion = findViewById(R.id.religionSp);
        mScrollView = findViewById(R.id.editAccountScrolling);
        avatar = findViewById(R.id.profileImage);
        loadView = findViewById(R.id.loadEdit);
        wrapper = findViewById(R.id.wrapperAccountEdit);
        loadCarryData();
    }

    private void loadCarryData() {
        Bundle bundle = getIntent().getBundleExtra("user_data");
        if (bundle != null) {
            Glide.with(this).load(bundle.getString("avatar")).apply(new RequestOptions().override(100, 100)).into(avatar);
            first.setText(bundle.getString("first_name"));
            last.setText(bundle.getString("last_name"));
            phone.setText(bundle.getString("phone_number"));
            mail.setText(bundle.getString("email"));
            religion.setSelection(Arrays.asList(getResources().getStringArray(R.array.religion)).indexOf(bundle.getString("religion")));
            gender.setSelection(Arrays.asList(getResources().getStringArray(R.array.gender)).indexOf(bundle.getString("gender")));
            marital.setSelection(Arrays.asList(getResources().getStringArray(R.array.marital_status)).indexOf(bundle.getString("marital")));
            Log.e(TAG, "loadCarryData: " + bundle.toString());
            avatarUrl = bundle.getString("avatar");
        }
    }


    @Override
    public void configureCallback() {
        fb.setOnClickListener(this);
        mScrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if (mScrollView.getScrollY() > oldScrollYPostion) {
                fb.hide();
            } else if (mScrollView.getScrollY() < oldScrollYPostion || mScrollView.getScrollY() <= 0) {
                fb.show();
            }
            oldScrollYPostion = mScrollView.getScrollY();
            Log.w(TAG, "onScrollChanged: " + oldScrollYPostion);
        });
        avatar.setOnClickListener(this);
    }

    @Override
    public void snakbar(String msg) {
        Snackbar snak = Snackbar.make(fb, msg, Snackbar.LENGTH_SHORT);
        snak.setAction("Go Back", view -> EditAccount.super.onBackPressed());
        snak.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
        snak.show();
    }

    @Override
    public void toast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public String firstName() {
        return first.getText().toString();
    }

    @Override
    public String lastName() {
        return last.getText().toString();
    }


    @Override
    public String maritalStatus() {
        return marital.getSelectedItem().toString();
    }

    @Override
    public String gmail() {
        return mail.getText().toString();
    }

    @Override
    public String religion() {
        return religion.getSelectedItem().toString();
    }

    @Override
    public String gender() {
        return gender.getSelectedItem().toString();
    }

    @Override
    public String phone() {
        return phone.getText().toString();
    }

    @Override
    public void updateImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, DumeUtils.GALLARY_IMAGE);
    }

    @Override
    public void setImage(Uri uri) {
        Glide.with(this).load(uri).into(avatar);
    }

    @Override
    public void setAvatarUrl(String url) {
        this.avatarUrl = url;
    }

    @Override
    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    @Override
    public void enableLoad() {
        fb.setEnabled(false);
        fb.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        if (!loadView.isRunningAnimation()) {
            TransitionManager.beginDelayedTransition(wrapper);
            loadView.setVisibility(View.VISIBLE);
            loadView.startLoading();
        }
    }

    @Override
    public void disableLoad() {
        fb.setEnabled(true);
        fb.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
        if (loadView.isRunningAnimation()) {
            TransitionManager.beginDelayedTransition(wrapper);
            loadView.setVisibility(View.GONE);
            loadView.stopLoading();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);

    }


}
