package com.example.demo.ui.courses.chat.menu.member;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;

public class MembersActivity extends AppCompatActivity {
    private MemberAdapter adapter;

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
        MemberViewModel memberViewModel = new MemberViewModel(getIntent().getStringExtra("id"), getIntent().getIntExtra("identity", 0));
        adapter = new MemberAdapter(this, memberViewModel.getUsers());
        memberViewModel.getUsers().observe(this, users -> adapter.notifyDataSetChanged());
        setContentView(R.layout.activity_members);
        RecyclerView memberRecyclerView = findViewById(R.id.recyclerViewInMenbersView);
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
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}