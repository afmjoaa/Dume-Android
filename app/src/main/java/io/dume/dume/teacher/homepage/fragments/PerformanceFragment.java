package io.dume.dume.teacher.homepage.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dume.dume.R;
import io.dume.dume.teacher.adapters.ReportAdapter;
import io.dume.dume.teacher.homepage.TeacherDataStore;
import io.dume.dume.teacher.model.KeyValueModel;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.EqualSpacingItemDecoration;
import io.dume.dume.util.GridSpacingItemDecoration;

public class PerformanceFragment extends Fragment {

    //  @BindView(R.id.performanceRV)
    RecyclerView performanceRV;
    private PerformanceViewModel mViewModel;
    private static ReportAdapter reportAdapter;
    private int spacing;
    private TeacherDataStore teacherDataStore;

    public static PerformanceFragment newInstance() {

        return new PerformanceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.performance_fragment, container, false);
        ButterKnife.bind(root);
        performanceRV = root.findViewById(R.id.performanceRV);
        //testing my code here
        int[] wh = DumeUtils.getScreenSize(container.getContext());
        spacing = (int) ((wh[0] - ((336) * (getResources().getDisplayMetrics().density))) / 4);
        performanceRV.setLayoutManager(new GridLayoutManager(getContext(), 3));
        performanceRV.addItemDecoration(new GridSpacingItemDecoration(3, spacing, true));


        /*Gathering Data For Report Adapter*/
        teacherDataStore = TeacherDataStore.getInstance();


        if (teacherDataStore.getSelfRating() != null) {
            ArrayList<KeyValueModel> arrayList = new ArrayList<>();
            String totalReview = (String) teacherDataStore.getSelfRating().get("star_count");
            String rating = (String) teacherDataStore.getSelfRating().get("star_rating");
            arrayList.add(new KeyValueModel("Ratings", rating + " ★ /" + totalReview));
            String responseTime = (String) teacherDataStore.getSelfRating().get("response_time");
            arrayList.add(new KeyValueModel("Response Time", responseTime));
            String totalStudents = (String) teacherDataStore.getSelfRating().get("student_guided");
            arrayList.add(new KeyValueModel("Total Students", totalStudents));
            String likeExpertize = (String) teacherDataStore.getSelfRating().get("l_expertize");
            String disLikeExpertize = (String) teacherDataStore.getSelfRating().get("dl_expertize");
            arrayList.add(new KeyValueModel("Expertize", "" + (100 * Integer.parseInt(likeExpertize)) / Integer.parseInt(disLikeExpertize) + "%"));
            String likeBehaviour = (String) teacherDataStore.getSelfRating().get("l_behaviour");
            String disLikeBehaviour = (String) teacherDataStore.getSelfRating().get("dl_behaviour");
            arrayList.add(new KeyValueModel("Behaviour", "" + (100 * Integer.parseInt(likeBehaviour)) / Integer.parseInt(disLikeBehaviour)+"%"));


            Map<String, Object> unread_records = (Map<String, Object>) teacherDataStore.getDocumentSnapshot().get("unread_records");
            final String acceptedCount = (String) unread_records.get("accepted_count");
            final String completedCount = (String) unread_records.get("completed_count");
            final String currentCount = (String) unread_records.get("current_count");
            final String pendingCount = (String) unread_records.get("pending_count");

            final String rejectedCount = (String) unread_records.get("rejected_count");

            arrayList.add(new KeyValueModel("Accept Ratio", ((Integer.parseInt(acceptedCount) + Integer.parseInt(completedCount) + Integer.parseInt(currentCount) + Integer.parseInt(pendingCount)) /

                    (Integer.parseInt(acceptedCount) + Integer.parseInt(completedCount) + Integer.parseInt(currentCount) + Integer.parseInt(pendingCount) + Integer.parseInt(rejectedCount))) * 100 + "%"
            ));

            performanceRV.setAdapter(new ReportAdapter(arrayList));
        }

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PerformanceViewModel.class);

    }
}
