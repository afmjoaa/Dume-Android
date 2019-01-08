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

import com.google.android.gms.location.places.AutocompletePrediction;

import java.util.ArrayList;
import java.util.List;

import io.dume.dume.R;
import io.dume.dume.inter_face.usefulListeners;

public abstract class PlaceMenualRecyAda extends RecyclerView.Adapter<PlaceMenualRecyAda.MyViewHolder> {

    private static final String TAG = "PlaceMenualRecyAda";
    private LayoutInflater inflater;
    private Context context;
    private List<MenualRecyclerData> data;

    public PlaceMenualRecyAda(Context context, List<MenualRecyclerData> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_autocomplete_recycler_row, parent, false);
        PlaceMenualRecyAda.MyViewHolder holder = new PlaceMenualRecyAda.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MenualRecyclerData current = data.get(position);
        holder.primaryText.setText(current.primaryText);
        holder.secondaryText.setText(current.secondaryText);
        holder.imageIcon.setImageResource(current.imageSrc);
        if (current.secondaryText.equals("")) {
            holder.primaryText.setTranslationY((8 * (context.getResources().getDisplayMetrics().density)));
        }

        holder.itemHostLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnItemClicked(view, holder.getAdapterPosition());
            }
        });


    }

    abstract void OnItemClicked(View v, int position);

    public void update(List<MenualRecyclerData> newData){
        data.clear();
        data.addAll(newData);
        this.notifyDataSetChanged();
    }

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
