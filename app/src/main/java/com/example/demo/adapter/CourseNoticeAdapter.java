package com.example.demo.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.entity.TimeFormat;
import com.example.demo.entity.Notification;

import java.util.List;

public class CourseNoticeAdapter extends RecyclerView.Adapter<CourseNoticeAdapter.NoticeHolder> {

    private final List<Notification> noticeList;

    public CourseNoticeAdapter(List<Notification> noticeList) {
        super();
        this.noticeList = noticeList;
    }

    class NoticeHolder extends RecyclerView.ViewHolder {

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
        return new CourseNoticeAdapter.NoticeHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull NoticeHolder holder, int position) {
        Notification notice = this.noticeList.get(position);
        holder.contentTextView.setText(notice.getContent());
        holder.authorTextView.setText(notice.getSenderId());
        holder.dateTextView.setText(TimeFormat.formatDateTime(notice.getTime()));
    }

    @Override
    public int getItemCount() {
        return this.noticeList.size();
    }
}
