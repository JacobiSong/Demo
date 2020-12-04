package com.example.demo.ui.notifications.notification;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.demo.R;

public class NotificationFragment extends Fragment {

    private NotificationViewModel mViewModel;
    private TextView title;
    private TextView subtitle;
    private TextView content;

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.notification_fragment, container, false);
        title = root.findViewById(R.id.notification_activity_title);
        subtitle = root.findViewById(R.id.notification_activity_subtitle);
        content = root.findViewById(R.id.notification_activity_content);
        content.setMovementMethod(ScrollingMovementMethod.getInstance());
        title.setText("作业发布");
        subtitle.setText("移动互联网\t18:30");
        content.setText("作业内容\n作业内容\n作业内容\n作业内容\n作业内容\n作业内容\n作业内容\n作业内容");
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
    }

}