package com.example.volunteerapp.Dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.volunteerapp.Entity.Join;
import com.example.volunteerapp.Entity.Project;

import java.util.ArrayList;
import java.util.List;

public class JoinDao extends DatabaseHelper {
    public JoinDao(Context context) {
        super(context);
    }
    public List<Project> getJoinedProjects(String userId) {
        List<Project> projectList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String[] columns = { "activity.Aid", "activity.name", "activity.start_time", "activity.end_time", "activity.address", "activity.content","activity.number","activity.telephone","activity.picture" };
        String selection = "user.Pid = ? AND activity.Aid = participate.Aid AND user.Pid = participate.Pid";
        String[] selectionArgs = { userId };

        String joinTables = "activity JOIN participate ON activity.Aid = participate.Aid JOIN user ON user.Pid = participate.Pid";

        Cursor cursor = db.query(joinTables, columns, selection, selectionArgs, null, null, null);

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

                Project project = new Project(Aid, name, startTime, endTime, address, content, number,telephone,picture);
                projectList.add(project);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return projectList;
    }
    public boolean addJoin(Join join) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Aid", join.getAid());
        values.put("Pid", join.getPid());

        long rowId = db.insert("participate", null, values);
        db.close();

        return rowId != -1;
    }

    public boolean removeJoin(int aid, String pid) {
        SQLiteDatabase db = getWritableDatabase();

        int rowsAffected = db.delete("participate", "Aid = ? AND Pid = ?", new String[]{String.valueOf(aid), pid});
        db.close();

        return rowsAffected > 0;
    }

    public boolean deleteJoinByAid(int aid) {
        SQLiteDatabase db = getWritableDatabase();

        int rowsAffected = db.delete("participate", "Aid = ?", new String[]{String.valueOf(aid)});

        db.close();

        return rowsAffected > 0;
    }


    public boolean deleteJoinByPid(String pid) {
        SQLiteDatabase db = getWritableDatabase();

        int rowsAffected = db.delete("participate", "Pid = ?", new String[]{pid});

        db.close();

        return rowsAffected > 0;
    }


    public boolean isUserJoinedActivity(String pid, int aid) {
        SQLiteDatabase db = getReadableDatabase();

        String selection = "Pid = ? AND Aid = ?";
        String[] selectionArgs = {pid, String.valueOf(aid)};

        Cursor cursor = db.query(
                "participate",
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean isJoined = cursor != null && cursor.getCount() > 0;

        cursor.close();
        db.close();

        return isJoined;
    }

    public int getJoinCount(int aid) {
        SQLiteDatabase db = getReadableDatabase();
        int count = 0;

        String query = "SELECT COUNT(*) FROM participate WHERE Aid = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(aid)});

        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }

        db.close();

        return count;
    }

}
