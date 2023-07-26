package com.example.volunteerapp.Dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.volunteerapp.Entity.News;
import com.example.volunteerapp.Entity.Project;

import java.util.ArrayList;
import java.util.List;

public class NewsDao {
    private DatabaseHelper dbHelper;

    public NewsDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public List<News> getAllNews() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<News> newsList = new ArrayList<>();

        String orderBy = "time DESC"; // 按开始时间升序排序
        Cursor cursor = db.query("news", null, null, null, null, null, orderBy);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndexOrThrow("Nid");
                int titleIndex = cursor.getColumnIndexOrThrow("name");
                int contentIndex = cursor.getColumnIndexOrThrow("content");
                int timeIndex = cursor.getColumnIndexOrThrow("time");

                int id = cursor.getInt(idIndex);
                String title = cursor.getString(titleIndex);
                String content = cursor.getString(contentIndex);
                String time = cursor.getString(timeIndex);

                News news = new News(id, title, time, content);
                newsList.add(news);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();

        return newsList;
    }

    public boolean addNews(News news) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", news.getName());
        values.put("content", news.getContent());
        values.put("time", news.getTime());

        long result = db.insert("news", null, values);
        db.close();

        return result != -1;
    }

    public News getNewsById(int Nid) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        News news = null;

        Cursor cursor = db.query("news", null, "Nid=?", new String[]{String.valueOf(Nid)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndexOrThrow("Nid");
            int titleIndex = cursor.getColumnIndexOrThrow("name");
            int contentIndex = cursor.getColumnIndexOrThrow("content");
            int timeIndex = cursor.getColumnIndexOrThrow("time");

            int id = cursor.getInt(idIndex);
            String title = cursor.getString(titleIndex);
            String content = cursor.getString(contentIndex);
            String time = cursor.getString(timeIndex);

            news = new News(id, title, time, content);
            cursor.close();
        }

        db.close();

        return news;
    }
    public List<News> searchNewsByName(String newsName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<News> filteredNews = new ArrayList<>();

        String selection = "name LIKE ?";
        String[] selectionArgs = {"%" + newsName + "%"};
        String orderBy = "time DESC"; // 按开始时间升序排序

        Cursor cursor = db.query(
                "news",
                null,
                selection,
                selectionArgs,
                null,
                null,
                orderBy
        );

        while (cursor.moveToNext()) {
            @SuppressLint("Range") int Nid = cursor.getInt(cursor.getColumnIndex("Nid"));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
            @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex("time"));
            @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));

            News news = new News(Nid, name, time, content);

            // 设置项目的其他属性

            filteredNews.add(news);
        }

        cursor.close();
        db.close();

        return filteredNews;
    }
    public void deleteNews(News news) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String whereClause = "Nid = ?";
        String[] whereArgs = { String.valueOf(news.getNid()) };

        db.delete("news", whereClause, whereArgs);
    }

    public void updateNews(News news) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", news.getName());
        values.put("time", news.getTime());
        values.put("content", news.getContent());

        String whereClause = "Nid = ?";
        String[] whereArgs = { String.valueOf(news.getNid()) };

        db.update("news", values, whereClause, whereArgs);
    }
}