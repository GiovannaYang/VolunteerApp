package com.example.volunteerapp.Activity.Project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.volunteerapp.Activity.MediaActivity;
import com.example.volunteerapp.Activity.News.NewsActivity;
import com.example.volunteerapp.Activity.User.UserActivity;
import com.example.volunteerapp.Entity.Project;
import com.example.volunteerapp.R;
import com.example.volunteerapp.Service.ProjectService;
import com.example.volunteerapp.Service.UserService;
import com.example.volunteerapp.utils.GlobalData;

import java.util.List;

public class ProjectActivity extends AppCompatActivity {

    private ListView projectList;
    private Button btn,btn1,btn2,btn3,btn4;
    private ProjectAdapter projectAdapter;
    private ProjectService projectService;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText etSearch;
    private ImageButton btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        projectList = findViewById(R.id.lv);
        btn=findViewById(R.id.btn);
        btn1=findViewById(R.id.btn1);
        btn2=findViewById(R.id.btn2);
        btn3=findViewById(R.id.btn3);
        btn4=findViewById(R.id.btn4);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        etSearch = findViewById(R.id.et_search);
        btnSearch = findViewById(R.id.btn_search);

        projectService = ProjectService.getInstance(this);

        // 初始化项目列表适配器
        projectAdapter = new ProjectAdapter(this);
        projectList.setAdapter(projectAdapter);

        // 加载项目列表数据
        refreshProjectList();
        setListener();

    }

    private void setListener() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserService userService = UserService.getInstance(ProjectActivity.this);

                if (!userService.isLoggedIn()) {
                    // 未登录，提示要登录
                    Toast.makeText(ProjectActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                } else {
                    int userRole = userService.getUserRole();

                    if (userRole == GlobalData.ROLE_ADMIN) {
                        // 是管理员，跳转到管理员添加项目页面
                        Intent intent = new Intent(ProjectActivity.this, AdminAddPjActivity.class);
                        startActivity(intent);
                    } else {
                        // 不是管理员，提示权限不足
                        Toast.makeText(ProjectActivity.this, "权限不足", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 触发刷新项目列表的操作
                refreshProjectList();
                swipeRefreshLayout.setRefreshing(false); // 停止刷新
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectActivity.this, ProjectActivity.class);
                startActivity(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectActivity.this, NewsActivity.class);
                startActivity(intent);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserService userService = UserService.getInstance(ProjectActivity.this);
                if (!userService.isLoggedIn()) {
                    // 未登录，提示要登录
                    Toast.makeText(ProjectActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(ProjectActivity.this, UserActivity.class);
                    startActivity(intent);
                }
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectActivity.this, MediaActivity.class);
                startActivity(intent);
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String projectName = etSearch.getText().toString().trim();
                searchProjects(projectName);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        // 触发刷新项目列表的操作
        refreshProjectList();
    }
    private void refreshProjectList() {
        List<Project> projects = projectService.getProjectList();
        projectAdapter.setProjects(projects);
        projectAdapter.notifyDataSetChanged();
    }
    private void searchProjects(String projectName) {
        List<Project> filteredProjects = projectService.searchProjectsByName(projectName);
        projectAdapter.setProjects(filteredProjects);
        projectAdapter.notifyDataSetChanged();
    }

}
