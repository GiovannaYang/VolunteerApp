package com.example.volunteerapp.Dao;

/**
 * Created by root on 18-5-12.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="volunteer.db";  //数据库名称
    private static final int DATABASE_VERSION=1;  //数据库版本号

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建表
        db.execSQL("CREATE TABLE activity (Aid INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(50), start_time DATETIME, end_time DATETIME, address VARCHAR(50), content VARCHAR(255), number INTEGER, telephone VARCHAR(11), picture TEXT)");
        db.execSQL("CREATE TABLE user (Pid INTEGER PRIMARY KEY, name VARCHAR(50),password VARCHAR(50),role INTEGER)");
        db.execSQL("CREATE TABLE participate (Aid INTEGER, Pid INTEGER)");
        db.execSQL("CREATE TABLE news (Nid INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(50), content TEXT, time DATETIME)");

        // 添加管理员到user表
        ContentValues adminValues = new ContentValues();
        adminValues.put("Pid", "11111111111"); // 设置管理员的Pid
        adminValues.put("name", "管理员"); // 设置管理员的名字
        adminValues.put("password", "123456"); // 设置管理员的密码
        adminValues.put("role", 1); // 设置管理员的角色（1表示管理员）

        long rowId = db.insert("user", null, adminValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        System.out.println("---版本更新---"+oldVersion+"--->"+newVersion);
    }
}
