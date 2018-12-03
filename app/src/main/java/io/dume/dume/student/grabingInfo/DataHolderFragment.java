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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.materialrangebar.IRangeBarFormatter;
import com.appyvet.materialrangebar.RangeBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.dume.dume.R;
import io.dume.dume.util.OnViewClick;

public class DataHolderFragment extends Fragment implements RadioGroup.OnCheckedChangeListener,
        CompoundButton.OnCheckedChangeListener {


    private static final String TAG = "GrabingInfoActivity";
    private OnViewClick listener;
    private int sectionNumber = 0;
    private ArrayList<String> list;
    public ArrayList<Integer> idList;
    private RadioGroup group;
    private int id;
    private int generatedId;
    private View root;
    private View rootLast;
    private Context mContext;
    private NestedScrollView hostingNestedScrollLayout;
    private RecyclerView mRecyclerView;
    private GrabingInfoActivity myMainActivity;
    private final int VERTICAL_ITEM_SPACE = -2;
    private final ArrayList<String> grabbedData;
    private RecyclerAdapter recyclerAdapter;
    private LinearLayout hostingChecklistLayout;
    private TextView sectionLevel;
    boolean firstTime = true;


    public DataHolderFragment() {
        idList = new ArrayList<>();
        grabbedData = new ArrayList<>();
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
            } else if (list != null && list.toString().equals("Cross Check")) {
                recyclerAdapter.update(getFinalData());
            } else if (list != null && (list.toString().equals("Subject") || list.toString().equals("Field")
                    || list.toString().equals("Software") || list.toString().equals("Language") ||
                    list.toString().equals("Flavour") || list.toString().equals("Type") ||
                    list.toString().equals("Course") || list.toString().equals(" Language "))) {
                if (hostingChecklistLayout != null) {
                    if (getArguments() != null) {
                        sectionNumber = getArguments().getInt("SECTION_NUMBER");
                        ArrayList<String> retrivedData = getArguments().getStringArrayList("" + sectionNumber) == null ? new ArrayList<>() : getArguments().getStringArrayList("" + sectionNumber);
                        ArrayList<Integer> myidList = getArguments().getIntegerArrayList("id" + sectionNumber) == null ? new ArrayList<>() : getArguments().getIntegerArrayList("id" + sectionNumber);
                        /*if(retrivedData.size() == idList.size()){
                            Log.e(TAG, "done something fuck you");
                        }*/
                        if (!(myidList.size() == 0 || retrivedData.size() == 0)) {
                            for (int i = 0; i < myidList.size(); i++) {
                                CheckBox cb = hostingChecklistLayout.findViewById(idList.get(i));
                                if (retrivedData.get(i).equals(cb.getText().toString())) {
                                    cb.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                                    cb.setOnCheckedChangeListener(null);
                                    cb.setChecked(true);
                                    cb.setOnCheckedChangeListener(this);

                                }

                            }
                        }
                    }
                }
            }
        } else {
            //not visible here
        }

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myMainActivity = (GrabingInfoActivity) getActivity();
        if (getArguments() != null) sectionNumber = getArguments().getInt("SECTION_NUMBER");
        list = getArguments().getStringArrayList("list");
        TextView titleTV;
        idList = new ArrayList<>();

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
                    String salaryValue = leftPinValue + "k - " + rightPinValue + "k";
                    AppCompatRadioButton rd = new AppCompatRadioButton(mContext);
                    rd.setText(salaryValue);
                    listener.onRadioButtonClick(rd, sectionNumber, "justForData");
                }
            });
            if(sectionNumber == 0){
                if(myMainActivity.hintIdOne.getText().equals("One")){
                    myMainActivity.hintIdOne.setText("Ex.4k-10k");
                }
            }else if(sectionNumber == 1){
                if(firstTime){
                    myMainActivity.hintIdOne.setText(myMainActivity.queryList.get(sectionNumber));
                    myMainActivity.hintIdTwo.setText("Ex.4k-10k");
                    firstTime= false;
                }
            }else{
                if(firstTime){
                    myMainActivity.hintIdOne.setText(myMainActivity.queryList.get(sectionNumber-1));
                    myMainActivity.hintIdTwo.setText(myMainActivity.queryList.get(sectionNumber));
                    myMainActivity.hintIdThree.setText("Ex.4k-10k");
                    firstTime= false;
                }
            }
            return view;
        } else if (list != null && list.toString().equals("Cross Check")) {
            View viewLast = inflater.inflate(R.layout.fragment_cross_check, container, false);
            hostingNestedScrollLayout = viewLast.findViewById(R.id.hosting_nestedScroll_layout);
            hostingNestedScrollLayout.getBackground().setAlpha(90);

            mRecyclerView = viewLast.findViewById(R.id.recycler_view_list);
            //toolbar button clicked
            recyclerAdapter = new RecyclerAdapter(getActivity(), getFinalData()) {
                @Override
                protected void OnButtonClicked(View v, int position) {
                    //toolbar button clicked
                    if (position == 0) {

                    } else if (position == 1) {

                    } else {
                        assert myMainActivity != null;
                        TabLayout.Tab tab = myMainActivity.tabLayout.getTabAt(position - 2);
                        if (tab != null) {
                            tab.select();
                        }
                    }

                }
            };
            mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE * (int) (mContext.getResources().getDisplayMetrics().density)));
            mRecyclerView.setAdapter(recyclerAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            if(sectionNumber >= 2){
                if(firstTime){
                    myMainActivity.hintIdOne.setText(myMainActivity.queryList.get(sectionNumber-1));
                    myMainActivity.hintIdTwo.setText(myMainActivity.queryList.get(sectionNumber));
                    myMainActivity.hintIdThree.setText(String.format("Ex.%s", list.get(0)));
                    firstTime= false;
                    //Log.e(TAG, String.format("Ex.%s", list.get(0)) );
                }
            }
            myMainActivity.hintIdThree.setText("→←");
            return viewLast;
        } else if (list != null && (list.toString().equals("Subject") || list.toString().equals("Field")
                || list.toString().equals("Software") || list.toString().equals("Language") ||
                list.toString().equals("Flavour") || list.toString().equals("Type") ||
                list.toString().equals("Course") || list.toString().equals(" Language "))) {
            //end of the nest so send different view here
            //Toast.makeText(mContext, "fucked the end of nest", Toast.LENGTH_SHORT).show();
            myMainActivity.fab.show();
            rootLast = inflater.inflate(R.layout.grabbing_info_end_fragment, container, false);
            hostingChecklistLayout = rootLast.findViewById(R.id.hosting_checklists);
            sectionLevel = rootLast.findViewById(R.id.section_label);
            sectionLevel.setText(String.format("Select %s", list.toString()));
            hostingNestedScrollLayout = rootLast.findViewById(R.id.hosting_nestedScroll_layout);
            hostingNestedScrollLayout.getBackground().setAlpha(90);
            if (list != null) {
                for (String title : list) {
                    CheckBox cb = new CheckBox(container.getContext());
                    generatedId = ViewCompat.generateViewId();
                    cb.setId(generatedId);
                    idList.add(generatedId);
                    cb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    cb.setText(title);
                    cb.setTextColor(mContext.getResources().getColor(R.color.textColorPrimary));
                    cb.setPadding((int) (5 * mContext.getResources().getDisplayMetrics().density),
                            (int) (5 * mContext.getResources().getDisplayMetrics().density),
                            (int) (5 * mContext.getResources().getDisplayMetrics().density),
                            (int) (5 * mContext.getResources().getDisplayMetrics().density));
                    hostingChecklistLayout.addView(cb);
                    cb.setOnCheckedChangeListener(this);
                }
                if (savedInstanceState != null) {
                    Log.w(TAG, savedInstanceState.toString());
                }
                if (getArguments() != null) {
                    getArguments().putIntegerArrayList("id" + sectionNumber, idList);
                }
                for (int i = 0; i < list.size(); i++) {
                    grabbedData.add("null");
                }
            }
            //TODO not finished
            //Log.e(TAG, grabbedData.toString());
            //Log.e(TAG, idList.toString());
            if(sectionNumber == 0){
                if(myMainActivity.hintIdOne.getText().equals("One")){
                    myMainActivity.hintIdOne.setText(String.format("Ex.%s", list.get(0)));
                }
            }else if(sectionNumber == 1){
                if(firstTime){
                    myMainActivity.hintIdOne.setText(myMainActivity.queryList.get(sectionNumber));
                    myMainActivity.hintIdTwo.setText(String.format("Ex.%s", list.get(0)));
                    firstTime= false;
                }
            }else{
                if(firstTime){
                    myMainActivity.hintIdOne.setText(myMainActivity.queryList.get(sectionNumber-1));
                    myMainActivity.hintIdTwo.setText(myMainActivity.queryList.get(sectionNumber));
                    myMainActivity.hintIdThree.setText(String.format("Ex.%s", list.get(0)));
                    firstTime= false;
                    //Log.e(TAG, String.format("Ex.%s", list.get(0)) );
                }
            }
            return rootLast;
        }

        root = inflater.inflate(R.layout.grabbing_info_fragment, container, false);
        group = root.findViewById(R.id.radioGrp);
        sectionLevel = root.findViewById(R.id.section_label);
        if (list.toString().equals("Gender")) {
            sectionLevel.setText("Select gender preference");
        }else{
            sectionLevel.setText(String.format("Select %s", list.toString()));
        }
        hostingNestedScrollLayout = root.findViewById(R.id.hosting_nestedScroll_layout);
        hostingNestedScrollLayout.getBackground().setAlpha(90);
        if (list != null) {
            for (String title : list) {
                AppCompatRadioButton rd = new AppCompatRadioButton(container.getContext());
                generatedId = ViewCompat.generateViewId();
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
        if(sectionNumber == 0){
            if(myMainActivity.hintIdOne.getText().equals("One")){
                myMainActivity.hintIdOne.setText(String.format("Ex.%s", list.get(0)));
            }
        }else if(sectionNumber == 1){
            if(firstTime){
                myMainActivity.hintIdOne.setText(myMainActivity.queryList.get(sectionNumber));
                myMainActivity.hintIdTwo.setText(String.format("Ex.%s", list.get(0)));
                firstTime= false;
            }
        }else{
            if(firstTime){
                myMainActivity.hintIdOne.setText(myMainActivity.queryList.get(sectionNumber-1));
                myMainActivity.hintIdTwo.setText(myMainActivity.queryList.get(sectionNumber));
                myMainActivity.hintIdThree.setText(String.format("Ex.%s", list.get(0)));
                firstTime= false;
                //Log.e(TAG, String.format("Ex.%s", list.get(0)) );
            }
        }
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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = compoundButton.getId();
        String lastOutput = null;
        if (b) {
            grabbedData.set(list.indexOf(compoundButton.getText().toString()), compoundButton.getText().toString());
        } else {
            grabbedData.set(list.indexOf(compoundButton.getText().toString()), "xxx");
        }
        lastOutput = grabbedDataToString(grabbedData);
        Log.e(TAG, grabbedData.toString() + "   : " + lastOutput);
        AppCompatRadioButton rd = new AppCompatRadioButton(mContext);
        rd.setText(lastOutput);

        if (getArguments() != null) {
            getArguments().putStringArrayList("" + sectionNumber, grabbedData);
        }

        listener.onRadioButtonClick(rd, sectionNumber, "justForData");
    }

    public String grabbedDataToString(ArrayList<String> grabbedData) {
        StringBuilder modifiedMulResult = null;
        for (int number = 0; number < grabbedData.size(); number++) {
            if (!(grabbedData.get(number).equals("xxx") || grabbedData.get(number).equals("null"))) {
                if (modifiedMulResult == null) {
                    modifiedMulResult = new StringBuilder(grabbedData.get(number));
                } else {
                    //TODO here something for debug
                    modifiedMulResult.append(", ").append(grabbedData.get(number));
                }
            }
        }
        if(modifiedMulResult == null){
            return "null";
        }else{
            return Objects.requireNonNull(modifiedMulResult).toString();
        }
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

    public List<RecycleData> getFinalData() {
        List<RecycleData> data = new ArrayList<>();

        List<String> queryList = myMainActivity.queryList;
        List<String> queryListName = myMainActivity.queryListName;

        String[] finalInfo = new String[(queryList.size() + 1)];
        for (int i = 0; i < queryListName.size() + 1; i++) {
            if (i == 0) {
                // set the person name for whom it is set
                finalInfo[i] = "For : Joaa";
            } else {
                finalInfo[i] = queryListName.get(i - 1) + " : " + queryList.get(i - 1);
            }
        }

        for (String title : finalInfo) {
            RecycleData current = new RecycleData();
            current.options = title;
            data.add(current);
        }
        return data;
    }
}