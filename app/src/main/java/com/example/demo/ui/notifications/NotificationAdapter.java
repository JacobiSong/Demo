package com.example.demo.ui.notifications;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.demo.MyApplication;
import com.example.demo.R;
import com.example.demo.datagram.DatagramProto;
import com.example.demo.utils.TimeConverter;

import java.time.LocalDateTime;
import java.util.List;

public class NotificationAdapter extends BaseAdapter {
    private final Context context;
    private final DatagramProto.Notifications data;
    public NotificationAdapter(Context context, DatagramProto.Notifications data) {
        this.context = context;
        this.data = data;
    }
    @Override
    public int getCount() {
        return data.getNotificationsCount();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.notification_item, null);

        final TextView notificationCourse = view.findViewById(R.id.notification_course);
        final TextView notificationMessage = view.findViewById(R.id.notification_message);
        final TextView notificationTime = view.findViewById(R.id.notification_time);
        Cursor cursor = MyApplication.getDatabase().query("select name from course where id = ?",  data.getNotifications(position).getReceiverId());
        if (cursor.moveToFirst()) {
            notificationCourse.setText(cursor.getString(0));
        }
        notificationMessage.setText(data.getNotifications(position).getTitle());
        LocalDateTime localDateTime = TimeConverter.long2LocalDateTime(data.getNotifications(position).getTime());
        notificationTime.setText(localDateTime.toString());
        return view;
    }
}
