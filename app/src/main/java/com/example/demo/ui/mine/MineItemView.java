package com.example.demo.ui.mine;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.demo.R;

public class MineItemView extends RelativeLayout {
    public MineItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.mine_item_view,this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MineItemView);
        ImageView imageView = findViewById(R.id.mine_icon);
        TextView textView = findViewById(R.id.mine_text);
        textView.setText(typedArray.getString(R.styleable.MineItemView_mine_text));
        imageView.setBackgroundResource(typedArray.getResourceId(R.styleable.MineItemView_mine_icon, R.drawable.settings_24));
        RelativeLayout relativeLayout = findViewById(R.id.mine_item_root);
        RelativeLayout line = findViewById(R.id.mine_line);
        if (typedArray.getBoolean(R.styleable.MineItemView_mine_line, false)) {
            line.setBackgroundColor(getResources().getColor(R.color.light_gray));
        }
        typedArray.recycle();
        setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    relativeLayout.setBackgroundColor(getResources().getColor(R.color.gray));
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    relativeLayout.setBackgroundColor(getResources().getColor(R.color.white));
                    break;
                }
                default:
                    break;
            }
            return false;
        });
    }
}
