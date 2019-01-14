package io.dume.dume.teacher.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import carbon.widget.RelativeLayout;
import io.dume.dume.R;
import io.dume.dume.teacher.pojo.Skill;
import io.dume.dume.teacher.skill.SkillActivity;
import io.dume.dume.util.VisibleToggleClickListener;

public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.SkillVH> {
    public static int FRAGMENT = 1;
    public static int ACTIVITY = 2;
    private final ArrayList<String> endOfNest;
    private int layoutSize;
    private ArrayList<Skill> skillList;
    private View inflate;
    private Context context;
    private String TAG = "SkillAdapter";

    public SkillAdapter(int layoutSize, ArrayList<Skill> skillList) {

        this.layoutSize = layoutSize;
        this.skillList = skillList;
        endOfNest = new ArrayList<>(Arrays.asList("Subject", "Field", "Software", "Language", "Flavour", "Type", "Course", " Language "));

    }

    @NonNull
    @Override
    public SkillVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        if (layoutSize == FRAGMENT) {
            inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.skill_item_small, viewGroup, false);

        } else if (layoutSize == ACTIVITY) {
            inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.skill_item_large, viewGroup, false);

        }
        return new SkillVH(inflate);
    }

    public String getLast(int i) {
        HashMap<String, Object> jizz = skillList.get(i).getJizz();
        for (int j = 0; j < endOfNest.size(); j++) {
            if (jizz.containsKey(endOfNest.get(j))) {
                return endOfNest.get(j);
            }
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SkillVH skillVH, int i) {
        if (layoutSize == ACTIVITY) {
            SkillDetailsAdapter skillDetailsAdapter = new SkillDetailsAdapter(null);
            skillVH.detailsRV.setAdapter(skillDetailsAdapter);
            skillVH.itemView.setOnClickListener(new VisibleToggleClickListener() {

                @SuppressLint("CheckResult")
                @Override
                protected void changeVisibility(boolean visible) {
                    TransitionSet set = new TransitionSet()
                            .addTransition(new Fade())
                            .addTransition(new Slide(Gravity.TOP))
                            .setInterpolator(visible ? new LinearOutSlowInInterpolator() : new FastOutLinearInInterpolator())
                            .addListener(new Transition.TransitionListener() {
                                @Override
                                public void onTransitionStart(@NonNull Transition transition) {

                                }

                                @Override
                                public void onTransitionEnd(@NonNull Transition transition) {
                                    if (!visible) {
                                        skillVH.recycleHostingLinear.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onTransitionCancel(@NonNull Transition transition) {

                                }

                                @Override
                                public void onTransitionPause(@NonNull Transition transition) {

                                }

                                @Override
                                public void onTransitionResume(@NonNull Transition transition) {

                                }
                            });
                   TransitionManager.beginDelayedTransition(skillVH.recycleHostingLinear, set);
                    if (visible) {
                        skillVH.recycleHostingLinear.setVisibility(View.VISIBLE);
                    } else {
                        skillVH.recycleHostingLinear.setVisibility(View.INVISIBLE);
                    }
                }
            });
        } else {
            skillVH.itemView.setOnClickListener(view -> {
                context.startActivity(new Intent(context, SkillActivity.class));
            });
        }
        HashMap<String, Object> jizz = skillList.get(i).getJizz();
        if (getLast(i) != null) {
            final Object o = jizz.get(getLast(i));
            skillVH.skillTitleTV.setText(o.toString() + " | " + jizz.get("Category"));
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(skillList.get(i).getCreation());
        skillVH.salaryTV.setText((int) skillList.get(i).getSalary() + " tk");
        skillVH.switchCompat.setChecked(skillList.get(i).isStatus());
        skillVH.publishDate.setText(dateString);
        skillVH.enrolledTV.setText("Enrolled Students : " + skillList.get(i).getEnrolled());
        skillVH.likeTV.setText((int) (skillList.get(i).getRating() * 100) / 5 + " likes");

        skillVH.moreVertSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, view);
                popup.getMenuInflater().inflate(R.menu.menu_edit_remove, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.action_remove:
                                Toast.makeText(context, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.action_edit:
                                Toast.makeText(context, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (skillList != null) {
            if (layoutSize == FRAGMENT) {
                if (skillList.size() > 4) {
                    return 4;
                } else return skillList.size();
            } else {
                return skillList.size();
            }
        }
        return 0;
    }

    class SkillVH extends RecyclerView.ViewHolder {
        @BindView(R.id.skillDetailsRV)
        RecyclerView detailsRV;
        @BindView(R.id.skillSalaryTV)
        TextView salaryTV;
        @BindView(R.id.skillAddDateTV)
        TextView publishDate;
        @BindView(R.id.skillTitleTV)
        TextView skillTitleTV;
        @BindView(R.id.skillStatus)
        SwitchCompat switchCompat;
        @BindView(R.id.enrolledTV)
        TextView enrolledTV;
        @BindView(R.id.likeTV)
        TextView likeTV;
        @BindView(R.id.skillDots)
        carbon.widget.ImageView moreVertSkill;
        @BindView(R.id.hosting_relative_layout)
        RelativeLayout hostingRelativeLayout;
        @BindView(R.id.recycle_hosting_linear)
        LinearLayout recycleHostingLinear;

        public SkillVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
