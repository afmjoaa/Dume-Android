package io.dume.dume.teacher.homepage;

import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import io.dume.dume.R;
import io.dume.dume.teacher.pojo.Feedback;
import io.dume.dume.teacher.pojo.Inbox;

public class TeacherPresenter implements TeacherContract.Presenter {

    private static final String TAG = TeacherPresenter.class.getSimpleName().toString();
    TeacherContract.View view;
    TeacherContract.Model model;

    public TeacherPresenter(TeacherContract.View view, TeacherModel model) {
        this.view = view;
        this.model = model;
        init();
    }

    @Override
    public void init() {
        view.init();
        model.getFeedBack(new TeacherContract.Model.Listener<ArrayList<Feedback>>() {
            @Override
            public void onSuccess(ArrayList<Feedback> list) {
                view.showFeedbackRV(list);
            }

            @Override
            public void onError(String msg) {
                view.flush(msg);
            }
        });
        model.getInbox(new TeacherContract.Model.Listener<ArrayList<Inbox>>() {
            @Override
            public void onSuccess(ArrayList<Inbox> list) {
                view.showInboxRV(list);
            }

            @Override
            public void onError(String msg) {
                view.flush(msg);
            }
        });
        model.getChartEntry(new TeacherContract.Model.Listener<List<ArrayList<Entry>>>() {
            @Override
            public void onSuccess(List<ArrayList<Entry>> entrieslist) {
                List<ILineDataSet> dataSets = new ArrayList<>();
                for (int i = 0; i < entrieslist.size(); i++) {
                    LineDataSet lineDataSet = null;
                    lineDataSet = new LineDataSet(entrieslist.get(i), "Impressions");
                    lineDataSet.setDrawFilled(true);
                    lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                    lineDataSet.setFillColor(Color.RED);
                    lineDataSet.setColor(Color.RED);
                    if (i == 1) {
                        lineDataSet = new LineDataSet(entrieslist.get(i), "Views");
                        lineDataSet.setDrawFilled(true);
                        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                        lineDataSet.setFillColor(Color.MAGENTA);
                        lineDataSet.setColor(Color.MAGENTA);
                    }
                    lineDataSet.setLineWidth(0.0f);
                    lineDataSet.setDrawValues(false);
                    dataSets.add(lineDataSet);
                }
                LineData lineData = new LineData(dataSets);
                view.showChart(lineData);
            }

            @Override
            public void onError(String msg) {
                view.flush(msg);
            }
        });

    }


    @Override
    public void onButtonClicked() {


    }

    @Override
    public void onViewInteracted(View component) {
        switch (component.getId()) {
            case R.id.switch_account_btn:
                view.flush("Fucked ");
                view.onSwitchAccount();

                break;

        }
    }


}
