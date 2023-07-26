package com.example.volunteerapp.Activity.News;

import android.content.Context;
import android.widget.Toast;

public class NewsValidator {
    public static boolean validateNewsInput(Context context, String title, String content) {
        if (title.isEmpty()) {
            Toast.makeText(context, "请输入标题", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (title.length() > 50) {
            Toast.makeText(context, "标题不得超过50字", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (content.isEmpty()) {
            Toast.makeText(context, "请输入内容", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (content.length() > 2000) {
            Toast.makeText(context, "内容不得超过2000字", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}

