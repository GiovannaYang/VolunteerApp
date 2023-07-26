package com.example.volunteerapp.Activity.News;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.volunteerapp.Entity.News;
import com.example.volunteerapp.R;
import com.example.volunteerapp.Service.NewsService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class NewsEditActivity extends AppCompatActivity {

    private EditText etTitle;
    private EditText etContent;
    private Button btnAdd;

    private NewsService newsService;
    private News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_edit);

        etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);
        btnAdd = findViewById(R.id.btn_add);

        newsService = NewsService.getInstance(this);

        // Retrieve the news object from the intent
        Intent intent = getIntent();
        int Nid = intent.getIntExtra("news", 0);

        // Get the News object from the Nid
        NewsService newsService = NewsService.getInstance(this);
        news = newsService.getNewsById(Nid);

        etTitle.setText(news.getName());
        etContent.setText(news.getContent());

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString().trim();
                String content = etContent.getText().toString().trim();
                String currentTime = getCurrentTime();
                int Nid=news.getNid();

                if (NewsValidator.validateNewsInput(NewsEditActivity.this,title, content)) {
                    // Add the news using the NewsService
                    newsService.updateNews(Nid,title, content, currentTime);
                    Toast.makeText(NewsEditActivity.this, "修改新闻成功", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity
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
