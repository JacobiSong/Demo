package com.example.demo.ui.mine;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.demo.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MineHeader extends RelativeLayout {
    private CircleImageView profilePhoto;
    private TextView userName;
    private TextView userID;

    public MineHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.mine_header, this);
        profilePhoto = findViewById(R.id.profile_photo);
        userID = findViewById(R.id.user_id);
        userName = findViewById(R.id.user_name);
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        userID.setText(sharedPreferences.getString("user_id", "118*******"));
        userName.setText(sharedPreferences.getString("user_name", "张三"));
        profilePhoto.setImageResource(R.drawable.account_circle_80);
        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        break;
                    }
                    default:
                        break;
                }
                return true;
            }
        });
    }
}