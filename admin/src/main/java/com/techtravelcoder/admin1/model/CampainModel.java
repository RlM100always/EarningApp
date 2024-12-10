package com.techtravelcoder.admin1.model;

public class CampainModel {

    String count,date,link ,names,points,uid,workerNumber,postKey,status,backColor;

    public CampainModel(){

    }

    public CampainModel(String count, String date, String link, String names, String points, String uid, String workerNumber, String postKey, String status, String backColor) {
        this.count = count;
        this.date = date;
        this.link = link;
        this.names = names;
        this.points = points;
        this.uid = uid;
        this.workerNumber = workerNumber;
        this.postKey = postKey;
        this.status = status;
        this.backColor = backColor;
    }

    public String getBackColor() {
        return backColor;
    }

    public void setBackColor(String backColor) {
        this.backColor = backColor;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getWorkerNumber() {
        return workerNumber;
    }

    public void setWorkerNumber(String workerNumber) {
        this.workerNumber = workerNumber;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
