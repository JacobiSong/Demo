package com.example.demo.ui.courses.chat.menu.member;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.datagram.DatagramProto;
import com.example.demo.ui.courses.chat.UserInfoActivity;

import java.util.List;
import java.util.Objects;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberHolder> {

    private final LiveData<List<DatagramProto.User>> data;
    private final Context context;

    public MemberAdapter(Context context, LiveData<List<DatagramProto.User>> data) {
        this.context = context;
        this.data = data;
    }

    static class MemberHolder extends RecyclerView.ViewHolder {
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
        return new MemberHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberHolder holder, int position) {
        DatagramProto.User student = Objects.requireNonNull(this.data.getValue()).get(position);
        holder.iconImageView.setImageResource(R.drawable.account_circle_80);
        holder.usernameTextView.setText(student.getName());
        holder.typeTextView.setText("学生");
        holder.itemView.setOnClickListener(v -> context.startActivity(new Intent(context, UserInfoActivity.class)
                .putExtra("id", data.getValue().get(position).getId())
                .putExtra("last_modified", data.getValue().get(position).getLastModified())));
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(this.data.getValue()).size();
    }
}
