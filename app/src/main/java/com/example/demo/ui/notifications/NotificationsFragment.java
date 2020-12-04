package com.example.demo.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.demo.R;
import com.example.demo.activity.CourseChatActivity;
import com.example.demo.activity.NotificationActivity;
import com.example.demo.ui.courses.CourseAdapter;
import com.example.demo.ui.courses.CourseProfile;
import com.example.demo.ui.courses.CoursesFragment;

import java.lang.ref.WeakReference;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private NotificationAdapter notificationAdapter;
    private MyHandler mHandler;
    private SwipeRefreshLayout swipeRefreshLayout;

    public NotificationsViewModel getNotificationsViewModel() {
        return notificationsViewModel;
    }

    public NotificationAdapter getNotificationAdapter() {
        return notificationAdapter;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    private static class MyHandler extends Handler {
        private final WeakReference<NotificationsFragment> mFragment;

        public MyHandler(NotificationsFragment fragment) {
            mFragment = new WeakReference<NotificationsFragment>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {
            NotificationsFragment fragment = mFragment.get();
            if (fragment != null) {
                switch (msg.what) {
                    case 100:
                        fragment.getNotificationsViewModel().getNotifications().getValue().add(new NotificationProfile("移动互联网", "作业发布", "18:00"));
                        fragment.getNotificationAdapter().notifyDataSetChanged();
                        fragment.getSwipeRefreshLayout().setRefreshing(false);
                        break;
                }
            }
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        notificationAdapter = new NotificationAdapter(getContext(), notificationsViewModel.getNotifications().getValue());
        mHandler = new NotificationsFragment.MyHandler(this);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        swipeRefreshLayout = root.findViewById(R.id.notification_refresh);
        ListView listView = root.findViewById(R.id.notifications_list);
        listView.setAdapter(notificationAdapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0)
                    swipeRefreshLayout.setEnabled(true);
                else
                    swipeRefreshLayout.setEnabled(false);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                startActivity(intent);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = 100;
                        msg.obj = "";
                        mHandler.sendMessage(msg);
                    }
                }).start();
            }
        });
        return root;
    }
}