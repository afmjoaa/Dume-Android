package io.dume.dume.teacher.homepage.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Keep;
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
import com.github.mikephil.charting.components.Description;
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
import io.dume.dume.teacher.homepage.TeacherActivtiy;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.homepage.TeacherDataStore;
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
    private TeacherActivtiy fragmentActivity;
    private Context context;
    private TeacherDataStore teacherDataStore;
    private StatAdapter statAdapter;

    public static StatisticsFragment getInstance() {
        if (statisticsFragment == null) {
            statisticsFragment = new StatisticsFragment();
        }
        return statisticsFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.statistics_fragment, container, false);
        ButterKnife.bind(this, root);
        fragmentActivity = (TeacherActivtiy) getActivity();
        mViewModel = new StatisticsViewModel(context);

        //testing
        assert container != null;
        int[] wh = DumeUtils.getScreenSize(container.getContext());
        float mDensity = getResources().getDisplayMetrics().density;
        int availableWidth = (int) (wh[0] - (93 * mDensity));
        itemWidth = (int) ((availableWidth - (30 * mDensity)) / 2);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, (int) (10 * mDensity), true));
        fragmentActivity = (TeacherActivtiy) getActivity();
        teacherDataStore = fragmentActivity != null ? fragmentActivity.teacherDataStore : null;
        if (teacherDataStore != null) {
            if (teacherDataStore.getDocumentSnapshot() == null) {
                fragmentActivity.presenter.loadProfile(new TeacherContract.Model.Listener<Void>() {
                    @Override
                    public void onSuccess(Void list) {
                        mViewModel.getChartEntry(new TeacherContract.Model.Listener<List<ArrayList<Entry>>>() {
                            @Override
                            public void onSuccess(List<ArrayList<Entry>> entrieslist) {
                                List<ILineDataSet> dataSets = new ArrayList<>();
                                for (int i = 0; i < entrieslist.size(); i++) {
                                    LineDataSet lineDataSet = null;
                                    lineDataSet = new LineDataSet(entrieslist.get(i), "Impressions");
                                    lineDataSet.setDrawFilled(true);
                                    lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                                    lineDataSet.setFillColor(context.getResources().getColor(R.color.impression));
                                    lineDataSet.setColor(context.getResources().getColor(R.color.impression));
                                    if (i == 1) {
                                        lineDataSet = new LineDataSet(entrieslist.get(i), "Requests");
                                        lineDataSet.setDrawFilled(true);
                                        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                                        lineDataSet.setFillColor(context.getResources().getColor(R.color.view));
                                        lineDataSet.setColor(context.getResources().getColor(R.color.view));
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
                        if (recyclerView.getAdapter() != null) {
                            statAdapter.update(teacherDataStore.getTodayStatList());
                        } else {
                            statAdapter = new StatAdapter(getContext(), itemWidth, teacherDataStore.getTodayStatList()) {
                                @Override
                                public void onItemClick(int position, View view) {
                                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                                }
                            };
                            recyclerView.setAdapter(statAdapter);
                        }
                    }
                    @Override
                    public void onError(String msg) {
                        fragmentActivity.flush(msg);
                    }
                });
            } else {
                mViewModel.getChartEntry(new TeacherContract.Model.Listener<List<ArrayList<Entry>>>() {
                    @Override
                    public void onSuccess(List<ArrayList<Entry>> entrieslist) {
                        List<ILineDataSet> dataSets = new ArrayList<>();
                        for (int i = 0; i < entrieslist.size(); i++) {
                            LineDataSet lineDataSet = null;
                            lineDataSet = new LineDataSet(entrieslist.get(i), "Impressions");
                            lineDataSet.setDrawFilled(true);
                            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                            lineDataSet.setFillColor(context.getResources().getColor(R.color.impression));
                            lineDataSet.setColor(context.getResources().getColor(R.color.impression));
                            if (i == 1) {
                                lineDataSet = new LineDataSet(entrieslist.get(i), "Requests");
                                lineDataSet.setDrawFilled(true);
                                lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                                lineDataSet.setFillColor(context.getResources().getColor(R.color.view));
                                lineDataSet.setColor(context.getResources().getColor(R.color.view));
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
                if (recyclerView.getAdapter() != null) {
                    statAdapter.update(teacherDataStore.getTodayStatList());
                } else {
                    statAdapter = new StatAdapter(getContext(), itemWidth, teacherDataStore.getTodayStatList()) {
                        @Override
                        public void onItemClick(int position, View view) {
                            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                        }
                    };
                    recyclerView.setAdapter(statAdapter);
                }
            }
        }

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void showChart(LineData data) {
        lineChart.setData(data);
        lineChart.setVisibleXRangeMaximum(7f);
        lineChart.setVisibleXRangeMinimum(7f);
        lineChart.setDragDecelerationEnabled(true);
        lineChart.setDragDecelerationFrictionCoef(0.5f);

        Description desc = new Description();
        desc.setText("Impression, view / week");
        desc.setPosition(-5f, -5f);
        desc.setTextColor(Color.BLACK);
        desc.setTypeface(Typeface.createFromAsset(fragmentActivity.getAssets(), "fonts/Cairo_Regular.ttf"));
        lineChart.setDescription(desc);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setEnabled(false);
        xAxis.setDrawGridLines(false);
        YAxis axisLeft = lineChart.getAxisLeft();
        axisLeft.setDrawGridLines(false);
        axisLeft.setDrawZeroLine(true);
        axisLeft.setTypeface(Typeface.createFromAsset(fragmentActivity.getAssets(), "fonts/Cairo-Light.ttf")); // set a different font
        axisLeft.setTextSize(10f);
        //axisLeft.setAxisMinimum(0f); // start at zero
        //axisLeft.setAxisMaximum(100f); // the axis maximum is 100
        axisLeft.setTextColor(getResources().getColor(R.color.textColorPrimary));
        //yAxis.setValueFormatter(new MyValueFormatter());
        axisLeft.setGranularity(2f); // interval 1
        YAxis axisRight = lineChart.getAxisRight();
        axisRight.setDrawGridLines(false);
        axisRight.setDrawLabels(false);
        axisRight.setEnabled(false);
        Legend legend = lineChart.getLegend();
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
        legend.setTextColor(getResources().getColor(R.color.textColorPrimary));
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setFormSize(6f);
        legend.setXEntrySpace(5f); // set the space between the legend entries on the x-axis
        legend.setYEntrySpace(5f); // set the space between the legend entries on the y-axis
        legend.setTypeface(Typeface.createFromAsset(fragmentActivity.getAssets(), "fonts/Cairo_Regular.ttf"));
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setDoubleTapToZoomEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.enableScroll();
        lineChart.invalidate();
        //lineChart.moveViewToX(23f);
        lineChart.moveViewToAnimated(23f, 0f,YAxis.AxisDependency.RIGHT , 800);
        lineChart.animateY(2600, Easing.EasingOption.EaseInOutElastic);
    }

}
