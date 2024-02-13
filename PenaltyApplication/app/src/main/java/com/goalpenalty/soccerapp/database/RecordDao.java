package com.goalpenalty.soccerapp.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecordDao {

    @Query("SELECT * FROM record")
    List<Record> getAllRecords();

    @Query("select * from record where id = :id")
    Record getById(String id);

    @Insert
    void insert(Record... records);

    @Update
    void update(Record... record);

}
