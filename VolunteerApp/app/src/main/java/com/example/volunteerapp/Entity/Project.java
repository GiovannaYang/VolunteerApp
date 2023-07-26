package com.example.volunteerapp.Entity;
import java.io.Serializable;

public class Project implements Serializable {
    private int Aid;
    private int number;
    private String name;
    private String start_time;
    private String end_time;
    private String address;
    private String content;
    private String telephone;
    private double latitude;
    private double longitude;
    private String picture;

    public Project() {
    }

    public Project(int Aid,String name, String start_time, String end_time, String address, String content,int number,String telephone, String picture) {
        this.Aid=Aid;
        this.name = name;
        this.start_time = start_time;
        this.end_time = end_time;
        this.address = address;
        this.content = content;
        this.number = number;
        this.telephone = telephone;
        this.picture = picture;
        parseAddress();
    }
    private void parseAddress() {
        // 解析地址并计算纬度和经度
        String[] parts = address.split(",");
        if (parts.length == 2) {
            String latitudeStr = parts[0].trim();
            String longitudeStr = parts[1].trim();

            double latitude = parseCoordinate(latitudeStr);
            double longitude = parseCoordinate(longitudeStr);

            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    private double parseCoordinate(String coordinateStr) {
        double coordinate = 0.0;

        // 去除度数符号、逗号和空格
        String cleanStr = coordinateStr.replace("°", "").replace(",", "").replace(" ", "");

        // 判断方向（N 或 E）
        String direction = cleanStr.substring(cleanStr.length() - 1);
        boolean isPositive = direction.equalsIgnoreCase("N") || direction.equalsIgnoreCase("E");

        // 提取数字部分并转换为 double 类型
        String numericStr = cleanStr.substring(0, cleanStr.length() - 1);
        double value = Double.parseDouble(numericStr);

        // 根据方向确定正负值
        coordinate = isPositive ? value : -value;

        return coordinate;
    }


    public int getAid() {
        return Aid;
    }

    public void setAid(int Aid) {
        this.Aid = Aid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return start_time;
    }

    public void setStartTime(String start_time) {
        this.start_time = start_time;
    }

    public String getEndTime() {
        return end_time;
    }

    public void setEndTime(String end_time) {
        this.end_time = end_time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public int getNumber(){ return number; }
    public void setNumber(int number){ this.number=number; }
    public String getTelephone(){
        return telephone;
    }
    public void setTelephone(String telephone){
        this.telephone=telephone;
    }
    public String getPicture(){ return picture;}
    public void setPicture(String picture){this.picture=picture;}
}

