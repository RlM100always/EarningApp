package com.techtravelcoder.admin1.model;

public class UserModel {
    String gmail,name,userStatus,statusColor,uid;



    public  UserModel(){

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStatusColor() {
        return statusColor;
    }

    public void setStatusColor(String statusColor) {
        this.statusColor = statusColor;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public UserModel(String gmail, String name, String userStatus, String statusColor) {
        this.gmail = gmail;
        this.name = name;
        this.userStatus = userStatus;
        this.statusColor = statusColor;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
