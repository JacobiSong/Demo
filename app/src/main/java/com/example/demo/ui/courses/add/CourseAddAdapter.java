package com.example.demo.ui.courses.add;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.demo.R;
import com.example.demo.ui.courses.CourseProfile;

import org.w3c.dom.Text;

import java.util.List;

public class CourseAddAdapter extends BaseAdapter {
    private Context context;
    private List<CourseAddProfile> data;
    public CourseAddAdapter(Context context, List<CourseAddProfile> data) {
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
        View view = inflater.inflate(R.layout.course_add_item, null);
        final TextView name = view.findViewById(R.id.course_add_name);
        final TextView time = view.findViewById(R.id.course_add_time);
        name.setText(data.get(position).getName());
        time.setText(data.get(position).getTime());
        return view;
    }
}
