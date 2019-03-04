package io.dume.dume.student.recordsPending;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import io.dume.dume.student.recordsPage.RecordsPageModel;
import io.dume.dume.teacher.homepage.TeacherContract;

public class RecordsPendingModel extends RecordsPageModel implements RecordsPendingContract.Model {

    private final Context context;

    public RecordsPendingModel(Context context) {
        super(context);
        this.context = context;
    }



    @Override
    public void recordsPendingHawwa() {

    }

}
