package io.dume.dume.teacher.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import io.dume.dume.R;
import io.dume.dume.teacher.pojo.Inbox;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxVH> {
    private ArrayList<Inbox> list;

    public InboxAdapter(ArrayList<Inbox> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public InboxVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_inbox_item, parent, false);
        return new InboxVH(item);
    }

    @Override
    public void onBindViewHolder(@NonNull InboxVH holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.value.setText(""+list.get(position).getUnreadNumber());
        if (list.get(position).isUnread()) {
            holder.value.setBackgroundColor(Color.RED);
            holder.value.setTextColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        Log.w("BAL", "getItemCount: "+list.size() );
        return list.size();
    }

    class InboxVH extends RecyclerView.ViewHolder {
        TextView title, value;

        public InboxVH(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.inboxTitleTV);
            value = itemView.findViewById(R.id.inboxValueTV);

        }
    }
}
