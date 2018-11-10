package io.dume.dume.student.studentSettings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import io.dume.dume.R;

public class SavedPlacesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "SavedPlacesAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<SavedPlacesAdaData> data;

    public SavedPlacesAdapter(Context context , List<SavedPlacesAdaData> data){
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 9997;
        } else if (position == 3) {
            return 9998;
        }else if(position == 3+3){
           return 9999;
        } //3==data.get(position).typeView
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == 9997 || viewType == 9998 || viewType == 9999) {
            View view = inflater.inflate(R.layout.custom_saved_places_barrier, viewGroup, false);
            return new HeaderVH(view);
        }
        View view = inflater.inflate(R.layout.custom_saved_places_row, viewGroup, false);
        SavedPlacesAdapter.MyViewHolder holder = new SavedPlacesAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 9997) {
            HeaderVH headerVH = (HeaderVH) holder;
            headerVH.headerText.setText(R.string.favorites);
        }else if(holder.getItemViewType() == 9998){
            HeaderVH headerVH = (HeaderVH) holder;
            headerVH.headerText.setText(R.string.saved_places);
        }else if(holder.getItemViewType() == 9999){
            HeaderVH headerVH = (HeaderVH) holder;
            headerVH.headerText.setText(R.string.recent_used_places);
        }
        else {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            switch (position) {
                case 1:
                break;
            }
            myViewHolder.moreVertImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "fucked it.....", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final carbon.widget.ImageView moreVertImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            moreVertImage = itemView.findViewById(R.id.more_vertical_icon);

        }
    }

    class HeaderVH extends RecyclerView.ViewHolder {

        private final TextView headerText;

        public HeaderVH(View itemView) {
            super(itemView);
            headerText = itemView.findViewById(R.id.recent_updates);
        }
    }
}
