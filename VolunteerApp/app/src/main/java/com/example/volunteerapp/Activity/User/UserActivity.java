package com.example.volunteerapp.Activity.User;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.volunteerapp.Activity.Join.JoinActivity;
import com.example.volunteerapp.Activity.MediaActivity;
import com.example.volunteerapp.Activity.News.NewsActivity;
import com.example.volunteerapp.Activity.Project.PjContentActivity;
import com.example.volunteerapp.Activity.Project.ProjectActivity;
import com.example.volunteerapp.MainActivity;
import com.example.volunteerapp.R;
import com.example.volunteerapp.Service.JoinService;
import com.example.volunteerapp.Service.UserService;
import com.example.volunteerapp.utils.GlobalData;

public class UserActivity extends AppCompatActivity {

    private TextView tvName;
    private TextView tvPid;
    private Button btn1, btn2, btn3, btn4;
    private TextView btnJoin, btnManage,btnExit, btnDelete, btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // 初始化视图
        tvName = findViewById(R.id.tv_name);
        tvPid = findViewById(R.id.tv_pid);
        btnJoin = findViewById(R.id.btn_join);
        btnManage = findViewById(R.id.btn_manage);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btnExit = findViewById(R.id.btn_exit);
        btnDelete = findViewById(R.id.btn_delete);
        btnEdit = findViewById(R.id.btn_edit);

        // 获取当前用户的姓名和Pid
        UserService userService = UserService.getInstance(this);
        String name = userService.getName();
        String pid = userService.getUserId();

        // 显示当前用户的姓名和Pid
        tvName.setText("Hi, "+name+"!");
        tvPid.setText("Tel: " + pid);

        // 设置管理人员按钮点击事件
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 执行页面跳转到我的项目页面
                Intent intent = new Intent(UserActivity.this, JoinActivity.class);
                startActivity(intent);
            }
        });
        btnManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUserAdmin()) {
                    // 执行页面跳转到管理人员页面
                    Intent intent = new Intent(UserActivity.this, ManageActivity.class);
                    startActivity(intent);
                } else {
                    // 提示权限不足
                    Toast.makeText(UserActivity.this, "权限不足，无法访问管理人员页面", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this, ProjectActivity.class);
                startActivity(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this, NewsActivity.class);
                startActivity(intent);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserService userService = UserService.getInstance(UserActivity.this);
                if (!userService.isLoggedIn()) {
                    // 未登录，提示要登录
                    Toast.makeText(UserActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(UserActivity.this, UserActivity.class);
                    startActivity(intent);
                }
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this, MediaActivity.class);
                startActivity(intent);
            }
        });

        // 设置退出按钮点击事件
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExitConfirmationDialog();
            }
        });

        // 设置删除按钮点击事件
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isUserAdmin()) {
                    showDeleteConfirmationDialog();
                }else{
                    Toast.makeText(UserActivity.this, "管理员不能自行删除账号", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });

    }

    // 显示退出确认对话框
    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确认退出");
        builder.setMessage("确定要退出登录吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 执行退出操作
                logout();
            }
        });
        builder.setNegativeButton("取消", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // 执行退出操作
    private void logout() {
        UserService userService = UserService.getInstance(this);
        userService.logout();

        // 跳转到登录页面
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // 结束当前页面
    }

    // 显示删除确认对话框
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确认删除账号");
        builder.setMessage("确定要删除账号吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 执行删除账号操作
                deleteUser();
            }
        });
        builder.setNegativeButton("取消", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteUser() {
        UserService userService = UserService.getInstance(this);
        JoinService.deleteJoinByPid(userService.getUserId());
        userService.deleteUser();

        // 跳转到登录页面
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // 结束当前页面
    }
    // 判断当前用户是否为管理员
    private boolean isUserAdmin() {
        UserService userService = UserService.getInstance(this);
        int currentUserRole = userService.getUserRole();
        return currentUserRole == GlobalData.ROLE_ADMIN;
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}
