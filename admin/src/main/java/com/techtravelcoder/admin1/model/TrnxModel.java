package com.techtravelcoder.admin1.model;

public class TrnxModel {
    String count,balancePoints,names,date,payColor,postKey,status,trnxId,uid,image;

    public TrnxModel(){

    }

    public TrnxModel(String count, String balancePoints, String names, String date, String payColor, String postKey, String status, String trnxId, String uid, String image) {
        this.count = count;
        this.balancePoints = balancePoints;
        this.names = names;
        this.date = date;
        this.payColor = payColor;
        this.postKey = postKey;
        this.status = status;
        this.trnxId = trnxId;
        this.uid = uid;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getBalancePoints() {
        return balancePoints;
    }

    public void setBalancePoints(String balancePoints) {
        this.balancePoints = balancePoints;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPayColor() {
        return payColor;
    }

    public void setPayColor(String payColor) {
        this.payColor = payColor;
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

    public String getTrnxId() {
        return trnxId;
    }

    public void setTrnxId(String trnxId) {
        this.trnxId = trnxId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
