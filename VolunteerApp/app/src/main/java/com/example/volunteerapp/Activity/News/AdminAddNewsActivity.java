package com.example.volunteerapp.Activity.News;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.volunteerapp.R;
import com.example.volunteerapp.Service.NewsService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class AdminAddNewsActivity extends AppCompatActivity {

    private EditText etTitle;
    private EditText etContent;
    private Button btnAdd;

    private NewsService newsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_news);

        etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);
        btnAdd = findViewById(R.id.btn_add);

        newsService = NewsService.getInstance(this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString().trim();
                String content = etContent.getText().toString().trim();
                String currentTime = getCurrentTime();

                if (NewsValidator.validateNewsInput(AdminAddNewsActivity.this,title, content)) {
                    // Add the news using the NewsService
                    boolean success = newsService.addNews(title, content, currentTime);

                    if (success) {
                        Toast.makeText(AdminAddNewsActivity.this, "添加新闻成功", Toast.LENGTH_SHORT).show();
                        finish(); // Close the activity
                    } else {
                        Toast.makeText(AdminAddNewsActivity.this, "添加新闻失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private String getCurrentTime() {
        // 设置时区为东八区
        TimeZone timeZone = TimeZone.getTimeZone("GMT+8:00");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(timeZone);

        // 获取当前时间
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }
}
