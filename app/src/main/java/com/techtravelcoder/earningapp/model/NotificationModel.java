package com.techtravelcoder.earningapp.model;

public class NotificationModel {
    String date,text,image;

    public NotificationModel(){

    }
    public NotificationModel(String date, String text, String image) {
        this.date = date;
        this.text = text;
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
