package io.dume.dume.student.grabingLocation;


import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.places.AutocompletePrediction;

import java.util.ArrayList;
import io.dume.dume.R;

public class PlaceAutoRecyAda extends RecyclerView.Adapter<PlaceAutoRecyAda.MyViewHolder> {

    private static final CharacterStyle STYLE_BOLD = new StyleSpan(Typeface.NORMAL);
    private static final String TAG = "PlaceAutoRecyAda";
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<AutocompletePrediction> data;

    public PlaceAutoRecyAda(Context context , ArrayList<AutocompletePrediction> data){
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_autocomplete_recycler_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AutocompletePrediction current = data.get(position);
        holder.primaryText.setText(current.getPrimaryText(STYLE_BOLD));
        holder.secondaryText.setText(current.getSecondaryText(STYLE_BOLD));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void update(ArrayList<AutocompletePrediction> newData){
        data.clear();
        data.addAll(newData);
        this.notifyDataSetChanged();
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