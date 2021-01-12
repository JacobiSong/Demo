package com.example.demo.ui.mine;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.demo.MyApplication;
import com.example.demo.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MineHeader extends RelativeLayout {

    public MineHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.mine_header, this);
        CircleImageView profilePhoto = findViewById(R.id.profile_photo);
        TextView userID = findViewById(R.id.user_id);
        TextView userName = findViewById(R.id.user_name);
        final RelativeLayout root = findViewById(R.id.mine_header_root);
        userID.setText(MyApplication.getUsername());
        Cursor cursor = MyApplication.getDatabase().query("select name from user where id = ?", MyApplication.getUsername());
        if (cursor.moveToFirst()) {
            userName.setText(cursor.getString(0));
        }
        profilePhoto.setImageResource(R.drawable.account_circle_80);
        setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    root.setBackgroundColor(getResources().getColor(R.color.gray));
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    root.setBackgroundColor(getResources().getColor(R.color.white));
                    break;
                }
                default:
                    break;
            }
            return false;
        });

    }
}