package com.example.demo.ui.courses;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.demo.MyApplication;
import com.example.demo.R;
import com.example.demo.activity.CourseAddActivity;
import com.example.demo.activity.CourseChatActivity;
import com.example.demo.datagram.DatagramProto;

import java.lang.reflect.Method;

public class CoursesFragment extends Fragment {

    private CoursesViewModel coursesViewModel;
    private CoursesAdapter coursesAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        coursesViewModel = new ViewModelProvider(getActivity()).get(CoursesViewModel.class);
        coursesViewModel.getCourses().observe(getActivity(), new Observer<DatagramProto.Courses>() {
            @Override
            public void onChanged(DatagramProto.Courses courses) {
                coursesAdapter.notifyDataSetChanged();
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        coursesAdapter = new CoursesAdapter(getContext(), coursesViewModel.getCourses().getValue());
        View root = inflater.inflate(R.layout.fragment_courses, container, false);
        SwipeRefreshLayout swipeRefreshLayout = root.findViewById(R.id.course_refresh);
        ListView listView = root.findViewById(R.id.courses_list);
        listView.setAdapter(coursesAdapter);
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
                startActivity(new Intent(getActivity(), CourseChatActivity.class).putExtra("id", coursesViewModel.getCourses().getValue().getCourses(position).getId()));
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
        if (getActivity().getSharedPreferences("user_" + MyApplication.getUsername(), Context.MODE_PRIVATE).getInt("identity", 0) == 1) {
            setHasOptionsMenu(true);
        }
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
                } catch (Exception ignored) {
                }
            }
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.course_add) {
            startActivity(new Intent(getActivity(), CourseAddActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}