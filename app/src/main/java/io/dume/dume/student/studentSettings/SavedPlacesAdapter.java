package io.dume.dume.student.studentSettings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.dume.dume.R;
import io.dume.dume.student.grabingLocation.GrabingLocationActivity;

public abstract class SavedPlacesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
    private int ADD_WORK_LOCATION = 1002;
    private int ADD_SAVED_PLACES = 1003;
    private int ADD_RECENT_PLACES = 1004;


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
        } else if (position == (3 + saved.size() + 2)) {
            return 9999;
        } else if (position == (3 + saved.size() + 1)) {
            return 1000;
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == 9997 || viewType == 9998 || viewType == 9999) {
            View view = inflater.inflate(R.layout.custom_saved_places_barrier, viewGroup, false);
            return new HeaderVH(view);
        } else if (viewType == 1000) {
            View view = inflater.inflate(R.layout.custom_add_saved_place_layout, viewGroup, false);
            return new AddSavedView(view);
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
            if (recent.size() == 0) {
                headerVH.containerLinearLayout.setVisibility(View.GONE);
            } else {
                headerVH.containerLinearLayout.setVisibility(View.VISIBLE);
            }
        } else if (holder.getItemViewType() == 1000) {
            AddSavedView addSavedView = (AddSavedView) holder;
            addSavedView.addSavedPlaceLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.startActivityForResult(new Intent(context, GrabingLocationActivity.class).setAction("fromSPAS"), ADD_SAVED_PLACES);
                }
            });
        } else {
            SavedPlacesAdaData current = new SavedPlacesAdaData();
            current.primary_text = "foo";
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            if (position == 1) {
                //home block
                myViewHolder.imageIcon.setImageResource(imageIcons[0]);
                if (favorite.size() > 0) {
                    for (SavedPlacesAdaData foo : favorite) {
                        if (foo.primary_text.equals("Home")) {
                            current = foo;
                        }
                    }
                    if (current.primary_text.equals("Home")) {
                        myViewHolder.primaryText.setText(current.primary_text);
                        myViewHolder.secondaryText.setText(current.secondary_text);
                        myViewHolder.moreVertImage.setVisibility(View.VISIBLE);
                        myViewHolder.primaryText.setTranslationY((0 * (context.getResources().getDisplayMetrics().density)));
                    } else {
                        myViewHolder.primaryText.setText("Add Home");
                        myViewHolder.secondaryText.setText("");
                        myViewHolder.moreVertImage.setVisibility(View.GONE);
                        myViewHolder.primaryText.setTranslationY((10 * (context.getResources().getDisplayMetrics().density)));
                    }
                } else {
                    myViewHolder.primaryText.setText("Add Home");
                    myViewHolder.secondaryText.setText("");
                    myViewHolder.moreVertImage.setVisibility(View.GONE);
                    myViewHolder.primaryText.setTranslationY((10 * (context.getResources().getDisplayMetrics().density)));
                }
            } else if (position == 2) {
                //work block
                myViewHolder.imageIcon.setImageResource(imageIcons[1]);
                if (favorite.size() > 0) {
                    for (SavedPlacesAdaData foo : favorite) {
                        if (foo.primary_text.equals("Work")) {
                            current = foo;
                        }
                    }
                    if (current.primary_text.equals("Work")) {
                        myViewHolder.primaryText.setText(current.primary_text);
                        myViewHolder.secondaryText.setText(current.secondary_text);
                        myViewHolder.moreVertImage.setVisibility(View.VISIBLE);
                        myViewHolder.primaryText.setTranslationY((0 * (context.getResources().getDisplayMetrics().density)));
                    } else {
                        myViewHolder.primaryText.setText("Add Work");
                        myViewHolder.secondaryText.setText("");
                        myViewHolder.moreVertImage.setVisibility(View.GONE);
                        myViewHolder.primaryText.setTranslationY((10 * (context.getResources().getDisplayMetrics().density)));
                    }
                } else {
                    myViewHolder.primaryText.setText("Add Work");
                    myViewHolder.secondaryText.setText("");
                    myViewHolder.moreVertImage.setVisibility(View.GONE);
                    myViewHolder.primaryText.setTranslationY((10 * (context.getResources().getDisplayMetrics().density)));
                }
            } else if (position >= 4 && position < (saved.size() + 4)) {
                myViewHolder.imageIcon.setImageResource(imageIcons[2]);

                SavedPlacesAdaData savedCurrent = saved.get((position - 4));
                myViewHolder.primaryText.setText(savedCurrent.primary_text);
                myViewHolder.secondaryText.setText(savedCurrent.secondary_text);
                myViewHolder.moreVertImage.setVisibility(View.VISIBLE);

            } else if (position > (saved.size() + 5) && position <= (saved.size() + 5 + recent.size())) {
                myViewHolder.imageIcon.setImageResource(imageIcons[3]);
                myViewHolder.moreVertImage.setVisibility(View.GONE);

                SavedPlacesAdaData recentCurrent = recent.get((position - (saved.size() + 6)));
                myViewHolder.primaryText.setText(recentCurrent.primary_text);
                myViewHolder.secondaryText.setText(recentCurrent.secondary_text);

            } else {
                Log.e(TAG, "onBindViewHolder: " + "error error error bound");
            }

            myViewHolder.moreVertImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getAdapterPosition();
                    final String myPrimaryText = myViewHolder.primaryText.getText().toString();
                    if (position == 1) {
                        updateFav("Home", null);
                        OnItemDeleteClicked(view, position, "Home");
                    } else if (position == 2) {
                        updateFav("Work", null);
                        OnItemDeleteClicked(view, position, "Work");
                    } else if (position >= 4 && position < (saved.size() + 4)) {
                        OnItemDeleteClicked(view, position, myPrimaryText);
                        updateSaved(myPrimaryText, null);
                    }
                }
            });

            //testing some onclick
            myViewHolder.hostLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.getAdapterPosition() == 1) {
                        activity.startActivityForResult(new Intent(context, GrabingLocationActivity.class).setAction("fromSPAH"), ADD_HOME_LOCATION);
                    } else if (holder.getAdapterPosition() == 2) {
                        activity.startActivityForResult(new Intent(context, GrabingLocationActivity.class).setAction("fromSPAW"), ADD_WORK_LOCATION);
                    } else if (position >= 4 && position < (saved.size() + 4)) {
                        Intent myIntent = new Intent(context, GrabingLocationActivity.class).setAction("fromSPASN");
                        myIntent.putExtra("addressName", myViewHolder.primaryText.getText());
                        activity.startActivityForResult(myIntent, ADD_SAVED_PLACES);
                    }else if (position > (saved.size() + 5) && position <= (saved.size() + 5 + recent.size())) {
                        Toast.makeText(context, "Can't edit this item", Toast.LENGTH_SHORT).show();
                    }
                }
            });



        }
    }

    public void updateFav(String identify, SavedPlacesAdaData newFav) {
        for (int i = 0; i < favorite.size(); i++) {
            if (favorite.get(i).primary_text.equals(identify)) {
                favorite.remove(i);
            }
        }
        if (newFav != null) {
            favorite.add(newFav);
        }
        this.notifyDataSetChanged();
    }

    public void updateSaved(String identify, SavedPlacesAdaData newSaved) {
        for (int i = 0; i < saved.size(); i++) {
            if (saved.get(i).primary_text.equals(identify)) {
                saved.remove(i);
            }
        }
        if (newSaved != null) {
            saved.add(newSaved);
        }
        this.notifyDataSetChanged();
    }

    public void updateRecent(List<SavedPlacesAdaData> newRecent) {
        recent.clear();
        recent.addAll(newRecent);
        this.notifyDataSetChanged();
    }

    abstract void OnItemDeleteClicked(View v, int position, String identify);

    @Override
    public int getItemCount() {
        return (saved.size() + 6 + recent.size());
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
        private final LinearLayout containerLinearLayout;

        public HeaderVH(View itemView) {
            super(itemView);
            headerText = itemView.findViewById(R.id.recent_updates);
            containerLinearLayout = itemView.findViewById(R.id.constraintLayout);
        }
    }

    class AddSavedView extends RecyclerView.ViewHolder {

        private final RelativeLayout addSavedPlaceLayout;

        public AddSavedView(View itemView) {
            super(itemView);
            addSavedPlaceLayout = itemView.findViewById(R.id.add_saved_place_layout);
        }
    }
}
