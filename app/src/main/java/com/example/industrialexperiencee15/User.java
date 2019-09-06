package com.example.industrialexperiencee15;

public class User {
    private String userid;

    public User(){

    }
    public User(String UID) {
        this.userid = UID;
    }

    public String getUID() {
        return userid;
    }

    public void setUID(String UID) {
        this.userid = UID;
    }
}
