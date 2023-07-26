package com.example.volunteerapp.Dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.volunteerapp.Entity.Project;

import java.util.ArrayList;
import java.util.List;

public class ProjectDao extends DatabaseHelper{
    public ProjectDao(Context context) {
        super(context);
    }
    public List<Project> getAllProjects() {
        List<Project> projectList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String[] columns = { "Aid", "name", "start_time", "end_time", "address", "content", "number", "telephone","picture" };
        String orderBy = "start_time DESC"; // 按开始时间升序排序
        Cursor cursor = db.query("activity", columns, null, null, null, null, orderBy);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int Aid = cursor.getInt(cursor.getColumnIndex("Aid"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String startTime = cursor.getString(cursor.getColumnIndex("start_time"));
                @SuppressLint("Range") String endTime = cursor.getString(cursor.getColumnIndex("end_time"));
                @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex("address"));
                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));
                @SuppressLint("Range") int number = cursor.getInt(cursor.getColumnIndex("number"));
                @SuppressLint("Range") String telephone = cursor.getString(cursor.getColumnIndex("telephone"));
                @SuppressLint("Range") String picture = cursor.getString(cursor.getColumnIndex("picture"));

                Project project = new Project(Aid, name, startTime, endTime, address, content, number, telephone, picture);
                projectList.add(project);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return projectList;
    }

    public void insertProject(Project project) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", project.getName());
        values.put("start_time", project.getStartTime());
        values.put("end_time", project.getEndTime());
        values.put("address", project.getAddress());
        values.put("content", project.getContent());
        values.put("number", project.getNumber());
        values.put("telephone",project.getTelephone());
        values.put("picture",project.getPicture());

        db.insert("activity", null, values);
    }
    public void updateProject(Project project) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", project.getName());
        values.put("start_time", project.getStartTime());
        values.put("end_time", project.getEndTime());
        values.put("address", project.getAddress());
        values.put("content", project.getContent());
        values.put("number", project.getNumber());
        values.put("telephone", project.getTelephone());
        values.put("picture", project.getPicture());

        String whereClause = "Aid = ?";
        String[] whereArgs = { String.valueOf(project.getAid()) };

        db.update("activity", values, whereClause, whereArgs);
    }
    public void deleteProject(Project project) {
        SQLiteDatabase db = getWritableDatabase();

        String whereClause = "Aid = ?";
        String[] whereArgs = { String.valueOf(project.getAid()) };

        db.delete("activity", whereClause, whereArgs);
    }
    public List<Project> searchProjectsByName(String projectName) {
        SQLiteDatabase db = getReadableDatabase();

        List<Project> filteredProjects = new ArrayList<>();

        String selection = "name LIKE ?";
        String[] selectionArgs = {"%" + projectName + "%"};
        String orderBy = "start_time DESC"; // 按开始时间升序排序

        Cursor cursor = db.query(
                "activity",
                null,
                selection,
                selectionArgs,
                null,
                null,
                orderBy
        );

        while (cursor.moveToNext()) {
            @SuppressLint("Range") int Aid = cursor.getInt(cursor.getColumnIndex("Aid"));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
            @SuppressLint("Range") String startTime = cursor.getString(cursor.getColumnIndex("start_time"));
            @SuppressLint("Range") String endTime = cursor.getString(cursor.getColumnIndex("end_time"));
            @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex("address"));
            @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));
            @SuppressLint("Range") int number = cursor.getInt(cursor.getColumnIndex("number"));
            @SuppressLint("Range") String telephone = cursor.getString(cursor.getColumnIndex("telephone"));
            @SuppressLint("Range") String picture = cursor.getString(cursor.getColumnIndex("picture"));
            Project project = new Project(Aid, name, startTime, endTime, address, content, number,telephone,picture);

            // 设置项目的其他属性

            filteredProjects.add(project);
        }

        cursor.close();
        db.close();

        return filteredProjects;
    }

}
