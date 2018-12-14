package io.dume.dume.teacher.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.dume.dume.R;

public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.SkillVH> {

    @NonNull
    @Override
    public SkillVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.skill_item_large, viewGroup, false);
        return new SkillVH(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillVH skillVH, int i) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class SkillVH extends RecyclerView.ViewHolder {
        public SkillVH(@NonNull View itemView) {
            super(itemView);
        }
    }
}
