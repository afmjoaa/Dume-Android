package io.dume.dume.student.studentSettings;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

import io.dume.dume.R;
import io.dume.dume.teacher.homepage.TeacherContract;

public class StudentSettingsPresenter implements StudentSettingsContract.Presenter {

    private StudentSettingsContract.View mView;
    private StudentSettingsContract.Model mModel;
    private Context context;
    private Activity activity;
    private static final String TAG = "StudentSettingsPresente";

    public StudentSettingsPresenter(Context context, StudentSettingsContract.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (StudentSettingsContract.View) context;
        this.mModel = mModel;
    }

    @Override
    public void studentSettingsEnqueue() {
        mView.findView();
        mView.initStudentSettings();
        mView.configStudentSettings();

    }

    @Override
    public void onStudentSettingsIntracted(View view) {
        switch (view.getId()) {
            case R.id.basic_info_layout:
                mView.gotoProfilePage();
                break;
            case R.id.fab:
                mView.fabClicked();
                break;
        }
    }

    @Override
    public void retriveSavedPlacesData(TeacherContract.Model.Listener<DocumentSnapshot> listener) {
        mModel.addShapShotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
              if(documentSnapshot != null){
                  listener.onSuccess(documentSnapshot);
              }else{
                  listener.onError(e.getMessage().toString());
              }
            }
        });
    }

}
