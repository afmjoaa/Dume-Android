package io.dume.dume.student.grabingInfo;

import android.app.Dialog;
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
import android.text.InputFilter;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.materialrangebar.IRangeBarFormatter;
import com.appyvet.materialrangebar.RangeBar;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.dume.dume.R;
import io.dume.dume.util.DumeUtils;
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
        NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(Locale.US);

        if (list != null && list.toString().equals("Salary")) {
            View view = inflater.inflate(R.layout.fragment_salary, container, false);
            hostingNestedScrollLayout = view.findViewById(R.id.hosting_nestedScroll_layout);
            hostingNestedScrollLayout.getBackground().setAlpha(90);
            RangeBar rangeBar = view.findViewById(R.id.rangeSlider);
            TextView min, max;
            min = view.findViewById(R.id.minSal);
            max = view.findViewById(R.id.maxSal);
            rangeBar.setFormatter(value -> Integer.parseInt(value) + "k");

            if (myMainActivity.retrivedAction.equals(DumeUtils.STUDENT)) {
                rangeBar.setRangeBarEnabled(true);
                rangeBar.setRangePinsByIndices(0, 9);
                min.setText("Min Salary\n1,000 ৳");
                max.setText("Max Salary\n10,000 ৳");
            } else {
                rangeBar.setRangeBarEnabled(false);
                rangeBar.setSeekPinByIndex(4);
                max.setVisibility(View.GONE);
                min.setText("Salary\n5,000 ৳");
            }

            min.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog d = new Dialog(mContext);
                    d.setTitle("NumberPicker");
                    d.setContentView(R.layout.number_picker_dialog_layout);
                    Button setBtn = (Button) d.findViewById(R.id.set_btn);
                    NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
                    TextView tittleTV = d.findViewById(R.id.sub_text);
                    tittleTV.setText("Minimum Salary");
                    np.setMaxValue(40);
                    np.setMinValue(1);
                    Field f = null;
                    try {
                        f = NumberPicker.class.getDeclaredField("mInputText");
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                    if (f != null) {
                        f.setAccessible(true);
                    }
                    EditText inputText = null;
                    try {
                        inputText = (EditText) f.get(np);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    if (inputText != null) {
                        inputText.setFilters(new InputFilter[0]);
                    }

                    NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
                        @Override
                        public String format(int value) {
                            int temp = value * 1000;
                            String format = currencyInstance.format(temp);
                            return format.substring(1, format.length() - 3) + " ৳";
                        }
                    };
                    np.setFormatter(formatter);
                    np.setWrapSelectorWheel(true);
                    if (myMainActivity.retrivedAction.equals(DumeUtils.STUDENT)) {
                        np.setValue(Integer.parseInt(rangeBar.getLeftPinValue()));
                    } else {
                        np.setValue(Integer.parseInt(rangeBar.getRightPinValue()));
                    }

                    np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                            if (myMainActivity.retrivedAction.equals(DumeUtils.STUDENT)) {
                                String format1 = currencyInstance.format(i1 * 1000);
                                min.setText("Min Salary\n" + format1.substring(1, format1.length() - 3) + " ৳");

                                rangeBar.setRangePinsByIndices(i1 - 1, rangeBar.getRightIndex());
                                rangeBar.setFormatter(value -> Integer.parseInt(value) + "k");
                            } else {
                                tittleTV.setText("Select Salary");
                                String format1 = currencyInstance.format(i1 * 1000);
                                min.setText("Salary\n" + format1.substring(1, format1.length() - 3) + " ৳");
                                rangeBar.setSeekPinByIndex(i1 - 1);
                                rangeBar.setFormatter(value -> Integer.parseInt(value) + "k");
                            }
                        }
                    });

                    setBtn.setOnClickListener(v -> {
                        if (myMainActivity.retrivedAction.equals(DumeUtils.STUDENT)) {
                            String format1 = currencyInstance.format(np.getValue() * 1000);
                            min.setText("Min Salary\n" + format1.substring(1, format1.length() - 3) + " ৳");

                            rangeBar.setRangePinsByIndices(np.getValue() - 1, rangeBar.getRightIndex());
                            rangeBar.setFormatter(value -> Integer.parseInt(value) + "k");
                        } else {
                            tittleTV.setText("Select Salary");
                            String format1 = currencyInstance.format(np.getValue() * 1000);
                            min.setText("Salary\n" + format1.substring(1, format1.length() - 3) + " ৳");
                            rangeBar.setSeekPinByIndex(np.getValue() - 1);
                            rangeBar.setFormatter(value -> Integer.parseInt(value) + "k");
                        }
                        d.dismiss();
                    });
                    d.show();
                }
            });

            max.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog d = new Dialog(mContext);
                    d.setTitle("NumberPicker");
                    d.setContentView(R.layout.number_picker_dialog_layout);
                    Button setBtn = (Button) d.findViewById(R.id.set_btn);
                    NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
                    TextView titleTV = d.findViewById(R.id.sub_text);
                    titleTV.setText("Maximum Salary");
                    np.setMaxValue(40);
                    np.setMinValue(1);
                    Field f = null;
                    try {
                        f = NumberPicker.class.getDeclaredField("mInputText");
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                    if (f != null) {
                        f.setAccessible(true);
                    }
                    EditText inputText = null;
                    try {
                        inputText = (EditText) f.get(np);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    if (inputText != null) {
                        inputText.setFilters(new InputFilter[0]);
                    }
                    NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
                        @Override
                        public String format(int value) {
                            int temp = value * 1000;
                            String format = currencyInstance.format(temp);
                            return format.substring(1, format.length() - 3) + " ৳";
                        }
                    };
                    np.setFormatter(formatter);
                    np.setWrapSelectorWheel(true);
                    //setting the prior value
                    np.setValue(Integer.parseInt(rangeBar.getRightPinValue()));

                    np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                            String format1 = currencyInstance.format(i1 * 1000);
                            max.setText("Max Salary\n" + format1.substring(1, format1.length() - 3) + " ৳");
                            rangeBar.setRangePinsByIndices(rangeBar.getLeftIndex(), i1 - 1);
                            rangeBar.setFormatter(value -> Integer.parseInt(value) + "k");
                        }
                    });
                    setBtn.setOnClickListener(v -> {
                        //tv.setText(String.valueOf(np.getValue()));
                        String format1 = currencyInstance.format(np.getValue() * 1000);
                        max.setText("Max Salary\n" + format1.substring(1, format1.length() - 3) + " ৳");
                        rangeBar.setRangePinsByIndices(rangeBar.getLeftIndex(), np.getValue() - 1);
                        rangeBar.setFormatter(value -> Integer.parseInt(value) + "k");
                        d.dismiss();
                    });
                    d.show();
                }
            });

            rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
                @Override
                public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                    String salaryValue = "";
                    NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(Locale.US);
                    String format = currencyInstance.format(Integer.parseInt(leftPinValue) * 1000);
                    String format1 = currencyInstance.format(Integer.parseInt(rightPinValue) * 1000);
                    if (myMainActivity.retrivedAction.equals(DumeUtils.STUDENT)) {
                        min.setText("Min Salary\n" + format.substring(1, format.length() - 3) + " ৳");
                        max.setText("Max Salary\n" + format1.substring(1, format1.length() - 3) + " ৳");
                        if( Integer.parseInt(leftPinValue)<=Integer.parseInt(rightPinValue) ){
                            salaryValue = leftPinValue + "k - " + rightPinValue + "k";
                        }else {
                            salaryValue = rightPinValue + "k - " + leftPinValue + "k";
                        }
                    } else {
                        min.setText("Salary\n" + format1.substring(1, format1.length() - 3) + " ৳");
                        salaryValue = rightPinValue + "k";
                    }
                    AppCompatRadioButton rd = new AppCompatRadioButton(mContext);
                    rd.setText(salaryValue);
                    listener.onRadioButtonClick(rd, sectionNumber, "justForData");
                }
            });
            if (sectionNumber == 0) {
                if (myMainActivity.hintIdOne.getText().equals("One")) {
                    myMainActivity.hintIdOne.setText("Ex.4k-10k");
                }
            } else if (sectionNumber == 1) {
                if (firstTime) {
                    try {
                        myMainActivity.hintIdOne.setText(myMainActivity.queryList.get(sectionNumber));
                    } catch (IndexOutOfBoundsException error) {
                        Log.w(TAG, error.getLocalizedMessage());
                    }
                    myMainActivity.hintIdTwo.setText("Ex.4k-10k");
                    firstTime = false;
                }
            } else {
                if (firstTime) {
                    try {
                        myMainActivity.hintIdOne.setText(myMainActivity.queryList.get(sectionNumber - 1));
                    } catch (IndexOutOfBoundsException error) {
                        Log.w(TAG, error.getLocalizedMessage());
                    }
                    try {
                        myMainActivity.hintIdTwo.setText(myMainActivity.queryList.get(sectionNumber));
                    } catch (IndexOutOfBoundsException error) {
                        Log.w(TAG, error.getLocalizedMessage());
                    }
                    myMainActivity.hintIdThree.setText("Ex.4k-10k");
                    firstTime = false;
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
                        Toast.makeText(getContext(), "Go back to reselect category", Toast.LENGTH_SHORT).show();
                    } else {
                        assert myMainActivity != null;
                        TabLayout.Tab tab = myMainActivity.tabLayout.getTabAt(position - 1);
                        if (tab != null) {
                            tab.select();
                        }
                    }
                }
            };
            mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE * (int) (mContext.getResources().getDisplayMetrics().density)));
            mRecyclerView.setAdapter(recyclerAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            if (sectionNumber >= 2) {
                if (firstTime) {
                    try {
                        myMainActivity.hintIdOne.setText(myMainActivity.queryList.get(sectionNumber - 1));
                    } catch (IndexOutOfBoundsException error) {
                        Log.w(TAG, error.getLocalizedMessage());
                    }
                    try {
                        myMainActivity.hintIdTwo.setText(myMainActivity.queryList.get(sectionNumber));
                    } catch (IndexOutOfBoundsException error) {
                        Log.w(TAG, error.getLocalizedMessage());
                    }
                    myMainActivity.hintIdThree.setText(String.format("Ex.%s", list.get(0)));
                    firstTime = false;
                    //Log.e(TAG, String.format("Ex.%s", list.get(0)) );
                }
            }
            myMainActivity.hintIdThree.setText("→←");
            return viewLast;
        } else if (list != null && (list.toString().equals("Subject") || list.toString().equals("Field")
                || list.toString().equals("Software") || list.toString().equals("Language") ||
                list.toString().equals("Flavour") || list.toString().equals("Type") ||
                list.toString().equals("Course") || list.toString().equals(" Language ")
                || list.toString().equals("Item"))) {
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
            if (sectionNumber == 0) {
                if (myMainActivity.hintIdOne.getText().equals("One")) {
                    myMainActivity.hintIdOne.setText(String.format("Ex.%s", list.get(0)));
                }
            } else if (sectionNumber == 1) {
                if (firstTime) {
                    try {
                        myMainActivity.hintIdOne.setText(myMainActivity.queryList.get(sectionNumber));
                    } catch (IndexOutOfBoundsException error) {
                        Log.w(TAG, error.getLocalizedMessage());
                    }
                    myMainActivity.hintIdTwo.setText(String.format("Ex.%s", list.get(0)));
                    firstTime = false;
                }
            } else {
                if (firstTime) {
                    try {
                        myMainActivity.hintIdOne.setText(myMainActivity.queryList.get(sectionNumber - 1));
                    } catch (IndexOutOfBoundsException error) {
                        Log.w(TAG, error.getLocalizedMessage());
                    }
                    try {
                        myMainActivity.hintIdTwo.setText(myMainActivity.queryList.get(sectionNumber));
                    } catch (IndexOutOfBoundsException error) {
                        Log.w(TAG, error.getLocalizedMessage());
                    }
                    myMainActivity.hintIdThree.setText(String.format("Ex.%s", list.get(0)));
                    firstTime = false;
                    //Log.e(TAG, String.format("Ex.%s", list.get(0)) );
                }
            }
            return rootLast;
        } else if (list != null && list.toString().equals("Capacity")) {
            View viewCapacity = inflater.inflate(R.layout.fragment_capacity, container, false);
            hostingNestedScrollLayout = viewCapacity.findViewById(R.id.hosting_nestedScroll_layout);
            hostingNestedScrollLayout.getBackground().setAlpha(90);
            RangeBar rangeBar = viewCapacity.findViewById(R.id.rangeSlider);
            TextView min, max;
            min = viewCapacity.findViewById(R.id.minSal);
            max = viewCapacity.findViewById(R.id.maxSal);
            rangeBar.setFormatter(value -> value + "옷");
            rangeBar.setRangePinsByIndices(6, 10);
            //⺅
            min.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog d = new Dialog(mContext);
                    d.setTitle("NumberPicker");
                    d.setContentView(R.layout.number_picker_dialog_layout);
                    Button setBtn = (Button) d.findViewById(R.id.set_btn);
                    NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
                    TextView tittleTV = d.findViewById(R.id.sub_text);
                    tittleTV.setText("Minimum Student Count");
                    np.setMaxValue(29);
                    np.setMinValue(1);
                    Field f = null;
                    try {
                        f = NumberPicker.class.getDeclaredField("mInputText");
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                    if (f != null) {
                        f.setAccessible(true);
                    }
                    EditText inputText = null;
                    try {
                        inputText = (EditText) f.get(np);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    if (inputText != null) {
                        inputText.setFilters(new InputFilter[0]);
                    }

                    NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
                        @Override
                        public String format(int value) {
                            int temp = value+1;
                            return temp + " 옷";
                        }
                    };
                    np.setFormatter(formatter);
                    np.setWrapSelectorWheel(true);
                    np.setValue(Integer.parseInt(rangeBar.getLeftPinValue())-1);

                    np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                            min.setText(String.format("Min Count : %s stu", i1));

                            rangeBar.setRangePinsByIndices(i1 - 1, rangeBar.getRightIndex());
                            rangeBar.setFormatter(value -> value + "옷");
                        }
                    });

                    setBtn.setOnClickListener(v -> {
                        min.setText(String.format("Min Count : %s stu", np.getValue()));

                        rangeBar.setRangePinsByIndices(np.getValue() - 1, rangeBar.getRightIndex());
                        rangeBar.setFormatter(value -> value + "옷");
                        d.dismiss();
                    });
                    d.show();
                }
            });

            max.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog d = new Dialog(mContext);
                    d.setTitle("NumberPicker");
                    d.setContentView(R.layout.number_picker_dialog_layout);
                    Button setBtn = (Button) d.findViewById(R.id.set_btn);
                    NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
                    TextView titleTV = d.findViewById(R.id.sub_text);
                    titleTV.setText("Minimum Student Count");
                    np.setMaxValue(29);
                    np.setMinValue(1);
                    Field f = null;
                    try {
                        f = NumberPicker.class.getDeclaredField("mInputText");
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                    if (f != null) {
                        f.setAccessible(true);
                    }
                    EditText inputText = null;
                    try {
                        inputText = (EditText) f.get(np);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    if (inputText != null) {
                        inputText.setFilters(new InputFilter[0]);
                    }
                    NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
                        @Override
                        public String format(int value) {
                            int temp = value+1;
                            return temp + " 옷";
                        }
                    };
                    np.setFormatter(formatter);
                    np.setWrapSelectorWheel(true);
                    //setting the prior value
                    np.setValue(Integer.parseInt(rangeBar.getRightPinValue())-1);

                    np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                            max.setText(String.format("Max Count : %s stu", i1));
                            rangeBar.setRangePinsByIndices(rangeBar.getLeftIndex(), i1 - 1);
                            rangeBar.setFormatter(value -> value + "옷");
                        }
                    });
                    setBtn.setOnClickListener(v -> {
                        max.setText(String.format("Max Count : %s stu", np.getValue()));
                        rangeBar.setRangePinsByIndices(rangeBar.getLeftIndex(), np.getValue() - 1);
                        rangeBar.setFormatter(value -> value + "옷");
                        d.dismiss();
                    });
                    d.show();
                }
            });

            rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
                @Override
                public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                    min.setText(String.format("Min Count : %s stu", leftPinValue));
                    max.setText(String.format("Max Count : %s stu", rightPinValue));
                    String capacityValue;
                    if( Integer.parseInt(leftPinValue)<=Integer.parseInt(rightPinValue) ){
                        capacityValue = leftPinValue + "옷 - " + rightPinValue + "옷";
                    }else {
                        capacityValue = rightPinValue + "옷 - " + leftPinValue + "옷";
                    }
                    AppCompatRadioButton rd = new AppCompatRadioButton(mContext);
                    rd.setText(capacityValue);
                    listener.onRadioButtonClick(rd, sectionNumber, "justForData");
                }
            });
            if (sectionNumber == 0) {
                if (myMainActivity.hintIdOne.getText().equals("One")) {
                    myMainActivity.hintIdOne.setText(R.string.example_capacity);
                }
            } else if (sectionNumber == 1) {
                if (firstTime) {
                    try {
                        myMainActivity.hintIdOne.setText(myMainActivity.queryList.get(sectionNumber));
                    } catch (IndexOutOfBoundsException error) {
                        Log.w(TAG, error.getLocalizedMessage());
                    }
                    myMainActivity.hintIdTwo.setText(R.string.example_capacity);
                    firstTime = false;
                }
            } else {
                if (firstTime) {
                    try {
                        myMainActivity.hintIdOne.setText(myMainActivity.queryList.get(sectionNumber - 1));
                    } catch (IndexOutOfBoundsException error) {
                        Log.w(TAG, error.getLocalizedMessage());
                    }
                    try {
                        myMainActivity.hintIdTwo.setText(myMainActivity.queryList.get(sectionNumber));
                    } catch (IndexOutOfBoundsException error) {
                        Log.w(TAG, error.getLocalizedMessage());
                    }
                    myMainActivity.hintIdThree.setText(R.string.example_capacity);
                    firstTime = false;
                }
            }
            return viewCapacity;
        }

        root = inflater.inflate(R.layout.grabbing_info_fragment, container, false);
        group = root.findViewById(R.id.radioGrp);
        sectionLevel = root.findViewById(R.id.section_label);
        if (list.toString().equals("Gender")) {
            sectionLevel.setText("Select gender preference");
        } else {
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
        if (sectionNumber == 0) {
            if (myMainActivity.hintIdOne.getText().equals("One")) {
                myMainActivity.hintIdOne.setText(String.format("Ex.%s", list.get(0)));
            }
        } else if (sectionNumber == 1) {
            if (firstTime) {
                try {
                    myMainActivity.hintIdOne.setText(myMainActivity.queryList.get(sectionNumber));
                } catch (IndexOutOfBoundsException error) {
                    Log.w(TAG, error.getLocalizedMessage());
                }
                myMainActivity.hintIdTwo.setText(String.format("Ex.%s", list.get(0)));
                firstTime = false;
            }
        } else {
            if (firstTime) {
                try {
                    myMainActivity.hintIdOne.setText(myMainActivity.queryList.get(sectionNumber - 1));
                } catch (IndexOutOfBoundsException error) {
                    Log.w(TAG, error.getLocalizedMessage());
                }
                try {
                    myMainActivity.hintIdTwo.setText(myMainActivity.queryList.get(sectionNumber));
                } catch (IndexOutOfBoundsException error) {
                    Log.w(TAG, error.getLocalizedMessage());
                }
                myMainActivity.hintIdThree.setText(String.format("Ex.%s", list.get(0)));
                firstTime = false;
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
        //  checked.setButtonDrawable(radioGroup.getActivtiyContext().getResources().getDrawable(R.drawable.radio_btn_background));
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
        if (modifiedMulResult == null) {
            return "null";
        } else {
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

        String[] finalInfo = new String[(queryList.size())];
        for (int i = 0; i < queryListName.size(); i++) {
            finalInfo[i] = queryListName.get(i) + " : " + queryList.get(i);
        }

        for (String title : finalInfo) {
            RecycleData current = new RecycleData();
            current.options = title;
            data.add(current);
        }
        return data;
    }
}