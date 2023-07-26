package com.example.volunteerapp.Activity.News;

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
import com.example.volunteerapp.Activity.Project.ProjectActivity;
import com.example.volunteerapp.Activity.User.UserActivity;
import com.example.volunteerapp.Entity.News;
import com.example.volunteerapp.Entity.Project;
import com.example.volunteerapp.R;
import com.example.volunteerapp.Service.NewsService;
import com.example.volunteerapp.Service.UserService;
import com.example.volunteerapp.utils.GlobalData;
import java.util.List;

public class NewsActivity extends AppCompatActivity {

    private Button btn, btn1, btn2, btn3,btn4;
    private ListView newsList;
    private NewsAdapter newsAdapter;
    private NewsService newsService;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText etSearch;
    private ImageButton btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        btn = findViewById(R.id.btn);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        newsList = findViewById(R.id.lv);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        etSearch = findViewById(R.id.et_search);
        btnSearch = findViewById(R.id.btn_search);

        newsAdapter = new NewsAdapter(this);
        newsList.setAdapter(newsAdapter);

        newsService = NewsService.getInstance(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserService userService = UserService.getInstance(NewsActivity.this);
                if (userService.getUserRole() == GlobalData.ROLE_ADMIN) {
                    // User is an admin, navigate to Add News activity
                    Intent intent = new Intent(NewsActivity.this, AdminAddNewsActivity.class);
                    startActivity(intent);
                } else {
                    // User is not an admin, display insufficient permissions message
                    Toast.makeText(NewsActivity.this, "权限不足", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewsActivity.this, ProjectActivity.class);
                startActivity(intent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewsActivity.this, NewsActivity.class);
                startActivity(intent);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserService userService = UserService.getInstance(NewsActivity.this);
                if (!userService.isLoggedIn()) {
                    // User is not logged in, display login required message
                    Toast.makeText(NewsActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(NewsActivity.this, UserActivity.class);
                    startActivity(intent);
                }
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewsActivity.this, MediaActivity.class);
                startActivity(intent);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 触发刷新项目列表的操作
                refreshNewsList();
                swipeRefreshLayout.setRefreshing(false); // 停止刷新
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newsName = etSearch.getText().toString().trim();
                searchNews(newsName);
            }
        });

        // Load news list data
        loadNewsList();
    }

    private void loadNewsList() {
        List<News> news = newsService.getAllNews();
        newsAdapter.setNewsList(news);
        newsAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onResume() {
        super.onResume();
        // 触发刷新项目列表的操作
        refreshNewsList();
    }
    private void refreshNewsList() {
        List<News> news = newsService.getAllNews();
        newsAdapter.setNewsList(news);
        newsAdapter.notifyDataSetChanged();
    }
    private void searchNews(String newsName) {
        List<News> filteredNews = newsService.searchNewsByName(newsName);
        newsAdapter.setNewsList(filteredNews);
        newsAdapter.notifyDataSetChanged();
    }
}
