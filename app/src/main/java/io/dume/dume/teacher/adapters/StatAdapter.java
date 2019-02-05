package io.dume.dume.teacher.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.dume.dume.R;
import io.dume.dume.teacher.pojo.Stat;

public abstract class StatAdapter extends RecyclerView.Adapter<StatAdapter.FeedBackVH> {
    private final int itemWidth;
    private final Context context;
    private Stat stat;
    private ViewGroup parent;

    public StatAdapter(Context context, int itemWidth, Stat stat) {
        this.stat = stat;
        this.itemWidth = itemWidth;
        this.context=context;
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

        ViewGroup.LayoutParams layoutParams = holder.hostingRelative.getLayoutParams();
        layoutParams.width = (itemWidth);
        holder.hostingRelative.setLayoutParams(layoutParams);

        holder.valueTV.setText(String.format("%s", position == 0 ? Integer.toString(stat.getImpression()) : Integer.toString(stat.getView())));
        holder.valueTitleTV.setText(String.format("%s", position == 0 ? "Profile Impressions" : "Profile Views"));
    }
    public abstract void onItemClick(int position, View view);

    @Override
    public int getItemCount() {
        return 2;
    }

    class FeedBackVH extends RecyclerView.ViewHolder {
        private final TextView valueTV;
        private final TextView valueTitleTV;
        private final carbon.widget.RelativeLayout hostingRelative;

        public FeedBackVH(View itemView) {
            super(itemView);
            valueTitleTV = itemView.findViewById(R.id.reportTitle);
            valueTV = itemView.findViewById(R.id.reportValue);
            hostingRelative = itemView.findViewById(R.id.hosting_relative_layout);

        }
    }
}
