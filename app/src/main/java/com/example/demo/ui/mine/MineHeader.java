package com.example.demo.ui.mine;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.demo.MyApplication;
import com.example.demo.R;
import com.squareup.sqlbrite3.SqlBrite;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.functions.Consumer;

public class MineHeader extends RelativeLayout {

    private CircleImageView profilePhoto;

    public MineHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.mine_header, this);
        profilePhoto = findViewById(R.id.profile_photo);
        TextView userID = findViewById(R.id.user_id);
        TextView userName = findViewById(R.id.user_name);
        final RelativeLayout root = findViewById(R.id.mine_header_root);
        userID.setText(MyApplication.getUsername());
        Cursor cursor = MyApplication.getDatabase().query("select name from user where id = ?", MyApplication.getUsername());
        if (cursor.moveToFirst()) {
            userName.setText(cursor.getString(0));
        }
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

    public void setPhoto(String image) {
        if (image.isEmpty()) {
            profilePhoto.setImageResource(R.drawable.account_circle_80);
        } else {
            byte[] bytes = Base64.decode(image.getBytes(), Base64.DEFAULT);
            profilePhoto.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        }
    }
}