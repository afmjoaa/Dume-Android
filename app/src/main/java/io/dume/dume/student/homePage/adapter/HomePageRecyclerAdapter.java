package io.dume.dume.student.homePage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.ArrayList;
import java.util.List;

import io.dume.dume.R;
import io.dume.dume.student.homePage.HomePageActivity;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class HomePageRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "HomePageRecyclerAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<HomePageRecyclerData> data;
    private String[] feedbackStrings;


    public HomePageRecyclerAdapter(Context context, List<HomePageRecyclerData> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
        feedbackStrings = context.getResources().getStringArray(R.array.review_hint_text_dependent);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 9999;
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 9999) {
            View view = inflater.inflate(R.layout.custom_rating_dialogue, parent, false);
            return new RatingVH(view);
        }
        View view = inflater.inflate(R.layout.custom_hp_bsr_row, parent, false);
        HomePageRecyclerAdapter.MyViewHolder holder = new HomePageRecyclerAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 9999) {
            RatingVH headerVH = (RatingVH) holder;
            headerVH.smallTitle.setVisibility(View.VISIBLE);
            headerVH.dismissBtn.setVisibility(View.GONE);
            headerVH.dismissBtnOne.setVisibility(View.GONE);
            //button margin fix
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) headerVH.nextSubmitBtn.getLayoutParams();
            params.setMargins(0, 0, (int) (20 * (context.getResources().getDisplayMetrics().density)), (int) (10 * (context.getResources().getDisplayMetrics().density)));
            headerVH.nextSubmitBtn.setLayoutParams(params);

            //changing margin for image view
            RelativeLayout.LayoutParams lp =(RelativeLayout.LayoutParams) headerVH.ratedMentorDP.getLayoutParams();
            lp.setMargins(0, (int) (38 * (context.getResources().getDisplayMetrics().density)), 0, (int) (10 * (context.getResources().getDisplayMetrics().density)));
            headerVH.ratedMentorDP.setLayoutParams(lp);

            // margin for the text view
            RelativeLayout.LayoutParams lpTextView =(RelativeLayout.LayoutParams) headerVH.ratingPrimaryText.getLayoutParams();
            lpTextView.setMargins((int) (16 * (context.getResources().getDisplayMetrics().density)), 0, (int) (16 * (context.getResources().getDisplayMetrics().density)), (int) (10 * (context.getResources().getDisplayMetrics().density)));
            headerVH.ratingPrimaryText.setLayoutParams(lpTextView);

            //testing code here
            HomePageRatingAdapter itemRatingRecycleAdapter = new HomePageRatingAdapter(context, getFinalRatingData());
            headerVH.itemRatingRecycleView.setAdapter(itemRatingRecycleAdapter);
            headerVH.itemRatingRecycleView.setLayoutManager(new LinearLayoutManager(context));

            headerVH.mDecimalRatingBars.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
                @Override
                public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                    headerVH.nextSubmitBtn.performClick();
                }
            });

            headerVH.feedbackTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        int rating = headerVH.mDecimalRatingBars.getProgress();
                        if (rating <= 100) {
                            String userName = "Azgor";
                            headerVH.feedbackTextView.setHint("Share how " + userName + " can improve");
                        } else if (rating > 100 && rating <= 200) {
                            headerVH.feedbackTextView.setHint(feedbackStrings[1]);
                        } else if (rating > 200 && rating <= 300) {
                            String userName = "Azgor";
                            headerVH.feedbackTextView.setHint("Say something about " + userName);
                        } else if (rating > 300 && rating <= 400) {
                            headerVH.feedbackTextView.setHint(feedbackStrings[3]);
                        } else if (rating > 400 && rating <= 500) {
                            headerVH.feedbackTextView.setHint(feedbackStrings[4]);
                        }
                    } else {
                        headerVH.feedbackTextView.setHint(feedbackStrings[4]);
                    }
                }
            });

            headerVH.nextSubmitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TransitionSet set = new TransitionSet()
                            .addTransition(new Fade())
                            .addTransition(new Slide(Gravity.START))
                            .setInterpolator(new FastOutLinearInInterpolator());
                    TransitionManager.beginDelayedTransition(headerVH.dialogHostingLayout, set);
                    if (headerVH.nextSubmitBtn.getText().equals("Next") && headerVH.mDecimalRatingBars.getProgress() != 0) {

                        headerVH.firstLayout.setVisibility(View.GONE);
                        headerVH.secondLayout.setVisibility(View.VISIBLE);

                    } else{
                        Toast.makeText(context, "please rate your experience", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            headerVH.submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeAt(holder.getAdapterPosition());
                }
            });

        } else {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
        }
    }

    public void removeAt(int position) {
        //mDataset.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, 1);
    }

    public List<HomePageRatingData> getFinalRatingData() {
        List<HomePageRatingData> data = new ArrayList<>();
        String[] primaryText = context.getResources().getStringArray(R.array.rating_demo_data);
        for (String aPrimaryText : primaryText) {
            HomePageRatingData current = new HomePageRatingData();
            current.ratingAboutName = aPrimaryText;
            data.add(current);
        }
        return data;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView mainTitle;
        private final ImageView mainAboutImage;
        private final Button mainAbotuBtn;
        private final TextView mainAboutSubBody;
        private final TextView mainAboutBody;
        private final TextView mainAboutIntro;

        public MyViewHolder(View itemView) {
            super(itemView);
            mainTitle = itemView.findViewById(R.id.main_title);
            mainAboutIntro = itemView.findViewById(R.id.main_about_intro);
            mainAboutBody = itemView.findViewById(R.id.main_about_body);
            mainAboutSubBody = itemView.findViewById(R.id.main_about_sub_body);
            mainAboutImage = itemView.findViewById(R.id.main_about_image);
            mainAbotuBtn = itemView.findViewById(R.id.main_about_btn);


        }
    }

    class RatingVH extends RecyclerView.ViewHolder {

        private final MaterialRatingBar mDecimalRatingBars;
        private final RecyclerView itemRatingRecycleView;
        private final carbon.widget.ImageView ratedMentorDP;
        private final TextView ratingPrimaryText;
        private final TextView ratingSecondaryText;
        private final TextInputLayout feedbackTextViewLayout;
        private final AutoCompleteTextView feedbackTextView;
        private final Button dismissBtn;
        private final Button dismissBtnOne;
        private final Button nextSubmitBtn;
        private final RelativeLayout dialogHostingLayout;
        private final TextView smallTitle;
        private final Button submitBtn;
        private final RelativeLayout secondLayout;
        private final RelativeLayout firstLayout;

        public RatingVH(View itemView) {
            super(itemView);
            mDecimalRatingBars = itemView.findViewById(R.id.rated_mentor_rating_bar);
            itemRatingRecycleView = itemView.findViewById(R.id.rating_item_recycler);
            ratedMentorDP = itemView.findViewById(R.id.rated_mentor_dp);
            ratingPrimaryText = itemView.findViewById(R.id.rating_primary_text);
            ratingSecondaryText = itemView.findViewById(R.id.rating_secondary_text);
            feedbackTextViewLayout = itemView.findViewById(R.id.input_layout_firstname);
            feedbackTextView = itemView.findViewById(R.id.feedback_textview);
            dismissBtn = itemView.findViewById(R.id.skip_btn);
            dismissBtnOne = itemView.findViewById(R.id.skip_btn_two);
            nextSubmitBtn = itemView.findViewById(R.id.next_btn);
            dialogHostingLayout = itemView.findViewById(R.id.dialog_hosting_layout);
            smallTitle = itemView.findViewById(R.id.small_title);
            submitBtn = itemView.findViewById(R.id.submit_btn);
            firstLayout = itemView.findViewById(R.id.first_layout);
            secondLayout = itemView.findViewById(R.id.second_layout);
        }
    }
}
