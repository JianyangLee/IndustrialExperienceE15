package com.example.industrialexperiencee15;

public class Consumption {

    String uid;
    String name;
    String con_date;
    double fat;
    double sugar;
    double calorie;
    int amount;
    String type;



    public Consumption(String id, String name, String date, double fat, double sugar, double calorie, int amount,String type){
        this.uid = id;
        this.name = name;
        this.con_date = date;
        this.fat = fat;
        this.sugar = sugar;
        this.calorie = calorie;
        this.amount = amount;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public void setCon_date(String con_date) {
        this.con_date = con_date;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setSugar(double sugar) {
        this.sugar = sugar;
    }

    public void setCalorie(double calorie) {
        this.calorie = calorie;
    }



    public String getCon_date() {
        return con_date;
    }

    public double getFat() {
        return fat;
    }

    public double getSugar() {
        return sugar;
    }

    public double getCalorie() {
        return calorie;
    }
    public String getUID() {
        return uid;
    }

    public void setUID(String UID) {
        this.uid = UID;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public Consumption(){

    }
}

