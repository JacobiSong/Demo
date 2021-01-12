package com.example.demo.ui.mine.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demo.MyApplication;
import com.example.demo.R;

public class UserProfileFragment extends Fragment {

    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.user_profile_fragment, container, false);
        String userId = MyApplication.getUsername();
        UserProfileItem id = root.findViewById(R.id.user_id);
        UserProfileItem name = root.findViewById(R.id.user_name);
        UserProfileItem gender = root.findViewById(R.id.user_gender);
        UserProfileItem phone = root.findViewById(R.id.user_phone);
        UserProfileItem email = root.findViewById(R.id.user_email);
        UserProfileItem department = root.findViewById(R.id.user_department);
        UserProfileItem major = root.findViewById(R.id.user_major);
        UserProfileItem classNo = root.findViewById(R.id.user_class_no);
        UserProfileItem password = root.findViewById(R.id.user_password);
        id.setText(userId);
        Cursor c = MyApplication.getDatabase().query("select name from user where id = ?", userId);
        if (c.moveToFirst()) {
            name.setText(c.getString(0));
        }

        gender.setOnClickListener(v -> startActivity(
                new Intent(requireActivity(), UserProfileChangeActivity.class).setAction("com.example.demo.user.change.gender").putExtra("text", gender.getText())));
        phone.setOnClickListener(v -> startActivity(
                new Intent(requireActivity(), UserProfileChangeActivity.class).setAction("com.example.demo.user.change.phone").putExtra("text", phone.getText())));
        email.setOnClickListener(v -> startActivity(
                new Intent(requireActivity(), UserProfileChangeActivity.class).setAction("com.example.demo.user.change.email").putExtra("text", email.getText())));
        department.setOnClickListener(v -> startActivity(
                new Intent(requireActivity(), UserProfileChangeActivity.class).setAction("com.example.demo.user.change.department").putExtra("text", department.getText())));
        password.setOnClickListener(v -> startActivity(
                new Intent(requireActivity(), UserProfileChangeActivity.class).setAction("com.example.demo.user.change.password")
        ));

        MyApplication.getDatabase().createQuery("user", "select gender, phone, email from user where id = ?", userId).subscribe(query -> {
            Cursor cursor = query.run();
            if (cursor.moveToFirst()) {
                int g = cursor.getInt(0);
                requireActivity().runOnUiThread(() -> {
                    gender.setText(g == 0 ? "保密" : g == 1 ? "女" : "男");
                    phone.setText(cursor.getString(1));
                    email.setText(cursor.getString(2));
                });
            }
        });
        if (requireActivity().getSharedPreferences("user_" + MyApplication.getUsername(), Context.MODE_PRIVATE).getInt("identity", 0) == 1) {
            major.setVisibility(View.GONE);
            classNo.setVisibility(View.GONE);
            MyApplication.getDatabase().createQuery("user", "select department from teacher where id = ?", userId).subscribe(query -> {
                Cursor cursor = query.run();
                if (cursor.moveToFirst()) {
                    requireActivity().runOnUiThread(() -> department.setText(cursor.getString(0)));

                }
            });

        } else {
            major.setOnClickListener(v -> startActivity(
                    new Intent(requireActivity(), UserProfileChangeActivity.class).setAction("com.example.demo.user.change.major").putExtra("text", major.getText())));
            classNo.setOnClickListener(v -> startActivity(
                    new Intent(requireActivity(), UserProfileChangeActivity.class).setAction("com.example.demo.user.change.class_no").putExtra("text", classNo.getText())));
            MyApplication.getDatabase().createQuery("user", "select department, major, class_no from student where id = ?", userId).subscribe(query -> {
                Cursor cursor = query.run();
                if (cursor.moveToFirst()) {
                    requireActivity().runOnUiThread(() -> {
                        department.setText(cursor.getString(0));
                        major.setText(cursor.getString(1));
                        classNo.setText(cursor.getString(2));
                    });

                }
            });
        }
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}