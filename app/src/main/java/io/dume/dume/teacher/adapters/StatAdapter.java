package io.dume.dume.teacher.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.AutocompletePrediction;

import java.util.ArrayList;
import java.util.List;

import io.dume.dume.R;
import io.dume.dume.teacher.pojo.Stat;

public abstract class StatAdapter extends RecyclerView.Adapter<StatAdapter.FeedBackVH> {
    private final int itemWidth;
    private final Context context;
    private List<Stat> stat;
    private ViewGroup parent;

    public StatAdapter(Context context, int itemWidth, List<Stat> stat) {
        this.stat = stat;
        this.itemWidth = itemWidth;
        this.context = context;
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
        holder.valueTV.setText(String.format("%s", position == 0 ? stat.get(0).getRequest_i() : stat.get(0).getRequest_r()));
        holder.valueTitleTV.setText(String.format("%s", position == 0 ? "Profile Impressions" : "Profile Request"));
        switch (position) {
            case 0:
                Integer i = Integer.parseInt(stat.get(1).getRequest_i());
                if(i== null || i == 0){
                    i =1;
                }else{
                    i = Integer.parseInt(stat.get(1).getRequest_i());
                }
                Integer valueStatTVValueI = (100 * (Integer.parseInt(stat.get(0).getRequest_i()) - Integer.parseInt(stat.get(1).getRequest_i())) /
                        i);
                holder.valueStatTV.setText(String.format("  (%s%%)", valueStatTVValueI.toString()));
                break;
            case 1:
                Integer ii = Integer.parseInt(stat.get(1).getRequest_r()) ;
                if(ii == null || ii ==0){
                    ii = 1;
                }else{
                    ii = Integer.parseInt(stat.get(1).getRequest_r());
                }
                Integer valueStatTVValueR = (100 * (Integer.parseInt(stat.get(0).getRequest_r()) - Integer.parseInt(stat.get(1).getRequest_r())) /
                        ii);
                holder.valueStatTV.setText(String.format("  (%s%%)", valueStatTVValueR.toString()));
                break;
            default:
                break;
        }

        holder.hostingRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick(position, view);
            }
        });
    }

    public abstract void onItemClick(int position, View view);

    public void update(List<Stat> newData) {
        this.stat = newData;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    class FeedBackVH extends RecyclerView.ViewHolder {
        private final TextView valueTV;
        private final TextView valueTitleTV;
        private final carbon.widget.RelativeLayout hostingRelative;
        private final TextView valueStatTV;

        public FeedBackVH(View itemView) {
            super(itemView);
            valueTitleTV = itemView.findViewById(R.id.reportTitle);
            valueTV = itemView.findViewById(R.id.reportValue);
            hostingRelative = itemView.findViewById(R.id.hosting_relative_layout);
            valueStatTV = itemView.findViewById(R.id.valueStatTV);

        }
    }
}
