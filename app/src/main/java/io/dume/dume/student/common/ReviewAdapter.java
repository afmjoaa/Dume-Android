package io.dume.dume.student.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import carbon.widget.ImageView;
import io.dume.dume.R;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    private static final String TAG = "ReviewAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<ReviewHighlightData> data;

    public ReviewAdapter(Context context, List<ReviewHighlightData> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
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

    }

    @Override
    public int getItemCount() {
        //testing purpose
        return 4;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        private final ImageView reviewProducerDP;
        private final TextView reviewProducerTime;
        private final TextView reviewProducerName;
        private final MaterialRatingBar reviewProducerRateingBar;
        private final AppCompatTextView mainReview;
        private final TextView dislikeTextView;
        private final TextView likeTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            reviewProducerDP = itemView.findViewById(R.id.review_producer_dp);
            reviewProducerName = itemView.findViewById(R.id.review_producer_name);
            reviewProducerRateingBar = itemView.findViewById(R.id.review_producer_rating_bar);
            reviewProducerTime = itemView.findViewById(R.id.review_produce_time);
            mainReview = itemView.findViewById(R.id.main_review);
            likeTextView = itemView.findViewById(R.id.like_text_view);
            dislikeTextView = itemView.findViewById(R.id.dislike_text_view);

        }
    }
}
