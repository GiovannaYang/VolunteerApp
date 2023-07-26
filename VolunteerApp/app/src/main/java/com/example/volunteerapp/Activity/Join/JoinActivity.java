package com.example.volunteerapp.Activity.Join;


import android.os.Bundle;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.volunteerapp.Entity.Project;
import com.example.volunteerapp.R;
import com.example.volunteerapp.Service.JoinService;
import com.example.volunteerapp.Service.UserService;


import java.util.List;

public class JoinActivity extends AppCompatActivity {

    private ListView joinList;
    private JoinAdapter joinAdapter;
    private JoinService joinService;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        joinList = findViewById(R.id.lv);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        joinService = JoinService.getInstance(this);

        // 初始化项目列表适配器
        joinAdapter = new JoinAdapter(this);
        joinList.setAdapter(joinAdapter);

        // 加载项目列表数据
        refreshJoinList();
        setListener();

    }

    private void setListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 触发刷新项目列表的操作
                refreshJoinList();
                swipeRefreshLayout.setRefreshing(false); // 停止刷新
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        // 触发刷新项目列表的操作
        refreshJoinList();
    }
    private void refreshJoinList() {
        UserService userService = UserService.getInstance(this);
        List<Project> joins = joinService.getProjectList(userService.getUserId());
        joinAdapter.setJoins(joins);
        joinAdapter.notifyDataSetChanged();
    }
}