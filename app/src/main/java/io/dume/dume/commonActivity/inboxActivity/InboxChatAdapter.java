package io.dume.dume.commonActivity.inboxActivity;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jackandphantom.circularprogressbar.CircleProgressbar;
import com.stfalcon.chatkit.utils.DateFormatter;

import java.util.Calendar;
import java.util.List;

import carbon.widget.ImageView;
import io.dume.dume.R;
import io.dume.dume.commonActivity.chatActivity.Room;

public abstract class InboxChatAdapter extends RecyclerView.Adapter<InboxChatAdapter.MyViewHolder> {

    private static final String TAG = "InboxChatAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<Room> data;

    public InboxChatAdapter(Context context, List<Room> data) {
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
        Room room = data.get(position);
        holder.chatUserName.setText(room.getOpponentName());
        Glide.with(context).load(room.getOpponentDP()).apply(new RequestOptions().placeholder(R.drawable.avatar)).into(holder.chatUserDP);
        holder.lastMessage.setText(room.getUnreadMsgString());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(room.getLastMsgTime());

        if (android.text.format.DateFormat.is24HourFormat(context)) {
            if(room.getLastMsgTime()!= null)
            holder.deliveryTime.setText(DateFormatter.format(room.getLastMsgTime(), DateFormatter.Template.TIME));
        } else {
            final int intHour = calendar.get(Calendar.HOUR);
            final int intMinute = calendar.get(Calendar.MINUTE);
            final int intAMPM = calendar.get(Calendar.AM_PM);

            String AM_PM;
            if (intAMPM == 0) {
                AM_PM = "AM";
            } else {
                AM_PM = "PM";
            }
            if(room.getLastMsgTime()!= null)
            holder.deliveryTime.setText(String.format("%d:%d %s", intHour, intMinute, AM_PM));
        }

        if(room.getLastMsgTime()!= null){
            holder.deliveryTime.setVisibility(View.VISIBLE);
        }else{
            holder.deliveryTime.setVisibility(View.GONE);
        }

        switch (room.getUnreadMsg()){
            case 0:
                holder.unreadCount.setVisibility(View.GONE);
                holder.deliveryImageAndGroupPersonImage.setVisibility(View.GONE);
                break;
            case 1:
                holder.unreadCount.setVisibility(View.VISIBLE);
                holder.deliveryImageAndGroupPersonImage.setVisibility(View.VISIBLE);
                holder.deliveryImageAndGroupPersonImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_sms_delivered));
                break;
            case 2:
                holder.unreadCount.setVisibility(View.VISIBLE);
                holder.deliveryImageAndGroupPersonImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(room.getOpponentDP()).apply(new RequestOptions().placeholder(R.drawable.avatar)).into(holder.deliveryImageAndGroupPersonImage);
                break;
        }

        if(room.isMute()){
            holder.muteCheckerImage.setVisibility(View.VISIBLE);
        }else {
            holder.muteCheckerImage.setVisibility(View.GONE);
        }

        holder.onlineIndicator.setVisibility(View.VISIBLE);
        holder.offlineIndicator.setVisibility(View.GONE);
       /* switch (position) {
            case 1:
                //testing offline
                break;
            case 2:
                //testing selected item
                //holder.chatUserDP.setHeight((int) (44*context.getResources().getDisplayMetrics().density));
                //holder.chatUserDP.setWidth((int) (44*context.getResources().getDisplayMetrics().density));
                break;
            case 4:
                break;
        }*/

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
        return data.size();
    }

    abstract void OnItemClicked(View v, int position);

    abstract void OnItemLongClicked(View v, int position);

    public void updateInboxChatData(List<Room> newData) {
        data.clear();
        data.addAll(newData);
        this.notifyDataSetChanged();
    }

    public void unSelectAllItem() {
        for (int i = 0; i < 3; i++) {
            /*Wrapper wrapper = items.get(i);
            if (wrapper.isSelected) {
                wrapper.isSelected = false;
                notifyItemChanged(i);
            }*/
        }
    }

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
