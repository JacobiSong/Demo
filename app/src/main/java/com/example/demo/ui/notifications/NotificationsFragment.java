package com.example.demo.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.demo.R;
import com.example.demo.ui.notifications.notification.NotificationActivity;

import java.util.Objects;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private NotificationAdapter notificationAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationsViewModel = new ViewModelProvider(requireActivity()).get(NotificationsViewModel.class);
        notificationAdapter = new NotificationAdapter(getContext(), notificationsViewModel.getNotifications());
        notificationsViewModel.getNotifications().observe(requireActivity(), notifications -> notificationAdapter.notifyDataSetChanged());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
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
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0);
            }
        });
        listView.setOnItemClickListener((parent, view, position, id) -> startActivity(new Intent(getActivity(), NotificationActivity.class)
                .putExtra("receiver_id", Objects.requireNonNull(notificationsViewModel.getNotifications().getValue()).get(position).getReceiverId())
                .putExtra("id", notificationsViewModel.getNotifications().getValue().get(position).getId())));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            notificationAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        });
        return root;
    }
}