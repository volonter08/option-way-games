package com.goalpenalty.soccerapp.database;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Record.class}, version = 1)
public abstract class GoalDatabase extends RoomDatabase {

    public abstract RecordDao getRecordDao();
}
