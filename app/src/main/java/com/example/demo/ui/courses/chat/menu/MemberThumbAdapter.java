package com.example.demo.ui.courses.chat.menu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.LiveData;

import com.example.demo.R;
import com.example.demo.datagram.DatagramProto;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberThumbAdapter extends BaseAdapter {

    private final Context context;
    private final LiveData<List<DatagramProto.User>> data;

    public MemberThumbAdapter(Context context, LiveData<List<DatagramProto.User>> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.getValue().size();
    }

    @Override
    public Object getItem(int position) {
        return data.getValue().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //后续需要修改
        DatagramProto.User user = data.getValue().get(position);

        UserThumbHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context.getApplicationContext(), R.layout.item_member_thumb, null);
            holder = new UserThumbHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (UserThumbHolder) convertView.getTag();
        }

        holder.iconImageView.setImageResource(R.drawable.account_circle_80);
        holder.usernameTextView.setText(user.getName());

        return convertView;
    }


    private static class UserThumbHolder {

        TextView usernameTextView;
        CircleImageView iconImageView;

        UserThumbHolder(View view) {
            this.usernameTextView = view.findViewById(R.id.textForUsernameInThumb);
            this.iconImageView = view.findViewById(R.id.imageForUserInThumb);
        }
    }
}


