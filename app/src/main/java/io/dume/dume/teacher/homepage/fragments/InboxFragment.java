package io.dume.dume.teacher.homepage.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dume.dume.R;
import io.dume.dume.teacher.adapters.InboxAdapter;
import io.dume.dume.teacher.homepage.TeacherActivtiy;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.pojo.Inbox;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.GridSpacingItemDecoration;

public class InboxFragment extends Fragment {

    private InboxViewModel mViewModel;
    @BindView(R.id.inboxRV)
    RecyclerView inboxRv;
    private TeacherActivtiy teacherActivtiy;
    private static InboxFragment inboxFragment = null;
    private Context context;


    public static InboxFragment getInstance() {
        if (inboxFragment == null) {
            inboxFragment = new InboxFragment();
        }
        return inboxFragment;
    }

    public static InboxFragment newInstance() {
        return new InboxFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.inbox_fragment, container, false);
        ButterKnife.bind(this, root);
        context = getContext();
        inboxRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mViewModel = new InboxViewModel();
        if (teacherActivtiy.teacherDataStore.getDocumentSnapshot() == null) {
            teacherActivtiy.presenter.loadProfile(new TeacherContract.Model.Listener<Void>() {
                @Override
                public void onSuccess(Void list) {
                    explodeData();
                }

                @Override
                public void onError(String msg) {
                    Toast.makeText(teacherActivtiy, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            explodeData();
        }

        return root;
    }

    private void explodeData() {
        String unreadMsg = (String) teacherActivtiy.teacherDataStore.getDocumentSnapshot().get("unread_msg");
        String unreadNoti = (String) teacherActivtiy.teacherDataStore.getDocumentSnapshot().get("unread_noti");
        Map<String, Object> unreadRecords = (Map<String, Object>) teacherActivtiy.teacherDataStore.getDocumentSnapshot().get("unread_records");
        String pendingCount = (String) unreadRecords.get("pending_count");
        String acceptedCount = (String) unreadRecords.get("accepted_count");
        String currentCount = (String) unreadRecords.get("current_count");
        ArrayList<Inbox> arrayList = new ArrayList<>();
        arrayList.add(new Inbox(true, "Unread Messages", Integer.parseInt(unreadMsg)));
        arrayList.add(new Inbox(true, "Unread Notification", Integer.parseInt(unreadNoti)));
        if(Integer.parseInt(pendingCount)>0){
            arrayList.add(new Inbox(true, "Pending Request", Integer.parseInt(pendingCount)));
        }else {
            arrayList.add(new Inbox(false, "Pending Request", Integer.parseInt(pendingCount)));
        }
        if(Integer.parseInt(acceptedCount)>0){
            arrayList.add(new Inbox(true, "Accepted Request", Integer.parseInt(acceptedCount)));
        }else {
            arrayList.add(new Inbox(false, "Accepted Request", Integer.parseInt(acceptedCount)));
        }
        if(Integer.parseInt(currentCount)>0){
            arrayList.add(new Inbox(true, "Current Dume", Integer.parseInt(currentCount)));
        }else {
            arrayList.add(new Inbox(false, "Current Dume", Integer.parseInt(currentCount)));
        }
        showInbox(arrayList);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(InboxViewModel.class);
        // TODO: Use the ViewModel
    }

    public void showInbox(ArrayList<Inbox> list) {
        // feedBackRV.setLayoutManager(new GridLayoutManager(this, 3));
        inboxRv.setAdapter(new InboxAdapter(context,list));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        teacherActivtiy = (TeacherActivtiy) context;
    }
}
