package com.techtravelcoder.earningapp.model;

public class UserModel {
    String name , gmail ,password ,uid ;
    int balance ;

    public UserModel(){

    }
    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }


    public UserModel(String name, String gmail, String password, String uid, int balance) {
        this.name = name;
        this.gmail = gmail;
        this.password = password;
        this.uid = uid;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
