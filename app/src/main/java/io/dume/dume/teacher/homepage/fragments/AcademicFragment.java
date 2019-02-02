package io.dume.dume.teacher.homepage.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dume.dume.R;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.teacher.adapters.AcademicAdapter;
import io.dume.dume.teacher.homepage.TeacherActivtiy;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.homepage.TeacherDataStore;
import io.dume.dume.teacher.mentor_settings.academic.AcademicActivity;
import io.dume.dume.teacher.mentor_settings.basicinfo.EditAccount;
import io.dume.dume.teacher.pojo.Academic;

public class AcademicFragment extends Fragment {

    private AcademicViewModel mViewModel;
    @BindView(R.id.academicRV)
    public RecyclerView academicRV;
    @BindView(R.id.no_data_block)
    LinearLayout noDataBlock;
    @BindView(R.id.more_btn)
    Button openQualificationBtn;
    @BindView(R.id.add_qualification_btn)
    Button addQualificationBtn;
    private TeacherActivtiy fragmentActivity;
    private TeacherDataStore teacherDataStore;
    public AcademicAdapter academicAdapter;
    private static AcademicFragment academicFragment = null;

    public static AcademicFragment getInstance() {
        if (academicFragment == null) {
            academicFragment = new AcademicFragment();
        }
        return academicFragment;
    }

    public static AcademicFragment newInstance() {
        return new AcademicFragment();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (teacherDataStore != null) {
                if (teacherDataStore.getDocumentSnapshot() == null) {
                    fragmentActivity.presenter.loadProfile(new TeacherContract.Model.Listener<Void>() {
                        @Override
                        public void onSuccess(Void list) {
                            if (academicRV.getAdapter() != null) {
                                loadData();
                            } else {
                                academicAdapter = new AcademicAdapter(getContext(), getAcademics(teacherDataStore.getDocumentSnapshot()));
                                academicRV.setAdapter(academicAdapter);
                            }
                        }
                        @Override
                        public void onError(String msg) {
                            fragmentActivity.flush(msg);
                        }
                    });
                } else {
                    if (academicRV.getAdapter() != null) {
                        loadData();
                    } else {
                        academicAdapter = new AcademicAdapter(getContext(), getAcademics(teacherDataStore.getDocumentSnapshot()));
                        academicRV.setAdapter(academicAdapter);
                    }
                }
            }
        } else {
            //not visible here
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.academic_fragment, container, false);
        ButterKnife.bind(this, root);

        addQualificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getContext(), AcademicActivity.class);
                intent.setAction("add");
                startActivityForResult(intent, 1234);
            }
        });
        openQualificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EditAccount.class).setAction("scroll_down"));
            }
        });
        academicRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        fragmentActivity = (TeacherActivtiy) getActivity();
        teacherDataStore = fragmentActivity != null ? fragmentActivity.teacherDataStore : null;
        if (teacherDataStore != null) {
            if (teacherDataStore.getDocumentSnapshot() == null) {
                fragmentActivity.presenter.loadProfile(new TeacherContract.Model.Listener<Void>() {
                    @Override
                    public void onSuccess(Void list) {
                        if (academicRV.getAdapter() != null) {
                            loadData();
                        } else {
                            academicAdapter = new AcademicAdapter(getContext(), getAcademics(teacherDataStore.getDocumentSnapshot()));
                            academicRV.setAdapter(academicAdapter);
                        }
                    }
                    @Override
                    public void onError(String msg) {
                        fragmentActivity.flush(msg);
                    }
                });
            } else {
                if (academicRV.getAdapter() != null) {
                    loadData();
                } else {
                    academicAdapter = new AcademicAdapter(getContext(), getAcademics(teacherDataStore.getDocumentSnapshot()));
                    academicRV.setAdapter(academicAdapter);
                }
            }
        }
        return root;
    }

    public void loadData() {
        if (teacherDataStore.getDocumentSnapshot() != null) {
            List<Academic> academicList = new ArrayList<>();
            Map<String, Map<String, Object>> academicMap = (Map<String, Map<String, Object>>) teacherDataStore.getDocumentSnapshot().get("academic");
            if (academicMap != null && academicMap.size() > 0) {
                for (Map.Entry<String, Map<String, Object>> entry : academicMap.entrySet()) {
                    String level = (String) entry.getValue().get("level");
                    String institution = (String) entry.getValue().get("institution");
                    String degree = (String) entry.getValue().get("degree");
                    String from_year = (String) entry.getValue().get("from_year");
                    String to_year = (String) entry.getValue().get("to_year");
                    String result = (String) entry.getValue().get("result");
                    Academic academic = new Academic(level, institution, degree, from_year, to_year, result);
                    academicList.add(academic);
                }
            }
            academicAdapter.update(academicList);
            if (academicList.size() == 0) {
                noDataBlock.setVisibility(View.VISIBLE);
            } else {
                noDataBlock.setVisibility(View.GONE);
            }
        }
    }

    @NonNull
    public List<Academic> getAcademics(Map<String, Object> documentSnapshot) {
        List<Academic> arrayList = new ArrayList<>();
        Map<String, Map<String, Object>> academicMap = (Map<String, Map<String, Object>>) documentSnapshot.get("academic");
        if (academicMap != null && academicMap.size() > 0) {
            for (Map.Entry<String, Map<String, Object>> entry : academicMap.entrySet()) {
                String level = (String) entry.getValue().get("level");
                String institution = (String) entry.getValue().get("institution");
                String degree = (String) entry.getValue().get("degree");
                String from_year = (String) entry.getValue().get("from_year");
                String to_year = (String) entry.getValue().get("to_year");
                String result = (String) entry.getValue().get("result");
                Academic academic = new Academic(level, institution, degree, from_year, to_year, result);
                arrayList.add(academic);
            }
        }
        if (arrayList.size() == 0) {
            noDataBlock.setVisibility(View.VISIBLE);
        } else {
            noDataBlock.setVisibility(View.GONE);
        }
        return arrayList;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AcademicViewModel.class);

    }

}
