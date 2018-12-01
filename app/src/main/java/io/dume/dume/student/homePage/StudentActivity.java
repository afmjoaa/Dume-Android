package io.dume.dume.student.homePage;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telecom.GatewayInfo;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import io.dume.dume.R;
import io.dume.dume.afterSplashTrp.AfterSplashActivity;
import io.dume.dume.auth.auth.AuthActivity;
import io.dume.dume.common.chatActivity.ChatActivity;
import io.dume.dume.student.grabingInfo.GrabingInfoActivity;
import io.dume.dume.student.grabingPackage.GrabingPackageActivity;
import io.dume.dume.student.recordsAccepted.RecordsAcceptedActivity;
import io.dume.dume.student.recordsCompleted.RecordsCompletedActivity;
import io.dume.dume.student.recordsCurrent.RecordsCurrentActivity;
import io.dume.dume.student.recordsPending.RecordsPendingActivity;
import io.dume.dume.student.recordsRejected.RecordsRejectedActivity;
import io.dume.dume.student.searchLoading.SearchLoadingActivity;
import io.dume.dume.student.searchResult.SearchResultActivity;
import io.dume.dume.teacher.crudskill.CrudSkillActivity;
import io.dume.dume.util.DumeUtils;

public class StudentActivity extends AppCompatActivity implements StudentContract.View {
    StudentContract.Presenter presenter;
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        presenter = new StudentPresenter(this, this, new StudentModel());
        presenter.enqueue();
    }

    @Override
    public void onSignOut() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, AuthActivity.class));
        }
    }

    @Override
    public void goToMapView() {
        startActivity(new Intent(this, SearchResultActivity.class));
    }

    @Override
    public void goToStudentHOmePage() {
        startActivity(new Intent(this, HomePageActivity.class));

    }

    @Override
    public void configView() {

    }

    @Override
    public void goTORecordsActivity() {
        startActivity(new Intent(this, GrabingInfoActivity.class).setAction(DumeUtils.STUDENT));
    }

    @Override
    public void goTORecordsActivity2() {
        startActivity(new Intent(this, SearchResultActivity.class));
        //startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
    }

    @Override
    public void goTORecordsActivity3() {
        startActivity(new Intent(this, CrudSkillActivity.class).setAction(DumeUtils.STUDENT));

    }

    public void onStudentViewClicked(View view) {
        presenter.onViewIntracted(view);
    }

}
