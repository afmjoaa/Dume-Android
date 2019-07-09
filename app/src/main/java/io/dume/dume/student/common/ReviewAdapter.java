package io.dume.dume.student.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import carbon.widget.ImageView;
import io.dume.dume.R;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    private static final String TAG = "ReviewAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<ReviewHighlightData> data;
    private boolean isSmall = false;
    private float mDensity;
    private String skillID = null;
    private Map<String, String> tracker;

    public ReviewAdapter(Context context, List<ReviewHighlightData> data, String skill_ID) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
        mDensity = context.getResources().getDisplayMetrics().density;
        this.skillID = skill_ID;
        tracker = new HashMap<>();
    }

    public ReviewAdapter(Context context, List<ReviewHighlightData> data, String skill_ID, boolean isSmall) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
        this.isSmall = isSmall;
        this.skillID = skill_ID;
        mDensity = context.getResources().getDisplayMetrics().density;
        tracker = new HashMap<>();
    }

    public void addMore(List<ReviewHighlightData> loaded) {
        data.addAll(loaded);
        notifyDataSetChanged();
    }

    public void update(List<ReviewHighlightData> newData, String skillID) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
        this.skillID = skillID;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_review_highlights_row, parent, false);
        ReviewAdapter.MyViewHolder holder = new ReviewAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //ReviewHighlightData current = data.get(position);
        ReviewHighlightData review = data.get(position);

        if (isSmall) {
            holder.reviewProducerTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);

        } else {
            holder.reviewProducerTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);

        }
        holder.reviewProducerName.setText(review.getName());
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        holder.reviewProducerTime.setText(format.format(review.getTime()));

        holder.likeBtn.setText(String.valueOf(review.getLikes()));
        holder.dislikeBtn.setText(String.valueOf(review.getDislikes()));
        holder.mainReview.setText(review.getBody());
        if (review.getR_avatar() != null && !review.getR_avatar().equals("")) {
            Glide.with(context).load(review.getR_avatar()).apply(new RequestOptions().override((int) (50 * mDensity), (int) (50 * mDensity)).placeholder(R.drawable.alias_profile_icon)).into(holder.reviewProducerDP);
        }
        holder.reviewProducerRateingBar.setRating(Float.parseFloat(review.getReviewer_rating()));

        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tracker.get(review.doc_id)== null){
                    String text = (String) holder.likeBtn.getText();
                    Integer likeVal = Integer.parseInt(text);
                    likeVal = likeVal + 1;
                    holder.likeBtn.setText(likeVal.toString());
                    if (skillID != null) {
                        FirebaseFirestore.getInstance().collection("/users/mentors/skills/").document(skillID).collection("reviews")
                                .document(review.doc_id).update("likes", likeVal);
                        tracker.put(review.doc_id, "likes");
                    }
                }else if(("dislikes").equals(tracker.get(review.doc_id))){
                    String text = (String) holder.likeBtn.getText();
                    Integer likeVal = Integer.parseInt(text);
                    likeVal = likeVal + 1;
                    holder.likeBtn.setText(likeVal.toString());

                    String dislikeText = (String) holder.dislikeBtn.getText();
                    Integer dislikeVal = Integer.parseInt(dislikeText);
                    dislikeVal = dislikeVal-1;
                    holder.dislikeBtn.setText(dislikeVal.toString());

                    if (skillID != null) {
                        FirebaseFirestore.getInstance().collection("/users/mentors/skills/").document(skillID).collection("reviews")
                                .document(review.doc_id).update("likes", likeVal, "dislikes", dislikeVal);
                        tracker.put(review.doc_id, "likes");
                    }
                }else {
                    Toast.makeText(context, "Can't like more then once", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.dislikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tracker.get(review.doc_id)== null){
                    String text = (String) holder.dislikeBtn.getText();
                    Integer dislikeVal = Integer.parseInt(text);
                    dislikeVal = dislikeVal + 1;
                    holder.dislikeBtn.setText(dislikeVal.toString());
                    if (skillID != null) {
                        FirebaseFirestore.getInstance().collection("/users/mentors/skills/").document(skillID).collection("reviews")
                                .document(review.doc_id).update("dislikes", dislikeVal);
                        tracker.put(review.doc_id, "dislikes");
                    }
                }else if(("likes").equals(tracker.get(review.doc_id))){

                    String text = (String) holder.likeBtn.getText();
                    Integer likeVal = Integer.parseInt(text);
                    likeVal = likeVal - 1;
                    holder.likeBtn.setText(likeVal.toString());

                    String dislikeText = (String) holder.dislikeBtn.getText();
                    Integer dislikeVal = Integer.parseInt(dislikeText);
                    dislikeVal = dislikeVal+1;
                    holder.dislikeBtn.setText(dislikeVal.toString());

                    if (skillID != null) {
                        FirebaseFirestore.getInstance().collection("/users/mentors/skills/").document(skillID).collection("reviews")
                                .document(review.doc_id).update("likes", likeVal, "dislikes", dislikeVal);
                        tracker.put(review.doc_id, "dislikes");
                    }
                }else {
                    Toast.makeText(context, "Can't dislike more then once", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        //testing purpose
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView reviewProducerDP;
        private final TextView reviewProducerTime;
        private final TextView reviewProducerName;
        private final MaterialRatingBar reviewProducerRateingBar;
        private final AppCompatTextView mainReview;
        private final Button dislikeBtn;
        private final Button likeBtn;

        public MyViewHolder(View itemView) {
            super(itemView);
            reviewProducerDP = itemView.findViewById(R.id.review_producer_dp);
            reviewProducerName = itemView.findViewById(R.id.review_producer_name);
            reviewProducerRateingBar = itemView.findViewById(R.id.review_producer_rating_bar);
            reviewProducerTime = itemView.findViewById(R.id.review_produce_time);
            mainReview = itemView.findViewById(R.id.main_review);
            likeBtn = itemView.findViewById(R.id.like_text_view);
            dislikeBtn = itemView.findViewById(R.id.dislike_text_view);

        }
    }
}
