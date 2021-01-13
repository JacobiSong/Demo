package com.example.demo.ui.courses.chat.menu.notification;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.MyApplication;
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

        private final TextView title;
        private final TextView author;
        private final TextView time;

        public NoticeHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notification_course);
            author = itemView.findViewById(R.id.notification_message);
            time = itemView.findViewById(R.id.notification_time);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public NoticeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item, parent, false);
        return new NoticeHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull NoticeHolder holder, int position) {
        DatagramProto.Notification notice = Objects.requireNonNull(data.getValue()).get(data.getValue().size() - 1 - position);
        holder.title.setText(notice.getTitle());
        Cursor cursor = MyApplication.getDatabase().query("select name from user where id = ?", notice.getSenderId());
        if (cursor.moveToFirst()) {
            holder.author.setText(cursor.getString(0));
        }
        holder.time.setText(TimeConverter.long2LocalDateTime(notice.getTime()).toString());
        holder.itemView.setOnClickListener(v -> context.startActivity(new Intent(context, NotificationActivity.class)
                .putExtra("id", notice.getId())
                .putExtra("receiver_id", notice.getReceiverId())));
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(data.getValue()).size();
    }
}
