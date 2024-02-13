package com.goalpenalty.soccerapp.database;

public abstract class TaskListener <T>{

    abstract public void onTaskCompleted(T... vals);
}
