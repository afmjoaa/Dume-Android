package io.dume.dume.student.searchResult;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Map;

import io.dume.dume.student.pojo.StuBaseModel;
import io.dume.dume.teacher.homepage.TeacherContract;

public class SearchResultModel extends StuBaseModel implements SearchResultContract.Model {


    public SearchResultModel(Context context) {
        super(context);
    }

    @Override
    public void searchResultHawwa() {

    }

}
