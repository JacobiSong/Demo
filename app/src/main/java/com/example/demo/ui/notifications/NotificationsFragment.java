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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.demo.R;
import com.example.demo.activity.NotificationActivity;
import com.example.demo.datagram.DatagramProto;

import java.lang.ref.WeakReference;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private NotificationAdapter notificationAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationsViewModel = new ViewModelProvider(getActivity()).get(NotificationsViewModel.class);
        notificationsViewModel.getNotifications().observe(getActivity(), new Observer<DatagramProto.Notifications>() {
            @Override
            public void onChanged(DatagramProto.Notifications notifications) {
                notificationAdapter.notifyDataSetChanged();
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationAdapter = new NotificationAdapter(getContext(), notificationsViewModel.getNotifications().getValue());
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        SwipeRefreshLayout swipeRefreshLayout = root.findViewById(R.id.notification_refresh);
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
                startActivity(new Intent(getActivity(), NotificationActivity.class)
                        .putExtra("receiver_id", notificationsViewModel.getNotifications().getValue().getNotifications(position).getSenderId())
                        .putExtra("id", notificationsViewModel.getNotifications().getValue().getNotifications(position).getId()));
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }).start();
            }
        });
        return root;
    }
}