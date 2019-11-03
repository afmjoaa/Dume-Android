package io.dume.dume.teacher.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import io.dume.dume.teacher.mentor_settings.academic.AcademicActivity;
import io.dume.dume.teacher.pojo.Academic;

import static io.dume.dume.util.DumeUtils.setMargins;

public class AcademicAdapter extends RecyclerView.Adapter<AcademicAdapter.AcademicVH> {

    private static final String TAG = "AcademicAdapter";
    private final Activity activity;
    private final List<Academic> data;
    private Context context;
    int[] imageIcons = {
            R.drawable.academic_school,
            R.drawable.academic_college,
            R.drawable.academic_undergraduate,
            R.drawable.academic_postgraduate
    };
    private final float mDensity;

    public AcademicAdapter(Context context, List<Academic> data) {
        this.context = context;
        this.activity = (Activity) context;
        this.data = data;
        mDensity = context.getResources().getDisplayMetrics().density;
    }

    @NonNull
    @Override
    public AcademicVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new AcademicVH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.academic_fragment_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AcademicVH academicVH, int position) {

        Academic current = data.get(position);
        switch (current.getLevel()) {
            case "School/O Level":
                academicVH.levelImage.setImageResource(imageIcons[0]);
                break;
            case "College/A Level":
                academicVH.levelImage.setImageResource(imageIcons[1]);
                break;
            case "Under Graduation":
                academicVH.levelImage.setImageResource(imageIcons[2]);
                break;
            case "Post Graduation":
                academicVH.levelImage.setImageResource(imageIcons[3]);
                break;
        }

        academicVH.institutionTV.setText(current.getInstitute());
        academicVH.degreeTV.setText(current.getDegree());
        academicVH.resultTV.setText(current.getResult());
        if (current.getResultType() == 1) {//studying
            academicVH.sessionTV.setText(String.format("Initiated -%s", current.getFrom()));
        } else {
            academicVH.sessionTV.setText(String.format("Session %s-%s", current.getFrom(), current.getTo()));
        }

        academicVH.hostRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(context, AcademicActivity.class);
                intent.setAction("edit");
                Bundle bundle = new Bundle();
                Academic hereCurrent = data.get(position);
                bundle.putString("level", hereCurrent.getLevel());
                bundle.putString("institution",hereCurrent.getInstitute());
                bundle.putString("degree", hereCurrent.getDegree());
                bundle.putString("from_year", hereCurrent.getFrom());
                bundle.putString("to_year", hereCurrent.getTo());
                bundle.putString("result", hereCurrent.getResult());
                bundle.putInt("resultType", hereCurrent.getResultType());
                intent.putExtra("academic_data", bundle);
                activity.startActivityForResult(intent, 1234);
            }
        });

        if(position==(data.size()-1)){
            setMargins( academicVH.hostRelativeLayout,10,10,10,4);
        }

    }

    public int getRecyItemCount() {
        return data.size();
    }

    public void update(List<Academic> newData) {
        data.clear();
        data.addAll(newData);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class AcademicVH extends RecyclerView.ViewHolder {

        private final TextView resultTV;
        private final TextView sessionTV;
        private final TextView institutionTV;
        private final TextView degreeTV;
        private final ImageView levelImage;
        private final RelativeLayout hostRelativeLayout;

        public AcademicVH(@NonNull View itemView) {
            super(itemView);
            hostRelativeLayout = itemView.findViewById(R.id.hosting_relative_layout);
            levelImage = itemView.findViewById(R.id.search_detail_icon);
            degreeTV = itemView.findViewById(R.id.levelET);
            institutionTV = itemView.findViewById(R.id.institutionET);
            sessionTV = itemView.findViewById(R.id.sessionTV);
            resultTV = itemView.findViewById(R.id.resultTV);

        }
    }
}
