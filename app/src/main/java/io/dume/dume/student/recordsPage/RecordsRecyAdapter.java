package io.dume.dume.student.recordsPage;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import carbon.widget.ImageView;
import carbon.widget.RecyclerView;
import io.dume.dume.R;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class RecordsRecyAdapter extends RecyclerView.Adapter<RecordsRecyAdapter.MyViewHolder> {

    private static final String TAG = "RecordsRecyAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<RecordsRecyData> data;

    public RecordsRecyAdapter(Context context, List<RecordsRecyData> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
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
        if(position % 2 == 0){
            holder.relativeHostLayout.setBackgroundColor(context.getResources().getColor(R.color.tabFirstItem));
            //holder.relativeHostLayout.setBackgroundResource(R.drawable.bg_tab_first_color);
        }else {
            holder.relativeHostLayout.setBackgroundColor(context.getResources().getColor(R.color.tabSecondItem));
            //holder.relativeHostLayout.setBackgroundResource(R.drawable.bg_tab_first_color);
        }

    }

    @Override
    public int getItemCount() {
        //testing purpose
        return 10;
    }

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
