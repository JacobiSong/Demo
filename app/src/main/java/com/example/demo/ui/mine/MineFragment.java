package com.example.demo.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.demo.MyApplication;
import com.example.demo.R;
import com.example.demo.ui.SettingsActivity;
import com.example.demo.ui.mine.profile.UserProfileActivity;

public class MineFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_mine, container, false);
        MineHeader mineHeader = root.findViewById(R.id.mine_header);
        mineHeader.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UserProfileActivity.class);
            startActivity(intent);
        });
        MineItemView mineItemView = root.findViewById(R.id.mine_settings);
        mineItemView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        });
        Button button = root.findViewById(R.id.logout_button);
        button.setOnClickListener(v -> MyApplication.getServer().logout());
        return root;
    }
}
