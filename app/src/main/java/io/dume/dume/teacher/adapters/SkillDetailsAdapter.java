package io.dume.dume.teacher.adapters;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dume.dume.R;
import io.dume.dume.teacher.model.KeyMap;
import io.dume.dume.teacher.model.KeyValueModel;

public class SkillDetailsAdapter extends RecyclerView.Adapter<SkillDetailsAdapter.SDVH> {

    private ArrayList<KeyMap> list;

    SkillDetailsAdapter(ArrayList<KeyMap> list) {
        this.list = list;
        Log.w("foo", "SkillDetailsAdapter: Detail Adpater Called With List size of : " + this.list.size());
    }

    @NonNull
    @Override
    public SDVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.w("foo", "onCreateViewHolder: ");
        SDVH sdvh = new SDVH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.skill_details_item, viewGroup, false));
        return sdvh;
    }

    @Override
    public void onBindViewHolder(@NonNull SDVH sdvh, int i) {
        sdvh.textView.setText(list.get(i).getTitle());
        sdvh.valueTV.setText(list.get(i).getValue().toString());
        if (list.get(i).getTitle().equals("Feedback")) {
            sdvh.textView.setText("Reviews");
            final Map<String, Object> value = (Map<String, Object>) list.get(i).getValue();
            StringBuilder mString = new StringBuilder();
            for (Map.Entry<String, Object> item : value.entrySet()) {
                mString.append(item.getValue().toString()).append(" - ").append(item.getKey()).append(" \n");
            }
            sdvh.valueTV.setText(mString.toString());
            sdvh.valueTV.setTypeface(sdvh.valueTV.getTypeface(), Typeface.ITALIC);
        }


    }

    @Override
    public int getItemCount() {
        Log.w("foo", "getItemCount: " + list.size());
        return list.size();
    }


    class SDVH extends RecyclerView.ViewHolder {
        @BindView(R.id.keyTV)
        TextView textView;
        @BindView(R.id.valueTV)
        TextView valueTV;

        SDVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
