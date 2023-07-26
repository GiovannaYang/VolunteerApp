package com.example.volunteerapp.utils;
public class GlobalData {
    public static final int ROLE_ADMIN = 1;
    public static final int ROLE_USER = 0;
    private static GlobalData instance;
    private boolean isLoggedIn;
    private String Pid;
    private String name;
    private int role;

    private GlobalData() {
        // 私有构造函数，确保只能通过getInstance()方法获取实例
    }

    public static synchronized GlobalData getInstance() {
        if (instance == null) {
            instance = new GlobalData();
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public String getUserId() {
        return Pid;
    }

    public void setUserId(String userId) {
        this.Pid = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public void logout() {
        isLoggedIn = false;
        Pid = "";
        name = "";
        role = 0;
    }

}


