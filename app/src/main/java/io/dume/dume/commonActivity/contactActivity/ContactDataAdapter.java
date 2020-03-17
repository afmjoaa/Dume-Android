package io.dume.dume.commonActivity.contactActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import carbon.widget.ImageView;
import io.dume.dume.util.Google;
import io.dume.dume.R;
import io.dume.dume.commonActivity.chatActivity.ChatActivity;
import io.dume.dume.commonActivity.chatActivity.DemoModel;
import io.dume.dume.teacher.homepage.TeacherContract;

public class ContactDataAdapter extends RecyclerView.Adapter<ContactDataAdapter.MyViewHolder> {

    private static final String TAG = "ContactDataAdapter";
    private final Boolean isFromMsg;
    private LayoutInflater inflater;
    private Context context;
    private List<ContactData> data;
    private int[] statusIcon = {
            R.drawable.ic_task_pending,
            R.drawable.ic_task_accepted,
            R.drawable.ic_task_current
    };
    private String[] statusText;

    public ContactDataAdapter(Context context, List<ContactData> data, Boolean isFromMsg) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
        this.isFromMsg = isFromMsg;
        statusText = context.getResources().getStringArray(R.array.statusText);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_contact_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ContactData single = this.data.get(position);
        Glide.with(context).load(single.getContactUserDP()).into(holder.contactUserDP);
        holder.contactUserName.setText(single.getContactUserName());
        holder.statusText.setText(single.getStatus());
        if (isFromMsg) {
            holder.callBtn.setVisibility(View.GONE);
        } else {
            if (single.getStatus().equals("Current") || single.getStatus().equals("Accepted")) {
                holder.callBtn.setVisibility(View.VISIBLE);
                holder.statusIndicatorImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_call_made_black_24dp));
                holder.statusText.setText(String.format("+88%s", single.getPhone()));
            } else {
                holder.callBtn.setVisibility(View.VISIBLE);
                holder.callBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_inbox_call_instant_disable));
            }
        }
        holder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.hostRelativeLayout.performClick();
            }
        });

        holder.hostRelativeLayout.setOnClickListener(view -> {
            if (isFromMsg) {
                if (single.getStatus().equals("Pending") || single.getStatus().equals("Rejected") || single.getStatus().equals("Completed")) {
                    Toast.makeText(context, "Your request is not accepted or current yet.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, Object> map = new HashMap<>();
                String sp_uid = (String) data.get(position).getRecord().get("sp_uid");
                String sh_uid = (String) data.get(position).getRecord().get("sh_uid");
                Map<String, Object> sp_info = (Map<String, Object>) data.get(position).getRecord().get("sp_info");
                Map<String, Object> sh_info = (Map<String, Object>) data.get(position).getRecord().get("for_whom");
                //mentor
                Map<String, Object> stringHashMap = new HashMap<String, Object>();
                stringHashMap.put("name", sp_info.get("first_name") + " " + sp_info.get("last_name"));
                stringHashMap.put("active", true);
                stringHashMap.put("mute", false);
                stringHashMap.put("dp", (String)sp_info.get("avatar"));
                stringHashMap.put("unread_msg", 0);
                stringHashMap.put("last_msg", "");
                stringHashMap.put("last_msg_time", new Date());

                //student
                Map<String, Object> stringHashMap1 = new HashMap<String, Object>();
                stringHashMap1.put("name", sh_info.get("stu_name"));
                stringHashMap1.put("active", true);
                stringHashMap1.put("mute", false);
                stringHashMap1.put("dp", (String) sh_info.get("stu_photo"));
                stringHashMap1.put("unread_msg", 0);
                stringHashMap1.put("last_msg", "");
                stringHashMap1.put("last_msg_time", new Date());

                map.put(sp_uid.substring(2), stringHashMap);
                map.put(sh_uid.substring(2), stringHashMap1);

                List<String> participants = new ArrayList<>();
                participants.add(sp_uid.substring(2));
                participants.add(sh_uid.substring(2));
                map.put("participants", participants);
                Map<String, Object> typing = new HashMap<>();
                typing.put(sp_uid.substring(2), false);
                typing.put(sh_uid.substring(2), false);
                map.put("typing", typing);
                map.put("expire_date", new Date());
                map.put("expired", false);
                map.put("spam", false);
                List<String> foo = new ArrayList<>((List<String>) map.get("participants"));
                Collections.sort(foo);

                List<String> roomIdList = Google.getInstance().getRoomIdList();
                if (roomIdList != null && roomIdList.contains(foo.get(0).concat(foo.get(1)))) {
                    Log.w(TAG, "onBindViewHolder: Already Have Room");
                    Google.getInstance().setCurrentRoom(foo.get(0).concat(foo.get(1)));
                    android.util.Pair[] pairsPending = new android.util.Pair[3];
                    pairsPending[0] = new android.util.Pair<View, String>(holder.statusIndicatorImage, "tn0ne");
                    pairsPending[1] = new android.util.Pair<View, String>(holder.contactUserDP, "tnTwo");
                    pairsPending[2] = new android.util.Pair<View, String>(holder.contactUserName, "tnFive");
                    ActivityOptions optionsPending = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        optionsPending = ActivityOptions.makeSceneTransitionAnimation((Activity) context, pairsPending);
                    }
                    if (optionsPending != null) {
                        context.startActivity(new Intent(context, ChatActivity.class).setAction("testing"), optionsPending.toBundle());
                        ((Activity) context).finish();
                    } else {
                        context.startActivity(new Intent(context, ChatActivity.class).setAction("testing"));
                        ((Activity) context).finish();
                    }

                } else
                    new DemoModel(context).addRoom(map, new TeacherContract.Model.Listener<Void>() {
                        @Override
                        public void onSuccess(Void list) {
                            Google.getInstance().setCurrentRoom(foo.get(0).concat(foo.get(1)));
                            Intent returnIntent = new Intent();
                            ((Activity) context).setResult(Activity.RESULT_OK,returnIntent);
                            ((Activity) context).finish();
                            //context.startActivity(new Intent(context, InboxActivity.class).setAction("testing"));
                        }

                        @Override
                        public void onError(String msg) {
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                        }
                    });
            } else {
                if (single.getStatus().equals("Pending") || single.getStatus().equals("Rejected") || single.getStatus().equals("Completed")) {
                    Toast toast = Toast.makeText(context, "Your request is not accepted or current yet.", Toast.LENGTH_SHORT);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    if( v != null) v.setGravity(Gravity.CENTER);
                    toast.show();
                    return;
                }
                //Toast.makeText(context, "from call", Toast.LENGTH_SHORT).show();
                Uri u = Uri.parse("tel:" + single.getPhone());
                Intent i = new Intent(Intent.ACTION_DIAL, u);
                context.startActivity(i);
            }
        });


        /*switch (position) {
            case 1:
                //pending test
                holder.statusIndicatorImage.setImageResource(statusIcon[0]);
                holder.statusText.setText(statusText[0]);
                holder.hostRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.status_viewed));
                holder.hostRelativeLayout.setOnClickListener(v -> Toast.makeText(context, "Request not accepted yet", Toast.LENGTH_SHORT).show());
                break;
            case 2:
                //accepted test
                holder.statusIndicatorImage.setImageResource(statusIcon[1]);
                holder.statusText.setText(statusText[1]);
                break;
            case 5:
                //current test
                holder.statusIndicatorImage.setImageResource(statusIcon[2]);
                holder.statusText.setText(statusText[2]);
                break;
            case 9:
                //testing selected item
                holder.contactUserDP.setHeight((int) (44 * context.getResources().getDisplayMetrics().density));
                holder.contactUserDP.setWidth((int) (44 * context.getResources().getDisplayMetrics().density));
                break;
        }*/
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView contactUserDP;
        private final ImageView statusIndicatorImage;
        private final TextView contactUserName;
        private final TextView statusText;
        private final RelativeLayout hostRelativeLayout;
        private final ImageView callBtn;

        public MyViewHolder(View itemView) {
            super(itemView);
            hostRelativeLayout = itemView.findViewById(R.id.hostRelativeLayout);
            contactUserDP = itemView.findViewById(R.id.chat_user_display_pic);
            statusIndicatorImage = itemView.findViewById(R.id.status_indicator_image);
            contactUserName = itemView.findViewById(R.id.chat_user_name);
            statusText = itemView.findViewById(R.id.status_text);
            callBtn = itemView.findViewById(R.id.call_btn_image);

        }
    }
}
