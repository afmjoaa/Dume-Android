package io.dume.dume.teacher.homepage.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dume.dume.R;
import io.dume.dume.teacher.adapters.InboxAdapter;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.pojo.Inbox;

public class InboxFragment extends Fragment {

    private InboxViewModel mViewModel;
    @BindView(R.id.inboxRV)
    RecyclerView inboxRv;

    public static InboxFragment newInstance() {
        return new InboxFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.inbox_fragment, container, false);
        ButterKnife.bind(this, root);
        mViewModel = new InboxViewModel();
        mViewModel.getInbox(new TeacherContract.Model.Listener<ArrayList<Inbox>>() {

            @Override
            public void onSuccess(ArrayList<Inbox> list) {
                Log.w("BAL", "onSuccess: ");
                showInbox(list);
            }

            @Override
            public void onError(String msg) {

            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(InboxViewModel.class);
        // TODO: Use the ViewModel
    }

    public void showInbox(ArrayList<Inbox> list) {
        // feedBackRV.setLayoutManager(new GridLayoutManager(this, 3));
        inboxRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        inboxRv.setAdapter(new InboxAdapter(list));
    }


}
