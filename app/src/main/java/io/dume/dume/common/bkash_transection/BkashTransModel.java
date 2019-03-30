package io.dume.dume.common.bkash_transection;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import io.dume.dume.teacher.homepage.TeacherContract;

public class BkashTransModel implements BkashTransContact.Model {


    private Context context;
    private final FirebaseFirestore firestore;
    private final FirebaseAuth auth;

    BkashTransModel(Context context) {
        this.context = context;
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }


    @Override
    public void pushTransection(Map<String, Object> transData, TeacherContract.Model.Listener<Void> listener) {
        firestore.collection("payments").document(transData.get("transection_id").toString()).set(transData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                listener.onSuccess(aVoid);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onError(e.getMessage());
            }
        });
    }
}
