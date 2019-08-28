package com.example.industrialexperiencee15;

public class Consumption {

    String name;
    String con_date;
    double fat;
    double sugar;
    double calorie;
    int amount;

    public Consumption(){

    }

    public Consumption(String name, String date, double fat, double sugar, double calorie, int amount){
        this.name = name;
        this.con_date = date;
        this.fat = fat;
        this.sugar = sugar;
        this.calorie = calorie;
        this.amount = amount;
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
}

