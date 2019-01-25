package io.dume.dume.teacher.homepage.fragments;

import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;

import io.dume.dume.model.DumeModel;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.pojo.Pay;

public class PayViewModel extends DumeModel {
    void getPayDetails(TeacherContract.Model.Listener<ArrayList<Pay>> listener) {
        ArrayList<Pay> payArrayList = new ArrayList<>();
        payArrayList.add(new Pay(120, "Obligation/Due", 0, true, 20.0f));
        payArrayList.add(new Pay(120, "Total Earnings", 0, false, 0));
        listener.onSuccess(payArrayList);
    }
}
