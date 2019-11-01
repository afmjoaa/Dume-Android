package io.dume.dume.student.grabingLocation;


import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.AutocompletePrediction;

import java.util.ArrayList;

import io.dume.dume.R;

public abstract class PlaceAutoRecyAda extends RecyclerView.Adapter<PlaceAutoRecyAda.MyViewHolder> {

    private static final CharacterStyle STYLE_BOLD = new StyleSpan(Typeface.NORMAL);
    private static final String TAG = "PlaceAutoRecyAda";
    private LayoutInflater inflater;
    private Context context;
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<AutocompletePrediction> data;

    public PlaceAutoRecyAda(Context context , ArrayList<AutocompletePrediction> data, GoogleApiClient mGoogleApiClient){
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
        this.mGoogleApiClient = mGoogleApiClient;
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
        AutocompletePrediction current = data.get(holder.getAdapterPosition());
        holder.primaryText.setText(current.getPrimaryText(STYLE_BOLD));
        holder.secondaryText.setText(current.getSecondaryText(STYLE_BOLD));

        holder.recyclerContainerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnItemClicked(v, current);
            }
        });


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

    abstract void OnItemClicked(View v, AutocompletePrediction clickedPrediction);

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView primaryText;
        private final TextView secondaryText;
        private final ImageView imageIcon;
        private final LinearLayout recyclerContainerLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            primaryText = itemView.findViewById(R.id.text_one);
            secondaryText = itemView.findViewById(R.id.text_two);
            imageIcon = itemView.findViewById(R.id.auto_image_icon);
            recyclerContainerLayout = itemView.findViewById(R.id.recycler_container_layout);
        }
    }
}