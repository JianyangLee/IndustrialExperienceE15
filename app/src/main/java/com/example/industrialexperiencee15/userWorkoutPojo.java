package com.example.industrialexperiencee15;

public class userWorkoutPojo {

    String exerciseID;
    Integer duration;
    Integer countOfExercise;
    boolean isMostDuration;

    public boolean isMostDuration() {
        return isMostDuration;
    }

    public void setMostDuration(boolean mostDuration) {
        isMostDuration = mostDuration;
    }

    public boolean isMostFrequent() {
        return isMostFrequent;
    }

    public void setMostFrequent(boolean mostFrequent) {
        isMostFrequent = mostFrequent;
    }

    boolean isMostFrequent;


    public String getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(String exerciseID) {
        this.exerciseID = exerciseID;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getCountOfExercise() {
        return countOfExercise;
    }

    public void setCountOfExercise(Integer countOfExercise) {
        this.countOfExercise = countOfExercise;
    }
}
