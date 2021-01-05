package com.example.demo.ui.coursemenu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.demo.R;
import com.example.demo.entity.Student;

import java.util.List;

public class MemberThumbAdapter extends BaseAdapter {

    private final Context context;
    private final List<Student> studentList;

    public MemberThumbAdapter(Context context, List<Student> studentList) {
        this.context = context;
        this.studentList = studentList;
    }

    @Override
    public int getCount() {
        return this.studentList.size();
    }

    @Override
    public Object getItem(int position) {
        return studentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //后续需要修改
        Student student = this.studentList.get(position);

        UserThumbHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context.getApplicationContext(), R.layout.item_member_thumb, null);
            holder = new UserThumbHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (UserThumbHolder) convertView.getTag();
        }

        holder.iconImageView.setImageResource(R.drawable.ic_user);
        holder.usernameTextView.setText(student.getName());

        return convertView;
    }


    class UserThumbHolder {

        TextView usernameTextView;
        ImageView iconImageView;

        UserThumbHolder(View view) {
            this.usernameTextView = view.findViewById(R.id.textForUsernameInThumb);
            this.iconImageView = view.findViewById(R.id.imageForUserInThumb);
        }
    }
}


