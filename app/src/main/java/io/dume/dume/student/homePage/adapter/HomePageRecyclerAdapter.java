package io.dume.dume.student.homePage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.dume.dume.R;

public class HomePageRecyclerAdapter extends RecyclerView.Adapter<HomePageRecyclerAdapter.MyViewHolder> {

    private static final String TAG = "HomePageRecyclerAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<HomePageRecyclerData> data;

    public HomePageRecyclerAdapter(Context context, List<HomePageRecyclerData> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_hp_bsr_row, parent, false);
        HomePageRecyclerAdapter.MyViewHolder holder = new HomePageRecyclerAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
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
}
