package com.example.demo.ui.mine;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.demo.R;

public class MineItemView extends RelativeLayout {
    private ImageView imageView;
    private TextView textView;
    private RelativeLayout relativeLayout;
    private RelativeLayout line;
    public MineItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.mine_item_view,this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MineItemView);
        imageView = findViewById(R.id.mine_icon);
        textView = findViewById(R.id.mine_text);
        textView.setText(typedArray.getString(R.styleable.MineItemView_mine_text));
        imageView.setBackgroundResource(typedArray.getResourceId(R.styleable.MineItemView_mine_icon, R.drawable.settings_24));
        relativeLayout = findViewById(R.id.mine_root);
        line = findViewById(R.id.mine_line);
        if (typedArray.getBoolean(R.styleable.MineItemView_mine_line, false)) {
            line.setBackgroundColor(getResources().getColor(R.color.light_gray));
        }
        typedArray.recycle();
    }

    public void TouchDown() {
        relativeLayout.setBackgroundColor(getResources().getColor(R.color.gray));
    }
    public void TouchUp() {
        relativeLayout.setBackgroundColor(getResources().getColor(R.color.white));
    }
}
