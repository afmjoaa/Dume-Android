package io.dume.dume.common.inboxActivity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jackandphantom.circularprogressbar.CircleProgressbar;

import java.util.List;

import carbon.widget.ImageView;
import io.dume.dume.R;

public class InboxNotiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "InboxNotiAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<InboxNotiData> data;

    public InboxNotiAdapter(Context context, List<InboxNotiData> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 9998;
        } else if (position == 4) {
            return 9999;
        }//data.get(position).typeView
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.inbox_notification_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        InboxNotiData item = data.get(position);
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.onlineOrOffline.setForegroundProgressColor(R.color.status_viewed);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) myViewHolder.dividerTwo.getLayoutParams();
        params.leftMargin = (int) (16 * context.getResources().getDisplayMetrics().density);
        params.rightMargin = (int) (16 * context.getResources().getDisplayMetrics().density);

        if (!item.isSeen()) {
            myViewHolder.host.setBackgroundColor(context.getResources().getColor(R.color.colorNarvik));
        }
        myViewHolder.notiUserName.setText(item.getTitle());
        myViewHolder.freqAndTime.setText(item.getBody());



        //position == data.size()-1;

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView notiUserDP;
        private final TextView notiUserName;
        private final TextView freqAndTime;
        private final CircleProgressbar onlineOrOffline;
        private final View dividerTwo;
        private final RelativeLayout host;

        public MyViewHolder(View itemView) {
            super(itemView);
            notiUserDP = itemView.findViewById(R.id.noti_user_dp);
            notiUserName = itemView.findViewById(R.id.noti_user_name);
            freqAndTime = itemView.findViewById(R.id.frequency_and_time);
            onlineOrOffline = itemView.findViewById(R.id.selected_indicator);
            dividerTwo = itemView.findViewById(R.id.divider2);
            host = itemView.findViewById(R.id.hostRelativeLayout);
        }
    }

    class HeaderVH extends RecyclerView.ViewHolder {

        private final TextView headerText;

        public HeaderVH(View itemView) {
            super(itemView);
            headerText = itemView.findViewById(R.id.recent_updates);
        }
    }
}
