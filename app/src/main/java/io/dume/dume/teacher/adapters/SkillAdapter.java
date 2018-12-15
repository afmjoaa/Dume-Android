package io.dume.dume.teacher.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dume.dume.R;

public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.SkillVH> {
    public static int FRAGMENT = 1;
    public static int ACTIVITY = 2;
    private int layoutSize;
    private View inflate;


    public SkillAdapter(int layoutSize) {

        this.layoutSize = layoutSize;
    }

    @NonNull
    @Override
    public SkillVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (layoutSize == FRAGMENT) {
            inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.skill_item_small, viewGroup, false);

        } else if (layoutSize == ACTIVITY) {
            inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.skill_item_large, viewGroup, false);

        }
        return new SkillVH(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillVH skillVH, int i) {
        skillVH.itemView.setOnClickListener(view -> {
            skillVH.detailsRV.setVisibility(skillVH.detailsRV.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class SkillVH extends RecyclerView.ViewHolder {
        @BindView(R.id.skillToggleButtonIV)
        ImageView toggleButton;

        @BindView(R.id.skillDetailsRV)
        RecyclerView detailsRV;

        public SkillVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
