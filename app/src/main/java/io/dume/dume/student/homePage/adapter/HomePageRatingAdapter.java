package io.dume.dume.student.homePage.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import carbon.widget.ImageView;
import io.dume.dume.R;

public class HomePageRatingAdapter extends RecyclerView.Adapter<HomePageRatingAdapter.MyViewHolder> {

    private static final String TAG = "HomePageRatingAdapter";
    private LayoutInflater inflater;
    private Context context;
    private HomePageRatingData data;
    Map<String, Boolean> inputMap;

    public HomePageRatingAdapter(Context context, HomePageRatingData data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
        inputMap = new HashMap<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = inflater.inflate(R.layout.custom_item_rating, viewGroup, false);
        HomePageRatingAdapter.MyViewHolder holder = new HomePageRatingAdapter.MyViewHolder(view);
        return holder;
    }

    public Map<String, Boolean> getInputRating() {
        if (inputMap.size() == data.ratingNameList.size()) {
            return inputMap;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        HomePageRatingData current = data;
        myViewHolder.ratingAboutName.setText(current.ratingNameList.get(position));

        myViewHolder.upExpertise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myViewHolder.progressBarDown.setProgress(0);
                myViewHolder.progressBarDown.setVisibility(View.INVISIBLE);
                myViewHolder.progressBarUp.setVisibility(View.VISIBLE);
                inputMap.put(data.ratingNameList.get(position), true);
                ObjectAnimator animation = ObjectAnimator.ofInt(myViewHolder.progressBarUp, "progress", 0, 100);
                animation.setDuration(2000); // 2 second
                animation.setInterpolator(new DecelerateInterpolator());
                animation.start();
            }
        });

        myViewHolder.downExpertise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myViewHolder.progressBarUp.setProgress(0);
                myViewHolder.progressBarUp.setVisibility(View.INVISIBLE);
                myViewHolder.progressBarDown.setVisibility(View.VISIBLE);
                inputMap.put(data.ratingNameList.get(position), false);
                ObjectAnimator animation = ObjectAnimator.ofInt(myViewHolder.progressBarDown, "progress", 0, 100);
                animation.setDuration(2000); // 2 second
                animation.setInterpolator(new DecelerateInterpolator());
                animation.start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.getRatingNameList().size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final RelativeLayout hostingLayout;
        private final ImageView upExpertise;
        private final TextView ratingAboutName;
        private final ProgressBar progressBarUp;
        private final ProgressBar progressBarDown;
        private final ImageView downExpertise;

        public MyViewHolder(View itemView) {
            super(itemView);
            hostingLayout = itemView.findViewById(R.id.secondary_rating);
            upExpertise = itemView.findViewById(R.id.up_expertise);
            ratingAboutName = itemView.findViewById(R.id.rating_about_name);
            progressBarUp = itemView.findViewById(R.id.progress_bar_up);
            progressBarDown = itemView.findViewById(R.id.progress_bar_down);
            downExpertise = itemView.findViewById(R.id.down_expertise);

        }
    }
}
