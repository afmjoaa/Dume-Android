package io.dume.dume.student.studentPayment.adapterAndData;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import carbon.widget.ImageView;
import carbon.widget.RelativeLayout;
import io.dume.dume.R;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ObligationAndClaimAdapter extends RecyclerView.Adapter<ObligationAndClaimAdapter.MyViewHolder> {

    private static final String TAG = "ObligationAndClaimAdapt";
    private LayoutInflater inflater;
    private Context context;
    private List<ObligationAndClaimData> data;

    public ObligationAndClaimAdapter(Context context, List<ObligationAndClaimData> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.custom_obligation_claim_row, viewGroup, false);
        ObligationAndClaimAdapter.MyViewHolder holder = new ObligationAndClaimAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.percent_off_btn)
        TextView title;
        @BindView(R.id.student_display_pic)
        ImageView studentDp;
        @BindView(R.id.mentor_display_pic)
        ImageView mentorDp;
        @BindView(R.id.student_name)
        TextView studentName;
        @BindView(R.id.mentor_name)
        TextView mentorName;

        @BindView(R.id.student_rating_bar)
        MaterialRatingBar stuRating;
        @BindView(R.id.mentor_rating_bar)
        MaterialRatingBar menRating;

        @BindView(R.id.subject_in_demand)
        TextView subjectInDemand;
        @BindView(R.id.salary_in_demand)
        TextView salaryInDemand;

        @BindView(R.id.sum_due_block)
        RelativeLayout dueBlock;
        @BindView(R.id.sum_paid_block)
        RelativeLayout paidBlock;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
