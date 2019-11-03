package io.dume.dume.teacher.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import io.dume.dume.R;
import io.dume.dume.teacher.model.KeyValueModel;

public class BasicInfoAdapter extends RecyclerView.Adapter<BasicInfoAdapter.BasicViewHolder> {


    private ArrayList<KeyValueModel> dataMap;

    public BasicInfoAdapter(ArrayList<KeyValueModel> dataMap) {

        this.dataMap = dataMap;
    }

    @NonNull
    @Override
    public BasicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_settings_basic_item, parent, false);
        return new BasicViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull BasicViewHolder holder, int position) {
        holder.title.setText(dataMap.get(position).getTitle());
        holder.value.setText(dataMap.get(position).getValue());
    }

    @Override
    public int getItemCount() {
        return dataMap.size();
    }

    class BasicViewHolder extends RecyclerView.ViewHolder {
        TextView title, value;

        public BasicViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.basicTitle);
            value = itemView.findViewById(R.id.basicValue);
        }
    }
}
