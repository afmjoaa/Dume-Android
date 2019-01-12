package io.dume.dume.teacher.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dume.dume.R;
import io.dume.dume.teacher.model.KeyValueModel;

public class SkillDetailsAdapter extends RecyclerView.Adapter<SkillDetailsAdapter.SDVH> {

    ArrayList<HashMap<String, Object>> list;

    public SkillDetailsAdapter(ArrayList<HashMap<String, Object>> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public SDVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new SDVH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.skill_details_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SDVH sdvh, int i) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SDVH extends RecyclerView.ViewHolder {
        @BindView(R.id.keyTV)
        TextView textView;
        @BindView(R.id.valueTV)
        TextView valueTV;

        public SDVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
