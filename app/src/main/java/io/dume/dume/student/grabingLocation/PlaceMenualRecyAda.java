package io.dume.dume.student.grabingLocation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.places.AutocompletePrediction;

import java.util.List;

import io.dume.dume.R;

public class PlaceMenualRecyAda extends RecyclerView.Adapter<PlaceMenualRecyAda.MyViewHolder>{

    private static final String TAG = "PlaceMenualRecyAda";
    private LayoutInflater inflater;
    private Context context;
    private List<MenualRecyclerData> data;

    public PlaceMenualRecyAda(Context context , List<MenualRecyclerData> data){
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
        if(current.secondaryText.equals("")){
            holder.primaryText.setTranslationY((8 * (context.getResources().getDisplayMetrics().density)));
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView primaryText;
        TextView secondaryText;
        ImageView imageIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            primaryText = itemView.findViewById(R.id.text_one);
            secondaryText = itemView.findViewById(R.id.text_two);
            imageIcon = itemView.findViewById(R.id.auto_image_icon);
        }
    }
}
