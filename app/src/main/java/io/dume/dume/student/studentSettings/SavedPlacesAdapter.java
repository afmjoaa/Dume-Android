package io.dume.dume.student.studentSettings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.dume.dume.R;
import io.dume.dume.student.grabingLocation.GrabingLocationActivity;

public class SavedPlacesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "SavedPlacesAdapter";
    private LayoutInflater inflater;
    private Context context;
    private Activity activity;
    private List<SavedPlacesAdaData> favorite;
    private List<SavedPlacesAdaData> saved;
    private List<SavedPlacesAdaData> recent;
    int[] imageIcons = {
            R.drawable.ic_home_place,
            R.drawable.ic_work_places,
            R.drawable.ic_star_border_black_24dp,
            R.drawable.ic_back_in_time
    };
    private int ADD_HOME_LOCATION = 1001;


    public SavedPlacesAdapter(Context context, List<SavedPlacesAdaData> favorite,
                              List<SavedPlacesAdaData> saved, List<SavedPlacesAdaData> recent) {
        inflater = LayoutInflater.from(context);
        this.favorite = favorite;
        this.saved = saved;
        this.recent = recent;
        this.context = context;
        this.activity = (Activity) context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 9997;
        } else if (position == 3) {
            return 9998;
        } else if (position == 3 + 3) {
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
        } else if (holder.getItemViewType() == 9998) {
            HeaderVH headerVH = (HeaderVH) holder;
            headerVH.headerText.setText(R.string.saved_places);
        } else if (holder.getItemViewType() == 9999) {
            HeaderVH headerVH = (HeaderVH) holder;
            headerVH.headerText.setText(R.string.recent_used_places);
        } else {
            SavedPlacesAdaData current = new SavedPlacesAdaData();
            current.primary_text = "foo";
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            switch (position) {
                //home block
                case 1:
                    myViewHolder.imageIcon.setImageResource(imageIcons[0]);
                    if (favorite.size() > 0) {
                        for (SavedPlacesAdaData foo : favorite) {
                            if (foo.primary_text.equals("Home")){
                                current = foo;
                            }
                        }
                        if (current.primary_text.equals("Home")){
                            myViewHolder.primaryText.setText(current.primary_text);
                            myViewHolder.secondaryText.setText(current.secondary_text);
                            myViewHolder.moreVertImage.setVisibility(View.VISIBLE);
                        }else {
                            myViewHolder.primaryText.setText("Add Home");
                            myViewHolder.secondaryText.setText("");
                            myViewHolder.moreVertImage.setVisibility(View.GONE);
                        }
                    }else{
                        myViewHolder.primaryText.setText("Add Home");
                        myViewHolder.secondaryText.setText("");
                        myViewHolder.moreVertImage.setVisibility(View.GONE);
                    }
                    break;
                //work block
                case 2:
                    myViewHolder.imageIcon.setImageResource(imageIcons[1]);
                    if (favorite.size() > 0) {
                        for (SavedPlacesAdaData foo : favorite) {
                            if (foo.primary_text.equals("Work")){
                                current = foo;
                            }
                        }
                        if (current.primary_text.equals("Work")){
                            myViewHolder.primaryText.setText(current.primary_text);
                            myViewHolder.secondaryText.setText(current.secondary_text);
                            myViewHolder.moreVertImage.setVisibility(View.VISIBLE);
                        }else {
                            myViewHolder.primaryText.setText("Add Work");
                            myViewHolder.secondaryText.setText("");
                            myViewHolder.moreVertImage.setVisibility(View.GONE);
                        }
                    }else{
                        myViewHolder.primaryText.setText("Add Work");
                        myViewHolder.secondaryText.setText("");
                        myViewHolder.moreVertImage.setVisibility(View.GONE);
                    }
                    break;

                case 4:
                    /*current  = favorite.get(1);
                    myViewHolder.imageIcon.setImageResource(imageIcons[1]);*/
                    break;


            }
            myViewHolder.moreVertImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PopupMenu popup = new PopupMenu(context, view);
                    popup.inflate(R.menu.menu_saved_places);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            Toast.makeText(context, "fucked it....." + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    });
                    popup.show();
                }
            });
            if (myViewHolder.secondaryText.getText().toString().equals("")) {
                myViewHolder.primaryText.setTranslationY((10 * (context.getResources().getDisplayMetrics().density)));
            }

            //testing some onclick
            myViewHolder.hostLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.getAdapterPosition() == 1 ){
                        activity.startActivityForResult(new Intent(context, GrabingLocationActivity.class).setAction("fromSPA"), ADD_HOME_LOCATION);
                    }
                }
            });


        }
    }

    public void updateFav(SavedPlacesAdaData newFav){
        favorite.add(newFav);
        //favorite.clear();
        //favorite.addAll(newFav);
        this.notifyDataSetChanged();
    }

    public void updateSaved(List<SavedPlacesAdaData> newSaved){
        saved.clear();
        saved.addAll(newSaved);
        this.notifyDataSetChanged();
    }

    public void updateRecent(List<SavedPlacesAdaData> newRecent){
        recent.clear();
        recent.addAll(newRecent);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final carbon.widget.ImageView moreVertImage;
        private final ImageView imageIcon;
        private final TextView primaryText;
        private final TextView secondaryText;
        private final View divider;
        private final RelativeLayout hostLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            moreVertImage = itemView.findViewById(R.id.more_vertical_icon);
            imageIcon = itemView.findViewById(R.id.auto_image_icon);
            primaryText = itemView.findViewById(R.id.text_one);
            secondaryText = itemView.findViewById(R.id.text_two);
            divider = itemView.findViewById(R.id.divider2);
            hostLayout = itemView.findViewById(R.id.recycler_container_layout);

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
