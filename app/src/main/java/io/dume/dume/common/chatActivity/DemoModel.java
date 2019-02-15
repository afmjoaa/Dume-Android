package io.dume.dume.common.chatActivity;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

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
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import io.dume.dume.Google;
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

    public void addMessage(String roomId, Letter letter, TeacherContract.Model.Listener<Void> listener) {

        firestore.collection("messages").document(roomId).collection("chatbox").add(letter).addOnSuccessListener((Activity) context, new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                listener.onSuccess(null);
            }
        });

    }


    public void addRoom(Map<String, Object> map, TeacherContract.Model.Listener<Void> listener) {
        List<String> participants = new ArrayList<>((List<String>) map.get("participants"));
        Collections.sort(participants);
        DocumentReference messages = firestore.collection("messages").document(participants.get(0).concat(participants.get(1)));
        messages.set(map, SetOptions.merge());
        messages.addSnapshotListener((Activity) context, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    listener.onError(e.getMessage());
                } else {
                    listener.onSuccess(null);
                }
            }
        });

    }

    public void onTypeStateChange(TeacherContract.Model.Listener<Boolean> listener) {
        firestore.collection("messages").document(Google.getInstance().getCurrentRoom()).addSnapshotListener((Activity) context, new EventListener<DocumentSnapshot>() {

            private boolean isTypeing = false;

            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                Map<String, Object> data = (Map<String, Object>) documentSnapshot.get("typing");
                List<String> participants = (List<String>) documentSnapshot.get("participants");
                if (data != null && participants != null) {
                    String opponentUid = opponentUid(participants);
                    Log.w("foo", "onEvent: " + isTypeing);

                    isTypeing = (boolean) data.get(opponentUid);
                }
                listener.onSuccess(isTypeing);
            }
        });
    }

    public void onType(boolean typing, TeacherContract.Model.Listener<Void> listener) {
        firestore.collection("messages").document(Google.getInstance().getCurrentRoom()).update("typing." + FirebaseAuth.getInstance().getUid(), typing).addOnSuccessListener((Activity) context, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onSuccess(aVoid);
            }
        });
    }

    public void onInboxChange(/*String room_id,*/ TeacherContract.Model.Listener<List<Letter>> messageListener) {
        firestore.collection("messages").document(Google.getInstance().getCurrentRoom()).collection("chatbox").orderBy("timestamp", Query.Direction.DESCENDING).limit(1).addSnapshotListener((Activity) context, new EventListener<QuerySnapshot>() {
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

    public void readLastThirty(String from, TeacherContract.Model.Listener<List<Letter>> messageListener) {
        Query query = firestore.collection("messages").document(Google.getInstance().getCurrentRoom()).collection("chatbox").orderBy("timestamp", Query.Direction.ASCENDING);
        if (from != null) {
            query = query.startAfter(from);
        }

        listenerRegistration = query.limit(30).addSnapshotListener((Activity) context, new EventListener<QuerySnapshot>() {
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

    private String opponentUid(List<String> participants) {
        String foo = participants.get(0);
        String bar = participants.get(1);
        if (foo.equals(FirebaseAuth.getInstance().getUid())) return bar;
        else return foo;
    }

    public void getNotification(String uid, TeacherContract.Model.Listener<List<Notif>> listener) {
        if (uid != null && !uid.equals("")) {
            Query query = firestore.collection("push_notifications").whereEqualTo("uid", uid).orderBy("timestamp", Query.Direction.ASCENDING);
            query.addSnapshotListener((Activity) context, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    List<DocumentSnapshot> documents = null;
                    List<Notif> list = new ArrayList<>();
                    if (queryDocumentSnapshots != null) {
                        documents = queryDocumentSnapshots.getDocuments();
                        if (documents.size() > 0) {
                            for (int i = 0; i < documents.size(); i++) {
                                Map<String, Object> data = documents.get(i).getData();
                                if (data != null) {
                                    String name = (String) data.get("name");
                                    String title = (String) data.get("title");
                                    String body = (String) data.get("reason");
                                    String uid = (String) data.get("uid");
                                    String token = (String) data.get("token");
                                    list.add(new Notif(name, title, body, uid, token));
                                }
                            }
                            listener.onSuccess(list);
                        } else listener.onError("");
                    } else listener.onError("");
                }
            });

        } else listener.onError("Error: Session Expired. Please Log In");
    }

    public void getRoom(String uid, TeacherContract.Model.Listener<List<Room>> listener) {
        if (uid != null && !uid.equals("")) {
            Query query = firestore.collection("messages").whereArrayContains("participants", uid);
            query.addSnapshotListener((Activity) context, (queryDocumentSnapshots, e) -> {
                if (e != null || queryDocumentSnapshots == null)
                    listener.onError(e != null ? e.getMessage() : "No Message History Found. Click Message Button To Create New One");
                else {
                    List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                    List<Room> roomList = new ArrayList<>();
                    for (int i = 0; i < documents.size(); i++) {
                        DocumentSnapshot snapshot = documents.get(i);
                        Map<String, Object> map = snapshot.getData();
                        String opUid, opDP = "", opName, lastMsgTime = "12/3/2019";
                        boolean mute = false;
                        if (map != null) {
                            List<String> participants = (List<String>) map.get("participants");
                            opUid = opponentUid(participants);
                           /* String foo = participants.get(0);
                            String bar = participants.get(1);
                            if (foo.equals(FirebaseAuth.getInstance().getUid())) opUid = bar;
                            else opUid = foo;*/

                            Map<String, Object> opMap = (Map<String, Object>) map.get(opUid);
                            opName = (String) opMap.get("name");
                            Object muteObj = opMap.get("mute");
                            if (muteObj != null) {
                                mute = (boolean) muteObj;
                            }


                        } else return;


                        roomList.add(new Room(snapshot.getId(), opUid, opDP, opName, lastMsgTime, mute))
                        ;


                    }
                    listener.onSuccess(roomList);
                }

            });
        } else {
            listener.onError("Session Expired. Please Log In");
        }
    }


}
