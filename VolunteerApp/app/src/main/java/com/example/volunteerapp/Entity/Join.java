package com.example.volunteerapp.Entity;

public class Join {
    private int Aid;
    private String Pid;
    public Join(){}
    public Join(int Aid,String Pid){
        this.Aid=Aid;
        this.Pid=Pid;
    }
    public int getAid(){
        return Aid;
    }
    public void setAid(int Aid){
        this.Aid=Aid;
    }
    public String getPid(){
        return Pid;
    }
    public void setPid(String Pid){
        this.Pid=Pid;
    }
}
