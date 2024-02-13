package com.goalpenalty.soccerapp.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Record {
    @PrimaryKey
    @NonNull
    public String id;

    public int value;

    public Record(String id, int value){
        this.id = id;
        this.value = value;
    }
}
