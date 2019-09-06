package com.example.industrialexperiencee15;

public class Workout {
    private String userid;
    private int exerciseid;
    private String date;
    private int duration;
    private int burned;

    public Workout(){

    }

    public Workout(String userid, int exerciseid, String date, int duration, int burned) {
        this.userid = userid;
        this.exerciseid = exerciseid;
        this.date = date;
        this.duration = duration;
        this.burned = burned;
    }

    public String getUserid() {
        return userid;
    }

    public int getExerciseid() {
        return exerciseid;
    }

    public String getDate() {
        return date;
    }

    public int getDuration() {
        return duration;
    }

    public int getBurned() {
        return burned;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setExerciseid(int exerciseid) {
        this.exerciseid = exerciseid;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setBurned(int burned) {
        this.burned = burned;
    }
}
