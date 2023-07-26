package com.example.volunteerapp.Activity.News;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.volunteerapp.Activity.Project.AdminEditPjActivity;
import com.example.volunteerapp.Activity.Project.PjContentActivity;
import com.example.volunteerapp.Entity.News;
import com.example.volunteerapp.Entity.Project;
import com.example.volunteerapp.R;
import com.example.volunteerapp.Service.NewsService;
import com.example.volunteerapp.Service.ProjectService;
import com.example.volunteerapp.Service.UserService;
import com.example.volunteerapp.utils.GlobalData;

public class NewsContentActivity extends AppCompatActivity {

    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvTime;
    private Button btnEdit,btnDelete;
    private News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);

        // Retrieve the news object from the intent
        Intent intent = getIntent();
        int Nid = intent.getIntExtra("news", 0);

        // Get the News object from the Nid
        NewsService newsService = NewsService.getInstance(this);
        news = newsService.getNewsById(Nid);

        // Initialize the views
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        tvTime = findViewById(R.id.tv_time);
        btnEdit = findViewById(R.id.btn_edit);
        btnDelete = findViewById(R.id.btn_delete);

        if (news != null) {
            tvTitle.setText(news.getName());
            tvContent.setText(news.getContent());
            tvTime.setText(news.getTime());
        }
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserService userService = UserService.getInstance(NewsContentActivity.this);

                if (!userService.isLoggedIn()) {
                    // 未登录，提示要登录
                    Toast.makeText(NewsContentActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                } else {
                    int userRole = userService.getUserRole();

                    if (userRole == GlobalData.ROLE_ADMIN) {
                        // 是管理员，跳转到管理员编辑项目页面
                        Intent intent = new Intent(NewsContentActivity.this, NewsEditActivity.class);
                        intent.putExtra("news", news.getNid());
                        startActivity(intent);
                    } else {
                        // 不是管理员，提示权限不足
                        Toast.makeText(NewsContentActivity.this, "权限不足", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserService userService = UserService.getInstance(NewsContentActivity.this);

                if (!userService.isLoggedIn()) {
                    // 未登录，提示要登录
                    Toast.makeText(NewsContentActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                } else {
                    int userRole = userService.getUserRole();

                    if (userRole == GlobalData.ROLE_ADMIN) {
                        // 是管理员，执行删除操作
                        showDeleteConfirmationDialog();
                    } else {
                        // 不是管理员，提示权限不足
                        Toast.makeText(NewsContentActivity.this, "权限不足", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确认删除");
        builder.setMessage("确定要删除该项目吗？");
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteNews();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }
    private void deleteNews() {
        // Call the deleteProject method in ProjectService to delete the project
        NewsService.getInstance(NewsContentActivity.this).deleteNews(news);
        // Display a success message and return to the previous activity
        Toast.makeText(NewsContentActivity.this, "项目删除成功", Toast.LENGTH_SHORT).show();
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();

        // 更新项目对象
        Intent intent = getIntent();
        int Nid = intent.getIntExtra("news", 0);

        // Get the News object from the Nid
        NewsService newsService = NewsService.getInstance(this);
        news = newsService.getNewsById(Nid);

        // 更新视图显示的内容
        tvTitle.setText(news.getName());
        tvTime.setText(news.getTime());
        tvContent.setText(news.getContent());
    }
}
