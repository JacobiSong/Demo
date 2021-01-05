package com.example.demo.ui.courses;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.demo.R;
import com.example.demo.activity.CourseAddActivity;
import com.example.demo.activity.CourseChatActivity;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

public class
CoursesFragment extends Fragment {

    private CoursesViewModel coursesViewModel;
    private CourseAdapter courseAdapter;
    private MyHandler mHandler;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;

    private static class MyHandler extends Handler {
        private final WeakReference<CoursesFragment> mFragment;

        public MyHandler(CoursesFragment fragment) {
            mFragment = new WeakReference<CoursesFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            CoursesFragment fragment = mFragment.get();
            if (fragment != null) {
                switch (msg.what) {
                    case 100:
                        fragment.getCoursesViewModel().getCourses().getValue().add(new CourseProfile("移动互联网", "移动互联网2020"));
                        fragment.getCourseAdapter().notifyDataSetChanged();
                        fragment.getSwipeRefreshLayout().setRefreshing(false);
                        break;
                }
            }
        }
    }

    public CoursesViewModel getCoursesViewModel() {
        return coursesViewModel;
    }

    public CourseAdapter getCourseAdapter() {
        return courseAdapter;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        coursesViewModel = new ViewModelProvider(this).get(CoursesViewModel.class);
        courseAdapter = new CourseAdapter(getContext(), coursesViewModel.getCourses().getValue());
        mHandler = new MyHandler(this);
        View root = inflater.inflate(R.layout.fragment_courses, container, false);
        swipeRefreshLayout = root.findViewById(R.id.course_refresh);
        ListView listView = root.findViewById(R.id.courses_list);
        listView.setAdapter(courseAdapter);
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
                Intent intent = new Intent(getActivity(), CourseChatActivity.class);
                startActivity(intent);
            }
        });
        //swipeRefreshLayout.setColorSchemeResources(new int[]{});
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
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.courses_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.course_add:
                Intent intent = new Intent(getActivity(), CourseAddActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}