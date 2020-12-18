package com.example.demo.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.demo.R;
import com.example.demo.activity.SettingsActivity;

public class MineFragment extends Fragment {

    private MineViewModel mineViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_mine, container, false);
        MineItemView mineItemView = root.findViewById(R.id.mine_settings);
        mineItemView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        mineItemView.TouchDown();
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        mineItemView.TouchUp();
                        Intent intent = new Intent(getActivity(), SettingsActivity.class);
                        startActivity(intent);
                        break;
                    }
                    default:
                        break;
                }
                return true;
            }
        });
        return root;
    }
}
