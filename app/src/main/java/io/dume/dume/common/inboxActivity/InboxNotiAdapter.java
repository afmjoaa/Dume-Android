package io.dume.dume.common.inboxActivity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jackandphantom.circularprogressbar.CircleProgressbar;

import java.util.List;

import carbon.widget.ImageView;
import io.dume.dume.R;

public class InboxNotiAdapter extends RecyclerView.Adapter<InboxNotiAdapter.MyViewHolder> {

    private static final String TAG = "InboxNotiAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<InboxNotiData> data;

    public InboxNotiAdapter(Context context, List<InboxNotiData> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_noti_row, parent, false);
        InboxNotiAdapter.MyViewHolder holder = new InboxNotiAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        switch (position) {
            case 1:
                //testing offline
                holder.onlineOrOffline.setForegroundProgressColor(R.color.status_viewed);
                break;
            case 2:
                break;
            case 9:
                holder.onlineOrOffline.setForegroundProgressColor(R.color.status_viewed);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.dividerTwo.getLayoutParams();
                params.leftMargin = (int) (16*context.getResources().getDisplayMetrics().density);
                params.rightMargin = (int) (16*context.getResources().getDisplayMetrics().density);
                break;
        }
        //position == data.size()-1;

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView notiUserDP;
        private final TextView notiUserName;
        private final TextView freqAndTime;
        private final CircleProgressbar onlineOrOffline;
        private final View dividerTwo;

        public MyViewHolder(View itemView) {
            super(itemView);
            notiUserDP = itemView.findViewById(R.id.noti_user_dp);
            notiUserName = itemView.findViewById(R.id.noti_user_name);
            freqAndTime = itemView.findViewById(R.id.frequency_and_time);
            onlineOrOffline = itemView.findViewById(R.id.selected_indicator);
            dividerTwo = itemView.findViewById(R.id.divider2);

        }
    }
}
