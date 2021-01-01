package com.example.demo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.entity.Student;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberHolder> {

    //临时使用Student类
    private final List<Student> studentList;

    public MemberAdapter(List<Student> studentList) {
        super();
        this.studentList = studentList;
    }

    class MemberHolder extends RecyclerView.ViewHolder {
        private final TextView usernameTextView;
        private final TextView typeTextView;
        private final ImageView iconImageView;

        public MemberHolder(View itemView) {
            super(itemView);
            this.usernameTextView = itemView.findViewById(R.id.textViewForUsername);
            this.typeTextView = itemView.findViewById(R.id.textViewForUserType);
            this.iconImageView = itemView.findViewById(R.id.imageViewForUser);
        }
    }

    @NonNull
    @Override
    public MemberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_member, parent, false);
        return new MemberAdapter.MemberHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberHolder holder, int position) {
        Student student = this.studentList.get(position);
        //设置头像，此处暂时设置为固定头像，有需要可更改
        holder.iconImageView.setImageResource(R.drawable.ic_user);
        holder.usernameTextView.setText(student.getName());
        //设置用户类型，此处暂时设置为学生，后续需要更改
        holder.typeTextView.setText("学生");
    }

    @Override
    public int getItemCount() {
        return this.studentList.size();
    }


}
