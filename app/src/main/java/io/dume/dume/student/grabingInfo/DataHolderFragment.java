package io.dume.dume.student.grabingInfo;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.appyvet.materialrangebar.IRangeBarFormatter;
import com.appyvet.materialrangebar.RangeBar;

import java.util.ArrayList;
import java.util.List;

import io.dume.dume.R;
import io.dume.dume.util.OnViewClick;

public class DataHolderFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {


    private static final String TAG = "Bal";
    private OnViewClick listener;
    private int sectionNumber = 0;
    private ArrayList<String> list;
    private List<Integer> idList;
    private RadioGroup group;
    private int id;
    private int generatedId;
    private View root;
    private Context mContext;
    private NestedScrollView hostingNestedScrollLayout;
    private RecyclerView mRecyclerView;
    private GrabingInfoActivity myMainActivity;
    private final int VERTICAL_ITEM_SPACE = -15;


    public DataHolderFragment() {
        idList = new ArrayList<>();
    }


    public static DataHolderFragment newInstance(int sectionNumber, List<String> list) {
        DataHolderFragment fragment = new DataHolderFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("list", (ArrayList<String>) list);
        bundle.putInt("SECTION_NUMBER", sectionNumber);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            if (group != null) {
                if (getArguments() != null) {
                    sectionNumber = getArguments().getInt("SECTION_NUMBER");
                    String selected_title = getArguments().getString("" + sectionNumber) == null ? "" : getArguments().getString("" + sectionNumber);
                    Log.w(TAG, "setUserVisibleHint : section  " + sectionNumber + "  |  title   " + selected_title);
                    for (int i = 0; i < group.getChildCount(); i++) {
                        AppCompatRadioButton rd = (AppCompatRadioButton) group.getChildAt(i);
                        // rd.setChecked(true);
                        assert selected_title != null;
                        rd.setTextColor(mContext.getResources().getColor(R.color.textColorPrimary));
                        if (selected_title.equals(rd.getText().toString())) {
                            rd.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                            // rd.setButtonDrawable(getResources().getDrawable(R.drawable.radio_btn_background));
                            group.setOnCheckedChangeListener(null);
                            rd.setChecked(true);
                            group.setOnCheckedChangeListener(this);
                        }

                    }
                }
            }
        } else {

        }

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myMainActivity = (GrabingInfoActivity) getActivity();
        if (getArguments() != null) sectionNumber = getArguments().getInt("SECTION_NUMBER");
        list = getArguments().getStringArrayList("list");


        if (list != null && list.toString().equals("Salary")) {
            View view = inflater.inflate(R.layout.fragment_salary, container, false);
            hostingNestedScrollLayout = view.findViewById(R.id.hosting_nestedScroll_layout);
            hostingNestedScrollLayout.getBackground().setAlpha(90);
            RangeBar rangeBar = view.findViewById(R.id.rangeSlider);
            TextView min, max;
            min = view.findViewById(R.id.minSal);
            max = view.findViewById(R.id.maxSal);
            rangeBar.setFormatter(new IRangeBarFormatter() {
                @Override
                public String format(String value) {
                    return value + "k";
                }
            });

            rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
                @Override
                public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                    min.setText("Min Salary : " + leftPinValue);
                    max.setText("Max Salary : " + rightPinValue);
                }
            });
            return view;
        }
        if (list != null && list.toString().equals("Cross Check")) {
            View viewLast = inflater.inflate(R.layout.fragment_cross_check, container, false);
            hostingNestedScrollLayout = viewLast.findViewById(R.id.hosting_nestedScroll_layout);
            hostingNestedScrollLayout.getBackground().setAlpha(90);

            mRecyclerView = viewLast.findViewById(R.id.recycler_view_list);
            List<RecycleData> data = new ArrayList<>();
            RecyclerAdapter recyclerAdapter = new RecyclerAdapter(getActivity(), data) {
                @Override
                protected void OnButtonClicked(View v, int position) {
                    //toolbar button clicked
                    if (position == 0) {

                    } else {
                        assert myMainActivity != null;
                        TabLayout.Tab tab = myMainActivity.tabLayout.getTabAt(position - 1);
                        if (tab != null) {
                            tab.select();
                        }
                    }

                }
            };
            //TODO this is not done
            mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
            mRecyclerView.setAdapter(recyclerAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            return viewLast;
        }

        root = inflater.inflate(R.layout.grabbing_info_fragment, container, false);
        group = root.findViewById(R.id.radioGrp);
        hostingNestedScrollLayout = root.findViewById(R.id.hosting_nestedScroll_layout);
        hostingNestedScrollLayout.getBackground().setAlpha(90);
        if (list != null) {
            for (String title : list) {
                AppCompatRadioButton rd = new AppCompatRadioButton(container.getContext());
                generatedId = ViewCompat.generateViewId();
                rd.setId(generatedId);
                idList.add(generatedId);
                rd.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                rd.setText(title);
                rd.setTextColor(mContext.getResources().getColor(R.color.textColorPrimary));
                group.addView(rd);
            }
            if (savedInstanceState != null) {
                Log.w(TAG, savedInstanceState.toString());
            }
        }
        group.setOnCheckedChangeListener(this);
        return root;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnViewClick) context;
            mContext = context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnRadioGroupSelectedListener");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        AppCompatRadioButton
                checked = radioGroup.findViewById(i);
        //  checked.setButtonDrawable(radioGroup.getContext().getResources().getDrawable(R.drawable.radio_btn_background));
        if (getArguments() != null) {
            getArguments().putString("" + sectionNumber, checked.getText().toString());
        }
        listener.onRadioButtonClick(radioGroup.findViewById(i), sectionNumber, list.toString());

    }

    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {
        private final int verticalSpaceHeight;
        VerticalSpaceItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                outRect.bottom = verticalSpaceHeight;
            }
        }
    }
}