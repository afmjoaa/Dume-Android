package io.dume.dume.student.common;

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

public abstract class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.MyViewHolder> {

    private static final String TAG = "SettingsAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List<SettingData> data;

    public SettingsAdapter(Context context , List<SettingData> data){
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_settings_row, parent, false);
        SettingsAdapter.MyViewHolder holder = new SettingsAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SettingData current = data.get(position);
        holder.settingsIcon.setImageResource(current.settingIcon);
        holder.settinsName.setText(current.settingName);
        holder.hostRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnButtonClicked(v, position);
            }
        });
        if(position == (data.size()-1)){
            holder.divider.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    protected abstract void OnButtonClicked(View v ,int position);

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView settingsIcon;
        private final TextView settinsName;
        private final RelativeLayout hostRelativeLayout;
        private final View divider;

        public MyViewHolder(View itemView) {
            super(itemView);
            settinsName = itemView.findViewById(R.id.settings_name);
            settingsIcon = itemView.findViewById(R.id.settings_icon);
            hostRelativeLayout = itemView.findViewById(R.id.hostRelativeLayout);
            divider = itemView.findViewById(R.id.divider2);
        }
    }
}
