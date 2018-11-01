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

public abstract class InboxChatAdapter extends RecyclerView.Adapter<InboxChatAdapter.MyViewHolder> {

    private static final String TAG = "InboxChatAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<InboxChatData> data;

    public InboxChatAdapter(Context context, List<InboxChatData> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_chat_row, parent, false);
        InboxChatAdapter.MyViewHolder holder = new InboxChatAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        switch (position){
            case 1:
                //testing offline
                holder.onlineIndicator.setVisibility(View.GONE);
                holder.offlineIndicator.setVisibility(View.VISIBLE);
                break;
            case 2:
                //testing selected item
                holder.chatUserDP.setHeight((int) (44*context.getResources().getDisplayMetrics().density));
                holder.chatUserDP.setWidth((int) (44*context.getResources().getDisplayMetrics().density));
                break;
            case 4:
                break;
        }

        holder.hostRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnItemClicked(v, holder.getAdapterPosition());
            }
        });

        holder.hostRelativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                OnItemLongClicked(v, holder.getAdapterPosition());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    abstract void OnItemClicked(View v, int position);
    abstract void OnItemLongClicked(View v, int position);

    class MyViewHolder extends RecyclerView.ViewHolder {


        private final RelativeLayout hostRelativeLayout;
        private final ImageView chatUserDP;
        private final CircleProgressbar onlineIndicator;
        private final CircleProgressbar offlineIndicator;
        private final TextView deliveryTime;
        private final TextView unreadCount;
        private final TextView chatUserName;
        private final ImageView deliveryImageAndGroupPersonImage;
        private final ImageView muteCheckerImage;
        private final TextView lastMessage;

        public MyViewHolder(View itemView) {
            super(itemView);
            hostRelativeLayout = itemView.findViewById(R.id.hostRelativeLayout);
            chatUserDP = itemView.findViewById(R.id.chat_user_display_pic);
            onlineIndicator = itemView.findViewById(R.id.online_Indicator);
            offlineIndicator = itemView.findViewById(R.id.offline_Indicator);
            deliveryTime = itemView.findViewById(R.id.delivery_time);
            unreadCount = itemView.findViewById(R.id.unread_count);
            chatUserName = itemView.findViewById(R.id.chat_user_name);
            muteCheckerImage = itemView.findViewById(R.id.active_option_image);
            deliveryImageAndGroupPersonImage = itemView.findViewById(R.id.group_user_dp);
            lastMessage = itemView.findViewById(R.id.last_message);

        }
    }
}
