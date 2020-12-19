package com.example.demo.ui.courses.add;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.demo.R;
import com.example.demo.activity.NotificationActivity;
import com.example.demo.ui.notifications.NotificationAdapter;
import com.example.demo.ui.notifications.NotificationProfile;
import com.example.demo.ui.notifications.NotificationsFragment;
import com.example.demo.ui.notifications.NotificationsViewModel;

import java.lang.ref.WeakReference;

public class CourseAddFragment extends Fragment {

    private CourseAddViewModel mViewModel;
    private CourseAddAdapter courseAddAdapter;
    private CourseAddFragment.MyHandler mHandler;
    private SwipeRefreshLayout swipeRefreshLayout;

    public CourseAddViewModel getmViewModel() {
        return mViewModel;
    }

    public CourseAddAdapter getCourseAddAdapter() {
        return courseAddAdapter;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    private static class MyHandler extends Handler {
        private final WeakReference<CourseAddFragment> mFragment;

        public MyHandler(CourseAddFragment fragment) {
            mFragment = new WeakReference<CourseAddFragment>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {
            CourseAddFragment fragment = mFragment.get();
            if (fragment != null) {
                switch (msg.what) {
                    case 100:
                        fragment.getmViewModel().getCourses().getValue().add(new CourseAddProfile("移动互联网", "周二7-8节，周四7-8节"));
                        fragment.getCourseAddAdapter().notifyDataSetChanged();
                        fragment.getSwipeRefreshLayout().setRefreshing(false);
                        break;
                }
            }
        }
    }
    public static CourseAddFragment newInstance() {
        return new CourseAddFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel =
                new ViewModelProvider(this).get(CourseAddViewModel.class);
        courseAddAdapter = new CourseAddAdapter(getContext(), mViewModel.getCourses().getValue());
        View root = inflater.inflate(R.layout.course_add_fragment, container, false);
        mHandler = new CourseAddFragment.MyHandler(this);
        swipeRefreshLayout = root.findViewById(R.id.course_add_refresh);
        ListView listView = root.findViewById(R.id.course_add_list);
        listView.setAdapter(courseAddAdapter);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CourseAddViewModel.class);
    }

}