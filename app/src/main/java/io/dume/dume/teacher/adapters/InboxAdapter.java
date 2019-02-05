package io.dume.dume.teacher.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import carbon.widget.RelativeLayout;
import io.dume.dume.R;
import io.dume.dume.teacher.pojo.Inbox;

import static io.dume.dume.util.DumeUtils.setMargins;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxVH> {
    private final float mDensity;
    private ArrayList<Inbox> list;
    private Context context;

    public InboxAdapter(Context context, ArrayList<Inbox> list) {
        this.list = list;
        mDensity = context.getResources().getDisplayMetrics().density;
    }

    @NonNull
    @Override
    public InboxVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_inbox_item, parent, false);
        return new InboxVH(item);
    }

    @Override
    public void onBindViewHolder(@NonNull InboxVH holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.value.setText("" + list.get(position).getUnreadNumber());
        if (list.get(position).isUnread()) {
            holder.value.setBackground(context.getResources().getDrawable(R.drawable.border_background));
            holder.value.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }

        if (position == (list.size() - 1)) {
            setMargins(holder.hostingRelative, 12,12,12,12);
        }
    }

    @Override
    public int getItemCount() {
        Log.w("BAL", "getItemCount: " + list.size());
        return list.size();
    }

    class InboxVH extends RecyclerView.ViewHolder {
        TextView title, value;
        private final RelativeLayout hostingRelative;

        public InboxVH(View itemView) {
            super(itemView);
            hostingRelative = itemView.findViewById(R.id.hosting_relative_layout);
            title = itemView.findViewById(R.id.inboxTitleTV);
            value = itemView.findViewById(R.id.inboxValueTV);
        }
    }
}
