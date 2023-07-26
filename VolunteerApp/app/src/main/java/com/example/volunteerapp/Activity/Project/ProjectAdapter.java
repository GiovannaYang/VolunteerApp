package com.example.volunteerapp.Activity.Project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.volunteerapp.Activity.Project.PjContentActivity;
import com.example.volunteerapp.Entity.Project;
import com.example.volunteerapp.R;
import com.example.volunteerapp.Service.UserService;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class ProjectAdapter extends BaseAdapter {
    private Context context;
    private List<Project> projects;

    public ProjectAdapter(Context context) {
        this.context = context;
        this.projects = new ArrayList<>();
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    @Override
    public int getCount() {
        return projects.size();
    }

    @Override
    public Object getItem(int position) {
        return projects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_project, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.image = convertView.findViewById(R.id.image);
            viewHolder.tvName = convertView.findViewById(R.id.tv_project_name);
            viewHolder.tvTime = convertView.findViewById(R.id.tv_project_time);
            viewHolder.tvAddress = convertView.findViewById(R.id.tv_project_address);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Project project = projects.get(position);
        viewHolder.tvName.setText(project.getName());

        String startTime = project.getStartTime();
        String endTime = project.getEndTime();
        viewHolder.tvTime.setText(startTime + " - " + endTime);

        viewHolder.tvAddress.setText(project.getAddress());

        String imagePath = project.getPicture();

        // 使用Glide加载图片，并进行裁剪保持1:1比例
        Glide.with(context)
                .load(imagePath)
                .transition(DrawableTransitionOptions.withCrossFade()) // 图片渐变效果
                .transform(new CenterCrop(), new ImageCropTransformation()) // 使用CenterCrop和自定义裁剪Transformation
                .into(viewHolder.image);

        // 设置ImageView的宽高为相等的值，实现1:1比例的显示
        int imageSize = calculateImageSize(); // 计算图片的宽高
        ViewGroup.LayoutParams layoutParams = viewHolder.image.getLayoutParams();
        layoutParams.width = imageSize;
        layoutParams.height = imageSize;
        viewHolder.image.setLayoutParams(layoutParams);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取点击的项目数据
                Project project = projects.get(position);
                UserService userService = UserService.getInstance(context);
                if (!userService.isLoggedIn()) {
                    // 未登录，提示要登录
                    Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 启动项目详情页，并传递项目数据
                Intent intent = new Intent(context, PjContentActivity.class);
                intent.putExtra("project", project);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    // 计算图片的宽高，使其保持1:1比例
    private int calculateImageSize() {
        // 这里可以根据需要调整图片的宽度和高度
        // 这里简单起见，设置图片宽高为屏幕宽度的1/3
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        return screenWidth / 4;
    }

    // 自定义裁剪Transformation，保持图片1:1比例
    private class ImageCropTransformation extends BitmapTransformation {
        @Override
        protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
            int size = Math.min(toTransform.getWidth(), toTransform.getHeight());
            int x = (toTransform.getWidth() - size) / 2;
            int y = (toTransform.getHeight() - size) / 2;
            Bitmap croppedBitmap = Bitmap.createBitmap(toTransform, x, y, size, size);
            return croppedBitmap;
        }

        @Override
        public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
            // Leave empty
        }
    }

    private static class ViewHolder {
        ImageView image;
        TextView tvName;
        TextView tvTime;
        TextView tvAddress;
    }
}
