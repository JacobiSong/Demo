package com.example.demo.ui.courses.chat.menu.member;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.datagram.DatagramProto;
import com.example.demo.ui.courses.chat.UserInfoActivity;

import java.util.List;

public class MembersActivity extends AppCompatActivity {

    private MemberViewModel memberViewModel;
    private MemberAdapter adapter;
    private RecyclerView memberRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (getIntent().getIntExtra("identity", 0)) {
            case 0:
                setTitle("群学生");
                break;
            case 1:
                setTitle("群教师");
                break;
            default:
                break;
        }
        memberViewModel = new MemberViewModel(getIntent().getStringExtra("id"), getIntent().getIntExtra("identity", 0));
        adapter = new MemberAdapter(this, memberViewModel.getUsers());
        memberViewModel.getUsers().observe(this, new Observer<List<DatagramProto.User>>() {
            @Override
            public void onChanged(List<DatagramProto.User> users) {
                adapter.notifyDataSetChanged();
            }
        });
        setContentView(R.layout.activity_members);
        memberRecyclerView = findViewById(R.id.recyclerViewInMenbersView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        memberRecyclerView.setLayoutManager(linearLayoutManager);
        memberRecyclerView.setAdapter(adapter);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}