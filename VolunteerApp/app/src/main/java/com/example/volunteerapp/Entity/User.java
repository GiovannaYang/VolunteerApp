package com.example.volunteerapp.Entity;

import java.io.Serializable;

import java.io.Serializable;

public class User implements Serializable {
    private String Pid;
    private String password;
    private String name;
    private int role;

    public User() {
    }

    public User(String Pid, String password, String name, int role) {
        this.Pid = Pid;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public String getPid() {
        return Pid;
    }

    public void setPid(String Pid) {
        this.Pid = Pid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}

