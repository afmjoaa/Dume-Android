package io.dume.dume.teacher.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.dume.dume.R;
import io.dume.dume.teacher.pojo.Stat;

public abstract class StatAdapter extends RecyclerView.Adapter<StatAdapter.FeedBackVH> {
    public abstract void onItemClick(int position, View view);

    private Stat stat;
    private ViewGroup parent;

    public StatAdapter(Stat stat) {
        super();
        this.stat = stat;

    }

    @NonNull
    @Override
    public FeedBackVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parent = parent;
        View feedBackItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.stat_view_item, parent, false);
        return new FeedBackVH(feedBackItem);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedBackVH holder, int position) {
        holder.view.setOnClickListener(view -> {
            onItemClick(holder.getAdapterPosition(), holder.view);
        });
        holder.valueTV.setText(String.format("%s", position == 0 ? Integer.toString(stat.getImpression()) : Integer.toString(stat.getView())));
        holder.valueTitleTV.setText(String.format("%s", position == 0 ? "Profile Impressions" : "Profile Views"));
    }

    @Override
    public int getItemCount() {
        return 2;
    }


    class FeedBackVH extends RecyclerView.ViewHolder {
        private static final String TAG = "Kflskdflksdf";
        TextView valueTV;
        TextView valueTitleTV;
        View view;

        public FeedBackVH(View itemView) {
            super(itemView);
            valueTitleTV = itemView.findViewById(R.id.reportTitle);
            valueTV = itemView.findViewById(R.id.reportValue);
            this.view = itemView;

        }
    }
}
