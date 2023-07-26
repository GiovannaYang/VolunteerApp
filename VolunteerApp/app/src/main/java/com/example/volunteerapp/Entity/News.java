package com.example.volunteerapp.Entity;

public class News {
    private int Nid;
    private String name;
    private String time;
    private String content;
    public News() {
    }

    public News(int Nid,String name, String time, String content) {
        this.Nid=Nid;
        this.name = name;
        this.time = time;
        this.content = content;
    }
    public int getNid() {
        return Nid;
    }

    public void setNid(int Aid) {
        this.Nid = Aid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String start_time) {
        this.time = start_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
