package io.dume.dume.teacher.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dume.dume.R;
import io.dume.dume.teacher.pojo.Skill;
import io.dume.dume.teacher.skill.SkillActivity;

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
            skillVH.itemView.setOnClickListener(view -> {
                skillVH.detailsRV.setVisibility(skillVH.detailsRV.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
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

        public SkillVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
