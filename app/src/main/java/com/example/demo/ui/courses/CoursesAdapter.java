package com.example.demo.ui.courses;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.LiveData;

import com.example.demo.MyApplication;
import com.example.demo.R;
import com.example.demo.datagram.DatagramProto;
import com.example.demo.utils.TimeConverter;
import com.squareup.sqlbrite3.SqlBrite;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.functions.Consumer;

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
        DatagramProto.Course course = data.getValue().get(position);
        ((CircleImageView) view.findViewById(R.id.course_icon)).setImageResource(R.drawable.school_60);
        ((TextView) view.findViewById(R.id.course_name)).setText(course.getName());
        ((TextView) view.findViewById(R.id.course_info)).setText(course.getSemester() + " " + course.getTime());
        String courseId = course.getId();
        final ImageView redPoint = view.findViewById(R.id.red_point);
        final TextView messageNum = view.findViewById(R.id.message_num);
        Cursor cursor = MyApplication.getDatabase().query("select count(1) from " + courseId + "_m where time > ? and sender_id != ?", course.getLastModified(), MyApplication.getUsername());
        if (cursor.moveToFirst()) {
            int num = cursor.getInt(0);
            if (num == 0) {
                messageNum.setText("");
                redPoint.setImageDrawable(null);
            } else {
                messageNum.setText(String.valueOf(cursor.getInt(0)));
                redPoint.setImageDrawable(context.getDrawable(R.drawable.red_point));
            }
        }
        cursor = MyApplication.getDatabase().query("select time, content from " + courseId + "_m order by time desc limit 1");
        if (cursor.moveToFirst()) {
            String message = cursor.getString(1);
            if (message.length() > 10) {
                ((TextView) view.findViewById(R.id.course_message)).setText(message.substring(0, 10) + "...");
            } else {
                ((TextView) view.findViewById(R.id.course_message)).setText(message);
            }
            ((TextView) view.findViewById(R.id.message_time)).setText(TimeConverter.long2LocalDateTime(cursor.getLong(0)).toString());
        }
        return view;
    }
}