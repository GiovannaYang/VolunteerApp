package com.example.volunteerapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.volunteerapp.Activity.News.NewsActivity;
import com.example.volunteerapp.Activity.Project.ProjectActivity;
import com.example.volunteerapp.Activity.User.UserActivity;
import com.example.volunteerapp.R;
import com.example.volunteerapp.Service.UserService;

import java.util.ArrayList;

public class MediaActivity extends AppCompatActivity {
    private Button btn4,btn1,btn2,btn3;
    ListView lv = null;
    VideoView v = null;
    ArrayList<String> customContentList = null;
    ArrayAdapter<String> adapter = null;
    MediaController mediaController = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        btn1=findViewById(R.id.btn1);
        btn2=findViewById(R.id.btn2);
        btn3=findViewById(R.id.btn3);
        btn4=findViewById(R.id.btn4);

        lv = findViewById(R.id.lv);
        v = findViewById(R.id.videoView);

        // 创建自定义的数据列表
        customContentList = new ArrayList<>();
        customContentList.add("视频1");
        customContentList.add("视频2");
        // 添加更多自定义内容...

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, customContentList);
        lv.setAdapter(adapter);

        mediaController = new MediaController(this);
        v.setMediaController(mediaController);

        String videoPaths[] = {
                "android.resource://" + getPackageName() + "/" + R.raw.vid_1,
                "android.resource://" + getPackageName() + "/" + R.raw.vid_2,
        };

        Uri uri = Uri.parse(videoPaths[0]);
        v.setVideoURI(uri);

        // 开始播放视频
        v.start();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 根据选中的位置获取对应的自定义内容
                String customContent = customContentList.get(position);
                // 在这里处理自定义内容的逻辑

                // 设置要播放的视频文件路径或URI
                String videoPath = videoPaths[position];
                Uri uri = Uri.parse(videoPath);
                v.setVideoURI(uri);
                // 开始播放视频
                v.start();
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaActivity.this, ProjectActivity.class);
                startActivity(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaActivity.this, NewsActivity.class);
                startActivity(intent);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserService userService = UserService.getInstance(MediaActivity.this);
                if (!userService.isLoggedIn()) {
                    // 未登录，提示要登录
                    Toast.makeText(MediaActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MediaActivity.this, UserActivity.class);
                    startActivity(intent);
                }
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaActivity.this, MediaActivity.class);
                startActivity(intent);
            }
        });
    }
}