package com.example.demo.ui.courses.chat.menu.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.datagram.DatagramProto;
import com.example.demo.ui.notifications.notification.NotificationActivity;
import com.example.demo.utils.TimeConverter;

import java.util.List;
import java.util.Objects;

public class CourseNoticeAdapter extends RecyclerView.Adapter<CourseNoticeAdapter.NoticeHolder> {

    private final LiveData<List<DatagramProto.Notification>> data;
    private final Context context;

    public CourseNoticeAdapter(Context context, LiveData<List<DatagramProto.Notification>> data) {
        this.context = context;
        this.data = data;
    }

    static class NoticeHolder extends RecyclerView.ViewHolder {

        private final TextView contentTextView;
        private final TextView authorTextView;
        private final TextView dateTextView;

        public NoticeHolder(@NonNull View itemView) {
            super(itemView);
            this.contentTextView = itemView.findViewById(R.id.textCourseNoticeContent);
            this.authorTextView = itemView.findViewById(R.id.textCourseNoticeAuthor);
            this.dateTextView = itemView.findViewById(R.id.textCourseNoticeDate);
        }
    }

    @NonNull
    @Override
    public NoticeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notice, parent, false);
        return new NoticeHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull NoticeHolder holder, int position) {
        DatagramProto.Notification notice = Objects.requireNonNull(data.getValue()).get(data.getValue().size() - 1 - position);
        holder.contentTextView.setText(notice.getContent());
        holder.authorTextView.setText(notice.getSenderId());
        holder.dateTextView.setText(TimeConverter.long2LocalDateTime(notice.getTime()).toString());
        holder.itemView.setOnClickListener(v -> context.startActivity(new Intent(context, NotificationActivity.class)
                .putExtra("id", data.getValue().get(position).getId())
                .putExtra("receiver_id", data.getValue().get(position).getReceiverId())));
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(this.data.getValue()).size();
    }
}
