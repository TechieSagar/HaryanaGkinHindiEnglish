package com.techietech.haryanagkinhindienglish.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.techietech.haryanagkinhindienglish.R;
import com.techietech.haryanagkinhindienglish.activities.SetsActivity;
import com.techietech.haryanagkinhindienglish.models.CategoryModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewholder>{

    private List<CategoryModel> categoryModelList;

    public CategoryAdapter(List<CategoryModel> categoryModelList) {
        this.categoryModelList = categoryModelList;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder.setData(categoryModelList.get(position).getUrl(),categoryModelList.get(position).getName(),position);

    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    class viewholder extends RecyclerView.ViewHolder {

        private CircleImageView circleImageView;
        private TextView title,subtitle;

        private viewholder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.circle_image_itemView);
            title = itemView.findViewById(R.id.tv_category_itemView);
            subtitle = itemView.findViewById(R.id.tv_sets_itemView);

        }

        private void setData(String url, final String title, final int position){
            Glide.with(itemView.getContext()).load(url).into(circleImageView);
            this.title.setText(title);



            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(itemView.getContext(), SetsActivity.class);
                intent.putExtra("title",title);
                intent.putExtra("position",position);
                itemView.getContext().startActivity(intent);
            });

        }
    }

}
