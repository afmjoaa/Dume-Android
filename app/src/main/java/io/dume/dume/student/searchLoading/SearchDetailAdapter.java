package io.dume.dume.student.searchLoading;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.dume.dume.R;

public class SearchDetailAdapter extends RecyclerView.Adapter<SearchDetailAdapter.MyViewHolder> {

    private static final String TAG = "SearchDetailAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<SearchDetailData> data;

    public SearchDetailAdapter(Context context , List<SearchDetailData> data){
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_search_detail_row, parent, false);
        SearchDetailAdapter.MyViewHolder holder = new SearchDetailAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView searchDetailMain;
        private final TextView searchDetailSub;
        private final TextView searchDetailChange;

        public MyViewHolder(View itemView) {
            super(itemView);
            searchDetailMain = itemView.findViewById(R.id.search_detail_main);
            searchDetailSub = itemView.findViewById(R.id.search_detail_sub);
            searchDetailChange = itemView.findViewById(R.id.search_interact_info);

        }
    }
}
