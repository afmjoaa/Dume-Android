package io.dume.dume.student.grabingInfo;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.dume.dume.R;
import io.dume.dume.util.OnViewClick;

public class DataHolderFragment extends Fragment {


    private static final String TAG = "Bal";
    private OnViewClick listener;
    private int sectionNumber = 0;
    private ArrayList<String> list;
    private List<Integer> idList;
    private RadioGroup group;
    private int id;
    private int generatedId;
    private TextView hawwa;
    private LinearLayout root;


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
                        RadioButton rd = (RadioButton) group.getChildAt(i);
                        assert selected_title != null;
                        if (selected_title.equals(rd.getText().toString())) {
                            rd.setTextColor(getResources().getColor(R.color.colorAccent));

                        }

                    }
                }
            }
        } else {

        }
      /*  if (idList != null || isVisibleToUser) {
            assert idList != null;
            group.check(idList.get(2));
        }*/

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = (LinearLayout) inflater.inflate(R.layout.grabbing_info_fragment, container, false);
        group = root.findViewById(R.id.radioGrp);
        hawwa = root.findViewById(R.id.hawwa);
        if (getArguments() != null) sectionNumber = getArguments().getInt("SECTION_NUMBER");
        list = getArguments().getStringArrayList("list");
        if (list != null) {
            for (String title : list) {
                RadioButton rd = new RadioButton(container.getContext());
                generatedId = ViewCompat.generateViewId();
                rd.setId(generatedId);
                idList.add(generatedId);
                rd.setText(title);
                group.addView(rd);
            }
            if (savedInstanceState != null) {
                Log.w(TAG, savedInstanceState.toString());
            }

        }
        group.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton checked = radioGroup.findViewById(i);
            getArguments().putString("" + sectionNumber, checked.getText().toString());
            listener.onRadioButtonClick(radioGroup.findViewById(i), sectionNumber, list.toString());

        });

        return root;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnViewClick) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnRadioGroupSelectedListener");
        }
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int verticalSpaceHeight;

        VerticalSpaceItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {

            if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                outRect.bottom = verticalSpaceHeight;
            }
        }
    }
}