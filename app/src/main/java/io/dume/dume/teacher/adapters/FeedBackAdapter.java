package io.dume.dume.teacher.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import io.dume.dume.R;
import io.dume.dume.teacher.pojo.Feedback;

public abstract class FeedBackAdapter extends RecyclerView.Adapter<FeedBackAdapter.FeedBackVH> {
    public abstract void onItemClick(int position, View view);

    private ArrayList<Feedback> feedbackArrayList;
    private ViewGroup parent;

    public FeedBackAdapter(ArrayList<Feedback> list) {
        super();
        this.feedbackArrayList = list;

    }

    @NonNull
    @Override
    public FeedBackVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parent = parent;
        View feedBackItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_grid_single_item, parent, false);
        return new FeedBackVH(feedBackItem);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedBackVH holder, int position) {
        holder.view.setOnClickListener(view -> {
            onItemClick(holder.getAdapterPosition(), holder.view);
        });
        holder.valueTV.setText(String.format("%s", feedbackArrayList.get(position).getValue()));
        holder.valueTitleTV.setText(String.format("%s", feedbackArrayList.get(position).getValueTitle()));
    }

    @Override
    public int getItemCount() {
        return feedbackArrayList.size();
    }


    class FeedBackVH extends RecyclerView.ViewHolder {
        private static final String TAG = "Kflskdflksdf";
        TextView valueTV;
        TextView valueTitleTV;
        View view;

        public FeedBackVH(View itemView) {
            super(itemView);
            valueTitleTV = itemView.findViewById(R.id.valueTitleTv);
            valueTV = itemView.findViewById(R.id.valueTV);
            this.view = itemView;

        }
    }
}
