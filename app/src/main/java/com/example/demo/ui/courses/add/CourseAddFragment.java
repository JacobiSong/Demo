package com.example.demo.ui.courses.add;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.demo.MyApplication;
import com.example.demo.R;
import com.example.demo.datagram.DatagramProto;
import com.example.demo.service.ClientHandler;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.List;

public class CourseAddFragment extends Fragment {

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            swipeRefreshLayout.setRefreshing(false);
            try {
                DatagramProto.Courses courses = DatagramProto.Courses.parseFrom(intent.getByteArrayExtra("courses"));
                list.clear();
                list.addAll(courses.getCoursesList());
            } catch (InvalidProtocolBufferException ignored) {
                list.clear();
            }
            courseAddAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private final MyBroadcastReceiver broadcastReceiver = new MyBroadcastReceiver();
    private CourseAddAdapter courseAddAdapter;
    private final List<DatagramProto.Course> list = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    public static CourseAddFragment newInstance() {
        return new CourseAddFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        courseAddAdapter = new CourseAddAdapter(getContext(), list);
        View root = inflater.inflate(R.layout.course_add_fragment, container, false);
        swipeRefreshLayout = root.findViewById(R.id.course_add_refresh);
        ListView listView = root.findViewById(R.id.course_add_list);
        listView.setAdapter(courseAddAdapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0);
            }
        });
        listView.setOnItemClickListener((parent, view, position, id) -> MyApplication.getServer().getChannel().writeAndFlush(DatagramProto.Datagram.newBuilder().setVersion(1).setDatagram(
                DatagramProto.DatagramVersion1.newBuilder().setToken(ClientHandler.getToken()).setOk(101)
                        .setType(DatagramProto.DatagramVersion1.Type.COURSE).setCourse(
                        DatagramProto.Course.newBuilder().setId(list.get(position).getId()).build()
                )
                        .setSubtype(DatagramProto.DatagramVersion1.Subtype.REQUEST).build().toByteString()
        ).build()));
        swipeRefreshLayout.setOnRefreshListener(() -> new Thread(() -> MyApplication.getServer().getChannel().writeAndFlush(DatagramProto.Datagram.newBuilder().setVersion(1).setDatagram(
                DatagramProto.DatagramVersion1.newBuilder().setType(DatagramProto.DatagramVersion1.Type.COURSE)
                        .setSubtype(DatagramProto.DatagramVersion1.Subtype.REQUEST).setOk(100)
                        .setToken(ClientHandler.getToken()).build().toByteString()
        ).build())).start());
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.demo.course");
        requireActivity().registerReceiver(broadcastReceiver, intentFilter);
        MyApplication.getServer().getChannel().writeAndFlush(DatagramProto.Datagram.newBuilder().setVersion(1).setDatagram(
                DatagramProto.DatagramVersion1.newBuilder().setType(DatagramProto.DatagramVersion1.Type.COURSE)
                        .setSubtype(DatagramProto.DatagramVersion1.Subtype.REQUEST).setOk(100)
                        .setToken(ClientHandler.getToken()).build().toByteString()
        ).build());
    }

    @Override
    public void onDestroy() {
        requireActivity().unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}