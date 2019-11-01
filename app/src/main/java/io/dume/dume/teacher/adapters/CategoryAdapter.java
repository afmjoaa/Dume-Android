package io.dume.dume.teacher.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.dume.dume.R;

public abstract class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryVH> {
    private List<String> categories;
    private List<Integer> categoryImage;


    private CategoryAdapter() {
    }

     public CategoryAdapter(List<String> categories, List<Integer> categoryImage) {
        this.categories = categories;
        this.categoryImage = categoryImage;
    }

    @NonNull
    @Override
    public CategoryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View catItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_crud_skill_category_item, parent, false);
        return new CategoryVH(catItem);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryVH holder, int position) {
        holder.itemView.setOnClickListener(view -> {
            onCategoryItemClick(holder.itemView, position);
        });
        holder.icon.setImageResource(categoryImage.get(position));
        holder.title.setText(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoryVH extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;

        CategoryVH(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.ctIV);
            title = itemView.findViewById(R.id.ctTitle);

        }
    }


    protected abstract void onCategoryItemClick(View view, int position);

}
