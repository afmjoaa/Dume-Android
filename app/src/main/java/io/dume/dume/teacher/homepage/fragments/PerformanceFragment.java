package io.dume.dume.teacher.homepage.fragments;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import carbon.widget.ImageView;
import io.dume.dume.R;
import io.dume.dume.teacher.adapters.ReportAdapter;
import io.dume.dume.teacher.homepage.TeacherActivtiy;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.homepage.TeacherDataStore;
import io.dume.dume.teacher.homepage.TeacherModel;
import io.dume.dume.teacher.model.KeyValueModel;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.EqualSpacingItemDecoration;
import io.dume.dume.util.GridSpacingItemDecoration;

public class PerformanceFragment extends Fragment {

    //  @BindView(R.id.performanceRV)
    RecyclerView performanceRV;
    private PerformanceViewModel mViewModel;
    private static ReportAdapter reportAdapter;
    private TeacherDataStore teacherDataStore;
    private TeacherActivtiy fragmentActivity;
    private static PerformanceFragment performanceFragment = null;
    private int itemWidth;
    private Context context;
    private RelativeLayout joinedHost;
    private ImageView achievementJoinedImage;
    private RelativeLayout inauguralHost;
    private ImageView achievementInauguralImage;
    private RelativeLayout leadingHost;
    private ImageView achievementLeadingImage;
    private RelativeLayout premierHost;
    private ImageView achievementPremierImage;
    private Dialog dialog;
    private String[] unlockRecipeValue;
    private String[] congratsValue;


    public static PerformanceFragment getInstance() {
        if (performanceFragment == null) {
            performanceFragment = new PerformanceFragment();
        }
        return performanceFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static PerformanceFragment newInstance() {
        return new PerformanceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.performance_fragment, container, false);
        ButterKnife.bind(root);
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_badge_dialogue);
        unlockRecipeValue = context.getResources().getStringArray(R.array.unlock_recipe_value);
        congratsValue = context.getResources().getStringArray(R.array.congrats_value);
        performanceRV = root.findViewById(R.id.performanceRV);
        joinedHost = root.findViewById(R.id.joined_host);
        achievementJoinedImage = root.findViewById(R.id.achievement_joined_image);
        inauguralHost = root.findViewById(R.id.inaugural_host);
        achievementInauguralImage = root.findViewById(R.id.achievement_inaugural_image);
        leadingHost = root.findViewById(R.id.leading_host);
        achievementLeadingImage = root.findViewById(R.id.achievement_leading_image);
        premierHost = root.findViewById(R.id.premier_host);
        achievementPremierImage = root.findViewById(R.id.achievement_premier_image);

        //testing my code here
        assert container != null;
        int[] wh = DumeUtils.getScreenSize(container.getContext());
        float mDensity = getResources().getDisplayMetrics().density;
        int availableWidth = (int) (wh[0] - (93 * mDensity));
        itemWidth = (int) ((availableWidth - (30 * mDensity)) / 2);
        //spacing = (int) ((wh[0] - ((336) * (getResources().getDisplayMetrics().density))) / 4);
        performanceRV.setLayoutManager(new GridLayoutManager(getContext(), 2));
        performanceRV.addItemDecoration(new GridSpacingItemDecoration(2, (int) (10 * mDensity), true));

        fragmentActivity = (TeacherActivtiy) getActivity();
        context = getContext();
        teacherDataStore = fragmentActivity != null ? fragmentActivity.teacherDataStore : null;
        if (teacherDataStore != null) {
            if (teacherDataStore.getDocumentSnapshot() == null) {
                fragmentActivity.presenter.loadProfile(new TeacherContract.Model.Listener<Void>() {
                    @Override
                    public void onSuccess(Void list) {
                        loadData();

                    }

                    @Override
                    public void onError(String msg) {
                        fragmentActivity.flush(msg);
                    }
                });
            } else {
                loadData();
            }
        }
        badgeClickListeners();
        return root;
    }

    private void loadData() {
        if (teacherDataStore.getSelfRating() != null) {
            ArrayList<KeyValueModel> arrayList = new ArrayList<>();
            String totalReview = (String) teacherDataStore.getSelfRating().get("star_count");
            String rating = (String) teacherDataStore.getSelfRating().get("star_rating");
            arrayList.add(new KeyValueModel("Ratings", rating + " ★ /" + totalReview));
            String responseTime = (String) teacherDataStore.getSelfRating().get("response_time");
            arrayList.add(new KeyValueModel("Response Time", responseTime));
            String totalStudents = (String) teacherDataStore.getSelfRating().get("student_guided");
            arrayList.add(new KeyValueModel("Total Students", totalStudents));
            String likeExpertize = (String) teacherDataStore.getSelfRating().get("l_expertise");
            String disLikeExpertize = (String) teacherDataStore.getSelfRating().get("dl_expertise");
            int expertize = (100 * Integer.parseInt(likeExpertize)) / (Integer.parseInt(disLikeExpertize + Integer.parseInt(likeExpertize)));
            arrayList.add(new KeyValueModel("Expertize", "" + expertize + "%"));
            String likeBehaviour = (String) teacherDataStore.getSelfRating().get("l_behaviour");
            String disLikeBehaviour = (String) teacherDataStore.getSelfRating().get("dl_behaviour");
            arrayList.add(new KeyValueModel("Behaviour", "" + (100 * Integer.parseInt(likeBehaviour)) / (Integer.parseInt(disLikeBehaviour) + Integer.parseInt(likeBehaviour)) + "%"));


            Map<String, Object> unread_records = (Map<String, Object>) teacherDataStore.getDocumentSnapshot().get("unread_records");
            final String acceptedCount = (String) unread_records.get("accepted_count");
            final String completedCount = (String) unread_records.get("completed_count");
            final String currentCount = (String) unread_records.get("current_count");
            final String pendingCount = (String) unread_records.get("pending_count");
            final String rejectedCount = (String) unread_records.get("rejected_count");

            arrayList.add(new KeyValueModel("Accept Ratio", ((Integer.parseInt(acceptedCount) + Integer.parseInt(completedCount) + Integer.parseInt(currentCount) + Integer.parseInt(pendingCount) + 1) /
                    (Integer.parseInt(acceptedCount) + Integer.parseInt(completedCount) + Integer.parseInt(currentCount) + Integer.parseInt(pendingCount) + Integer.parseInt(rejectedCount) + 1)) * 100 + "%"
            ));

            performanceRV.setAdapter(new ReportAdapter(context, itemWidth, arrayList));
            if (teacherDataStore.getBadgeList() != null) {
                updateAchievementBadge(teacherDataStore.getBadgeList());
                updateOnlineStatBadge();
            }
        }
    }

    private void updateAchievementBadge(List<Boolean> badgeStatus) {
        if (badgeStatus.get(0)) {
            achievementJoinedImage.setImageResource(R.drawable.ic_badge_joined);
        }
        if (badgeStatus.get(1)) {
            achievementInauguralImage.setImageResource(R.drawable.ic_badge_inaugural);
        }
        if (badgeStatus.get(2)) {
            achievementLeadingImage.setImageResource(R.drawable.ic_badge_leading);
        }
        if (badgeStatus.get(3)) {
            achievementPremierImage.setImageResource(R.drawable.ic_badge_premier);
        }
    }

    private void updateOnlineStatBadge() {
        TeacherModel teacherModel = new TeacherModel(context);
        if (teacherDataStore.getSelfRating() != null) {
            Map<String, Object> unread_records = (Map<String, Object>) teacherDataStore.getDocumentSnapshot().get("unread_records");
            String acceptedCount = (String) unread_records.get("accepted_count");
            String completedCount = (String) unread_records.get("completed_count");
            String currentCount = (String) unread_records.get("current_count");
            String pendingCount = (String) unread_records.get("pending_count");
            String rejectedCount = (String) unread_records.get("rejected_count");
            int accept_ratio = 100 * ((Integer.parseInt(acceptedCount) + Integer.parseInt(completedCount) + Integer.parseInt(currentCount) + Integer.parseInt(pendingCount) + 1) /
                    (Integer.parseInt(acceptedCount) + Integer.parseInt(completedCount) + Integer.parseInt(currentCount) + Integer.parseInt(pendingCount) + Integer.parseInt(rejectedCount) + 1));

            String rating = (String) teacherDataStore.getSelfRating().get("star_rating");
            String totalStudents = (String) teacherDataStore.getSelfRating().get("student_guided");
            String likeExpertize = (String) teacherDataStore.getSelfRating().get("l_expertise");
            String disLikeExpertize = (String) teacherDataStore.getSelfRating().get("dl_expertise");
            int expertize = (100 * Integer.parseInt(likeExpertize)) / (Integer.parseInt(disLikeExpertize + Integer.parseInt(likeExpertize)));
            String likeBehaviour = (String) teacherDataStore.getSelfRating().get("l_behaviour");
            String disLikeBehaviour = (String) teacherDataStore.getSelfRating().get("dl_behaviour");
            int behaviour = (100 * Integer.parseInt(likeBehaviour)) / (Integer.parseInt(disLikeBehaviour + Integer.parseInt(likeBehaviour)));
            List<Boolean> badgeList = teacherDataStore.getBadgeList();
            Boolean inaugural = badgeList.get(1);
            Boolean leading = badgeList.get(2);
            Boolean premier = badgeList.get(3);

            if (Integer.parseInt(currentCount) > 1 || Integer.parseInt(totalStudents) >= 1) {
                //TODO inaugural
                String badgeName = "inaugural";
                if (!inaugural) {
                    teacherModel.updateBadeStatus(badgeName, true, new TeacherContract.Model.Listener<Void>() {
                        @Override
                        public void onSuccess(Void list) {
                            testingCustomDialogue(context.getResources().getDrawable(R.drawable.ic_badge_disable), badgeName, true);
                            achievementInauguralImage.setImageResource(R.drawable.ic_badge_inaugural);
                        }

                        @Override
                        public void onError(String msg) {
                            fragmentActivity.flush("Network err !!");
                        }
                    });

                }
            }

            if (Integer.parseInt(currentCount) > 2 && Integer.parseInt(totalStudents) >= 4 && Float.parseFloat(rating) > 4.70f &&
                    expertize >= 80 && behaviour >= 80 && accept_ratio >= 80) {
                //TOdo leading
                String badgeName = "leading";
                if (!leading) {
                    teacherModel.updateBadeStatus(badgeName, true, new TeacherContract.Model.Listener<Void>() {
                        @Override
                        public void onSuccess(Void list) {
                            testingCustomDialogue(context.getResources().getDrawable(R.drawable.ic_badge_leading), badgeName, true);
                            achievementLeadingImage.setImageResource(R.drawable.ic_badge_leading);

                        }

                        @Override
                        public void onError(String msg) {
                            fragmentActivity.flush("Network err !!");
                        }
                    });
                }
            }

            if (Integer.parseInt(currentCount) > 3 && Integer.parseInt(totalStudents) >= 6 && Float.parseFloat(rating) > 4.80f &&
                    expertize >= 90 && behaviour >= 90 && accept_ratio >= 80) {
                //TOdo Premier
                String badgeName = "premier";
                if (!premier) {
                    teacherModel.updateBadeStatus(badgeName, true, new TeacherContract.Model.Listener<Void>() {
                        @Override
                        public void onSuccess(Void list) {
                            testingCustomDialogue(context.getResources().getDrawable(R.drawable.ic_badge_premier), badgeName, true);
                            achievementPremierImage.setImageResource(R.drawable.ic_badge_premier);
                        }

                        @Override
                        public void onError(String msg) {
                            fragmentActivity.flush("Network err !!");
                        }
                    });
                }
            }
        }
    }

    public void badgeClickListeners() {
        joinedHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = achievementJoinedImage.getDrawable();
                if (teacherDataStore.getBadgeList().get(0)) {
                    testingCustomDialogue(drawable, "joined", true);
                } else {
                    testingCustomDialogue(drawable, "joined", false);
                }
            }
        });
        inauguralHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = achievementInauguralImage.getDrawable();
                if (teacherDataStore.getBadgeList().get(1)) {
                    testingCustomDialogue(drawable, "inaugural", true);
                } else {
                    testingCustomDialogue(drawable, "inaugural", false);
                }
            }
        });
        leadingHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = achievementLeadingImage.getDrawable();
                if (teacherDataStore.getBadgeList().get(2)) {
                    testingCustomDialogue(drawable, "leading", true);
                } else {
                    testingCustomDialogue(drawable, "leading", false);
                }
            }
        });
        premierHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = achievementPremierImage.getDrawable();
                if (teacherDataStore.getBadgeList().get(3)) {
                    testingCustomDialogue(drawable, "premier", true);
                } else {
                    testingCustomDialogue(drawable, "premier", false);
                }
            }
        });
    }

    public void testingCustomDialogue(Drawable drawable, String badgeName, boolean badgeStatus) {
        if (dialog != null) {
            //all find view here
            Button dismissBtn = dialog.findViewById(R.id.dismiss_btn);
            TextView dialogText = dialog.findViewById(R.id.dialog_text);
            TextView dialogTitleText = dialog.findViewById(R.id.dialog_title_text);
            carbon.widget.ImageView dialogImage = dialog.findViewById(R.id.dialog_image);
            dialogText.setGravity(Gravity.START);
            dialogImage.setCornerRadius(0);
            dialogImage.setElevation(0);
            dismissBtn.setText("Gotcha");
            dialogImage.setImageDrawable(drawable);
            dismissBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            switch (badgeName) {
                case "joined":
                    if(badgeStatus){
                        dialogTitleText.setText("Congrats " + TeacherDataStore.getInstance().gettUserName());
                        dialogText.setText(congratsValue[0]);
                    }else {
                        dialogTitleText.setText(R.string.unlock_recipe);
                        dialogText.setText(unlockRecipeValue[0]);
                    }
                    break;
                case "inaugural":
                    if(badgeStatus){
                        dialogTitleText.setText("Congrats " + TeacherDataStore.getInstance().gettUserName());
                        dialogText.setText(congratsValue[1]);
                    }else {
                        dialogTitleText.setText(R.string.unlock_recipe);
                        dialogText.setText(unlockRecipeValue[1]);
                    }
                    break;
                case "leading":
                    if(badgeStatus){
                        dialogTitleText.setText("Congrats " + TeacherDataStore.getInstance().gettUserName());
                        dialogText.setText(congratsValue[2]);
                    }else {
                        dialogTitleText.setText(R.string.unlock_recipe);
                        dialogText.setText(unlockRecipeValue[2]);
                    }
                    break;
                case "premier":
                    if(badgeStatus){
                        dialogTitleText.setText("Congrats " + TeacherDataStore.getInstance().gettUserName());
                        dialogText.setText(congratsValue[3]);
                    }else {
                        dialogTitleText.setText(R.string.unlock_recipe);
                        dialogText.setText(unlockRecipeValue[3]);
                    }
                    break;
            }

            dialog.show();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PerformanceViewModel.class);
    }
}
