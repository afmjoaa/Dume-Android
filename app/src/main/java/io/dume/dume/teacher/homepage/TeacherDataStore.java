package io.dume.dume.teacher.homepage;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;
import java.util.Map;

public class TeacherDataStore implements Serializable {
    private Map<String, Object> selfRating = null;
    private DocumentSnapshot documentSnapshot;
    private static TeacherDataStore teacherDataStore = null;

    public static TeacherDataStore getInstance() {
        if (teacherDataStore == null) {
            teacherDataStore = new TeacherDataStore();
        }
        return teacherDataStore;
    }

    public Map<String, Object> getSelfRating() {
        return selfRating;
    }

    public void setSelfRating(Map<String, Object> selfRating) {
        this.selfRating = selfRating;
    }

    public DocumentSnapshot getDocumentSnapshot() {
        return documentSnapshot;
    }

    public void setDocumentSnapshot(DocumentSnapshot documentSnapshot) {
        this.documentSnapshot = documentSnapshot;
    }
}
