package io.dume.dume.teacher.homepage.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dume.dume.R;
import io.dume.dume.teacher.adapters.PayAdapter;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.pojo.Pay;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.GridSpacingItemDecoration;

public class PayFragment extends Fragment {
    @BindView(R.id.payRV)
    RecyclerView payRv;
    private PayViewModel mViewModel;

    public static PayFragment newInstance() {
        return new PayFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.pay_fragment, container, false);
        ButterKnife.bind(this, root);
        if (mViewModel == null) {
            mViewModel = new PayViewModel();
        }
        int[] wh = DumeUtils.getScreenSize(container.getContext());
        int spacing = (int) ((wh[0] - ((336) * (getResources().getDisplayMetrics().density))) / 3);
        payRv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        //testing code
        payRv.addItemDecoration(new GridSpacingItemDecoration(2, spacing, true));
        mViewModel.getPayDetails(new TeacherContract.Model.Listener<ArrayList<Pay>>() {
            @Override
            public void onSuccess(ArrayList<Pay> list) {
                payRv.setAdapter(new PayAdapter(list));
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PayViewModel.class);

    }

}
