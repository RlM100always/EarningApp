package com.techtravelcoder.earningapp.model;

public class SpecificUserModel {
    String backColor,count,date,points,status,workerNumber;


    public SpecificUserModel(){

    }



    public SpecificUserModel(String backColor, String count, String date, String points, String status, String workerNumber) {
        this.backColor = backColor;
        this.count = count;
        this.date = date;
        this.points = points;
        this.status = status;
        this.workerNumber = workerNumber;

    }

    public String getBackColor() {
        return backColor;
    }

    public void setBackColor(String backColor) {
        this.backColor = backColor;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWorkerNumber() {
        return workerNumber;
    }

    public void setWorkerNumber(String workerNumber) {
        this.workerNumber = workerNumber;
    }
}
