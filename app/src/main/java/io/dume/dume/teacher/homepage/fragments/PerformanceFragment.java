package io.dume.dume.teacher.homepage.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dume.dume.R;
import io.dume.dume.teacher.adapters.ReportAdapter;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.GridSpacingItemDecoration;

public class PerformanceFragment extends Fragment {

    //  @BindView(R.id.performanceRV)
    RecyclerView performanceRV;
    /*
     * */
    private PerformanceViewModel mViewModel;
    private static ReportAdapter reportAdapter;
    private int spacing;

    public static PerformanceFragment newInstance() {
        reportAdapter = new ReportAdapter();
        return new PerformanceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.performance_fragment, container, false);
        ButterKnife.bind(root);
        performanceRV = root.findViewById(R.id.performanceRV);
        int[] wh = DumeUtils.getScreenSize(container.getContext());
        spacing = (int) ((root.getWidth()- ((270) * (getResources().getDisplayMetrics().density))) / 3);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_space);
        performanceRV.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        performanceRV.setLayoutManager(new GridLayoutManager(getContext(), 3));
        performanceRV.setAdapter(new ReportAdapter());
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PerformanceViewModel.class);
        // TODO: Use the ViewModel
    }
    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.top = space;
            } else {
                outRect.top = 0;
            }
        }
    }

}
