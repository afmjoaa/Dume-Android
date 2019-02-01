package io.dume.dume.teacher.homepage.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dume.dume.R;
import io.dume.dume.teacher.adapters.AcademicAdapter;
import io.dume.dume.teacher.pojo.Academic;

public class AcademicFragment extends Fragment {

    private AcademicViewModel mViewModel;
    @BindView(R.id.academicRV)
    RecyclerView academicRV;

    public static AcademicFragment newInstance() {
        return new AcademicFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.academic_fragment, container, false);
        ButterKnife.bind(this, root);

        List<Academic> aData = new ArrayList<>();
        AcademicAdapter academicAdapter = new AcademicAdapter(getContext(), aData);
        academicRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        academicRV.setAdapter(academicAdapter);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AcademicViewModel.class);

    }

}
