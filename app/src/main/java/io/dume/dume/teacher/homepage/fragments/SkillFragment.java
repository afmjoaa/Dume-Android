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

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dume.dume.R;
import io.dume.dume.teacher.adapters.SkillAdapter;

public class SkillFragment extends Fragment {

    private SkillViewModel mViewModel;
    @BindView(R.id.skillRV)
    RecyclerView skillRV;

    public static SkillFragment newInstance() {
        return new SkillFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.skill_fragment, container, false);
        ButterKnife.bind(this, root);
        skillRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        skillRV.setAdapter(new SkillAdapter(SkillAdapter.FRAGMENT));
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SkillViewModel.class);
        // TODO: Use the ViewModel
    }

}
