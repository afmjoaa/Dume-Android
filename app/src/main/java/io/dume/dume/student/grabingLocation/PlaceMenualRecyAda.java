package io.dume.dume.student.grabingLocation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import io.dume.dume.R;
import io.dume.dume.student.studentSettings.SavedPlacesAdaData;

public abstract class PlaceMenualRecyAda extends RecyclerView.Adapter<PlaceMenualRecyAda.MyViewHolder> {

    private static final String TAG = "PlaceMenualRecyAda";
    private final Activity activity;
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
    private List<SavedPlacesAdaData> favorite;
    private List<SavedPlacesAdaData> saved;
    private List<MenualRecyclerData> recent;
    private String start;
    private int ADD_HOME_LOCATION = 1001;
    private int ADD_WORK_LOCATION = 1002;
    private int ADD_RECENT_PLACES = 1004;

    public PlaceMenualRecyAda(Context context, List<SavedPlacesAdaData> favorite,
                              List<SavedPlacesAdaData> saved, List<MenualRecyclerData> recent, String start) {
        inflater = LayoutInflater.from(context);
        this.favorite = favorite;
        this.saved = saved;
        this.recent = recent;
        this.start = start;
        this.context = context;
        this.activity = (Activity) context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_autocomplete_recycler_row, parent, false);
        PlaceMenualRecyAda.MyViewHolder holder = new PlaceMenualRecyAda.MyViewHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SavedPlacesAdaData current = new SavedPlacesAdaData();
        current.primary_text = "foo";
        MenualRecyclerData currentMenual = new MenualRecyclerData();
        currentMenual.primaryText = "foo";
        if (position == 0) {
            //current location block
            holder.imageIcon.setImageResource(imageIcons[0]);
            if (favorite.size() > 0) {
                for (SavedPlacesAdaData foo : favorite) {
                    if (foo.primary_text.equals("Permanent address")) {
                        current = foo;
                    }
                }
                if (current.primary_text.equals("Permanent address")) {
                    holder.primaryText.setText(current.primary_text);
                    holder.secondaryText.setText(current.secondary_text);
                    holder.primaryText.setTranslationY((0 * (context.getResources().getDisplayMetrics().density)));
                } else {
                    holder.primaryText.setText("Add permanent address");
                    holder.secondaryText.setText("");
                    holder.primaryText.setTranslationY((10 * (context.getResources().getDisplayMetrics().density)));
                }
            } else {
                holder.primaryText.setText("Add permanent address");
                holder.secondaryText.setText("");
                holder.primaryText.setTranslationY((10 * (context.getResources().getDisplayMetrics().density)));
            }
        } else if (position == 1) {
            //home block
            holder.imageIcon.setImageResource(imageIcons[1]);
            if (favorite.size() > 0) {
                for (SavedPlacesAdaData foo : favorite) {
                    if (foo.primary_text.equals("Home")) {
                        current = foo;
                    }
                }
                if (current.primary_text.equals("Home")) {
                    holder.primaryText.setText(current.primary_text);
                    holder.secondaryText.setText(current.secondary_text);
                    holder.primaryText.setTranslationY((0 * (context.getResources().getDisplayMetrics().density)));
                } else {
                    holder.primaryText.setText("Add Home");
                    holder.secondaryText.setText("");
                    holder.primaryText.setTranslationY((10 * (context.getResources().getDisplayMetrics().density)));
                }
            } else {
                holder.primaryText.setText("Add Home");
                holder.secondaryText.setText("");
                holder.primaryText.setTranslationY((10 * (context.getResources().getDisplayMetrics().density)));
            }

        } else if (position == 2) {
            //work block
            holder.imageIcon.setImageResource(imageIcons[2]);
            if (favorite.size() > 0) {
                for (SavedPlacesAdaData foo : favorite) {
                    if (foo.primary_text.equals("Work")) {
                        current = foo;
                    }
                }
                if (current.primary_text.equals("Work")) {
                    holder.primaryText.setText(current.primary_text);
                    holder.secondaryText.setText(current.secondary_text);
                    holder.primaryText.setTranslationY((0 * (context.getResources().getDisplayMetrics().density)));
                } else {
                    holder.primaryText.setText("Add Work");
                    holder.secondaryText.setText("");
                    holder.primaryText.setTranslationY((10 * (context.getResources().getDisplayMetrics().density)));
                }
            } else {
                holder.primaryText.setText("Add Work");
                holder.secondaryText.setText("");
                holder.primaryText.setTranslationY((10 * (context.getResources().getDisplayMetrics().density)));
            }

        } else if (position > 2 && position <= (2 + saved.size())) {
            //saved block
            holder.imageIcon.setImageResource(imageIcons[3]);
            SavedPlacesAdaData savedCurrent = saved.get((position - 3));
            holder.primaryText.setText(savedCurrent.primary_text);
            holder.secondaryText.setText(savedCurrent.secondary_text);

        } else if (position > (2 + saved.size()) && position <= (2 + saved.size() + recent.size())) {
            //back in time block
            //TODO
            holder.imageIcon.setImageResource(imageIcons[4]);
            switch (start) {
                case "1":
                    if (position == (3 + saved.size())){
                        for (MenualRecyclerData foo : recent) {
                            if (foo.identify.equals("rp_3")) {
                                currentMenual = foo;
                            }
                        }
                        holder.primaryText.setText(currentMenual.primaryText);
                        holder.secondaryText.setText(currentMenual.secondaryText);
                    }else if(position == (4 + saved.size())){
                        for (MenualRecyclerData foo : recent) {
                            if (foo.identify.equals("rp_2")) {
                                currentMenual = foo;
                            }
                        }
                        holder.primaryText.setText(currentMenual.primaryText);
                        holder.secondaryText.setText(currentMenual.secondaryText);
                    }else{
                        for (MenualRecyclerData foo : recent) {
                            if (foo.identify.equals("rp_1")) {
                                currentMenual = foo;
                            }
                        }
                        holder.primaryText.setText(currentMenual.primaryText);
                        holder.secondaryText.setText(currentMenual.secondaryText);
                    }
                    break;
                case "2":
                    if (position == (3 + saved.size())){
                        for (MenualRecyclerData foo : recent) {
                            if (foo.identify.equals("rp_1")) {
                                currentMenual = foo;
                            }
                        }
                        holder.primaryText.setText(currentMenual.primaryText);
                        holder.secondaryText.setText(currentMenual.secondaryText);
                    }else if(position == (4 + saved.size())){
                        for (MenualRecyclerData foo : recent) {
                            if (foo.identify.equals("rp_3")) {
                                currentMenual = foo;
                            }
                        }
                        holder.primaryText.setText(currentMenual.primaryText);
                        holder.secondaryText.setText(currentMenual.secondaryText);
                    }else{
                        for (MenualRecyclerData foo : recent) {
                            if (foo.identify.equals("rp_2")) {
                                currentMenual = foo;
                            }
                        }
                        holder.primaryText.setText(currentMenual.primaryText);
                        holder.secondaryText.setText(currentMenual.secondaryText);
                    }
                    break;
                case "3":
                    if (position == (3 + saved.size())){
                        for (MenualRecyclerData foo : recent) {
                            if (foo.identify.equals("rp_2")) {
                                currentMenual = foo;
                            }
                        }
                        holder.primaryText.setText(currentMenual.primaryText);
                        holder.secondaryText.setText(currentMenual.secondaryText);
                    }else if(position == (4 + saved.size())){
                        for (MenualRecyclerData foo : recent) {
                            if (foo.identify.equals("rp_1")) {
                                currentMenual = foo;
                            }
                        }
                        holder.primaryText.setText(currentMenual.primaryText);
                        holder.secondaryText.setText(currentMenual.secondaryText);
                    }else{
                        for (MenualRecyclerData foo : recent) {
                            if (foo.identify.equals("rp_3")) {
                                currentMenual = foo;
                            }
                        }
                        holder.primaryText.setText(currentMenual.primaryText);
                        holder.secondaryText.setText(currentMenual.secondaryText);
                    }
                    break;
            }
        } else if (position == (3 + saved.size() + recent.size())) {
            //set location on map block
            holder.imageIcon.setImageResource(imageIcons[5]);
            holder.primaryText.setText(R.string.set_location_on_map);
            holder.secondaryText.setText("");
            holder.primaryText.setTranslationY((10 * (context.getResources().getDisplayMetrics().density)));
        }

        holder.itemHostLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String identify;
                if (position > (2 + saved.size()) && position <= (2 + saved.size() + recent.size())) {
                    if (position == (3 + saved.size())){
                        identify = "one";
                    }else if(position == (4 + saved.size())){
                        identify = "two";
                    }else{
                        identify = "three";
                    }
                    OnItemClicked(view, holder.getAdapterPosition(), identify);
                }else {
                    TextView primaryTextView = view.findViewById(R.id.text_one);
                    identify = primaryTextView.getText().toString();
                    OnItemClicked(view, holder.getAdapterPosition(), identify);
                }
            }
        });

    }

    abstract void OnItemClicked(View v, int position, String identify);

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

    public void updateRecent(String identify, MenualRecyclerData newFav, String start) {
        for (int i = 0; i < recent.size(); i++) {
            if (recent.get(i).identify.equals(identify)) {
                recent.remove(i);
            }
        }
        if (newFav != null) {
            recent.add(newFav);
            this.start = start;
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (4 + saved.size() + recent.size());
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
