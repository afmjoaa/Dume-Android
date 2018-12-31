package io.dume.dume.teacher.homepage.fragments;

import android.arch.lifecycle.ViewModelProviders;
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
import io.dume.dume.teacher.adapters.SkillAdapter;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.GridSpacingItemDecoration;

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

        assert container != null;
        int[] wh = DumeUtils.getScreenSize(container.getContext());
        int spacing = (int) ((wh[0] - ((336) * (getResources().getDisplayMetrics().density))) / 3);
        skillRV.setLayoutManager(new GridLayoutManager(getContext(), 2));
        skillRV.addItemDecoration(new GridSpacingItemDecoration(2, spacing, true));
        skillRV.setAdapter(new SkillAdapter(SkillAdapter.FRAGMENT,null));
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SkillViewModel.class);
        // TODO: Use the ViewModel
    }

}
