package io.dume.dume.student.studentPayment.adapterAndData;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import carbon.widget.ImageView;
import carbon.widget.LinearLayout;
import carbon.widget.RelativeLayout;
import io.dume.dume.R;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.util.VisibleToggleClickListener;
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

        ObligationAndClaimData current = data.get(i);
        myViewHolder.studentName.setText(current.getStuName());
        myViewHolder.mentorName.setText(current.getMenaName());

        if (current.getStuDP() != null && !current.getStuDP().equals("")) {
            Glide.with(context).load(current.getStuDP()).apply(new RequestOptions().placeholder(R.drawable.demo_default_avatar_dark)).into(myViewHolder.studentDp);
        } else {
            String defaultUrl = SearchDataStore.DEFAULTMALEAVATER;
            Glide.with(context).load(defaultUrl).apply(new RequestOptions().placeholder(R.drawable.demo_default_avatar_dark)).into(myViewHolder.studentDp);
        }
        Glide.with(context).load(current.menDP).into(myViewHolder.mentorDp);

        myViewHolder.menRating.setRating(current.getMenRating());
        myViewHolder.stuRating.setRating(current.getStuRating());
        myViewHolder.subjectInDemand.setText(current.getSubjectInDemand());
        myViewHolder.salaryInDemand.setText(current.salaryInDemand);
        if(current.getPaidAmount().intValue() == -1){
            if(current.getDueAmount().intValue()== 0){
                myViewHolder.dueAmountVal.setText(current.getDueAmount().intValue() + " ৳");
                myViewHolder.dueBlock.setVisibility(View.GONE);
            }else {
                //todo change color and text as well
                myViewHolder.dueAmountTextTV.setText("Mentor Wage");
                myViewHolder.dueBlock.setBackgroundColor(context.getResources().getColor(R.color.payment_wage));
                myViewHolder.dueAmountVal.setText(current.getDueAmount().intValue() + " ৳");
                myViewHolder.dueBlock.setVisibility(View.VISIBLE);
            }
            myViewHolder.paidBlock.setVisibility(View.GONE);
        }else {
            if(current.getDueAmount().intValue()== 0){
                myViewHolder.dueAmountVal.setText(current.getDueAmount().intValue() + " ৳");
                myViewHolder.dueBlock.setVisibility(View.GONE);
            }else {
                myViewHolder.dueAmountTextTV.setText("Due Amount");
                myViewHolder.dueBlock.setBackgroundColor(context.getResources().getColor(R.color.payment_red));
                myViewHolder.dueAmountVal.setText(current.getDueAmount().intValue() + " ৳");
                myViewHolder.dueBlock.setVisibility(View.VISIBLE);
            }
            if(current.getPaidAmount().intValue() == 0){
                myViewHolder.PaidAmountVal.setText(current.getPaidAmount().intValue() + " ৳");
                myViewHolder.paidBlock.setVisibility(View.GONE);
            }else {
                myViewHolder.PaidAmountVal.setText(current.getPaidAmount().intValue() + " ৳");
                myViewHolder.paidBlock.setVisibility(View.VISIBLE);
            }
        }
        myViewHolder.salaryValue.setText(current.salaryInDemand + " ৳");
        myViewHolder.startingValue.setText(current.getStartingDate());
        myViewHolder.finishingValue.setText(current.getFinishingDate());
        myViewHolder.categoryVal.setText(current.getCategoryName());
        myViewHolder.productValue.setText(current.getProductName());

        myViewHolder.host.setOnClickListener(new VisibleToggleClickListener() {

            @SuppressLint("CheckResult")
            @Override
            protected void changeVisibility(boolean visible) {
                TransitionSet set = new TransitionSet()
                        .addTransition(new Fade())
                        .addTransition(new Slide(Gravity.TOP))
                        .setInterpolator(visible ? new LinearOutSlowInInterpolator() : new FastOutLinearInInterpolator())
                        .addListener(new Transition.TransitionListener() {
                            @Override
                            public void onTransitionStart(@NonNull Transition transition) {

                            }

                            @Override
                            public void onTransitionEnd(@NonNull Transition transition) {
                                if (!visible) {
                                    myViewHolder.hideableLayout.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onTransitionCancel(@NonNull Transition transition) {

                            }

                            @Override
                            public void onTransitionPause(@NonNull Transition transition) {

                            }

                            @Override
                            public void onTransitionResume(@NonNull Transition transition) {

                            }
                        });
                TransitionManager.beginDelayedTransition(myViewHolder.hideableLayout, set);
                if (visible) {
                    myViewHolder.hideableLayout.setVisibility(View.VISIBLE);
                } else {
                    myViewHolder.hideableLayout.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.hosting_relative_layout)
        RelativeLayout host;

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

        @BindView(R.id.due_amount_value)
        TextView dueAmountVal;
        @BindView(R.id.paid_amount_value)
        TextView PaidAmountVal;

        @BindView(R.id.subject_in_demand)
        TextView subjectInDemand;
        @BindView(R.id.salary_in_demand)
        TextView salaryInDemand;

        @BindView(R.id.sum_due_block)
        RelativeLayout dueBlock;
        @BindView(R.id.sum_paid_block)
        RelativeLayout paidBlock;

        @BindView(R.id.recycle_hosting_linear)
        LinearLayout hideableLayout;
        @BindView(R.id.salary_due_block)
        android.widget.RelativeLayout salaryDue;
        @BindView(R.id.salary_value)
        TextView salaryValue;

        @BindView(R.id.product_name_block)
        android.widget.RelativeLayout productName;
        @BindView(R.id.product_value)
        TextView productValue;

        @BindView(R.id.starting_date_block)
        android.widget.RelativeLayout startingDate;
        @BindView(R.id.staring_value)
        TextView startingValue;

        @BindView(R.id.finishing_date_block)
        android.widget.RelativeLayout finishingDate;
        @BindView(R.id.finishing_value)
        TextView finishingValue;

        @BindView(R.id.category_block)
        android.widget.RelativeLayout categoryBlock;
        @BindView(R.id.category_value)
        TextView categoryVal;

        @BindView(R.id.due_amount_text)
        TextView dueAmountTextTV;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
