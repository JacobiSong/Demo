package com.example.demo.ui.courses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.lifecycle.LiveData;

import com.example.demo.R;
import com.example.demo.datagram.DatagramProto;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CoursesAdapter extends BaseAdapter {
    private final Context context;
    private final LiveData<List<DatagramProto.Course>> data;
    public CoursesAdapter(Context context, LiveData<List<DatagramProto.Course>> data) {
        this.context = context;
        this.data = data;
    }
    @Override
    public int getCount() {
        return Objects.requireNonNull(data.getValue()).size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.course_item, null);
        final CircleImageView circleImageView = view.findViewById(R.id.course_icon);
        final TextView courseName = view.findViewById(R.id.course_name);
        circleImageView.setImageResource(R.drawable.school_60);
        courseName.setText(Objects.requireNonNull(data.getValue()).get(position).getName());
        return view;
    }
}