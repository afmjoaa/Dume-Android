package io.dume.dume.common.bkash_transection;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import io.dume.dume.teacher.homepage.TeacherContract;

public class BkashTransModel implements BkashTransContact.Model {


    private Context context;
    private final FirebaseFirestore firestore;
    private final FirebaseAuth auth;
    private ListenerRegistration listenerRegistration;

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
        }).addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    @Override
    public void isTransectionIdExists(String id, TeacherContract.Model.Listener<Boolean> listener) {
        listenerRegistration = firestore.collection("payments").whereEqualTo("transection_id", id).addSnapshotListener((Activity) context, (queryDocumentSnapshots, e) -> {
            Log.w("FooBar", "isTransectionIdExists: ");
            listenerRegistration.remove();
            if (queryDocumentSnapshots != null) {
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                if (documents.size() > 0) {
                    listener.onSuccess(true);
                } else {
                    listener.onSuccess(false);
                }
            } else {
                listener.onSuccess(false);
            }
        });
    }


}
