package io.dume.dume.teacher.homepage.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dume.dume.R;
import io.dume.dume.teacher.adapters.StatAdapter;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.pojo.Stat;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.GridSpacingItemDecoration;

public class StatisticsFragment extends Fragment {
    @BindView(R.id.statChart)
    LineChart lineChart;
    private StatisticsViewModel mViewModel;
    @BindView(R.id.statRV)
    RecyclerView recyclerView;
    private static StatisticsFragment statisticsFragment = null;
    private int itemWidth;


    public static StatisticsFragment newInstance() {
        return new StatisticsFragment();
    }

    public static StatisticsFragment getInstance() {
        if (statisticsFragment == null) {
            statisticsFragment = new StatisticsFragment();
        }
        return statisticsFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.statistics_fragment, container, false);
        ButterKnife.bind(this, root);
        mViewModel = new StatisticsViewModel();
        mViewModel.getChartEntry(new TeacherContract.Model.Listener<List<ArrayList<Entry>>>() {
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
                        lineDataSet.setFillColor(Color.BLUE);
                        lineDataSet.setColor(Color.BLUE);
                    }
                    lineDataSet.setLineWidth(0.0f);
                    lineDataSet.setDrawValues(false);
                    dataSets.add(lineDataSet);
                }
                LineData lineData = new LineData(dataSets);
                showChart(lineData);

            }

            @Override
            public void onError(String msg) {

            }
        });

        //testing
        assert container != null;
        int[] wh = DumeUtils.getScreenSize(container.getContext());
        float mDensity = getResources().getDisplayMetrics().density;
        int availableWidth = (int) (wh[0] - (93 * mDensity));
        itemWidth = (int) ((availableWidth - (30 * mDensity)) / 2);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, (int) (10 * mDensity), true));
        recyclerView.setAdapter(new StatAdapter(getContext(),itemWidth ,new Stat(123, 3, 1, null)) {
            @Override
            public void onItemClick(int position, View view) {
                Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void showChart(LineData data) {
        lineChart.setData(data);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setEnabled(false);
        xAxis.setDrawGridLines(false);
        YAxis axisLeft = lineChart.getAxisLeft();
        axisLeft.setDrawGridLines(false);
        YAxis axisRight = lineChart.getAxisRight();
        axisRight.setDrawGridLines(false);
        axisRight.setDrawLabels(false);
        Legend legend = lineChart.getLegend();
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
        lineChart.setTouchEnabled(false);
        lineChart.enableScroll();
        lineChart.invalidate();
        lineChart.animateY(3000, Easing.EasingOption.EaseInOutElastic);
    }

}
