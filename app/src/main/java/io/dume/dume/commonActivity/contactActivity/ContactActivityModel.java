package io.dume.dume.commonActivity.contactActivity;

import android.app.Activity;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.dume.dume.teacher.homepage.TeacherContract;

public class ContactActivityModel implements ContactActivityContact.Model {

    private FirebaseFirestore firestore;
    private Context context;


    public ContactActivityModel(Context context) {
        this.context = context;
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void contactActivityHawwa() {

    }

    @Override
    public void readContact(String accountMajor, TeacherContract.Model.Listener<List<ContactData>> listener) {

        if (FirebaseAuth.getInstance().getUid() == null) {
            listener.onError("Error Type : Session Expired. Please Login To Feel Better");
            return;
        }

        Query query = firestore.collection("records").whereArrayContains("participants", FirebaseAuth.getInstance().getUid());
        query.addSnapshotListener((Activity) context, (queryDocumentSnapshots, e) -> {
            if (e != null) {
                listener.onError("Error Code : " + e.getCode() + " \n" + e.getLocalizedMessage());

            } else {
                List<DocumentSnapshot> documents = queryDocumentSnapshots != null ? queryDocumentSnapshots.getDocuments() : null;
                if ((documents != null ? documents.size() : 0) > 0) {
                    List<ContactData> list = new ArrayList<>();
                    List<String> fooList = new ArrayList<>();
                    for (DocumentSnapshot record : documents) {
                        Map<String, Object> sp_info = (Map<String, Object>) record.get("sp_info");
                        Map<String, Object> sh_info = (Map<String, Object>) record.get("for_whom");

                        String gender = "";
                        String name = "";
                        String avatar = "";
                        String phone = "";

                        String record_status = (String) record.get("record_status");
                        String sh_uid = record.getString("sh_uid");
                        List<String> pList = (List<String>) record.get("participants");
                        int participant;
                        String pUid;
                        if (pList != null) {
                            participant = pList.indexOf(FirebaseAuth.getInstance().getUid());
                        } else {
                            listener.onError("Participant Not Found");
                            return;
                        }
                        if (participant == 0) {
                            pUid = pList.get(1);
                        } else {
                            pUid = pList.get(0);
                        }
                        /*I am User*/
                        if (sh_uid.endsWith(FirebaseAuth.getInstance().getUid())) {
                            if (sp_info != null) {
                                gender = (String) sp_info.get("gender");
                                avatar = (String) sp_info.get("avatar");
                                name = sp_info.get("first_name") + " " + sp_info.get("last_name");
                                phone = (String) sp_info.get("phone_number");


                            }
                        }/*I am Teacher*/ else {
                            if (sh_info != null) {
                                gender = (String) sh_info.get("request_gender");
                                String stu_photo = (String) sh_info.get("stu_photo");
                                if (stu_photo != null && !sh_info.get("stu_photo").equals("")) {
                                    avatar = stu_photo;
                                } else avatar = (String) sh_info.get("request_avatar");
                                name = (String) sh_info.get("stu_name");
                                phone = (String) sh_info.get("stu_phone_number");

                            }
                        }


                        ContactData data = new ContactData(record.getId(), avatar, name, record_status, pUid, record.getData(), phone);
                        if (fooList.contains(pUid)) {
                            if (record_status != null && record_status.equals("Accepted")) {
                                for (int i = 0; i < list.size(); i++) {
                                    if (pUid.equals(list.get(i).userId)) {
                                        list.get(i).setStatus("Accepted");
                                        list.get(i).setRecord(record.getData());
                                        list.get(i).setRecord_id(record.getId());
                                    }
                                }
                            }
                            break;
                        } else {
                            fooList.add(pUid);
                            list.add(data);
                        }

                    }

               /*     Set<ContactData> set = new HashSet<>(list);
                    list.clear();
                    list.addAll(set);
                    Log.w("foo", "readContact: List 0:" + list.get(0).getUserId().hashCode());
                    Log.w("foo", "readContact: List 1:" + list.get(1).getUserId().hashCode());
                    Log.w("foo", "readContact: Set  :" + set.toString());*/
                    listener.onSuccess(list);
                } else {
                    listener.onSuccess(new ArrayList<>());
                    listener.onError("You don't have any records right now. \nRecord is a deal between Mentor and Students");
                }
            }
        });
    }
}
