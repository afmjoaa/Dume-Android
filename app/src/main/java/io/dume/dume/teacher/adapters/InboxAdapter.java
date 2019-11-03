package io.dume.dume.teacher.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import carbon.widget.RelativeLayout;
import io.dume.dume.R;
import io.dume.dume.common.inboxActivity.InboxActivity;
import io.dume.dume.student.recordsPage.RecordsPageActivity;
import io.dume.dume.teacher.pojo.Inbox;
import io.dume.dume.util.DumeUtils;

import static io.dume.dume.util.DumeUtils.setMargins;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxVH> {
    private final float mDensity;
    private ArrayList<Inbox> list;
    private Context context;

    public InboxAdapter(Context context, ArrayList<Inbox> list) {
        this.list = list;
        this.context = context;
        mDensity = context.getResources().getDisplayMetrics().density;
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
        switch (list.get(position).getTitle()) {
            case "Unread Messages":
            case "Unread Notification":
                if (list.get(position).getUnreadNumber() == 0) {
                    holder.value.setText("⎗");
                } else {
                    holder.value.setText("" + list.get(position).getUnreadNumber());
                }
                break;
            default:
                holder.value.setText("" + list.get(position).getUnreadNumber());

        }
        if (list.get(position).isUnread()) {
            holder.value.setBackground(context.getResources().getDrawable(R.drawable.border_background));
            holder.value.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }

        if (position == (list.size() - 1)) {
            setMargins(holder.hostingRelative, 12, 12, 12, 12);
        }

        holder.hostingRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position) {
                    case 0:
                        context.startActivity(new Intent(context, InboxActivity.class));
                        break;
                    case 1:
                        Intent notificationTabIntent = new Intent(context, InboxActivity.class);
                        notificationTabIntent.putExtra("notiTab", 1);
                        context.startActivity(notificationTabIntent);
                        break;
                    case 2:
                        Intent pendingIntent = new Intent(context, RecordsPageActivity.class).setAction(DumeUtils.TEACHER);
                        pendingIntent.putExtra("seletedTab", 0);
                        context.startActivity(pendingIntent);
                        break;
                    case 3:
                        Intent acceptedIntent = new Intent(context, RecordsPageActivity.class).setAction(DumeUtils.TEACHER);
                        acceptedIntent.putExtra("seletedTab", 1);
                        context.startActivity(acceptedIntent);
                        break;
                    case 4:
                        Intent currentIntent = new Intent(context, RecordsPageActivity.class).setAction(DumeUtils.TEACHER);
                        currentIntent.putExtra("seletedTab", 2);
                        context.startActivity(currentIntent);
                        break;
                    default:
                        break;
                }
            }
        });
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
