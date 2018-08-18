package io.dume.dume.teacher.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import io.dume.dume.R;

public class AccountSettingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<String> dataList;
    private static final int PROFILE_VIEW = 0, ACADEMIC_QUALIFICATION = 1, LOCATION_VIEW = 2, THIRD = 3;
    private static final String TAG = "AccountSettingsAdapter";

    public AccountSettingsAdapter(Context context, ArrayList<String> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return PROFILE_VIEW;
        } else if (position == 1) {
            return ACADEMIC_QUALIFICATION;
        } else if (position == 2) {
            return LOCATION_VIEW;
        }
        return THIRD;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder;
        if (viewType == PROFILE_VIEW) {
            View badgeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_settings_profile_bedge, parent, false);
            holder = new BadgeSection(badgeView);
        } else if (viewType == ACADEMIC_QUALIFICATION) {
            View academicView = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_settings_academic_qualification, parent, false);
            holder = new AcademicSectionVH(academicView);
        } else if (viewType == LOCATION_VIEW) {
            View locationView = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_settings_item_location, parent, false);
            holder = new LocationVH(locationView);
        } else {
            View dummy = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_settings_item, parent, false);
            holder = new AccountViewHolder(dummy);
        }
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == PROFILE_VIEW) {
            BadgeSection badgeHolder = (BadgeSection) holder;
            badgeHolder.sectionTitle.setText("Badge");
            badgeHolder.sectionRecyclerView.setLayoutManager(new LinearLayoutManager(context, OrientationHelper.HORIZONTAL, false));
            badgeHolder.sectionRecyclerView.setAdapter(new RecyclerView.Adapter<AccountSettingsAdapter.BadgeImageHolder>() {

                @NonNull
                @Override
                public BadgeImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View badgeImage = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_settings_badge_item, parent, false);

                    return new BadgeImageHolder(badgeImage);
                }

                @Override
                public void onBindViewHolder(@NonNull BadgeImageHolder holder, int position) {
                    if (holder.getItemId() > 2) {
                        holder.badgeImageView.setAlpha(0.4f);
                    }
                    Log.w(TAG, "onBindViewHolder: ItemID : " + holder.getItemId());
                    Log.w(TAG, "onBindViewHolder: Holder Position : " + holder.getAdapterPosition());
                    Log.w(TAG, "onBindViewHolder: Position" + position);


                }

                @Override
                public int getItemCount() {
                    return 10;
                }


            });
        } else if (holder.getItemViewType() == ACADEMIC_QUALIFICATION) {
            AcademicSectionVH vh = (AcademicSectionVH) holder;
            vh.sectionTitle.setText("Academic Qualification");
        } else if (holder.getItemViewType() == LOCATION_VIEW) {
            LocationVH vh = (LocationVH) holder;
            vh.title.setText("Location");
        } else {
            AccountViewHolder dummy = (AccountViewHolder) holder;
            dummy.item.setText(dataList.get(position));
            dummy.subItem.setText("Sub Item " + position);
        }


    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class AccountViewHolder extends RecyclerView.ViewHolder {
        TextView item, subItem;

        AccountViewHolder(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.itemTextView);
            subItem = itemView.findViewById(R.id.itemSubTextView);
        }
    }

    class BadgeSection extends RecyclerView.ViewHolder {
        TextView sectionTitle;
        RecyclerView sectionRecyclerView;

        BadgeSection(View itemView) {
            super(itemView);
            sectionRecyclerView = itemView.findViewById(R.id.badgeRecyclerView);
            sectionTitle = itemView.findViewById(R.id.sectionTitle);
        }
    }

    class BadgeImageHolder extends RecyclerView.ViewHolder {
        ImageView badgeImageView;

        BadgeImageHolder(View itemView) {
            super(itemView);
            badgeImageView = itemView.findViewById(R.id.badgeImageView);
        }
    }

    class AcademicSectionVH extends RecyclerView.ViewHolder {
        TextView sectionTitle;
        RecyclerView academicRecyclerView;

        AcademicSectionVH(View itemView) {
            super(itemView);
            sectionTitle = itemView.findViewById(R.id.sectionTextView);
            academicRecyclerView = itemView.findViewById(R.id.academic_list);
        }
    }

    class LocationVH extends RecyclerView.ViewHolder {
        TextView location, warning, title;
        Button update, add;

        LocationVH(View itemView) {
            super(itemView);
            location = itemView.findViewById(R.id.locationTV);
            warning = itemView.findViewById(R.id.warningTV);
            update = itemView.findViewById(R.id.updateBTN);
            add = itemView.findViewById(R.id.addLocationBTN);
            title = itemView.findViewById(R.id.title);
        }
    }
}
