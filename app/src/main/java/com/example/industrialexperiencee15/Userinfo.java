package com.example.industrialexperiencee15;

public class Userinfo {
    private String userid;
    private int height;
    private int weight;
    private int age;
    private String gender;
    private double activitylevel;
    private int BMR;
    private String goal;

    public Userinfo(){

    }

    public Userinfo(String userid, int height, int weight, int age, String gender, double activitylevel, int BMR, String goal) {
        this.userid = userid;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.gender = gender;
        this.activitylevel = activitylevel;
        this.BMR = BMR;
        this.goal = goal;
    }

    public String getUserid() {
        return userid;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public double getActivitylevel() {
        return activitylevel;
    }

    public int getBMR() {
        return BMR;
    }

    public String getGoal() {
        return goal;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setActivitylevel(double activitylevel) {
        this.activitylevel = activitylevel;
    }

    public void setBMR(int BMR) {
        this.BMR = BMR;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }
}
