package com.goalpenalty.soccerapp.database;

import android.os.AsyncTask;

import com.goalpenalty.soccerapp.ThisApp;


public class InsertRecordTask extends AsyncTask<Record, Void, Void> {

    RecordDao dao;
    GoalDatabase database;

    @Override
    protected Void doInBackground(Record... records){
        database = ThisApp.getInstance().getDatabase();
        dao = database.getRecordDao();

        dao.insert(records);

        return null;
    }

}
