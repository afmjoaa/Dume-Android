package io.dume.dume.teacher.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
            skillVH.itemView.setOnClickListener(view -> {
                skillVH.detailsRV.setVisibility(skillVH.detailsRV.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            });
            HashMap<String, Object> jizz = skillList.get(i).getJizz();
            if (getLast(i) != null) {
                final Object o = jizz.get(getLast(i));
                skillVH.skillTitleTV.setText(o.toString());
            }


        } else {
            skillVH.itemView.setOnClickListener(view -> {
                context.startActivity(new Intent(context, SkillActivity.class));
            });
        }
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (skillList != null) {
            if (layoutSize == FRAGMENT) {
                if (skillList.size() > 4) {
                    size = 4;
                } else size = skillList.size();
            } else {
                return size = skillList.size();
            }

        }
        return size;
    }

    class SkillVH extends RecyclerView.ViewHolder {
        @BindView(R.id.skillToggleButtonIV)
        ImageView toggleButton;

        @BindView(R.id.skillDetailsRV)
        RecyclerView detailsRV;

        @BindView(R.id.skillTitleTV)
        TextView skillTitleTV;
        @BindView(R.id.skillStatus)
        SwitchCompat switchCompat;

        public SkillVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
