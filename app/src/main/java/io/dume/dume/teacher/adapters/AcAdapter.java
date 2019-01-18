package io.dume.dume.teacher.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dume.dume.R;
import io.dume.dume.teacher.mentor_settings.academic.AcademicActivity;
import io.dume.dume.teacher.mentor_settings.academic.AcademicContract;
import io.dume.dume.teacher.mentor_settings.academic.AcademicModel;
import io.dume.dume.teacher.mentor_settings.basicinfo.EditAccount;
import io.dume.dume.teacher.mentor_settings.basicinfo.EditModel;
import io.dume.dume.teacher.pojo.Academic;

public class AcAdapter extends RecyclerView.Adapter<AcAdapter.AcademicVH> {
    private List<Academic> list;
    private Context context;

    public AcAdapter(List<Academic> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public AcademicVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        return new AcademicVH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_saved_places_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AcademicVH academicVH, int i) {
        academicVH.title.setText("Degree Title Goes Here");
        academicVH.menu.setImageResource(R.drawable.ic_more_vert_black_24dp);
        academicVH.menu.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(context, view);
            popup.getMenuInflater().inflate(R.menu.menu_edit_remove, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_remove:
                         AcademicModel model = AcademicModel.getInstance();
                        model.removeFromDatabase(list.get(i).getDegree(), new AcademicContract.Model.ModelCallback() {
                            @Override
                            public void onStart() {
                                Toast.makeText(context, "Item Removed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess() {
                                AcademicActivity.isDeleted = true;
                            }

                            @Override
                            public void onFail(String error) {
                              
                            }
                        });
                        break;
                    case R.id.action_edit:
                        final Intent intent = new Intent(context, AcademicActivity.class);
                        intent.setAction("edit");
                        context.startActivity(intent);
                        break;
                }
                return true;
            });
            popup.show();

        });
        academicVH.title.setText(list.get(i).getDegree());
        academicVH.subTitle.setText(list.get(i).getInstitute());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class AcademicVH extends RecyclerView.ViewHolder {
        @BindView(R.id.text_one)
        TextView title;
        @BindView(R.id.text_two)
        TextView subTitle;
        @BindView(R.id.more_vertical_icon)
        ImageView menu;

        public AcademicVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
