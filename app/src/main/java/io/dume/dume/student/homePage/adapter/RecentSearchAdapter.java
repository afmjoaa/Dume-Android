package io.dume.dume.student.homePage.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import carbon.widget.ImageView;
import io.dume.dume.R;

import static io.dume.dume.util.DumeUtils.giveIconOnCategoryName;

public abstract class RecentSearchAdapter extends RecyclerView.Adapter<RecentSearchAdapter.MyViewHolder> {

    private static final String TAG = "RecentSearchAdapter";
    private String start;
    private LayoutInflater inflater;
    private Context context;
    private List<RecentSearchData> data;
    private int[] navIcons = {
            R.drawable.education,
            R.drawable.software,
            R.drawable.programming,
            R.drawable.language,
            R.drawable.dance,
            R.drawable.art,
            R.drawable.cooking,
            R.drawable.music,
            R.drawable.others
    };


    public RecentSearchAdapter(Context context, List<RecentSearchData> data, String start) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.start = start;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.custom_recent_search_row, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        RecentSearchData currentMenual = new RecentSearchData();
        switch (start) {
            case "1":
                if (position == 0) {
                    for (RecentSearchData foo : data) {
                        if (foo.identify.equals("rs_3")) {
                            currentMenual = foo;
                        }
                    }
                    myViewHolder.image.setImageResource(navIcons[giveIconOnCategoryName(currentMenual.getCategoryName())]);
                    myViewHolder.primaryText.setText(currentMenual.primaryText);
                    myViewHolder.secondaryText.setText(currentMenual.secondaryText);
                } else if (position == 1) {
                    for (RecentSearchData foo : data) {
                        if (foo.identify.equals("rs_2")) {
                            currentMenual = foo;
                        }
                    }
                    myViewHolder.image.setImageResource(navIcons[giveIconOnCategoryName(currentMenual.getCategoryName())]);
                    myViewHolder.primaryText.setText(currentMenual.primaryText);
                    myViewHolder.secondaryText.setText(currentMenual.secondaryText);
                } else {
                    for (RecentSearchData foo : data) {
                        if (foo.identify.equals("rs_1")) {
                            currentMenual = foo;
                        }
                    }
                    myViewHolder.image.setImageResource(navIcons[giveIconOnCategoryName(currentMenual.getCategoryName())]);
                    myViewHolder.primaryText.setText(currentMenual.primaryText);
                    myViewHolder.secondaryText.setText(currentMenual.secondaryText);
                }
                break;
            case "2":
                if (position == 0) {
                    for (RecentSearchData foo : data) {
                        if (foo.identify.equals("rs_1")) {
                            currentMenual = foo;
                        }
                    }
                    myViewHolder.image.setImageResource(navIcons[giveIconOnCategoryName(currentMenual.getCategoryName())]);
                    myViewHolder.primaryText.setText(currentMenual.primaryText);
                    myViewHolder.secondaryText.setText(currentMenual.secondaryText);
                } else if (position == 1) {
                    for (RecentSearchData foo : data) {
                        if (foo.identify.equals("rs_3")) {
                            currentMenual = foo;
                        }
                    }
                    myViewHolder.image.setImageResource(navIcons[giveIconOnCategoryName(currentMenual.getCategoryName())]);
                    myViewHolder.primaryText.setText(currentMenual.primaryText);
                    myViewHolder.secondaryText.setText(currentMenual.secondaryText);
                } else {
                    for (RecentSearchData foo : data) {
                        if (foo.identify.equals("rs_2")) {
                            currentMenual = foo;
                        }
                    }
                    myViewHolder.image.setImageResource(navIcons[giveIconOnCategoryName(currentMenual.getCategoryName())]);
                    myViewHolder.primaryText.setText(currentMenual.primaryText);
                    myViewHolder.secondaryText.setText(currentMenual.secondaryText);
                }
                break;
            case "3":
                if (position == 0) {
                    for (RecentSearchData foo : data) {
                        if (foo.identify.equals("rs_2")) {
                            currentMenual = foo;
                        }
                    }
                    myViewHolder.image.setImageResource(navIcons[giveIconOnCategoryName(currentMenual.getCategoryName())]);
                    myViewHolder.primaryText.setText(currentMenual.primaryText);
                    myViewHolder.secondaryText.setText(currentMenual.secondaryText);
                } else if (position == 1) {
                    for (RecentSearchData foo : data) {
                        if (foo.identify.equals("rs_1")) {
                            currentMenual = foo;
                        }
                    }
                    myViewHolder.image.setImageResource(navIcons[giveIconOnCategoryName(currentMenual.getCategoryName())]);
                    myViewHolder.primaryText.setText(currentMenual.primaryText);
                    myViewHolder.secondaryText.setText(currentMenual.secondaryText);
                } else {
                    for (RecentSearchData foo : data) {
                        if (foo.identify.equals("rs_3")) {
                            currentMenual = foo;
                        }
                    }
                    myViewHolder.image.setImageResource(navIcons[giveIconOnCategoryName(currentMenual.getCategoryName())]);
                    myViewHolder.primaryText.setText(currentMenual.primaryText);
                    myViewHolder.secondaryText.setText(currentMenual.secondaryText);
                }
                break;
        }
        myViewHolder.relativeHostLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecentSearchData thisData = new RecentSearchData();
                int thisPosition = myViewHolder.getAdapterPosition();
                String primary = (String) myViewHolder.primaryText.getText();
                String secondary = (String) myViewHolder.secondaryText.getText();
                for (RecentSearchData foo : data) {
                    if (foo.primaryText.equals(primary) && foo.secondaryText.equals(secondary)) {
                        thisData = foo;
                    }
                }
                OnItemClicked(view, thisPosition, thisData.identify);
            }
        });

        if (position == (data.size() - 1)) {
            myViewHolder.divider.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public abstract void OnItemClicked(View v, int position, String identify);

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final View divider;
        private final TextView primaryText;
        private final TextView secondaryText;
        private final ImageView image;
        private final RelativeLayout relativeHostLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            divider = itemView.findViewById(R.id.divider2);
            primaryText = itemView.findViewById(R.id.search_detail_main);
            secondaryText = itemView.findViewById(R.id.search_detail_sub);
            image = itemView.findViewById(R.id.search_detail_icon);
            relativeHostLayout = itemView.findViewById(R.id.hostRelativeLayout);

        }
    }
}
