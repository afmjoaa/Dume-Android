package io.dume.dume.teacher.pojo;

import java.util.ArrayList;

public interface GlobalListener {
    interface AcademicQuery {
        void onSuccess(ArrayList<Education> educationlist);

        void onFailure(String error);

        void onStart();
    }
}
