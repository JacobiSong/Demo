package com.example.demo.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
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
import com.squareup.sqlbrite3.SqlBrite;

import io.reactivex.functions.Consumer;

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
        MyApplication.getDatabase().createQuery("user", "").subscribe(
                new Consumer<SqlBrite.Query>() {
                    @Override
                    public void accept(SqlBrite.Query query) throws Exception {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            SharedPreferences sp = requireActivity().getSharedPreferences("user_" + MyApplication.getUsername(), Context.MODE_PRIVATE);
                                            mineHeader.setPhoto(sp.getString("photo", ""));
                                        }
                                    }
                            );
                        }
                    }
                }
        );
        return root;
    }
}
