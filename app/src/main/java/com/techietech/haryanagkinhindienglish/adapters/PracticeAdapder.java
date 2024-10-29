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
import com.techietech.haryanagkinhindienglish.models.PracticeModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PracticeAdapder extends RecyclerView.Adapter<PracticeAdapder.viewholder>{

    private List<PracticeModel> practiceModelList;

    public PracticeAdapder(List<PracticeModel> practiceModelList) {
        this.practiceModelList = practiceModelList;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.practice_category_item, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder.setData(practiceModelList.get(position).getUrl(),practiceModelList.get(position).getName(),position);

    }

    @Override
    public int getItemCount() {
        return practiceModelList.size();
    }

    class viewholder extends RecyclerView.ViewHolder {

        private CircleImageView circleImageView;
        private TextView title,subtitle;

        private viewholder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.circle_practiceLogo);
            title = itemView.findViewById(R.id.tv_practiceView_title);
            subtitle = itemView.findViewById(R.id.tv_practiceView_subtitle);

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
