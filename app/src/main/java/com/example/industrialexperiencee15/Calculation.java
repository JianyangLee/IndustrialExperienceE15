package com.example.industrialexperiencee15;

public class Calculation {
    String uid;
    double calorieLimit;
    double fatGoal;
    double sugarGoal;

    public Calculation(String UID, double calorieLimit, double fatGoal, double sugarGoal) {
        this.uid = UID;
        this.calorieLimit = calorieLimit;
        this.fatGoal = fatGoal;
        this.sugarGoal = sugarGoal;
    }
    public Calculation(){

    }

    public String getUID() {
        return uid;
    }

    public double getCalorieLimit() {
        return calorieLimit;
    }

    public void setUID(String UID) {
        this.uid = UID;
    }

    public void setCalorieLimit(double calorieLimit) {
        this.calorieLimit = calorieLimit;
    }

    public void setFatGoal(double fatGoal) {
        this.fatGoal = fatGoal;
    }

    public void setSugarGoal(double sugarGoal) {
        this.sugarGoal = sugarGoal;
    }

    public double getFatGoal() {
        return fatGoal;
    }

    public double getSugarGoal() {
        return sugarGoal;
    }
}
