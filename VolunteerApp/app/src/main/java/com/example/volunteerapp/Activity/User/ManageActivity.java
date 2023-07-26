package com.example.volunteerapp.Activity.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.volunteerapp.Entity.Project;
import com.example.volunteerapp.R;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.volunteerapp.Dao.UserDao;
import com.example.volunteerapp.Entity.User;
import com.example.volunteerapp.R;
import com.example.volunteerapp.Service.UserService;
import com.example.volunteerapp.utils.GlobalData;

import java.util.ArrayList;
import java.util.List;

public class ManageActivity extends AppCompatActivity {

    private ListView userList;
    private UserAdapter userAdapter;
    private UserService userService;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button btnAdd;
    private EditText etSearch;
    private ImageButton btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        userList = findViewById(R.id.user_list);
        userService = UserService.getInstance(this);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        btnAdd = findViewById(R.id.btn);
        etSearch = findViewById(R.id.et_search);
        btnSearch = findViewById(R.id.btn_search);

        // 初始化用户列表适配器
        userAdapter = new UserAdapter(this);
        userList.setAdapter(userAdapter);

        // 加载用户列表数据
        refreshUserList();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 触发刷新项目列表的操作
                refreshUserList();
                swipeRefreshLayout.setRefreshing(false); // 停止刷新
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 执行页面跳转到添加管理员页面
                Intent intent = new Intent(ManageActivity.this, AddAdminActivity.class);
                startActivity(intent);
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String projectName = etSearch.getText().toString().trim();
                searchUsers(projectName);
            }
        });
    }

    private void refreshUserList() {
        List<User> users = userService.getAllUsers();
        userAdapter.setUsers(users);
        userAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshUserList();
    }
    private void searchUsers(String Pid) {
        List<User> filteredUsers = userService.searchUsersByName(Pid);
        userAdapter.setUsers(filteredUsers);
        userAdapter.notifyDataSetChanged();
    }
}
