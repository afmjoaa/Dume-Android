package io.dume.dume.common.chatActivity;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.pojo.Letter;

public class DemoModel {
    private final FirebaseFirestore firestore;
    private final Context context;
    private ListenerRegistration listenerRegistration;

    public DemoModel(Context context) {
        this.context = context;
        firestore = FirebaseFirestore.getInstance();
    }

    public void addMessage(Letter letter, TeacherContract.Model.Listener<Void> listener) {

        firestore.collection("messages").document("C0u7RSY2NItOqh2gaYag").collection("chatbox").add(letter).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                listener.onSuccess(null);
            }
        });

    }

    public void onTypeStateChange(TeacherContract.Model.Listener<Boolean> listener) {
        firestore.collection("messages").document("C0u7RSY2NItOqh2gaYag").addSnapshotListener(new EventListener<DocumentSnapshot>() {

            private boolean isTypeing;

            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                Map<String, Object> data = (Map<String, Object>) documentSnapshot.get("typing");

                if (FirebaseAuth.getInstance().getUid().equals("nlVlsVd1okMRdAlI1rEzkkfFLY72")) {
                    isTypeing = (boolean) data.get("mHwz528eBdMZCPVB77m8xXmFka52");
                } else {
                    isTypeing = (boolean) data.get("nlVlsVd1okMRdAlI1rEzkkfFLY72");
                }
                listener.onSuccess(isTypeing);
            }
        });
    }

    public void onType(boolean typing, TeacherContract.Model.Listener<Void> listener) {
        firestore.collection("messages").document("C0u7RSY2NItOqh2gaYag").update("typing." + FirebaseAuth.getInstance().getUid(), typing).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onSuccess(aVoid);
            }
        });
    }

    public void onInboxChange(/*String room_id,*/ TeacherContract.Model.Listener<List<Letter>> messageListener) {
        firestore.collection("messages").document("C0u7RSY2NItOqh2gaYag").collection("chatbox").orderBy("timestamp", Query.Direction.DESCENDING).limit(1).addSnapshotListener((Activity) context, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                List<Letter> letters = new ArrayList<>();
                for (int i = 0; i < documents.size(); i++) {
                    Letter letter = documents.get(i).toObject(Letter.class);
                    letters.add(letter);
                }
                messageListener.onSuccess(letters);
                if (e != null) {
                    messageListener.onError(e.getCode() + e.getMessage());
                }
            }
        });
    }

    public void readLastThirty(TeacherContract.Model.Listener<List<Letter>> messageListener) {
        listenerRegistration = firestore.collection("messages").document("C0u7RSY2NItOqh2gaYag").collection("chatbox").orderBy("timestamp", Query.Direction.ASCENDING).limit(30).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                listenerRegistration.remove();
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                List<Letter> letters = new ArrayList<>();
                for (int i = 0; i < documents.size(); i++) {
                    Letter letter = documents.get(i).toObject(Letter.class);
                    letters.add(letter);
                }
                messageListener.onSuccess(letters);
                if (e != null) {
                    messageListener.onError(e.getCode() + e.getMessage());
                }
            }
        });
    }


}
