package com.example.demo.ui.mine.profile;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.demo.R;

public class UserProfileItem extends RelativeLayout {
    private TextView text;
    private ImageView arrow;
    private RelativeLayout line;
    private RelativeLayout root;
    public UserProfileItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.user_profile_item,this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UserProfileItem);
        text = findViewById(R.id.user_profile_item_text);
        arrow = findViewById(R.id.user_profile_item_arrow);
        line = findViewById(R.id.user_profile_item_line);
        root = findViewById(R.id.user_profile_root);
        text.setText(typedArray.getString(R.styleable.UserProfileItem_user_profile_text));
        if (typedArray.getBoolean(R.styleable.UserProfileItem_user_profile_line, false)) {
            line.setBackgroundColor(getResources().getColor(R.color.light_gray));
        }
        else {
            line.setBackgroundColor(getResources().getColor(R.color.white));
        }
        if (typedArray.getBoolean(R.styleable.UserProfileItem_user_profile_arrow, false)) {
            arrow.setBackground(getResources().getDrawable(R.drawable.arrow_right_24));
        }
        else {
            arrow.setBackground(null);
        }
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
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
                return true;
            }
        });
    }
}
