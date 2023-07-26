package com.example.volunteerapp.Activity.News;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.volunteerapp.Activity.Project.ProjectActivity;
import com.example.volunteerapp.Entity.News;
import com.example.volunteerapp.R;
import com.example.volunteerapp.Service.UserService;

import java.util.ArrayList;
import java.util.List;
public class NewsAdapter extends BaseAdapter {

    private Context context;
    private List<News> newsList;

    public NewsAdapter(Context context) {
        this.context = context;
        this.newsList = new ArrayList<>();
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTitle = convertView.findViewById(R.id.tv_title);
            viewHolder.tvContent = convertView.findViewById(R.id.tv_content);
            viewHolder.tvTime = convertView.findViewById(R.id.tv_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        News news = newsList.get(position);

        viewHolder.tvTitle.setText(news.getName());
        viewHolder.tvContent.setText(news.getContent());
        viewHolder.tvTime.setText(news.getTime());


        final int itemPosition = position;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取点击的项目数据
                News news = newsList.get(itemPosition);
                UserService userService = UserService.getInstance(context);
                if (!userService.isLoggedIn()) {
                    // 未登录，提示要登录
                    Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 启动项目详情页，并传递项目数据
                Intent intent = new Intent(context, NewsContentActivity.class);
                intent.putExtra("news", news.getNid());
                context.startActivity(intent);
            }
        });


        return convertView;
    }


    private static class ViewHolder {
        TextView tvTitle;
        TextView tvContent;
        TextView tvTime;
    }
}
