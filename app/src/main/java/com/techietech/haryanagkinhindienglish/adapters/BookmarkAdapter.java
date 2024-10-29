package com.techietech.haryanagkinhindienglish.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techietech.haryanagkinhindienglish.R;
import com.techietech.haryanagkinhindienglish.models.QuestionModel;

import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.viewholder> {

    private final List<QuestionModel> list;

    public BookmarkAdapter(List<QuestionModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_item, parent, false);

        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder.setData(list.get(position).getQuestion(),list.get(position).getAnswer(),position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewholder extends RecyclerView.ViewHolder{

        private final TextView question;
        private final TextView answer;
        private final ImageButton deleteButton;

        private viewholder(@NonNull View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.question_bItem);
            answer = itemView.findViewById(R.id.answer_bItem);
            deleteButton = itemView.findViewById(R.id.btn_deleteBI);
        }

        private void setData(String question, String answer, final int position){
            this.question.setText(question);
            this.answer.setText(answer);

            deleteButton.setOnClickListener(view -> {
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemChanged(position);
            });

        }

    }



}
