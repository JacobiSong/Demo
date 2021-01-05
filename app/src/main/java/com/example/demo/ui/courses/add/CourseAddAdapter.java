package com.example.demo.ui.courses.add;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.demo.R;
import com.example.demo.datagram.DatagramProto;

public class CourseAddAdapter extends BaseAdapter {
    private final Context context;
    private final DatagramProto.Courses data;
    public CourseAddAdapter(Context context, DatagramProto.Courses data) {
        this.context = context;
        this.data = data;
    }
    @Override
    public int getCount() {
        return data.getCoursesCount();
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
        View view = inflater.inflate(R.layout.course_add_item, null);
        final TextView name = view.findViewById(R.id.course_add_name);
        final TextView time = view.findViewById(R.id.course_add_time);
        name.setText(data.getCourses(position).getName());
        time.setText(data.getCourses(position).getTime());
        return view;
    }
}
