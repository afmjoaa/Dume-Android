package io.dume.dume.student.grabingLocation;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import io.dume.dume.R;

public abstract class SearchFoundRecyAda extends RecyclerView.Adapter<SearchFoundRecyAda.MyViewHolder> {
    private final Activity activity;
    private final List<MenualRecyclerData> data;
    private LayoutInflater inflater;
    private Context context;
    int[] imageIcons = {
            R.drawable.ic_current_location_icon,
            R.drawable.ic_home_place,
            R.drawable.ic_work_places,
            R.drawable.ic_star_border_black_24dp,
            R.drawable.ic_back_in_time,
            R.drawable.ic_set_location_on_map
    };

    public SearchFoundRecyAda(Context context, List<MenualRecyclerData> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
        this.activity = (Activity) context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.custom_autocomplete_recycler_row, viewGroup, false);
        return new SearchFoundRecyAda.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        MenualRecyclerData savedCurrent = data.get(position);
        myViewHolder.primaryText.setTranslationY((0 * (context.getResources().getDisplayMetrics().density)));
        switch (savedCurrent.identify) {
            case "Permanent address":
            case "permanent":
            case"permanent address":
                myViewHolder.imageIcon.setImageResource(imageIcons[0]);
                break;
            case "Home":
            case "home":
                myViewHolder.imageIcon.setImageResource(imageIcons[1]);
                break;
            case "Work":
            case "work":
                myViewHolder.imageIcon.setImageResource(imageIcons[2]);
                break;
            case "saved_one":
            case "Saved_one":
                myViewHolder.imageIcon.setImageResource(imageIcons[3]);
                break;
            case "set_location":
                myViewHolder.imageIcon.setImageResource(imageIcons[5]);
                myViewHolder.primaryText.setTranslationY((10 * (context.getResources().getDisplayMetrics().density)));
                break;
            default:
                myViewHolder.imageIcon.setImageResource(imageIcons[4]);
                break;
        }
        myViewHolder.primaryText.setText(savedCurrent.primaryText);
        myViewHolder.secondaryText.setText(savedCurrent.secondaryText);

        myViewHolder.itemHostLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int myPosition = myViewHolder.getAdapterPosition();
                OnItemClicked(view, data.get(myPosition));
            }
        });
    }

    public void update(List<MenualRecyclerData> newData){
        data.clear();
        data.addAll(newData);
        this.notifyDataSetChanged();
    }

    abstract void OnItemClicked(View v, MenualRecyclerData clickedData);

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView primaryText;
        private final TextView secondaryText;
        private final ImageView imageIcon;
        private final LinearLayout itemHostLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            primaryText = itemView.findViewById(R.id.text_one);
            secondaryText = itemView.findViewById(R.id.text_two);
            imageIcon = itemView.findViewById(R.id.auto_image_icon);
            itemHostLayout = itemView.findViewById(R.id.recycler_container_layout);
        }
    }
}
