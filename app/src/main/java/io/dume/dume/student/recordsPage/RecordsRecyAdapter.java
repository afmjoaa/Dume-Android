package io.dume.dume.student.recordsPage;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.stfalcon.chatkit.utils.DateFormatter;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import carbon.widget.ImageView;
import carbon.widget.RecyclerView;
import io.dume.dume.R;
import io.dume.dume.student.pojo.SearchDataStore;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static com.facebook.FacebookSdk.getApplicationContext;

public abstract class RecordsRecyAdapter extends RecyclerView.Adapter<RecordsRecyAdapter.MyViewHolder> {

    private static final String TAG = "InboxChatAdapter";
    private final float mDensity;
    private LayoutInflater inflater;
    private Context context;
    private List<Record> data;
    private String defaultUrl;

    public RecordsRecyAdapter(Context context, List<Record> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
        mDensity = context.getResources().getDisplayMetrics().density;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_records_row, parent, false);
        RecordsRecyAdapter.MyViewHolder holder = new RecordsRecyAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Record current = data.get(position);
        if (position % 2 == 0) {
            holder.relativeHostLayout.setBackgroundColor(context.getResources().getColor(R.color.tabFirstItem));
        } else {
            holder.relativeHostLayout.setBackgroundColor(context.getResources().getColor(R.color.tabSecondItem));
        }
        holder.mentorName.setText(data.get(position).getMentorName());
        holder.studentName.setText(data.get(position).getStudentName());
        String salaryInDemand = data.get(position).getSalaryInDemand();
        salaryInDemand = NumberFormat.getCurrencyInstance(Locale.US).format(Double.parseDouble(salaryInDemand));
        holder.salaryInDemand.setText(salaryInDemand.substring(1, salaryInDemand.length() - 3) + " BDT");
        holder.subjectInDemand.setText(current.getSubjectExchange());
        Calendar calendar = Calendar.getInstance();
        if (current.getStatus().equals("Pending")) {
            calendar.setTime(current.getDate());
            String timeFormatted = null;
            if (android.text.format.DateFormat.is24HourFormat(context)) {
                timeFormatted = DateFormatter.format(current.getDate(), DateFormatter.Template.TIME);
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
                timeFormatted = String.format("%d:%d %s", intHour, intMinute, AM_PM);
            }
            String dateFormatted = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM).format(current.getDate().getTime());
            holder.deliveryTime.setText(dateFormatted + " " + timeFormatted);
        } else {

            calendar.setTime(current.getModiDate() == null ? new Date() : current.getModiDate());
            String timeFormatted = null;
            if (android.text.format.DateFormat.is24HourFormat(context)) {
                timeFormatted = DateFormatter.format(current.getModiDate(), DateFormatter.Template.TIME);
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
                timeFormatted = String.format("%d:%d %s", intHour, intMinute, AM_PM);
            }
            String dateFormatted = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM).format(current.getModiDate().getTime());
            holder.deliveryTime.setText(dateFormatted + " " + timeFormatted);
        }
        holder.relativeHostLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnItemClicked(v, position);
            }
        });

        holder.relativeHostLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                OnItemLongClicked(v, holder.getAdapterPosition());
                return true;
            }
        });

        if (current.getStudentDpUrl() != null && !current.getStudentDpUrl().equals("")) {
            Glide.with(getApplicationContext()).load(current.getStudentDpUrl()).apply(new RequestOptions().override((int) (50 * mDensity), (int) (50 * mDensity)).placeholder(R.drawable.demo_default_avatar_dark)).into(holder.studentDisplayPic);
        } else {
            if (current.sGender != null || current.sGender.equals("Male") || current.sGender.equals("")) {
                defaultUrl = SearchDataStore.DEFAULTMALEAVATER;
            } else {
                defaultUrl = SearchDataStore.DEFAULTFEMALEAVATER;
            }
            Glide.with(getApplicationContext()).load(defaultUrl).apply(new RequestOptions().override((int) (50 * mDensity), (int) (50 * mDensity)).placeholder(R.drawable.demo_default_avatar_dark)).into(holder.studentDisplayPic);
        }

        Glide.with(context).load(current.getMentorDpUrl()).into(holder.mentorDisplayPic);

        holder.mentorRatingBar.setRating(current.getMentorRating());
        holder.studentRatingBar.setRating(current.getStudentRating());
    }

    @Override
    public int getItemCount() {
        //testing purpose
        return data.size();
    }

    abstract void OnItemClicked(View v, int position);

    abstract void OnItemLongClicked(View v, int position);

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final android.widget.ImageView deliveryImageStatus;
        private final ImageView studentDisplayPic;
        private final ImageView mentorDisplayPic;
        private final TextView studentName;
        private final TextView mentorName;
        private final MaterialRatingBar studentRatingBar;
        private final MaterialRatingBar mentorRatingBar;
        private final TextView subjectInDemand;
        private final TextView salaryInDemand;
        private final TextView deliveryTime;
        private final RelativeLayout relativeHostLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            relativeHostLayout = itemView.findViewById(R.id.recordsHostLayout);
            studentDisplayPic = itemView.findViewById(R.id.student_display_pic);
            mentorDisplayPic = itemView.findViewById(R.id.mentor_display_pic);
            studentName = itemView.findViewById(R.id.student_name);
            mentorName = itemView.findViewById(R.id.mentor_name);
            studentRatingBar = itemView.findViewById(R.id.student_rating_bar);
            mentorRatingBar = itemView.findViewById(R.id.mentor_rating_bar);
            subjectInDemand = itemView.findViewById(R.id.subject_in_demand);
            salaryInDemand = itemView.findViewById(R.id.salary_in_demand);
            deliveryTime = itemView.findViewById(R.id.delivery_time);
            deliveryImageStatus = itemView.findViewById(R.id.delivery_status_image_view);

        }
    }

}
