package com.example.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.ui.coursemenber.MemberAdapter;
import com.example.demo.ui.coursemenber.MemberViewModel;

public class MembersActivity extends AppCompatActivity {

    private MemberViewModel memberViewModel;
    private MemberAdapter adapter;
    private RecyclerView memberRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        memberViewModel = new ViewModelProvider(this).get(MemberViewModel.class);
        initMembers();
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

    public void toUserInfo(View view) {
        Intent intent = new Intent(this, UserInfoActivity.class);
        startActivity(intent);
    }

    private void initMembers() {
        memberRecyclerView = findViewById(R.id.recyclerViewInMenbersView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        memberRecyclerView.setLayoutManager(linearLayoutManager);
        this.adapter = new MemberAdapter(this.memberViewModel.getStudentList());
        memberRecyclerView.setAdapter(this.adapter);
    }
}