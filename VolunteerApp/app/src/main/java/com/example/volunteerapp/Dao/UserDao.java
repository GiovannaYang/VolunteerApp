package com.example.volunteerapp.Dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.volunteerapp.Entity.Project;
import com.example.volunteerapp.Entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private static DatabaseHelper dbHelper;

    public UserDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public static User getUserByPid(String pid) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = "Pid = ?";
        String[] selectionArgs = {pid};
        String[] columns = {"Pid", "password", "name", "role"};

        Cursor cursor = db.query("user", columns, selection, selectionArgs, null, null, null);

        User user = null;

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndexOrThrow("Pid");
            int pwdIndex = cursor.getColumnIndexOrThrow("password");
            int nameIndex = cursor.getColumnIndexOrThrow("name");
            int roleIndex = cursor.getColumnIndexOrThrow("role");

            String id = cursor.getString(idIndex);
            String pwd = cursor.getString(pwdIndex);
            String name = cursor.getString(nameIndex);
            int role = cursor.getInt(roleIndex);
            user = new User(id, pwd, name, role);
            cursor.close();
        }

        db.close();

        return user;
    }

    public boolean isUserExistsByPid(String pid) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = "pid = ?";
        String[] selectionArgs = {pid};

        Cursor cursor = db.query("user", null, selection, selectionArgs, null, null, null);
        boolean exists = cursor != null && cursor.getCount() > 0;

        if (cursor != null) {
            cursor.close();
        }

        db.close();

        return exists;
    }

    public boolean insertUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("pid", user.getPid());
        values.put("password", user.getPassword());
        values.put("name", user.getName());
        values.put("role", user.getRole());

        long rowId = db.insert("user", null, values);

        db.close();

        return rowId != -1;
    }

    public boolean deleteUserByPid(String pid) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String whereClause = "pid = ?";
        String[] whereArgs = { pid };

        int deletedRows = db.delete("user", whereClause, whereArgs);

        db.close();

        return deletedRows > 0;
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {"Pid", "password", "name", "role"};
        Cursor cursor = db.query("user", columns, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String pid = cursor.getString(cursor.getColumnIndex("Pid"));
                @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex("password"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") int role = cursor.getInt(cursor.getColumnIndex("role"));

                User user = new User(pid, password, name, role);
                userList.add(user);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();

        return userList;
    }

    public void updateUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("role", user.getRole());

        String whereClause = "pid = ?";
        String[] whereArgs = {user.getPid()};

        db.update("user", values, whereClause, whereArgs);

        db.close();
    }

    public void updateUsername(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", user.getName());

        String whereClause = "pid = ?";
        String[] whereArgs = {user.getPid()};

        db.update("user", values, whereClause, whereArgs);

        db.close();
    }
    public void updateUserPassword(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("password", user.getPassword());

        String whereClause = "pid = ?";
        String[] whereArgs = {user.getPid()};

        db.update("user", values, whereClause, whereArgs);

        db.close();
    }
    public List<User> searchUsersByName(String Pid) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<User> filteredUsers = new ArrayList<>();

        String selection = "Pid LIKE ?";
        String[] selectionArgs = {"%" + Pid + "%"};

        Cursor cursor = db.query(
                "user",
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            @SuppressLint("Range") int Aid = cursor.getInt(cursor.getColumnIndex("Pid"));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
            @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex("password"));
            @SuppressLint("Range") int role = cursor.getInt(cursor.getColumnIndex("role"));
            User user = new User(Pid, password, name, role);

            // 设置项目的其他属性

            filteredUsers.add(user);
        }

        cursor.close();
        db.close();

        return filteredUsers;
    }
}

