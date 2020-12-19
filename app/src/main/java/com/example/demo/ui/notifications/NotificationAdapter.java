package com.example.demo.ui.notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.demo.R;
import com.example.demo.ui.courses.CourseProfile;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends BaseAdapter {
    private Context context;
    private List<NotificationProfile> data;
    public NotificationAdapter(Context context, List<NotificationProfile> data) {
        this.context = context;
        this.data = data;
    }
    @Override
    public int getCount() {
        return data.size();
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
        notificationCourse.setText(data.get(position).getCourse());
        notificationMessage.setText(data.get(position).getMessage());
        notificationTime.setText(data.get(position).getTime());
        return view;
    }
}
