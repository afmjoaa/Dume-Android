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

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dume.dume.R;
import io.dume.dume.teacher.adapters.ReportAdapter;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.GridSpacingItemDecoration;

public class PerformanceFragment extends Fragment {

    //  @BindView(R.id.performanceRV)
    RecyclerView performanceRV;
    private PerformanceViewModel mViewModel;
    private static ReportAdapter reportAdapter;
    private int spacing;

    public static PerformanceFragment newInstance() {
        reportAdapter = new ReportAdapter();
        return new PerformanceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.performance_fragment, container, false);
        ButterKnife.bind(root);
        performanceRV = root.findViewById(R.id.performanceRV);
        //testing my code here
        int[] wh = DumeUtils.getScreenSize(container.getContext());
        spacing = (int) ((wh[0]-((336) * (getResources().getDisplayMetrics().density))) / 4);
        performanceRV.setLayoutManager(new GridLayoutManager(getContext(), 3));
        performanceRV.addItemDecoration(new GridSpacingItemDecoration(3, spacing, true));
        performanceRV.setAdapter(new ReportAdapter());
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PerformanceViewModel.class);
        // TODO: Use the ViewModel
    }
}
